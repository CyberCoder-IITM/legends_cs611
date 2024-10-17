import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

/*
 * Super Tic Tac Toe is implemented in this class. It extends the Game abstract class.
 */
public class SuperTicTacToe extends Game<XOCell> {
    private int winCount;
    private Checker checker;
    private Random rand;
    private static final int GROUP_SIZE = 3;

    protected SuperTicTacToe(Board<XOCell> board, int winCount, IOHelper ioHelper) {
        super(board, ioHelper);
        this.checker = new Checker();
        this.rand = new Random();
        this.winCount = winCount;
    }

    public static SuperTicTacToe init(int n, int m, int c) {
        return new SuperTicTacToe(new Board<>(GROUP_SIZE * n, 3 * m, () -> XOCell.EMPTY), c,
                new IOHelper(new Scanner(System.in)));
    }

    @Override
    public void play() {
        int turn = 0;
        // initialize the teams
        Teams teams = Teams.init(super.iohelper);
        // print the board size and teams/players before the game begins
        this.printBeforeStart(teams);

        while (true) {
            // find the team playing in this turn
            Team team = teams.getTeams().get(turn % teams.numTeams());
            // find the player playing in this turn
            Player player = teams.randomPlayerInTeam(turn % teams.numTeams(), rand);
            // print the board in groups of 3
            this.printBoardInGroups();
            // read the move and do an extra validation of whether the group is won or not
            this.readMoveWithGroupValidation(player, team).doAction(this.board);
            // check the status of the board
            Status status = checker.checkGroups(board, GROUP_SIZE, winCount);
            this.printAfterTurn(team, status);
            // end the game if it is done
            if (status == Status.WIN || status == Status.LOSE || status == Status.END) {
                return;
            }
            turn++;
        }
    }

    private SetPositionMove readMoveWithGroupValidation(Player player, Team team) {
        while (true) {
            SetPositionMove move = super.readMove(player, team.getSign());
            Status groupStatus = checker.check(
                    board.getGroupFor(move.getPostion().getX(), move.getPostion().getY(), GROUP_SIZE), GROUP_SIZE);
            if (groupStatus == Status.WIN) {
                this.iohelper
                        .printErr("cannot place here as the group(3x3) is already won, place it in a different group");
            } else {
                return move;
            }
        }
    }

    private void printBeforeStart(Teams teams) {
        this.iohelper.println("Super TicTacToe Game is starting.....");
        this.iohelper.println("Board is of size: " + this.board.getNumRows() + " x " + board.getNumCols());
        this.iohelper.println("Teams and players are: ");
        for (Team team : teams.getTeams()) {
            this.iohelper.println("Team: " + team.getName() + " consists of players: {" +
                    team.getPlayers().stream().map(Player::getName).collect(Collectors.joining(",")) + "}");
        }
        this.iohelper.println(".....");
    }

    public void printAfterTurn(Team team, Status status) {
        switch (status) {
            case WIN:
                this.printBoardInGroups();
                this.iohelper.println("Game completed, winner is team: " + team.getName());
                break;
            case END:
                this.printBoardInGroups();
                this.iohelper.println("Game Ended, no more cells to place X or O");
                break;
            default:
                break;
        }
    }

    private void printBoardInGroups() {
        List<List<Cell<XOCell>>> b = this.board.getBoard();
        int n = b.size();
        int m = b.get(0).size();

        StringBuilder out = new StringBuilder();

        out.append(lineGroup(m));
        out.append("\n");

        for (int i = 0; i < n; i++) {
            if (i != 0 && i % GROUP_SIZE == 0) {
                out.append("\n");
                out.append(lineGroup(m));
                out.append("\n");
            }

            for (int j = 0; j < m; j++) {
                if (j != 0 && j % GROUP_SIZE == 0) {
                    out.append("| ");
                }
                out.append(String.format("|%3s", xoToString(b.get(i).get(j), m, i, j)));
            }
            out.append("|\n");
            out.append(lineGroup(m));
            out.append("\n");
        }

        this.iohelper.print(out.toString());
    }

    private String lineGroup(int n) {
        String unit = String.join("", Collections.nCopies(GROUP_SIZE, "+---")) + "+";
        return String.join(" ", Collections.nCopies(n / GROUP_SIZE, unit));
    }

}
