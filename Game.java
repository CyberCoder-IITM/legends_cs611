import java.util.Collections;
import java.util.List;

/*
 * An abstract class representing the game. All other games extends this and implements the play() function.
 */
public abstract class Game<T> {
    protected Board<T> board;
    protected IOHelper iohelper;

    protected Game(Board<T> board, IOHelper ioHelper) {
        this.board = board;
        this.iohelper = ioHelper;
    }

    public abstract void play();

    protected void printBoard() {
        List<List<Cell<T>>> b = this.board.getBoard();
        int n = b.size();
        int m = b.get(0).size();

        StringBuilder out = new StringBuilder();

        out.append(line(m));
        out.append("\n");

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                out.append(String.format("|%2s", xoToString(b.get(i).get(j), m, i, j)));
            }
            out.append("|\n");
            out.append(line(m));
            out.append("\n");
        }

        this.iohelper.print(out.toString());
    }

    protected String line(int n) {
        return String.join("", Collections.nCopies(n, "+--")) + "+";
    }

    protected String xoToString(Cell<T> c, int n, int i, int j) {
        if (c.has(XOCell.EMPTY)) {
            return (n * i + j + 1) + "";
        }
        return c.toString();
    }

    protected SetPositionMove readMove(Player player, XOCell sign) {
        int m = board.getNumCols();

        int pos = this.iohelper.nextLineInt(String.format("Player:%s Enter your move:", player.toString()),
                "input should be an integer in the board");
        pos--;

        Position p = new Position(pos / m, pos % m);
        if (!board.isValid(p)) {
            this.iohelper.printErr("cell number is invalid, they should be within the board. Try again");
            return this.readMove(player, sign);
        }

        if (!board.at(p).has(XOCell.EMPTY)) {
            this.iohelper.printErr("the cell already have another piece. Try again");
            return this.readMove(player, sign);
        }

        return new SetPositionMove(p, sign);
    }

    protected SetPositionMove readMoveWithSign(Player player) {
        int n = board.getNumRows();

        String posAndSymbol = this.iohelper
                .nextLine(String.format("Player:%s Enter your position and symbol:", player.toString()));
        String[] split = posAndSymbol.split(",");
        if (split.length != 2) {
            this.iohelper.printErr("input is in incorrect format, should be {number},{symbol}. Try again");
            return this.readMoveWithSign(player);
        }
        split[0] = split[0].trim();
        split[1] = split[1].trim();

        int pos;
        try {
            pos = Integer.parseInt(split[0]);
        } catch (NumberFormatException e) {
            this.iohelper.printErr("cell number is not valid. Try again");
            return this.readMoveWithSign(player);
        }

        if (!split[1].equals("X") && !split[1].equals("O")) {
            this.iohelper.printErr("symbol is not valid. Try again");
            return this.readMoveWithSign(player);
        }

        pos--;
        Position p = new Position(pos / n, pos % n);
        if (!board.isValid(p)) {
            this.iohelper.printErr("cell is invalid, they should be within the board. Try again");
            return this.readMoveWithSign(player);
        }

        if (!board.at(p).has(XOCell.EMPTY)) {
            this.iohelper.printErr("the cell already have another piece. Try again");
            return this.readMoveWithSign(player);
        }

        return new SetPositionMove(p, split[1].equals("X") ? XOCell.X : XOCell.O);

    }

}
