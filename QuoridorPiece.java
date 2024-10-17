public class QuoridorPiece {

    private QuoridorPieceType type;
    private String label;

    public QuoridorPieceType getType() {
        return type;
    }

    public String getLabel() {
        return label;
    }

    public static QuoridorPiece Wall() {
        return new QuoridorPiece(QuoridorPieceType.Wall, "");
    }

    public static QuoridorPiece Empty() {
        return new QuoridorPiece(QuoridorPieceType.Empty, "");
    }

    public static QuoridorPiece Pawn(String label) {
        return new QuoridorPiece(QuoridorPieceType.Pawn, label);
    }

    public QuoridorPiece(QuoridorPieceType type, String label) {
        this.type = type;
        this.label = label;
    }

    public boolean isWall() {
        return this.type.equals(QuoridorPieceType.Wall);
    }

    public boolean isEmpty() {
        return this.type.equals(QuoridorPieceType.Empty);
    }

    public boolean isPawn() {
        return this.type.equals(QuoridorPieceType.Pawn);
    }

}
