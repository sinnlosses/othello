package othello;

/**
 * コマの表示に使用する文字の列挙.
 */
public enum PieceType {
    /** 白のコマ */
    WHITE("○"),
    /** 黒のコマ */
    BLACK("●"),
    /** 空の状態 */
    EMPTY("#");

    /**
     * コマの表示に使用する文字.
     */
    private String image;

    PieceType(String image) {
        this.image = image;
    }

    /**
     * 反対のコマを取得する.
     *
     * <p>引数が白なら黒、引数が黒なら白を返す</p>
     * <p>空きの場合は空きを返す</p>
     *
     * @param pieceType コマのタイプ
     * @return 反対のコマ
     */
    public static PieceType getEnemyType(final PieceType pieceType) {
        if (pieceType == BLACK) {
            return WHITE;
        } else if (pieceType == WHITE) {
            return BLACK;
        } else {
            return EMPTY;
        }
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
