package othello;

import java.util.ArrayList;
import java.util.List;

/**
 * 座標を表すクラス.
 *
 * 行番号, 列番号を保持する.
 * 一度座標を設定すると取得はできるが再設定はできない.
 * インスタンスの生成にはファクトリメソッドを通して行う.
 */
public final class Coordinate {
    /**
     * 生成された座標を保持するリスト.
     */
    private static final List<Coordinate> coordinates = new ArrayList<>();
    /**
     * 行番号.
     */
    private final int row;
    /**
     * 列番号.
     */
    private final int col;

    /**
     * 本インスタンスを返すファクトリメソッド.
     *
     * 指定した行番号と列番号に一致するオブジェクトがすでに保持されている場合それを返す.
     * 新規であれば新しくオブジェクトを生成して返す.
     *
     * @param row 行番号
     * @param col 列番業
     * @return インスタンス
     */
    synchronized public static Coordinate valueOf(final int row, final int col) {
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
     * 座標を指定されたベクトルへ移動する.
     *
     * @param moveTo 移動方向.
     * @return 移動後の座標.
     */
    public Coordinate move(Vector moveTo) {
        int movedRow = getRow() + moveTo.getVectorRow();
        int movedCol = getCol() + moveTo.getVectorCol();

        return Coordinate.valueOf(movedRow, movedCol);
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
