package othello;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 座標を表すクラス.
 * <p>
 * 行番号, 列番号を保持する. 一度座標を設定すると取得はできるが再設定はできない. インスタンスの生成にはファクトリメソッドを通して行う.
 */
public final class Coordinate {

  /**
   * 生成された座標を保持するリスト.
   */
  private static final List<Coordinate> coordinates = new ArrayList<>();
  /**
   * 座標の比較関数.
   */
  private static final Comparator<Coordinate> comparator = Comparator
      .comparingInt(Coordinate::getRow).thenComparingInt(Coordinate::getCol);
  /**
   * 行番号.
   */
  private final int row;
  /**
   * 列番号.
   */
  private final int col;

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
   * 本インスタンスを返すファクトリメソッド.
   * <p>
   * 指定した行番号と列番号に一致するオブジェクトがすでに保持されている場合それを返す. 新規であれば新しくオブジェクトを生成して返す.
   *
   * @param row 行番号
   * @param col 列番号
   * @return インスタンス
   */
  synchronized public static Coordinate valueOf(final int row, final int col) {
    int index = binarySearch(row, col);
    if (index >= 0) {
      return coordinates.get(index);
    }

    Coordinate newCoordinate = new Coordinate(row, col);
    coordinates.add(newCoordinate);
    coordinates.sort(comparator);

    return newCoordinate;
  }

  /**
   * 指定した行番号と列番号を持つ座標を二分探索する.
   *
   * @param row 探索する行番号
   * @param col 探索する列番号
   * @return 見つかった場合そのインデックス, そうでない場合は-1
   */
  private static int binarySearch(final int row, final int col) {
    int low = 0;
    int high = coordinates.size() - 1;

    while (low <= high) {
      int mid = (low + high) / 2;
      Coordinate target = coordinates.get(mid);
      if (target.getRow() < row) {
        low = mid + 1;
      } else if (target.getRow() > row) {
        high = mid - 1;
      } else {
        if (target.getCol() < col) {
          low = mid + 1;
        } else if (target.getCol() > col) {
          high = mid - 1;
        } else {
          return mid;
        }
      }
    }
    return -(low + 1);
  }

  /**
   * 指定されたベクトルへ移動した座標を返す.
   *
   * @param moveTo 移動方向
   * @return 移動後の座標
   */
  public Coordinate move(Vector moveTo) {
    final int movedRow = row + moveTo.getRow();
    final int movedCol = col + moveTo.getCol();

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
