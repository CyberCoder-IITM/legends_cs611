import java.util.*;
import java.util.function.Supplier;

/*
 * A 2D board of size n x m with cells in it
 * Generic class for board
*/

public class Board<T> {
    private int rows;
    private int cols;
    private List<List<Cell2D<T>>> board;
    private Map<Position, Hero> heroPositions;
    private Map<Position, Monster> monsterPositions;
    private Map<Position, TerrainEffect> terrainEffects;
    private Supplier<T> defaultValueSupplier;  // Add this field


    private static final int BOARD_SIZE = 8;
    private static final int[] LANE_DIVIDERS = {2, 5};
    private static final double SPECIAL_SPACE_PERCENTAGE = 0.20; // 20% for each special space

    public Board(int rows, int cols, Supplier<T> defaultValueSupplier) {
        this.rows = rows;
        this.cols = cols;
        this.defaultValueSupplier = defaultValueSupplier;
        this.heroPositions = new HashMap<>();
        this.monsterPositions = new HashMap<>();
        this.terrainEffects = new HashMap<>();
        initializeBoard();
    }

    private void initializeBoard() {
        this.board = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            List<Cell2D<T>> row = new ArrayList<>();
            for (int j = 0; j < cols; j++) {
                row.add(new Cell2D<>(defaultValueSupplier.get()));
            }
            this.board.add(row);
        }

        // Set Nexus rows
        for (int j = 0; j < cols; j++) {
            set(new Position(0, j), (T)CellType.NEXUS);    // Monster Nexus
            set(new Position(rows-1, j), (T)CellType.NEXUS); // Hero Nexus
        }

        // Set inaccessible columns
        for (int i = 0; i < rows; i++) {
            for (int divider : LANE_DIVIDERS) {
                set(new Position(i, divider), (T)CellType.INACCESSIBLE);
            }
        }

        // Distribute terrain types
        distributeTerrainTypes();
    }

    private void distributeTerrainTypes() {
        List<Position> availablePositions = new ArrayList<>();

        // Collect available positions (excluding nexus and inaccessible)
        for (int i = 1; i < rows-1; i++) {
            for (int j = 0; j < cols; j++) {
                if (!isInaccessible(new Position(i, j))) {
                    availablePositions.add(new Position(i, j));
                }
            }
        }

        Collections.shuffle(availablePositions);
        int totalSpaces = availablePositions.size();
        int specialSpaces = (int)(totalSpaces * SPECIAL_SPACE_PERCENTAGE);

        // Distribute Bush spaces
        for (int i = 0; i < specialSpaces; i++) {
            Position pos = availablePositions.remove(0);
            set(pos, (T)CellType.BUSH);
            terrainEffects.put(pos, new TerrainEffect(CellType.BUSH));
        }

        // Distribute Cave spaces
        for (int i = 0; i < specialSpaces; i++) {
            Position pos = availablePositions.remove(0);
            set(pos, (T)CellType.CAVE);
            terrainEffects.put(pos, new TerrainEffect(CellType.CAVE));
        }

        // Distribute Koulou spaces
        for (int i = 0; i < specialSpaces; i++) {
            Position pos = availablePositions.remove(0);
            set(pos, (T)CellType.KOULOU);
            terrainEffects.put(pos, new TerrainEffect(CellType.KOULOU));
        }

        // Remaining spaces stay as Plain
    }

    public void set(Position p, T value) {
        if (!isValid(p)) {
            throw new IllegalArgumentException("Invalid position");
        }
        board.get(p.getX()).set(p.getY(), new Cell2D<>(value));
    }

    public Cell2D<T> at(Position p) {
        return board.get(p.getX()).get(p.getY());
    }

    public boolean isValid(Position p) {
        return p.getX() >= 0 && p.getX() < rows &&
                p.getY() >= 0 && p.getY() < cols;
    }

    public boolean isInaccessible(Position p) {
        return Arrays.asList(LANE_DIVIDERS).contains(p.getY());
    }

    public boolean isNexus(Position p) {
        return p.getX() == 0 || p.getX() == rows - 1;
    }

    public boolean isHeroNexus(Position p) {
        return p.getX() == rows - 1;
    }

    public boolean isMonsterNexus(Position p) {
        return p.getX() == 0;
    }


    public void placeHero(Position p, Hero hero) {
        if (!isValid(p)) {
            throw new IllegalArgumentException("Invalid position");
        }
        heroPositions.put(p, hero);
    }

    public void placeMonster(Position p, Monster monster) {
        monsterPositions.put(p, monster);
    }

    public void removeHero(Position p) {
        heroPositions.remove(p);
    }

    public void removeMonster(Position p) {
        monsterPositions.remove(p);
    }

    public boolean hasMonster(Position p) {
        return monsterPositions.containsKey(p);
    }

    public void applyTerrainEffect(Position pos, Hero hero) {
        TerrainEffect effect = terrainEffects.get(pos);
        if (effect != null) {
            effect.applyEffect(hero);
        }
    }

    public boolean hasTerrainEffect(Position pos) {
        CellType cellType = (CellType)at(pos).getValue();
        return cellType == CellType.BUSH ||
                cellType == CellType.CAVE ||
                cellType == CellType.KOULOU;
    }

    public TerrainEffect getTerrainEffect(Position pos) {
        if (!hasTerrainEffect(pos)) {
            return null;
        }
        return terrainEffects.get(pos);
    }

    public void removeTerrainEffect(Position pos, Hero hero) {
        TerrainEffect effect = terrainEffects.get(pos);
        if (effect != null) {
            effect.removeEffect(hero);
        }
    }

    public void display() {

        System.out.print("   "); // Initial spacing for alignment
        for (int j = 0; j < cols; j++) {
            System.out.print(j + "          "); // 11 spaces after each number
        }
        System.out.println();

        for (int i = 0; i < rows; i++) {
            // Top border
            for (int j = 0; j < cols; j++) {
                Position pos = new Position(i, j);
                CellType cellType = (CellType)at(pos).getValue();
                String symbol = getCellSymbol(cellType);
                System.out.print(symbol + " - " + symbol + " - " + symbol + "  ");
            }
            System.out.println();

            // Middle section with improved spacing
            for (int j = 0; j < cols; j++) {
                Position pos = new Position(i, j);
                if (j == 0) {
                    System.out.print(" |");
                }

                if (hasHero(pos) && hasMonster(pos)) {
                    System.out.printf(" H%d M%d  |",
                            heroPositions.get(pos).getAssignedLane().getLaneNumber() + 1,
                            monsterPositions.get(pos).getAssignedLane().getLaneNumber() + 1);
                } else if (hasHero(pos)) {
                    System.out.printf(" H%-6d|",
                            heroPositions.get(pos).getAssignedLane().getLaneNumber() + 1);
                } else if (hasMonster(pos)) {
                    System.out.printf(" M%-6d|",
                            monsterPositions.get(pos).getAssignedLane().getLaneNumber() + 1);
                } else if (at(pos).getValue() == CellType.INACCESSIBLE) {
                    System.out.print("  X X X X  |");
                } else {
                    System.out.print("         |");
                }
            }
            System.out.println();

            // Bottom border
            for (int j = 0; j < cols; j++) {
                Position pos = new Position(i, j);
                CellType cellType = (CellType)at(pos).getValue();
                String symbol = getCellSymbol(cellType);
                System.out.print(symbol + " - " + symbol + " - " + symbol + "  ");
            }
            System.out.println();

            // Add extra newline only between rows, not after the last row
            if (i < rows - 1) {
                System.out.println();
            }
        }
    }



    private String getCellSymbol(CellType cellType) {
        if (cellType == null) return " ";

        switch (cellType) {
            case BUSH:
                return ConsoleColors.GREEN + "B" + ConsoleColors.RESET;
            case CAVE:
                return ConsoleColors.PURPLE + "C" + ConsoleColors.RESET;
            case KOULOU:
                return ConsoleColors.YELLOW_BOLD + "K" + ConsoleColors.RESET;
            case PLAIN:
                return ConsoleColors.WHITE + "P" + ConsoleColors.RESET;
            case NEXUS:
                return ConsoleColors.CYAN + "N" + ConsoleColors.RESET;
            case INACCESSIBLE:
                return ConsoleColors.RED_BOLD + "I" + ConsoleColors.RESET;
            default:
                return " ";
        }
    }
    // Helper method to determine if a cell should have brackets
    private boolean isSpecialCell(int row, int col) {
        Position pos = new Position(row, col);

        // Check if it's a hero or monster position
        boolean hasHeroOrMonster = hasHero(pos) || hasMonster(pos);

        // Check if it's a nexus
        boolean isNexus = at(pos).getValue() == CellType.NEXUS;

        // Special cells are heroes, monsters, or nexuses in the first and last rows
        return (row == 0 || row == rows - 1) && (hasHeroOrMonster || isNexus);
    }

    // Optional: Color support class
    public class ConsoleColors {
        // Reset
        public static final String RESET = "\033[0m";

        // Regular Colors
        public static final String GREEN = "\033[0;32m";    // GREEN
        public static final String BLUE = "\033[0;34m";     // BLUE
        public static final String PURPLE = "\033[0;35m";   // PURPLE
        public static final String CYAN = "\033[0;36m";     // CYAN

        // Bold
        public static final String RED_BOLD = "\033[1;31m";    // RED
        public static final String YELLOW_BOLD = "\033[1;33m"; // YELLOW

        public static final String WHITE = "\033[0;37m";     // WHITE
        public static final String RED = "\033[0;31m";       // RED
        public static final String BLUE_BOLD = "\033[1;34m"; // BLUE_BOLD
    }

    public boolean hasHero(Position p) {
        boolean has = heroPositions.containsKey(p);
        return has;
    }
}