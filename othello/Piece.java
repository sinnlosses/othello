package othello;

public class Piece {

    private PieceType state;

    public Piece() {
        this.state = PieceType.EMPTY;
    }

    public PieceType getState() {
        return state;
    }

    public void setState(final PieceType state) {
        this.state = state;
    }

    public boolean isEmpty() {
        return state == PieceType.EMPTY;
    }

    public void flipPiece() {
        state = PieceType.getType(state.getValue()*-1);
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
