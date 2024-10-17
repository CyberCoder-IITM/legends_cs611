/* 
 * Represents a position on the 2D board
*/
public class Position {
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Position move(Direction d) {
        switch (d) {
            case UP:
                return new Position(x - 1, y);
            case DOWN:
                return new Position(x + 1, y);
            case LEFT:
                return new Position(x, y - 1);
            case RIGHT:
                return new Position(x, y + 1);
            default:
                return new Position(-1, -1);
        }
    }

    public boolean isSeparated(Position pos, int k) {
        return isSeparated(pos, k, Direction.UP) || isSeparated(pos, k, Direction.RIGHT);
    }

    public boolean isSeparated(Position pos, int k, Direction d) {
        if (d.equals(Direction.UP) || d.equals(Direction.DOWN)) {
            return (modulus(this.getY() - pos.getY()) == k) && (this.getX() == pos.getX());
        } else {
            return (modulus(this.getX() - pos.getX()) == k) && (this.getY() == pos.getY());
        }
    }

    private int modulus(int i) {
        return i > 0 ? i : -i;
    }

    public Position middle(Position pos) {
        return new Position((this.getX() + pos.getX()) / 2, (this.getY() + pos.getY()) / 2);
    }
}