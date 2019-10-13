package othello;

/**
 * 座標を表すクラス
 */
public class Coordinate {
    /**
     * 行番号
     */
    private int row;
    /**
     * 列番号
     */
    private int col;

    public Coordinate(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * 行番号を取得する.
     * @return 行番号
     */
    public int getRow() {
        return row;
    }

    /**
     * 列番号を取得する
     * @return 列番号
     */
    public int getCol() {
        return col;
    }
}
