import java.util.*;

/*
 * Generates the whole world
*/

public class WorldGenerator {
    private Random rand;
    private Board<CellType> board;  // Add this field
    private static final int BOARD_SIZE = 8;
    private static final int NUM_HEROES = 3;
    private static final int NUM_LANES = 3;
    private IOHelper ioHelper;  // Add this field

    // Space distribution percentages
    private static final double BUSH_PERCENTAGE = 0.20;   // 20% Bush
    private static final double CAVE_PERCENTAGE = 0.20;   // 20% Cave
    private static final double KOULOU_PERCENTAGE = 0.20; // 20% Koulou
    private static final double PLAIN_PERCENTAGE = 0.40;  // 40% Plain

    public WorldGenerator() {
        this.rand = new Random();
    }

    public Legends generate() {
        // Create board with Nexus and lane dividers
//        Board<CellType> board = createBoard();
        this.board = createBoard();  // Assign to field

        // Create lanes
        List<Lane> lanes = createLanes();

        // Select and initialize heroes
        HeroParty heroParty = generateHeroParty(lanes);

        // Create monster factory with medium difficulty
        RandomMonsterFactory monsterFactory = new RandomMonsterFactory(GameDifficulty.MEDIUM);

        // Create market (accessible at hero nexus)
        Market market = new Market(new HashSet<>(Items.all()));

        IOHelper ioHelper = new IOHelper(new Scanner(System.in));

        return new Legends(
                board,
                lanes,
                heroParty,
                market,
                new MarketStrategy(ioHelper),
                new AllFightStrategy(ioHelper),
                monsterFactory,
                ioHelper
        );
    }


    private Board<CellType> createBoard() {
        Board<CellType> board = new Board<>(BOARD_SIZE, BOARD_SIZE, () -> CellType.PLAIN);

        // Set Nexus rows
        for (int i = 0; i < BOARD_SIZE; i++) {
            board.set(new Position(0, i), CellType.NEXUS);  // Monster nexus
            board.set(new Position(BOARD_SIZE-1, i), CellType.NEXUS);  // Hero nexus
        }

        // Set inaccessible lane dividers (columns 2 and 5)
        for (int row = 0; row < BOARD_SIZE; row++) {
            board.set(new Position(row, 2), CellType.INACCESSIBLE);
            board.set(new Position(row, 5), CellType.INACCESSIBLE);
        }

        // Randomly distribute terrain types
        distributeTerrainTypes(board);

        return board;
    }

    private void distributeTerrainTypes(Board<CellType> board) {
        List<Position> availablePositions = new ArrayList<>();

        // Collect available positions (excluding nexus and inaccessible)
        for (int row = 1; row < BOARD_SIZE-1; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (col != 2 && col != 5) {  // Skip inaccessible columns
                    availablePositions.add(new Position(row, col));
                }
            }
        }

        Collections.shuffle(availablePositions, rand);
        int totalSpaces = availablePositions.size();
        int bushSpaces = (int)(totalSpaces * BUSH_PERCENTAGE);
        int caveSpaces = (int)(totalSpaces * CAVE_PERCENTAGE);
        int koulouSpaces = (int)(totalSpaces * KOULOU_PERCENTAGE);

        // Set Bush spaces
        for (int i = 0; i < bushSpaces; i++) {
            Position pos = availablePositions.remove(0);
            board.set(pos, CellType.BUSH);
        }

        // Set Cave spaces
        for (int i = 0; i < caveSpaces; i++) {
            Position pos = availablePositions.remove(0);
            board.set(pos, CellType.CAVE);
        }

        // Set Koulou spaces
        for (int i = 0; i < koulouSpaces; i++) {
            Position pos = availablePositions.remove(0);
            board.set(pos, CellType.KOULOU);
        }
        // Remaining spaces stay as Plain
    }

    private List<Lane> createLanes() {
        List<Lane> lanes = new ArrayList<>();

        // Create three lanes with proper nexus positions
        lanes.add(new Lane(0,
                new Position(BOARD_SIZE-1, 0),  // Hero nexus
                new Position(0, 0)              // Monster nexus
        ));

        lanes.add(new Lane(1,
                new Position(BOARD_SIZE-1, 3),  // Hero nexus
                new Position(0, 3)              // Monster nexus
        ));

        lanes.add(new Lane(2,
                new Position(BOARD_SIZE-1, 6),  // Hero nexus
                new Position(0, 6)              // Monster nexus
        ));

        return lanes;
    }

    private HeroParty generateHeroParty(List<Lane> lanes) {
        Set<Hero> heroes = new HashSet<>();
        IOHelper ioHelper = new IOHelper(new Scanner(System.in));

        ioHelper.println("\nSelect 3 heroes for your team:");

        for (int i = 0; i < NUM_HEROES; i++) {
            displayHeroOptions(ioHelper);
            String heroName = ioHelper.nextLine(
                    "Select hero for lane " + (i+1) + ": ",
                    "Invalid hero name",
                    name -> Heroes.all().stream()
                            .anyMatch(h -> h.getName().equals(name))
            );

            Hero baseHero = Heroes.all().stream()
                    .filter(h -> h.getName().equals(heroName))
                    .findFirst().get();

            Position nexusPos = lanes.get(i).getHeroNexus();
            Hero hero = Heroes.createHeroForLane(baseHero, nexusPos, lanes.get(i));


            // Add these lines:
            board.placeHero(nexusPos, hero);  // Place hero on board
            hero.setCurrentPosition(nexusPos);        // Set hero's position

            heroes.add(hero);
            lanes.get(i).assignHero(hero);
        }

        return new HeroParty(heroes);
    }

    private void displayHeroOptions(IOHelper ioHelper) {
        ioHelper.println("\nAvailable Heroes:");
        ioHelper.println("----------------");
        for (Hero hero : Heroes.all()) {
            ioHelper.println(String.format("%-20s | Type: %-10s | Level: %d",
                    hero.getName(),
                    hero.getType(),
                    hero.getLevel()));
        }
        ioHelper.println("");
    }
}