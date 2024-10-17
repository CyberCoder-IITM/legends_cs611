
import java.util.List;

/*
 * A team consists of the players, its name and the sign they are playing for.
 */
class Team {
    private XOCell sign;
    private List<Player> players;
    private String name;

    public Team(XOCell cell, List<Player> players, String name) {
        this.sign = cell;
        this.players = players;
        this.name = name;
    }

    public XOCell getSign() {
        return sign;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public String getName() {
        return name;
    }
}