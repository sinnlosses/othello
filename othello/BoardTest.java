package othello;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;

class BoardTest {

  private Board othello;

  @BeforeEach
  void setUp() {
    othello = new Board();
  }

  /**
   * テスト項目
   * <ol>
   *     <li>正常な入力 = 1文字かつa...h</li>
   *     <li>異常な入力 = 入力が1文字でないまたはa...hでない</li>
   *     <li>境界値 = a, h, i</li>
   * </ol>
   */
  @org.junit.jupiter.api.Test
  void toRowNumber() {
    // 正常な入力
    int normalInput = Board.toRowNumber("c");
    assertEquals(2, normalInput);

    // 異常な入力
    int errorLength = Board.toRowNumber("HH");
    assertEquals(-1, errorLength);

    errorLength = Board.toRowNumber("");
    assertEquals(-1, errorLength);

    int errorInput = Board.toRowNumber("z");
    assertEquals(-1, errorInput);

    // 境界値
    int normalAtFirst = Board.toRowNumber("a");
    assertEquals(0, normalAtFirst);

    int normalAtLast = Board.toRowNumber("h");
    assertEquals(7, normalAtLast);

    int error = Board.toRowNumber("i");
    assertEquals(-1, error);
  }

  /**
   * テスト項目
   * <ol>
   *     <li>正常な入力 = 1文字かつ1...8</li>
   *     <li>異常な入力 = 入力が1文字でないまたは1...8でない</li>
   *     <li>境界値 = 0, 1, 8, 9</li>
   * </ol>
   */
  @org.junit.jupiter.api.Test
  void toColNumber() {
    // 正常な入力
    int normalInput = Board.toColNumber("3");
    assertEquals(2, normalInput);

    // 異常な入力
    int errorLength = Board.toColNumber("10");
    assertEquals(-1, errorLength);

    // 境界値
    int normalAtFirst = Board.toColNumber("1");
    assertEquals(0, normalAtFirst);

    int errorOutOfFirst = Board.toColNumber("0");
    assertEquals(-1, errorOutOfFirst);

    int normalAtLast = Board.toColNumber("8");
    assertEquals(7, normalAtLast);

    int errorOutOfLast = Board.toColNumber("9");
    assertEquals(-1, errorOutOfLast);
  }

  /**
   * 初期値としての手番が設定と一致するか調べる
   */
  @org.junit.jupiter.api.Test
  void getCurrentTurn() {
    PieceType state = othello.getCurrentTurn();
    assertEquals(PieceType.BLACK, state);
  }

  /**
   * 手番が次のプレイヤーに移っているか調べる
   */
  @org.junit.jupiter.api.Test
  void nextPlayer() {
    PieceType initState = othello.getCurrentTurn();
    assertEquals(PieceType.BLACK, initState);
    othello.nextTurn();
    PieceType changedState = othello.getCurrentTurn();
    assertEquals(PieceType.WHITE, changedState);
  }

  /**
   * 初期値の時点ではゲームオーバーではない コマが片方無くなったか、コマが白黒で埋まったかでゲームオーバー.
   *
   * @throws NoSuchFieldException   フィールド名がない場合.
   * @throws IllegalAccessException アクセス権がない場合.
   */
  @org.junit.jupiter.api.Test
  void isGameOver() throws NoSuchFieldException, IllegalAccessException {
    boolean result = othello.isGameOver();
    assertFalse(result);

    Class<?> ref = othello.getClass();
    java.lang.reflect.Field field = ref.getDeclaredField("field");
    field.setAccessible(true);
    Piece[][] pieces = (Piece[][]) field.get(othello);
    pieces[3][3].setState(PieceType.BLACK);
    pieces[4][4].setState(PieceType.BLACK);

    result = othello.isGameOver();
    assertTrue(result);
  }

  @org.junit.jupiter.api.Test
  void printCurrentSituation() {
  }

  @org.junit.jupiter.api.Test
  void canPutForCurrentTurn() throws NoSuchFieldException, IllegalAccessException {
    boolean canPut = othello.canPutForCurrentTurn();
    assertTrue(canPut);

    Class<?> othelloClass = othello.getClass();
    java.lang.reflect.Field ref = othelloClass.getDeclaredField("field");
    ref.setAccessible(true);
    Piece[][] field = (Piece[][]) ref.get(othello);
    field[3][3].setState(PieceType.BLACK);
    field[4][4].setState(PieceType.BLACK);

    canPut = othello.canPutForCurrentTurn();
    assertFalse(canPut);
  }

  @org.junit.jupiter.api.Test
  void canPutPiece() {
    boolean canPut = othello.canPutPiece(Coordinate.valueOf(4, 5));
    assertTrue(canPut);

    canPut = othello.canPutPiece(Coordinate.valueOf(4, 6));
    assertFalse(canPut);
  }

  @org.junit.jupiter.api.Test
  void flipPiecesFromPlaced() {
  }

  @Ignore
  @org.junit.jupiter.api.Test
  void putPiece() throws NoSuchFieldException, IllegalAccessException {
    Class<?> othelloClass = othello.getClass();
    java.lang.reflect.Field ref = othelloClass.getDeclaredField("field");
    ref.setAccessible(true);
    Piece[][] field = (Piece[][]) ref.get(othello);
    field[4][5].setState(PieceType.BLACK);

    assertEquals(PieceType.BLACK, field[4][5].getState());
  }

  @org.junit.jupiter.api.Test
  void testExistOwnPieceAhead() throws NoSuchMethodException,
      InvocationTargetException,
      IllegalAccessException, NoSuchFieldException {
    Class<?> othelloClass = othello.getClass();
    java.lang.reflect.Field refField = othelloClass.getDeclaredField("field");
    refField.setAccessible(true);
    Piece[][] field = (Piece[][]) refField.get(othello);
    field[4][5].setState(PieceType.BLACK);
    Method refMethod = othelloClass.getDeclaredMethod("existOwnPieceAhead",
        Coordinate.class,
        Vector.class);
    refMethod.setAccessible(true);

    boolean exist = (boolean) refMethod.invoke(othello, Coordinate.valueOf(4, 5), Vector.LEFT);
    assertTrue(exist);
  }

  @org.junit.jupiter.api.Test
  void testGetEachPiecesCnt() throws NoSuchMethodException,
      InvocationTargetException,
      IllegalAccessException {
    Class<?> othelloClass = othello.getClass();
    Method ref = othelloClass.getDeclaredMethod("getEachPiecesCnt");
    ref.setAccessible(true);

    Map<PieceType, Integer> expected = (Map<PieceType, Integer>) ref.invoke(othello);

    assertEquals(expected.get(PieceType.BLACK), 2);
    assertEquals(expected.get(PieceType.WHITE), 2);
    assertEquals(expected.get(PieceType.EMPTY), 60);
  }


}