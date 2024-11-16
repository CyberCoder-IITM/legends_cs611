import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

/*
 * Generates the whole world
 */
public class WorldGenerator {

    public WorldGenerator() {
        this.rand = new Random();
    }

    private Random rand;

    private static final int NUM_HEROES = 3;
    private static final int NUM_COLS = 8;
    private static final int NUM_ROWS = 8;
    private static final int NUM_WALLS = 2;

    public Legends generate(int numHeroes, int numCols, int numRows, int numWalls) {
        Board<CellType> board = new Board<>(numRows, numCols, () -> CellType.NONE);
        board.set(new Position(0, 0), CellType.HEROES);
        board.set(new Position(2, 0), CellType.WALL);
        IOHelper helper = new IOHelper(new Scanner(System.in));
        return new Legends(
                new Position(0, 0),
                board,
                generateHeroParty(numHeroes),
                generateMarket(board),
                new MarketStrategy(helper),
                new AllFightStrategy(helper),
                new RandomMonsterFactory(rand),
                helper);
    }

    private Market generateMarket(Board<CellType> board) {
        board.set(new Position(1, 1), CellType.MARKET);
        return new Market(new HashSet<>(Items.all()));
    }

    private HeroParty generateHeroParty(int numHeroes) {
        Set<Hero> heroes = new HashSet<>();
        for (int i = 0; i < numHeroes; i++) {
            heroes.add(Heroes.randomHero(rand));
        }
        return new HeroParty(heroes);
    }

    public Legends generate() {
        return generate(NUM_HEROES, NUM_COLS, NUM_ROWS, NUM_WALLS);
    }
}
