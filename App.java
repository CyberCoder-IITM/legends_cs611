/*
 * The main class
 */

import java.util.Scanner;

public class App {
    private static final String TITLE =
            "\n" +
                    "╔══════════════════════════════════════════╗\n" +
                    "║           Legends of Valor               ║\n" +
                    "║    A MOBA-style Heroes vs Monsters Game  ║\n" +
                    "╚══════════════════════════════════════════╝\n";

    public static void main(String[] args) {
        try {
            // Initialize IO helper
            Scanner scanner = new Scanner(System.in);
            IOHelper ioHelper = new IOHelper(scanner);

            // Display welcome message
            displayWelcome(ioHelper);

            // Create and start game runner
            GameRunner gameRunner = new GameRunner();
            gameRunner.start();

        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Cleanup
            System.out.println("\nThanks for playing!");
        }
    }

    private static void displayWelcome(IOHelper ioHelper) {
        ioHelper.println(TITLE);
        ioHelper.println("Welcome to Legends of Valor!");
        ioHelper.println("Get ready to battle in this MOBA-style adventure.");
        ioHelper.println("\nPress Enter to continue...");
        ioHelper.nextLine("");
    }
}
