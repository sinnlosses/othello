package othello;

import java.util.ArrayList;
import java.util.List;

/**
 * 座標を表すクラス.
 *
 * 行番号, 列番号を保持するが, 一度座標を設定すると
 * 取得はできるが設定し直すことはできない.
 */
public final class Coordinate {
    /**
     * 生成された座標を保持するリスト
     */
    private static final List<Coordinate> coordinates = new ArrayList<>();
    /**
     * 行番号
     */
    private final int row;
    /**
     * 列番号
     */
    private final int col;

    /**
     * 本インスタンスを返すファクトリメソッド.
     *
     * 指定した行番号と列番号に一致する座標がすでに保持されている場合それを返す.
     * 新規の座標であれば新しくオブジェクトを生成して返す
     *
     * @param row 行番号
     * @param col 列番業
     * @return 座標を設定したインスタンス
     */
    public synchronized static Coordinate valueOf(final int row, final int col) {
        for (Coordinate c : coordinates) {
            if (c.getRow() == row && c.getCol() == col) {
                return c;
            }
        }
        Coordinate newCoordinate = new Coordinate(row, col);
        coordinates.add(newCoordinate);
        return newCoordinate;
    }

    /**
     * 座標を設定するコンストラクタ.
     *
     * @param row 行番号
     * @param col 列番号
     */
    private Coordinate(final int row, final int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * 行番号を取得する.
     *
     * @return 行番号
     */
    public int getRow() {
        return row;
    }

    /**
     * 列番号を取得する.
     *
     * @return 列番号
     */
    public int getCol() {
        return col;
    }

    /**
     * 座標の情報を返す.
     *
     * @return 行番号と列番号
     */
    @Override
    public String toString() {
        return String.format("row = %d, col = %d", row, col);
    }
}
