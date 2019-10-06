package othello;

/**
 * コマの色を保持する.
 */
public enum PieceType {
    WHITE("○"),
    BLACK("●"),
    EMPTY("・");

    /**
     * コマの表示に使用する文字.
     */
    private String image;

    PieceType(String image) {
        this.image = image;
    }

    /**
     * 反対のコマを取得する.
     * <p>引数が白なら黒、引数が黒なら白を返す</p>
     * <p>空きの場合は空きを返す</p>
     *
     * @param pieceType コマのタイプ
     * @return 反対のコマ
     */
    public static PieceType getEnemyType(PieceType pieceType) {
        if (pieceType == PieceType.BLACK) {
            return PieceType.WHITE;
        } else if (pieceType == PieceType.WHITE) {
            return PieceType.BLACK;
        } else {
            return PieceType.EMPTY;
        }
    }

    /**
     * コマの表示に使用する文字を取得する
     *
     * @return コマを表す文字
     */
    @Override
    public String toString() {
        return image;
    }

}
