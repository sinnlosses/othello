package othello;

/**
 * コマの状態を保持、操作する.
 */
public final class Piece {

    /**
     * コマの状態
     */
    private PieceType state;

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
        if (pieceType == PieceType.BLACK) {
            return PieceType.WHITE;
        } else if (pieceType == PieceType.WHITE) {
            return PieceType.BLACK;
        } else {
            return PieceType.EMPTY;
        }
    }

    /**
     * 空き状態を初期値とするコンストラクタ.
     */
    public Piece() {
        this.state = PieceType.EMPTY;
    }

    /**
     * コマの状態を取得するgetterメソッド.
     *
     * @return コマの状態
     */
    public PieceType getState() {
        return state;
    }

    /**
     * コマの状態を更新するsetterメソッド.
     *
     * @param state 更新後のコマの状態
     */
    public void setState(final PieceType state) {
        this.state = state;
    }

    /**
     * コマが空きを表す状態かどうかを判定するメソッド.
     *
     * @return 空き状態ならtrue, そうでないならfalse
     */
    public boolean isEmpty() {
        return state == PieceType.EMPTY;
    }

    /**
     * コマの状態を黒なら白に、白なら黒に変換する.
     */
    public void flip() {
        state = Piece.getEnemyType(state);
    }

    /**
     * コマの色(表示文字)を表示する
     *
     * @return コマの表示文字
     */
    @Override
    public String toString() {
        return state.toString();
    }
}
