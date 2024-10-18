import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

/*
 * Tic Tac Toe is implemented in this class. It extends the Game abstract class.
 */
public class TicTacToe extends Game<XOCell> {
    private int winCount;
    private Checker checker;
    private Random rand;
    // initializer
    public static TicTacToe init(int n, int m, int c) {
        return new TicTacToe(new Board<>(n, m, () -> XOCell.EMPTY), c, new IOHelper(new Scanner(System.in)));
    }
    // constructor
    private TicTacToe(Board<XOCell> b, int c, IOHelper helper) {
        super(b, helper);
        this.checker = new Checker();
        this.rand = new Random();
        this.winCount = c;
    }
    // game logic, rule and logic for tic tac toe
    @Override
    public void play() {
        int turn = 0;
        // initialize the players
        Teams teams = Teams.init(super.iohelper);
        this.printBeforeStart(teams);

        while (true) {
            // figure out the team and player of this turn
            Team team = teams.getTeams().get(turn % teams.numTeams());
            Player player = teams.randomPlayerInTeam(turn % teams.numTeams(), rand);
            this.printBoard();
            // read the move and place it on the board
            this.readMove(player, team.getSign()).doAction(this.board);
            Status status = checker.check(board, winCount);
            this.printAfterTurn(team, status);
            // check for game end condition
            if (status == Status.WIN || status == Status.LOSE || status == Status.END) {
                return;
            }
            turn++;
        }
    }
    // print game information before the start of the game
    private void printBeforeStart(Teams teams) {
        this.iohelper.println("TicTacToe Game is starting.....");
        this.iohelper.println("Board is of size: " + this.board.getNumRows() + " x " + board.getNumCols());
        this.iohelper.println("Teams and players are: ");
        for (Team team : teams.getTeams()) {
            this.iohelper.println("Team: " + team.getName() + " consists of players: {" +
                    team.getPlayers().stream().map(Player::getName).collect(Collectors.joining(",")) + "}");
        }
        this.iohelper.println(".....");
    }
    // print the winner or of the game ended
    public void printAfterTurn(Team team, Status status) {
        switch (status) {
            case WIN:
                this.printBoard();
                this.iohelper.println("Game completed, winner is team: " + team.getName());
                break;
            case END:
                this.printBoard();
                this.iohelper.println("Game Ended, no more cells to place X or O");
                break;
            default:
                break;
        }
    }

}
