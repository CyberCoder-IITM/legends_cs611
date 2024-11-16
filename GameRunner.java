import java.util.Scanner;

/*
 * Plays the game
 */
public class GameRunner {

    public static void run() {
        IOHelper iohelper = new IOHelper(new Scanner(System.in));
        iohelper.println("generating a random world and heroes");
        Legends legends = new WorldGenerator().generate();
        legends.play();
    }

}
