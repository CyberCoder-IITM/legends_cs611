
import java.util.List;

/*
 * A team consists of the players, its name and the sign they are playing for.
 */
class Team {
    private XOCell sign;
    private List<Player> players;
    private String name;
    // constructor
    public Team(XOCell cell, List<Player> players, String name) {
        this.sign = cell;
        this.players = players;
        this.name = name;
    }
    // get the game piece the current team uses
    public XOCell getSign() {
        return sign;
    }
    // get the players in this team
    public List<Player> getPlayers() {
        return players;
    }
    // get the name of this team
    public String getName() {
        return name;
    }
}