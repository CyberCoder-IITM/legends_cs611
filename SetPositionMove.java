/* 
 * Represents a move which is placement of X or O on some position on the board
*/
public class SetPositionMove {

    private Position postion;
    private XOCell value;

    public SetPositionMove(Position postion, XOCell value) {
        this.postion = postion;
        this.value = value;
    }
    // return position
    public Position getPostion() {
        return postion;
    }
    // return value (XO cell)
    public XOCell getValue() {
        return value;
    }
    // sets the game piece at position on the board
    public void doAction(Board<XOCell> b) {
        b.set(postion, value);
    }
}
