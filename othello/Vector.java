package othello;

/**
 * 方向を表すクラス.
 *
 * <p>
 * 左上, 上, 右上,
 * 左, 右,
 * 左下, 下, 右下,
 * 上記の方向を保持する.
 * 各項目は行および列の方向を表す整数型の値を
 * 状態として持つ.
 * <p>
 * 例 : 左上(行方向, 列方向) = (-1, -1).
 * 例 : 上(行方向, 列方向) = (-1, 0)
 * 例 : 右下(行方向, 列方向) = (1, 1).
 * </p>
 */
public enum Vector {
    LEFT_UPPER(-1, -1),
    UP(-1, 0),
    RIGHT_UPPER(-1, 1),
    LEFT(0, -1),
    RIGHT(0, 1),
    LEFT_DOWN(1, -1),
    DOWN(1, 0),
    RIGHT_DOWN(1, 1);

    /**
     * 行方向.
     */
    private int row;
    /**
     * 列方向.
     */
    private int col;

    Vector(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * 行方向を取得する.
     *
     * @return 行方向を表す整数値
     */
    public int getRow() {
        return row;
    }

    /**
     * 列方向を取得する.
     *
     * @return 列方向を表す整数値
     */
    public int getCol() {
        return col;
    }
}
