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

    public Position getPostion() {
        return postion;
    }

    public XOCell getValue() {
        return value;
    }

    public void doAction(Board<XOCell> b) {
        b.set(postion, value);
    }
}
