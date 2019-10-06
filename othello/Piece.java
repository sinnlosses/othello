package othello;

/**
 * コマの状態を保持、操作するクラス.
 */
public class Piece {

    /**
     * コマの状態
     */
    private PieceType state;

    /**
     * コンストラクタ.
     *
     * 初期値は空き状態
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
        state = PieceType.getEnemyType(state);
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
