import java.util.Arrays;
import java.util.List;
// used for choosing the wall placement for the quoridor game
// this is used with the two cell positions where the wall would be placed.
public enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT;
    // return all the direction values as a list
    public static List<Direction> all() {
        return Arrays.asList(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT);
    }
}
