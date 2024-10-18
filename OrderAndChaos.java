import java.util.Scanner;

/*
 * Order and Chaos is implemented in this class. It extends the Game abstract class.
 */
public class OrderAndChaos extends Game<XOCell> {

    private Checker checker;
    private static final int WIN_COUNT = 5;
    private static final int NUM_ROWS = 6;
    private static final int NUM_COLS = 6;
    // initializer
    public static OrderAndChaos init() {
        return new OrderAndChaos();
    }
    // constructor
    private OrderAndChaos() {
        super(new Board<>(NUM_ROWS, NUM_COLS, () -> XOCell.EMPTY), new IOHelper(new Scanner(System.in)));
        this.checker = new Checker();
    }
    // implement the play method that is inherited from the game class
    // this is used for playing the game of order and chaos
    @Override
    public void play() {
        int turn = 0;

        // initialize the two players
        TwoPlayer players = TwoPlayer.init(super.iohelper);
        // print player and board information before the game starts
        this.printBeforeStart(players);

        while (true) {
            // figure out the player playing in this turn
            Player player = (turn % 2 == 0 ? players.getP1() : players.getP2());
            // print the board so that player can choose the cell
            this.printBoard();
            // read the move and place the piece
            this.readMoveWithSign(player).doAction(this.board);
            // check the status of the board
            Status status = checker.check(board, WIN_COUNT);
            this.printAfterTurn(player, status);
            if (status == Status.WIN || status == Status.LOSE || status == Status.END) {
                return;
            }
            turn++;
        }
    }
    // print the game information before the game play
    private void printBeforeStart(TwoPlayer players) {
        this.iohelper.println("Order and Chaos is starting.....");
        this.iohelper.println("Board is of size: " + board.getNumRows() + " x " + board.getNumCols());
        this.iohelper.println(
                "Two players " + players.getP1() + " and " + players.getP2() + " are playing");
        this.iohelper.println(".....");
    }
    // print the winner or loose
    // this if for end of the game, either win or loss
    public void printAfterTurn(Player player, Status status) {
        switch (status) {
            case WIN:
                this.printBoard();
                this.iohelper.println("Game completed, winner is player: " + player.getName());
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
