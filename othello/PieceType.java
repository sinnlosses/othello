package othello;

/**
 * コマの表示に使用する文字の列挙.
 */
public enum PieceType {
    WHITE("○"),
    BLACK("●"),
    EMPTY("#");

    /**
     * コマの表示に使用する文字.
     */
    private String image;

    PieceType(String image) {
        this.image = image;
    }

    /**
     * コマの表示に使用する文字を取得する.
     *
     * @return コマを表す文字
     */
    @Override
    public String toString() {
        return image;
    }

}
