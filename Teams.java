
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/* 
 * Consists of multiple teams, all playing in the game.
*/
class Teams {
    private List<Team> teams;
    // constructor, list of teams
    private Teams() {
        this.teams = new ArrayList<>();
    }
    // adding a team to list of teams
    private void add(Team team) {
        teams.add(team);
    }
    // return the list of teams
    public List<Team> getTeams() {
        return teams;
    }
    // return the total number of teams
    public int numTeams() {
        return this.teams.size();
    }
    // get a random player from specified team
    public Player randomPlayerInTeam(int idx, Random rand) {
        List<Player> players = this.teams.get(idx).getPlayers();
        return players.get(rand.nextInt(players.size()));
    }
    // initializer, get the team number from user, and players in each team
    public static Teams init(IOHelper helper) {
        int numTeams = helper.nextLineInt("Enter the number of teams: ", "number is invalid. Try again",
                i -> i >= 2 && i%2==0, "should be at least 2 and even number.");

        Teams teams = new Teams();

        int totalPlayers = 0;
        for (int i = 0; i < numTeams; i++) {
            List<Player> players = new ArrayList<>();
            String teamName = helper.nextLine("Team " + (i + 1) + ", Enter your team name:");

            int numPlayers = helper.nextLineInt("Enter the number of players in " + teamName + ":",
                    "number is invalid. Try again",
                    y -> y >= 1 , "should be at least 1");
            for (int j = totalPlayers + 1; j <= totalPlayers + numPlayers; j++) {
                String playerName = helper.nextLine("Player " + j + ", Enter your name:");
                Player player = new Player(playerName);
                players.add(player);
            }
            totalPlayers += numPlayers;

            String xoSign;
            while (true) {
                xoSign = helper.nextLine("Team " + teamName + ", Enter your sign(X or O):");
                if (!xoSign.equals("X") && !xoSign.equals("O")) {
                    helper.printErr("Sign can only be X or O, try again");
                } else {
                    break;
                }
            }

            Team team = new Team(xoSign.equals("X") ? XOCell.X : XOCell.O, players, teamName);
            teams.add(team);

        }

        return teams;
    }

}