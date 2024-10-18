/*
 * An X or O Cell.
 */
public enum XOCell {
    EMPTY,
    X,
    O;
    // string representation of game pieces 
    @Override
    public String toString() {
        switch (this) {
            case X:
                return "X";
            case O:
                return "O";
            default:
                return "";
        }
    }
}
