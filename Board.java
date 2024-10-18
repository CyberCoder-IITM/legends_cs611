import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/*
 * A 2D board of size n x m with cells in it
 * Generic class for board
 */
public class Board<T> {
    private int n;
    private int m;
    private List<List<Cell<T>>> board;

    // Constructor for board, n and m are the col and row of the board
    public Board(int n, int m, Supplier<T> f) {
        this.n = n;
        this.m = m;
        this.board = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            List<Cell<T>> row = new ArrayList<>();
            for (int j = 0; j < m; j++) {
                row.add(new Cell<>(f.get()));
            }
            this.board.add(row);
        }
    }
    // constructor for the board
    public Board(List<List<Cell<T>>> board) {
        this.board = board;
        this.n = board.size();
        this.m = board.get(0).size();
    }
    // get the row num for the board
    public int getNumRows() {
        return n;
    }
    // get the col num for the board
    public int getNumCols() {
        return m;
    }
    // places the pawns/ walls/ other game pieces on the board
    public void set(Position p, T value) {
        if (!isValid(p)) {
            throw new IllegalArgumentException("position is not valid");
        }
        this.board.get(p.getX()).set(p.getY(), new Cell<>(value));
    }

    private List<List<Cell<T>>> copy(int fromI, int toI, int fromJ, int toJ) {
        List<List<Cell<T>>> boardCopy = new ArrayList<>();
        for (int i = fromI; i < toI; i++) {
            List<Cell<T>> row = new ArrayList<>();
            for (int j = fromJ; j < toJ; j++) {
                row.add(this.board.get(i).get(j));
            }
            boardCopy.add(row);
        }
        return boardCopy;
    }
    // return the copy of the current board
    public List<List<Cell<T>>> getBoard() {
        return copy(0, n, 0, m);
    }

    public List<List<Cell<T>>> getGroupFor(int x, int y, int groupSize) {
        int fromI = (x / groupSize) * groupSize;
        int toI = fromI + groupSize;
        int fromJ = (y / groupSize) * groupSize;
        int toJ = fromJ + groupSize;
        return copy(fromI, toI, fromJ, toJ);
    }
    // checks if the entered position is on the board or not
    public boolean isValid(Position p) {
        return p.getX() >= 0 && p.getX() < n && p.getY() >= 0 && p.getY() < m;
    }
    // get the cell at specific position
    public Cell<T> at(Position p) {
        return this.board.get(p.getX()).get(p.getY());
    }

}
