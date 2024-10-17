/*
 * An X or O Cell.
 */
public enum XOCell {
    EMPTY,
    X,
    O;

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
