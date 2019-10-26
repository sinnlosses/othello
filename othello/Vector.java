package othello;

/**
 * 方向を表すクラス.
 *
 * 左上, 上, 右上,
 * 左, 右,
 * 左下, 下, 右下,
 * 上記の方向を保持する.
 * 各項目は行および列の方向を表す整数型の値を
 * 状態として持つ
 *
 * 例 : 左上 = (-1, -1)
 */
public enum Vector {
    LEFT_UPPER(-1, -1),
    UP(0, -1),
    RIGHT_UPPER(1, -1),
    LEFT(-1, 0),
    RIGHT(1, 0),
    LEFT_DOWN(-1, 1),
    DOWN(0, 1),
    RIGHT_DOWN(1, 1);

    /**
     * 行方向.
     */
    private int vectorRow;
    /**
     * 列方向.
     */
    private int vectorCol;

    Vector(int vectorRow, int vectorCol) {
        this.vectorRow = vectorRow;
        this.vectorCol = vectorCol;
    }

    /**
     * 行方向を取得する.
     *
     * @return 行方向
     */
    public int getVectorRow() {
        return vectorRow;
    }

    /**
     * 列方向を取得する.
     *
     * @return 列方向
     */
    public int getVectorCol() {
        return vectorCol;
    }
}
