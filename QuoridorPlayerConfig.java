import java.util.function.BiPredicate;

class QuoridorPlayerConfig {
    Player p;
    int remainingWalls;
    Position position;
    String label;
    BiPredicate<Integer, Integer> f;
    // constructor
    public QuoridorPlayerConfig(Player p, int numWalls, Position pos, String label, BiPredicate<Integer, Integer> f) {
        this.p = p;
        this.remainingWalls = numWalls;
        this.position = pos;
        this.label = label;
        this.f = f;
    }
    // get the remaining walls piece for this player
    public int getRemainingWalls() {
        return remainingWalls;
    }
    // get the player object
    public Player getPlayer() {
        return p;
    }
    // get the current positon of the player
    public Position getPosition() {
        return position;
    }
    // get the label of the player
    public String getLabel() {
        return label;
    }
    // check if player still has wall pieces available
    public boolean canPlaceWalls() {
        return this.remainingWalls > 0;
    }
    // decrease wall number
    public void decreaseRemainingWalls() {
        this.remainingWalls--;
    }
    // get the position on the board
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
    // check if the current position is at the finish side
    public boolean isFinish(Position pos) {
        return f.test(pos.getX(), pos.getY());
    }
}