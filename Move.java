/*
 * All possible moves
 */
public enum Move {
    // Basic movements
    UP("W", "Move up"),
    DOWN("S", "Move down"),
    LEFT("A", "Move left"),
    RIGHT("D", "Move right"),

    // Special actions
    ATTACK("X", "Attack a monster"),
    SPELL("C", "Cast a spell"),
    TELEPORT("T", "Teleport near another hero"),
    RECALL("R", "Return to Nexus"),

    // Inventory actions
    INVENTORY("I", "Access inventory"),
    EQUIP("E", "Change equipment"),
    POTION("P", "Use potion"),

    // Market and info
    MARKET("M", "Access market (at Nexus only)"),
    INFO("N", "Show hero information"),
    HELP("H", "Show help"),
    QUIT("Q", "Quit game"),
    NONE("", "Invalid move");

    private final String key;
    private final String description;

    Move(String key, String description) {
        this.key = key;
        this.description = description;
    }

    public String getKey() {
        return key;
    }

    public String getDescription() {
        return description;
    }

    public static Move fromKey(String input) {
        String upperInput = input.toUpperCase();
        for (Move move : Move.values()) {
            if (move.key.equals(upperInput)) {
                return move;
            }
        }
        return NONE;
    }

    public static boolean isMovement(Move move) {
        return move == UP || move == DOWN || move == LEFT || move == RIGHT;
    }

    public static boolean requiresTarget(Move move) {
        return move == ATTACK || move == SPELL || move == TELEPORT;
    }

    public static boolean isAction(Move move) {
        return move == ATTACK || move == SPELL || move == TELEPORT ||
                move == RECALL || move == EQUIP || move == POTION;
    }

    public static boolean isValid(Move move, Hero hero, Board<CellType> board) {
        if (move == MARKET && !hero.getCurrentPosition().isHeroNexus()) {
            return false;
        }

        if (isMovement(move)) {
            Position newPos = hero.getCurrentPosition().move(toDirection(move));
            return isValidMove(newPos, hero, board);
        }

        return true;
    }

    private static boolean isValidMove(Position newPos, Hero hero, Board<CellType> board) {
        // Check board boundaries
        if (!board.isValid(newPos)) {
            return false;
        }

        // Check for inaccessible spaces
        if (board.isInaccessible(newPos)) {
            return false;
        }

        // Check if space is occupied by another hero
        if (board.hasHero(newPos)) {
            return false;
        }

        // Check if trying to move behind a monster
        if (hasMonsterAhead(hero.getCurrentPosition(), newPos, board)) {
            return false;
        }

        return true;
    }

    private static boolean hasMonsterAhead(Position current, Position target, Board<CellType> board) {
        // Check all positions between current and target for monsters
        int minRow = Math.min(current.getX(), target.getX());
        int maxRow = Math.max(current.getX(), target.getX());

        for (int row = minRow; row <= maxRow; row++) {
            Position checkPos = new Position(row, target.getY());
            if (board.hasMonster(checkPos)) {
                return true;
            }
        }
        return false;
    }

    public static Direction toDirection(Move move) {
        switch (move) {
            case UP: return Direction.UP;
            case DOWN: return Direction.DOWN;
            case LEFT: return Direction.LEFT;
            case RIGHT: return Direction.RIGHT;
            default: throw new IllegalArgumentException("Not a movement move");
        }
    }

    public static void displayMoves(IOHelper ioHelper) {
        ioHelper.println("\nAvailable Actions:");
        ioHelper.println("----------------");
        for (Move move : Move.values()) {
            if (move != NONE) {
                ioHelper.println(String.format("%-2s - %s",
                        move.getKey(), move.getDescription()));
            }
        }
    }
}
