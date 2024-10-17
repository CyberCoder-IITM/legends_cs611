import java.util.Scanner;

/*
 * The top class which creates the game from the user input and plays it
 */
public class GameRunner {

    private GameRunner() {
    }

    public static void run() {
        IOHelper helper = new IOHelper(new Scanner(System.in));
        helper.prompt("Hello! Lets play a game.");
        while (true) {
            helper.prompt("Enter a number for the following options:");
            helper.prompt("1. Tic Tac Toe");
            helper.prompt("2. Order and Chaos");
            helper.prompt("3. Super Tic Tac Toe");
            helper.prompt("4. Quoridor");
            helper.prompt("5. Quit");

            int num = helper.nextLineInt("invalid number");

            if (num == 1) {
                helper.println("You have selected Tic Tac Toe");
                int rows = helper.nextLineInt("number of rows: ", "input should be an integer");
                int cols = helper.nextLineInt("number of cols: ", "input should be an integer");
                int win = helper.nextLineInt("number of consecutive X or O to win: ", "input should be an integer");
                TicTacToe.init(rows, cols, win).play();
            } else if (num == 2) {
                helper.println("You have selected Order and Chaos");
                OrderAndChaos.init().play();
            } else if (num == 3) {
                helper.println("You have selected Super Tic Tac Toe");
                int rows = helper.nextLineInt("number of Tic Tac Toe's in a row: ", "input should be an integer");
                int cols = helper.nextLineInt("number of Tic Tac Toe's in a colum: ", "input should be an integer");
                int win = helper.nextLineInt("number of consecutive Tic Tac Toe's needed to win: ",
                        "input should be an integer");
                SuperTicTacToe.init(rows, cols, win).play();
            } else if (num == 4) {
                helper.println("You have selected Quoridor");
                int rows = helper.nextLineInt("number of rows: ", "input should be an integer",
                        i -> i > 0 && i % 2 == 1, "should be an odd number");
                int cols = helper.nextLineInt("number of columns: ", "input should be an integer",
                        i -> i > 0 && i % 2 == 1, "should be an odd number");
                int np = helper.nextLineInt("number of players: ", "input should be an integer",
                        i -> i == 2 || i == 4, "It can be 2 or 4");
                int nw = helper.nextLineInt("number of walls each player can place: ", "input should be an integer",
                        i -> i >= 0, "should be greater than zero");
                Quoridor.init(rows, cols, np, nw).play();
            } else {
                helper.println("Quitting the game");
                break;
            }
            helper.println("...........................................");
            helper.prompt("Well done! Lets play another game.");
        }
    }

}
