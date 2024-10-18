import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class QuoridorConfig {
    int numPlayers;
    List<QuoridorPlayerConfig> confs;
    // constructor
    public QuoridorConfig(int numPlayers, List<QuoridorPlayerConfig> confs) {
        this.numPlayers = numPlayers;
        this.confs = confs;
    }
    // initialization basic configuration for quoridor game
    // get the row and cols of the board, the num of players and num of walls,
    // also specify the player's starting position
    public static <T> QuoridorConfig init(int numPlayers, int numWalls, Board<T> b, IOHelper helper) {
        int n = b.getNumRows();
        int m = b.getNumRows();
        if (numPlayers == 2) {
            return new QuoridorConfig(2, Arrays.asList(
                    new QuoridorPlayerConfig(readPlayer(helper, 0), numWalls, new Position(n - 2, (m - 1) / 2), "A",
                            (i, j) -> i == 1),
                    new QuoridorPlayerConfig(readPlayer(helper, 1), numWalls, new Position(1, (m - 1) / 2), "B",
                            (i, j) -> i == n - 2)));
        } else {
            return new QuoridorConfig(4, Arrays.asList(
                    new QuoridorPlayerConfig(readPlayer(helper, 0), numWalls, new Position(n - 2, (m - 1) / 2), "A",
                            (i, j) -> i == 1),
                    new QuoridorPlayerConfig(readPlayer(helper, 1), numWalls, new Position((n - 1) / 2, m - 2), "B",
                            (i, j) -> j == 1),
                    new QuoridorPlayerConfig(readPlayer(helper, 2), numWalls, new Position(1, (m - 1) / 2), "C",
                            (i, j) -> i == n - 2),
                    new QuoridorPlayerConfig(readPlayer(helper, 3), numWalls, new Position((n - 1) / 2, 1), "D",
                            (i, j) -> j == m - 2)));
        }
    }
    // get name for player
    // create player object
    private static Player readPlayer(IOHelper ioHelper, int i) {
        String name = ioHelper.nextLine("name of player " + (i + 1) + ": ");
        return new Player(name);
    }
    // get the list of players
    public List<Player> getPlayers() {
        return this.confs.stream().map(QuoridorPlayerConfig::getPlayer).collect(Collectors.toList());
    }

    public Player getPlayerForTurn(int turn) {
        return this.getPlayers().get(turn % this.numPlayers);
    }
    // list the player configuration
    public List<QuoridorPlayerConfig> getConfigs() {
        return this.confs;
    }
    // get the configuration for rand player based on turn.
    public QuoridorPlayerConfig getForTurn(int turn) {
        return this.getConfigs().get(turn % this.numPlayers);
    }

}
