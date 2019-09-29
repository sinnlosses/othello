package othello;

public class Piece {

    /**
     * コマの状態
     */
    private PieceType state;

    /**
     * コンストラクタ.
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
    public void flipPiece() {
        state = PieceType.getType(state.getValue() * -1);
    }

    /**
     * コマの数字の状態に基づいて色を表示する
     *
     * @return
     */
    @Override
    public String toString() {
        return state.getImage();
    }
}
