package othello;

public enum PieceType {
    WHITE(-1, "○"),
    BLACK(1, "●"),
    EMPTY(0, "・");

    private int value;
    private String image;

    PieceType(int value, String image) {
        this.value = value;
        this.image = image;
    }

    public int getValue() {
        return value;
    }

    public String getImage() {
        return image;
    }

    public static PieceType getType(int value) {
        PieceType[] pieceTypes = PieceType.values();
        for (PieceType p : pieceTypes) {
            if (p.getValue() == value) {
                return p;
            }
        }
        return null;
    }

}
