/*
 * Players of a two player game are in this class.
 */
public class TwoPlayer {
    private Player p1;
    private Player p2;

    public TwoPlayer(Player p1, Player p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public Player getP1() {
        return p1;
    }

    public Player getP2() {
        return p2;
    }

    public static TwoPlayer init(IOHelper ioHelper) {
        String p1Name = ioHelper.nextLine("name of player 1:");
        String p2Name = ioHelper.nextLine("name of player 2:");
        Player p1 = new Player(p1Name);
        Player p2 = new Player(p2Name);
        return new TwoPlayer(p1, p2);
    }

}