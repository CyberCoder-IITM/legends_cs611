
import java.util.ArrayList;
import java.util.List;

/*
 * A class to check if there are a certain number of consecutive X or O on the 2D board
 */
public class Checker {

    public Checker() {
    }

    public Status check(Board<XOCell> board, int winCount) {
        return check(board.getBoard(), winCount);
    }

    public Status check(List<List<Cell<XOCell>>> b, int winCount) {
        int n = b.size();
        int m = b.get(0).size();

        int count = 0;
        // check the rows
        for (int i = 0; i < n; i++) {
            count = 0;
            for (int j = 0; j < m; j++) {
                if (j < winCount) {
                    count += value(i, j, b);
                } else {
                    count += value(i, j, b);
                    count -= value(i, j - winCount, b);
                }
                if (count == winCount || count == -winCount) {
                    return Status.WIN;
                }
            }
        }

        // check the columns
        for (int j = 0; j < m; j++) {
            count = 0;
            for (int i = 0; i < n; i++) {
                if (i < winCount) {
                    count += value(i, j, b);
                } else {
                    count -= value(i - winCount, j, b);
                    count += value(i, j, b);
                }
                if (count == winCount || count == -winCount) {
                    return Status.WIN;
                }
            }
        }

        // check the diagonal
        for (int j = 0; j < m; j++) {
            count = 0;
            for (int i = 0; i < n && i + j < m; i++) {
                if (i < winCount) {
                    count += value(i, i + j, b);
                } else {
                    count += value(i, i + j, b);
                    count -= value(i - winCount, i + j - winCount, b);
                }
                if (count == winCount || count == -winCount) {
                    return Status.WIN;
                }
            }
        }

        // check the diagonal
        for (int j = 0; j < m; j++) {
            count = 0;
            for (int i = 0; i < n && j - i >= 0; i++) {
                if (i < winCount) {
                    count += value(i, j - i, b);
                } else {
                    count += value(i, j - i, b);
                    count -= value(i - winCount, j - i + winCount, b);
                }
                if (count == winCount || count == -winCount) {
                    return Status.WIN;
                }
            }
        }

        // check if there are no more empty cells
        boolean noneEmpty = true;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                noneEmpty &= !b.get(i).get(j).has(XOCell.EMPTY);
            }
        }

        if (noneEmpty) {
            return Status.END;
        }

        return Status.NONE;
    }

    private int value(int i, int j, List<List<Cell<XOCell>>> b) {
        return (b.get(i).get(j).has(XOCell.O) ? 1 : (b.get(i).get(j).has(XOCell.X) ? -1 : 0));
    }

    public Status checkGroups(Board<XOCell> board, int groupSize, int winCount) {
        List<List<Cell<XOCell>>> boardGroup = new ArrayList<>();
        int n = board.getBoard().size();
        int m = board.getBoard().get(0).size();

        for (int i = 0; i < n / groupSize; i++) {
            List<Cell<XOCell>> groupRow = new ArrayList<>();
            for (int j = 0; j < m / groupSize; j++) {
                List<List<Cell<XOCell>>> group = board.getGroupFor(i * groupSize, j * groupSize, groupSize);
                Status groupStatus = check(group, groupSize);
                groupRow.add((groupStatus == Status.WIN) ? new Cell<XOCell>(XOCell.X) : new Cell<XOCell>(XOCell.EMPTY));
            }
            boardGroup.add(groupRow);
        }
        return check(boardGroup, winCount);
    }

}
