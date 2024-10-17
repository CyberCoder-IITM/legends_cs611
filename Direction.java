import java.util.Arrays;
import java.util.List;

public enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT;

    public static List<Direction> all() {
        return Arrays.asList(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT);
    }
}
