public class QuoridorPiece {

    private QuoridorPieceType type;
    private String label;
    // return the game piece type
    public QuoridorPieceType getType() {
        return type;
    }
    // return the string representation of the game piece
    public String getLabel() {
        return label;
    }
    // set the game piece to be a wall
    public static QuoridorPiece Wall() {
        return new QuoridorPiece(QuoridorPieceType.Wall, "");
    }
    // set the game piece to be empty piece
    public static QuoridorPiece Empty() {
        return new QuoridorPiece(QuoridorPieceType.Empty, "");
    }
    // set the game piece to be a pawn(player)
    public static QuoridorPiece Pawn(String label) {
        return new QuoridorPiece(QuoridorPieceType.Pawn, label);
    }
    // constructor
    public QuoridorPiece(QuoridorPieceType type, String label) {
        this.type = type;
        this.label = label;
    }
    // check if wall piece
    public boolean isWall() {
        return this.type.equals(QuoridorPieceType.Wall);
    }
    // check if empty piece
    public boolean isEmpty() {
        return this.type.equals(QuoridorPieceType.Empty);
    }
    // check if pawn piece
    public boolean isPawn() {
        return this.type.equals(QuoridorPieceType.Pawn);
    }

}
