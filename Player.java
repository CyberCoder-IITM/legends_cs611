/* 
 * Represents a player playing in the game
*/
public class Player {
    private String name;
    // constructor
    public Player(String name) {
        this.name = name;
    }
    // return the name
    public String getName() {
        return name;
    }
    // set the name of the player
    public void setName(String name) {
        this.name = name;
    }
    // palyer's tostirng method
    @Override
    public String toString() {
        return name;
    }
}
