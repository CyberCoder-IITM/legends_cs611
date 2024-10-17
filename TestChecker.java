
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * A tester for game status check, ideally JUNIT will be used instead of this
 */
public class TestChecker {
        public static void main(String[] args) {
                validate(Status.END,
                                3, 3, 3,
                                Arrays.asList(
                                                Arrays.asList("O", "X", "O"),
                                                Arrays.asList("X", "O", "X"),
                                                Arrays.asList("X", "O", "X")));

                validate(Status.WIN,
                                3, 3, 3,
                                Arrays.asList(
                                                Arrays.asList("O", "O", "O"),
                                                Arrays.asList("", "", ""),
                                                Arrays.asList("", "", "")));

                validate(Status.WIN,
                                3, 3, 3,
                                Arrays.asList(
                                                Arrays.asList("X", "O", "O"),
                                                Arrays.asList("X", "", ""),
                                                Arrays.asList("X", "", "")));

                validate(Status.WIN,
                                3, 3, 3,
                                Arrays.asList(
                                                Arrays.asList("X", "O", "O"),
                                                Arrays.asList("O", "X", ""),
                                                Arrays.asList("O", "", "X")));

                validate(Status.WIN,
                                3, 3, 3,
                                Arrays.asList(
                                                Arrays.asList("O", "O", "X"),
                                                Arrays.asList("O", "X", ""),
                                                Arrays.asList("X", "", "O")));

                validate(Status.WIN,
                                5, 4, 4,
                                Arrays.asList(
                                                Arrays.asList("O", "O", "O", "X"),
                                                Arrays.asList("O", "O", "X", "O"),
                                                Arrays.asList("O", "X", "O", "O"),
                                                Arrays.asList("X", "O", "O", "O"),
                                                Arrays.asList("X", "O", "O", "")));

                validate(Status.WIN,
                                5, 4, 4,
                                Arrays.asList(
                                                Arrays.asList("", "", "", ""),
                                                Arrays.asList("X", "O", "X", "O"),
                                                Arrays.asList("O", "X", "O", "O"),
                                                Arrays.asList("X", "O", "X", "O"),
                                                Arrays.asList("X", "O", "O", "X")));
        }

        private static void validate(Status status, int n, int m, int win, List<List<String>> l) {
                Checker ch = new Checker();
                List<List<Cell<XOCell>>> boardList = new ArrayList<>();
                for (int i = 0; i < n; i++) {
                        boardList.add(new ArrayList<>());
                        for (int j = 0; j < m; j++) {
                                XOCell c = (l.get(i).get(j).equals("O") ? XOCell.O
                                                : (l.get(i).get(j).equals("X") ? XOCell.X : XOCell.EMPTY));
                                boardList.get(i).add(new Cell<>(c));
                        }
                }
                Board<XOCell> board = new Board<>(boardList);
                System.out.println("Checker expected: " + status + " actual: " + ch.check(board, win));
        }
}
