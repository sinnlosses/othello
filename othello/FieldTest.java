package othello;

import org.junit.jupiter.api.BeforeEach;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class FieldTest {
    private Field othello;
    @BeforeEach
    void setUp() {
        othello = new Field();
    }

    /**
     * テスト項目
     * <ol>
     *     <li>正常な入力 = 1文字かつa...h</li>
     *     <li>異常な入力 = 入力が1文字でないまたはa...hでない</li>
     *     <li>境界値 = a, h, i</li>
     * </ol>
     *
     */
    @org.junit.jupiter.api.Test
    void toRowNumber() {
        // 正常な入力
        int normalInput = Field.toRowNumber("c");
        assertEquals(2, normalInput);

        // 異常な入力
        int errorLength = Field.toRowNumber("HH");
        assertEquals(-1, errorLength);

        errorLength = Field.toRowNumber("");
        assertEquals(-1, errorLength);

        int errorInput = Field.toRowNumber("z");
        assertEquals(-1, errorInput);

        // 境界値
        int normalAtFirst = Field.toRowNumber("a");
        assertEquals(0, normalAtFirst);

        int normalAtLast = Field.toRowNumber("h");
        assertEquals(7, normalAtLast);

        int error = Field.toRowNumber("i");
        assertEquals(-1, error);
    }

    /**
     * テスト項目
     * <ol>
     *     <li>正常な入力 = 1文字かつ1...8</li>
     *     <li>異常な入力 = 入力が1文字でないまたは1...8でない</li>
     *     <li>境界値 = 0, 1, 8, 9</li>
     * </ol>
     *
     */
    @org.junit.jupiter.api.Test
    void toColNumber() {
        // 正常な入力
        int normalInput = Field.toColNumber("3");
        assertEquals(2, normalInput);

        // 異常な入力
        int errorLength = Field.toColNumber("10");
        assertEquals(-1, errorLength);

        // 境界値
        int normalAtFirst = Field.toColNumber("1");
        assertEquals(0, normalAtFirst);

        int errorOutOfFirst = Field.toColNumber("0");
        assertEquals(-1, errorOutOfFirst);

        int normalAtLast = Field.toColNumber("8");
        assertEquals(7, normalAtLast);

        int errorOutOfLast = Field.toColNumber("9");
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
        othello.nextPlayer();
        PieceType changedState = othello.getCurrentTurn();
        assertEquals(PieceType.WHITE, changedState);
    }

    /**
     * 初期値の時点ではゲームオーバーではない
     * コマが片方無くなったか、コマが白黒で埋まったかでゲームオーバー.
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
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

    @org.junit.jupiter.api.Test
    void putPiece() throws NoSuchFieldException, IllegalAccessException {
        othello.putPiece(Coordinate.valueOf(4, 5));

        Class<?> othelloClass = othello.getClass();
        java.lang.reflect.Field ref = othelloClass.getDeclaredField("field");
        ref.setAccessible(true);
        Piece[][] field = (Piece[][]) ref.get(othello);

        assertEquals(PieceType.BLACK, field[4][5].getState());
    }

    @org.junit.jupiter.api.Test
    void printField() {
        othello.printField();
    }

    @org.junit.jupiter.api.Test
    void testToString() {
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

        boolean exist = (boolean)refMethod.invoke(othello, Coordinate.valueOf(4, 5), Vector.LEFT);
        assertTrue(exist);
    }
}