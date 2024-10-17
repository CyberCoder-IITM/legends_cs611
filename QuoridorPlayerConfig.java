import java.util.function.BiPredicate;

class QuoridorPlayerConfig {
    Player p;
    int remainingWalls;
    Position position;
    String label;
    BiPredicate<Integer, Integer> f;

    public QuoridorPlayerConfig(Player p, int numWalls, Position pos, String label, BiPredicate<Integer, Integer> f) {
        this.p = p;
        this.remainingWalls = numWalls;
        this.position = pos;
        this.label = label;
        this.f = f;
    }

    public int getRemainingWalls() {
        return remainingWalls;
    }

    public Player getPlayer() {
        return p;
    }

    public Position getPosition() {
        return position;
    }

    public String getLabel() {
        return label;
    }

    public boolean canPlaceWalls() {
        return this.remainingWalls > 0;
    }

    public void decreaseRemainingWalls() {
        this.remainingWalls--;
    }

    public Position getPositionInBoard(Board<QuoridorPiece> board) {
        for (int i = 0; i < board.getNumCols(); i++) {
            for (int j = 0; j < board.getNumRows(); j++) {
                if (board.at(new Position(i, j)).getValue().getLabel().equals(label)) {
                    return new Position(i, j);
                }
            }
        }
        return new Position(-1, -1);
    }

    public boolean isFinish(Position pos) {
        return f.test(pos.getX(), pos.getY());
    }
}