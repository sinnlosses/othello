package othello;

/**
 * コマの状態を保持、操作する.
 */
public final class Piece {

  /**
   * コマの状態.
   */
  private PieceType state;

  /*
   * 空き状態を初期値とするコンストラクタ.
   */
  public Piece() {
    this.state = PieceType.EMPTY;
  }

  /**
   * コマの状態を取得する.
   *
   * @return コマの状態
   */
  public PieceType getState() {
    return state;
  }

  /**
   * コマの状態を更新する.
   *
   * @param state 更新後のコマの状態
   */
  public void setState(final PieceType state) {
    this.state = state;
  }

  /**
   * コマが空きを表す状態かどうかを判定する.
   *
   * @return 空き状態なら {@code true}
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
