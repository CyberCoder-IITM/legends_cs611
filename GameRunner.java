import java.util.Scanner;

/*
 * Plays the game
 */
public class GameRunner {
    private IOHelper ioHelper;
    private static final String WELCOME_MESSAGE =
            "\n=== Welcome to Legends of Valor ===\n" +
                    "A MOBA-style game where heroes battle monsters!\n";

    public GameRunner() {
        this.ioHelper = new IOHelper(new Scanner(System.in));
    }

    public void start() {
        while (true) {
            displayMainMenu();
            String choice = getMenuChoice();

            switch (choice) {
                case "1":
                    playGame();
                    break;
                case "2":
                    showInstructions();
                    break;
                case "3":
                    ioHelper.println("Thanks for playing! Goodbye!");
                    return;
                default:
                    ioHelper.printErr("Invalid choice!");
            }
        }
    }

    private void displayMainMenu() {
        ioHelper.println(WELCOME_MESSAGE);
        ioHelper.println("1. Start New Game");
        ioHelper.println("2. Show Instructions");
        ioHelper.println("3. Quit");
    }

    private String getMenuChoice() {
        return ioHelper.nextLine(
                "Enter your choice (1-3): ",
                "Invalid choice. Please enter 1, 2, or 3",
                s -> s.matches("[123]")
        );
    }

    private void playGame() {
        // Create and initialize game components
        WorldGenerator generator = new WorldGenerator();
        Legends game = generator.generate();

        // Start game loop
        game.play();

        // Ask to play again
        String playAgain = ioHelper.nextLine(
                "Would you like to play again? (y/n): ",
                "Please enter 'y' or 'n'",
                s -> s.matches("[yYnN]")
        );

        if (playAgain.toLowerCase().equals("n")) {
            System.exit(0);
        }
    }

    private void showInstructions() {
        ioHelper.println("\n=== Game Instructions ===");
        ioHelper.println("Goal: Guide your heroes to the monster's Nexus while defending your own!");

        ioHelper.println("\nControls:");
        ioHelper.println("W/A/S/D - Move hero");
        ioHelper.println("T - Teleport to another lane");
        ioHelper.println("R - Recall to Nexus");
        ioHelper.println("I - Access inventory");
        ioHelper.println("M - Access market (at Nexus only)");
        ioHelper.println("Q - Quit game");

        ioHelper.println("\nTerrain Types:");
        ioHelper.println("N - Nexus (spawn point and market)");
        ioHelper.println("I - Inaccessible space");
        ioHelper.println("P - Plain space");
        ioHelper.println("B - Bush (+10% Dexterity)");
        ioHelper.println("C - Cave (+10% Agility)");
        ioHelper.println("K - Koulou (+10% Strength)");

        ioHelper.println("\nPress Enter to continue...");
        ioHelper.nextLine("");
    }

    public static void main(String[] args) {
        GameRunner runner = new GameRunner();
        runner.start();
    }
}
