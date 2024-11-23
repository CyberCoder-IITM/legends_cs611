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

//    // In Board.java
//    public void display() {
//        System.out.println("\nCurrent Board State:");
//        System.out.println("\nI: Inaccessible  N: Nexus  P: Plain  B: Bush  C: Cave  K: Koulou");
//        System.out.println("H1/H2/H3: Heroes  M1/M2/M3: Monsters  X: Combat\n");
//
//        // Print column numbers with proper spacing
//        System.out.print("    ");
//        for (int j = 0; j < cols; j++) {
//            System.out.print(j + "   ");
//        }
//        System.out.println();
//
//
//        // Print board content
//        for (int i = 0; i < rows; i++) {
//            System.out.print(i + " ||");
//            for (int j = 0; j < cols; j++) {
//                Position currentPos = new Position(i, j);
//
//                if (hasHero(currentPos) && hasMonster(currentPos)) {
//                    System.out.print(" X  |");
//                } else if (hasHero(currentPos)) {
//                    Hero hero = heroPositions.get(currentPos);
//                    System.out.print(" H" + (hero.getAssignedLane().getLaneNumber() + 1) + " |");
//                } else if (hasMonster(currentPos)) {
//                    Monster monster = monsterPositions.get(currentPos);
//                    System.out.print(" M" + (monster.getAssignedLane().getLaneNumber() + 1) + " |");
//                } else {
//                    CellType cellType = (CellType)at(currentPos).getValue();
//                    System.out.print(" " + getCellSymbol(cellType) + "  |");
//                }
//            }
//            System.out.println("|");
//        }
//    }
//



    // In Board.java

    public void display() {
        System.out.println("\nCurrent Board State:");
        System.out.println("\nI: Inaccessible  N: Nexus  P: Plain  B: Bush  C: Cave  K: Koulou");
        System.out.println("H1/H2/H3: Heroes  M1/M2/M3: Monsters  X: Combat\n");

        // Print column numbers
        System.out.print("    ");
        for (int j = 0; j < cols; j++) {
            System.out.print(j + "   ");
        }
        System.out.println();

        // Print board content
        for (int i = 0; i < rows; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < cols; j++) {
                Position currentPos = new Position(i, j);

                // Start cell
                if (isSpecialCell(i, j)) {
                    System.out.print("|[");
                } else {
                    System.out.print("| ");
                }

                // Cell content
                if (hasHero(currentPos) && hasMonster(currentPos)) {
                    System.out.print("X");
                } else if (hasHero(currentPos)) {
                    Hero hero = heroPositions.get(currentPos);
                    System.out.print("H" + (hero.getAssignedLane().getLaneNumber() + 1));
                } else if (hasMonster(currentPos)) {
                    Monster monster = monsterPositions.get(currentPos);
                    System.out.print("M" + (monster.getAssignedLane().getLaneNumber() + 1));
                } else {
                    CellType cellType = (CellType)at(currentPos).getValue();
                    System.out.print(getCellSymbol(cellType));
                }

                // End cell
                if (isSpecialCell(i, j)) {
                    System.out.print("]");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println("|");

            // Print horizontal separator
            System.out.print("  ");
            for (int j = 0; j < cols; j++) {
                System.out.print("----");
            }
            System.out.println("-");
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
    }

    // If you want to add colors, here's the Java 8 compatible version
    private String getCellSymbol(CellType cellType) {
        if (cellType == null) return " ";

        switch (cellType) {
            case BUSH:
                return ConsoleColors.GREEN + "🌳" + ConsoleColors.RESET;
            case CAVE:
                return ConsoleColors.PURPLE + "⛰️" + ConsoleColors.RESET;
            case KOULOU:
                return ConsoleColors.YELLOW_BOLD + "⚡" + ConsoleColors.RESET;
            case PLAIN:
                return "·";
            case NEXUS:
                return ConsoleColors.CYAN + "N" + ConsoleColors.RESET;
            case INACCESSIBLE:
                return ConsoleColors.RED_BOLD + "X" + ConsoleColors.RESET;
            default:
                return " ";
        }
    }

    public boolean hasHero(Position p) {
        boolean has = heroPositions.containsKey(p);
        return has;
    }


//    private String getCellSymbol(CellType cellType) {
//        switch(cellType) {
//            case NEXUS: return "N";
//            case INACCESSIBLE: return "I";
//            case BUSH: return "B";
//            case CAVE: return "C";
//            case KOULOU: return "K";
//            case PLAIN: return "P";
//            default: return " ";
//        }
//    }
}