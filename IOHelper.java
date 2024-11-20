
import java.util.Scanner;
import java.util.function.Predicate;

/*
 * A simple helper for all IO operations
 */

public class IOHelper {
    // ANSI color codes for terminal output
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String BELL = "\u0007";

    private Scanner in;

    public IOHelper(Scanner in) {
        this.in = in;
    }

    // Get string input with specific prompt
    public String nextLine(String prompt) {
        this.prompt(prompt);
        this.waitInput();
        return in.nextLine();
    }

    // Get validated string input
    public String nextLine(String prompt, String err, Predicate<String> validator) {
        while (true) {
            String s = nextLine(prompt);
            if (validator.test(s)) {
                return s;
            } else {
                this.printErr(err);
            }
        }
    }

    // Get integer input with validation
    public int nextLineInt(String prompt, String err) {
        while (true) {
            this.prompt(prompt);
            this.waitInput();
            try {
                String s = in.nextLine();
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                this.printErr(err);
            }
        }
    }

    // Get integer input for game choices
    public int nextLineInt(String err) {
        while (true) {
            try {
                this.waitInput();
                String s = in.nextLine();
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                this.printErr(err);
            }
        }
    }

    // Add this specific method for integer input with validation
    public int nextLineInt(String prompt, String err, Predicate<Integer> validator) {
        while (true) {
            try {
                this.prompt(prompt);
                this.waitInput();
                String input = in.nextLine();
                int value = Integer.parseInt(input);
                if (validator.test(value)) {
                    return value;
                } else {
                    this.printErr(err);
                }
            } catch (NumberFormatException e) {
                this.printErr(err);
            }
        }
    }

    // Get validated integer input within range

    // Display methods with different colors
    public void println(String s) {
        System.out.println(ANSI_GREEN + s + ANSI_RESET);
    }

    public void printErr(String s) {
        System.out.println(ANSI_RED + s + ANSI_RESET + BELL);
    }

    public void print(String s) {
        System.out.print(s);
    }

    public void prompt(String s) {
        System.out.println(ANSI_YELLOW + "[+] " + s + ANSI_RESET);
    }

    public void waitInput() {
        System.out.print(ANSI_BLUE + ">> " + ANSI_RESET);
    }

    private String getCellSymbol(CellType cellType) {
        switch(cellType) {
            case NEXUS: return "N";
            case INACCESSIBLE: return "I";
            case BUSH: return "B";
            case CAVE: return "C";
            case KOULOU: return "K";
            case PLAIN: return "P";
            default: return " ";
        }
    }


    public void displayGameControls() {
        println("\n=== Controls ===");
        println("W/A/S/D - Move");
        println("T - Teleport");
        println("R - Recall");
        println("I - Inventory");
        println("M - Market (at Nexus only)");
        println("Q - Quit game");
    }

    public void displayCombatResult(String attacker, String defender, double damage, boolean dodged) {
        if (dodged) {
            println(defender + " dodged " + attacker + "'s attack!");
        } else {
            println(attacker + " dealt " + damage + " damage to " + defender);
        }
    }
}