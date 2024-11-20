/* 
 * Represents a position on the 2D board
//*/
import java.util.*;

public class Position {
    private int x;
    private int y;
    private static final int BOARD_SIZE = 8;
    private static final int[] LANE_DIVIDERS = {2, 5}; // Inaccessible columns

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public Position move(Direction d) {
        switch (d) {
            case UP: return new Position(x - 1, y);
            case DOWN: return new Position(x + 1, y);
            case LEFT: return new Position(x, y - 1);
            case RIGHT: return new Position(x, y + 1);
            default: return new Position(-1, -1);
        }
    }

    public boolean isValid() {
        return x >= 0 && x < BOARD_SIZE && y >= 0 && y < BOARD_SIZE;
    }

    public boolean isNexus() {
        return x == 0 || x == BOARD_SIZE - 1;
    }

    public boolean isHeroNexus() {
        return x == BOARD_SIZE - 1;
    }

    public boolean isMonsterNexus() {
        return x == 0;
    }

    public boolean isInaccessible() {
        return Arrays.asList(LANE_DIVIDERS).contains(y);
    }

    public int getLane() {
        if (y < LANE_DIVIDERS[0]) return 0;      // Top lane
        if (y < LANE_DIVIDERS[1]) return 1;      // Middle lane
        return 2;                                 // Bottom lane
    }

    public boolean isInSameLane(Position other) {
        return this.getLane() == other.getLane();
    }

    public boolean isAdjacent(Position other) {
        int dx = Math.abs(x - other.getX());
        int dy = Math.abs(y - other.getY());
        return (dx == 1 && dy == 0) || (dx == 0 && dy == 1);
    }

    public boolean isInAttackRange(Position target) {
        int dx = Math.abs(target.getX() - x);
        int dy = Math.abs(target.getY() - y);
        return (dx <= 1 && dy <= 1) && (dx + dy <= 1);
    }

    public boolean isValidTeleportTarget(Position heroPosition) {
        // Must be in different lanes
        if (isInSameLane(heroPosition)) {
            return false;
        }

        // Must be adjacent to target hero
        return isAdjacent(heroPosition);
    }

    public boolean isBehindPosition(Position other) {
        return this.x > other.x;
    }

    public List<Position> getAdjacentPositions() {
        List<Position> adjacent = new ArrayList<>();
        for (Direction dir : Direction.values()) {
            Position newPos = this.move(dir);
            if (newPos.isValid() && !newPos.isInaccessible()) {
                adjacent.add(newPos);
            }
        }
        return adjacent;
    }

    public double getDistanceTo(Position other) {
        return Math.abs(this.x - other.x) + Math.abs(this.y - other.y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position position = (Position) o;
        return x == position.x && y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}