package othello;

/**
 * コマの値と色を保持する.
 *
 * 白は-1, 黒は1, なにもない場所は0とする
 */
public enum PieceType {
    WHITE(-1, "○"),
    BLACK(1, "●"),
    EMPTY(0, "・");

    /**
     * コマの値.
     */
    private int value;

    /**
     * コマの表示に使用する文字.
     */
    private String image;

    PieceType(int value, String image) {
        this.value = value;
        this.image = image;
    }

    /**
     * コマの値を取得するgetterメソッド.
     *
     * 黒なら1, 白なら-1, 空きなら0
     * @return コマの値
     */
    public int getValue() {
        return value;
    }

    /**
     * コマの表示に使用する文字を取得するgetterメソッド.
     *
     * 黒なら●, 白なら○, 空きなら・
     * @return コマを表す文字
     */
    public String getImage() {
        return image;
    }

    /**
     * 入力した数値を対応するコマに変換する.
     *
     * @param value 入力する値(黒なら1, 白なら-1, 空きなら0)
     * @return 対応するコマ
     */
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
