package othello;

import java.util.HashMap;
import java.util.Map;

/**
 * オセロのフィールドを構築するクラス.
 *
 */
public class Field {
    /**
     * フィールドの行を表す全アルファベット.
     */
    private static final String ROW_ALPHABETS = "ABCDEFGH";
    /**
     * フィールドの列を表す全番号.
     */
    private static final String COL_NUMBERS = "12345678";
    /**
     * アルファベットを対応する行番号に変換する.
     *
     * <pre>a → 0, b → 1 ... h → 7</pre>
     *
     * @param alRow 変換するアルファベット
     * @return 対応する行番号
     */
    public static int toRowNumber(final String alRow) {
        if (alRow.trim().length() != 1) {
            return -1;
        }
        return ROW_ALPHABETS.indexOf(alRow.trim().toUpperCase());
    }
    /**
     * 番号を対応する列番号に変換する.
     *
     * <pre> 1 → 0, 2 → 1, ..., 8 → 7</pre>
     *
     * @param col 変換対象の番号
     * @return 対応する列番号
     */
    public static int toColNumber(final String col) {
        if (col.trim().length() != 1) {
            return -1;
        }
        return COL_NUMBERS.indexOf((col.trim()));
    }
    /**
     * フィールドの行数.
     */
    private final int ROW = 8;
    /**
     * フィールドの列数.
     */
    private final int COL = 8;
    /**
     * フィールド本体.
     */
    private Piece[][] field;
    /**
     * 現在の手番.
     */
    private PieceType currentTurn;

    /**
     * コンストラクタ.
     * <br>
     * 行われることは以下
     * <ul>
     *     <li>1. フィールドの初期化</li>
     *     <li>2. プレイヤーの初期化</li>
     * </ul>
     */
    public Field() {
        field = new Piece[ROW][COL];

        for (int r = 0; r < ROW; r++) {
            for (int c = 0; c < COL; c++) {
                field[r][c] = new Piece();
            }
        }

        field[3][3].setState(PieceType.BLACK);
        field[4][3].setState(PieceType.WHITE);
        field[3][4].setState(PieceType.WHITE);
        field[4][4].setState(PieceType.BLACK);

        currentTurn = PieceType.BLACK;
    }

    /**
     * 現在の手番を返すgetter.
     *
     * @return 現在の手番
     */
    public PieceType getCurrentTurn() {
        return currentTurn;
    }

    /**
     * 手番を次に移す.
     */
    public void nextPlayer() {
        currentTurn = PieceType.getEnemyType(currentTurn);
    }

    /**
     * ゲームが終了したかどうかを判定する.
     *
     * <p>ゲームが終了したかどうかはフィールドの各値の数をカウントし、
     * 0となっているものがあるかどうかで判定できる</p>
     *
     * <p>もし白、黒のコマが0であればすべて相手によってひっくり返されたと判定でき、
     * 空きが0ならすべてコマで埋まったと判定できるため</p>
     *
     * @return ゲームが終わったならtrue, まだであればfalse
     */
    public boolean isGameOver() {
        Map<PieceType, Integer> PiecesCnt = getEachPiecesCnt();

        // White, Empty, Black、どれか1つでも0なら勝負がついたと判定できる
        for (int cnt : PiecesCnt.values()) {
            if (cnt == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 現在の黒と白の数を出力する.
     */
    public void printCurrentScores() {
        Map<PieceType, Integer> piecesCnt = getEachPiecesCnt();
        System.out.println(PieceType.BLACK.toString() + " : " + piecesCnt.get(PieceType.BLACK));
        System.out.println(PieceType.WHITE.toString() + " : " + piecesCnt.get(PieceType.WHITE));
    }

    /**
     * 手番がコマを置くことができるかどうかを調べる.
     *
     * @return コマを置くことができればtrue, そうでなければfalse
     */
    public boolean canPutForCurrentTurn() {
        for (int r = 0; r < ROW; r++) {
            for (int c = 0; c < COL; c++) {
                if (canPutPiece(new Coordinate(r, c))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * コマを置くことができるかどうかを判定する.
     * <br>
     * 具体的には以下の3つを調べる
     * <ul>
     *    <li>1. フィールドの内部に置かれているかどうか</li>
     *    <li>2. まだコマが置かれていない場所かどうか</li>
     *    <li>3. 挟むコマが存在するかどうか</li>
     * </ul>
     *
     * @param coordinate 置く座標
     * @return コマを置くことができるかどうか
     */
    public boolean canPutPiece(final Coordinate coordinate) {
        int inpRow = coordinate.getRow();
        int inpCol = coordinate.getCol();
        // フィールド外に置こうとした場合
        if (!isInsideField(new Coordinate(inpRow, inpCol))) {
            return false;
        }
        // すでにコマが置かれていた場合
        if (!field[inpRow][inpCol].isEmpty()) {
            return false;
        }
        // 置いたコマから見て周囲8方向に挟むコマがあるかどうかを調べる
        for (int r = inpRow - 1; r <= inpRow + 1; r++) {
            for (int c = inpCol - 1; c <= inpCol + 1; c++) {
                // 自分のコマが調べる方向の先にあるか
                if (!existOwnPieceAhead(new Coordinate(r,c),
                        new Vector(r-inpRow, c-inpCol))) {
                    continue;
                }
                // 挟むコマがある場合
                return true;
            }
        }
        return false;
    }

    /**
     * 指定したコマの座標から見て周囲8方向に対して自分のコマで挟んでいる相手のコマをひっくり返す.
     *
     * @param coordinate ひっくり返す始点となるコマの座標
     */
    public void flipPiecesFromPlaced(final Coordinate coordinate) {
        int inpRow = coordinate.getRow();
        int inpCol = coordinate.getCol();

        for (int r = inpRow - 1; r <= inpRow + 1; r++) {
            for (int c = inpCol - 1; c <= inpCol + 1; c++) {
                Vector vector = new Vector(r-inpRow, c-inpCol);
                // 自分のコマが調べる方向の先にあるか
                if (!existOwnPieceAhead(new Coordinate(r, c), vector)) {
                    continue;
                }
                // 挟むコマがあると判定された方向に向かって相手のコマをひっくり返す
                flipBetweenOwnPieces(new Coordinate(inpRow, inpCol), vector);
            }
        }
    }

    /**
     * 指定した座標にコマを置く.
     *
     * @param coordinate 置く座標
     */
    public void putPiece(final Coordinate coordinate) {
        int inpRow = coordinate.getRow();
        int inpCol = coordinate.getCol();

        field[inpRow][inpCol].setState(currentTurn);
    }

    /**
     * フィールドの描画を行う.
     */
    public void printField() {
        System.out.println(toString());
    }

    /**
     * フィールドを文字列化して返す.
     *
     * @return フィールド
     */
    @Override
    public String toString() {

        String lineSeparator = System.lineSeparator();
        String[] alphabets = ROW_ALPHABETS.split("");
        String colNumbers = "  " + String.join(" ", COL_NUMBERS.split(""));
        StringBuilder sb = new StringBuilder();

        // 列番号の並び
        // 先頭の空白は行列の交差する部分を示す
        sb.append(colNumbers);
        sb.append(lineSeparator);

        // 行番号と各フィールドの値が1行分の値
        for (int r = 0; r < ROW; r++) {
            sb.append(alphabets[r]);
            sb.append(" ");
            for (int c = 0; c < COL; c++) {
                sb.append(field[r][c]).append(" ");
            }
            sb.append(alphabets[r]);
            sb.append(lineSeparator);
        }

        sb.append(colNumbers);
        sb.append(lineSeparator);

        return sb.toString();
    }

    /**
     * コマを置いた場所から見て指定された方向の先に自分のコマがあるか調べる.
     *
     * @param coordinate 置く座標
     * @param vector
     * @return 自分のコマがあるかどうか
     */
    private boolean existOwnPieceAhead(final Coordinate coordinate, final Vector vector) {

        int inpRow = coordinate.getRow();
        int inpCol = coordinate.getCol();
        int vectorR = vector.getVectorR();
        int vectorC = vector.getVectorC();

        // 1つとなりの状態が外部、または自分のコマならfalse
        int movedR = inpRow + vectorR;
        int movedC = inpCol + vectorC;

        if (!isInsideField(new Coordinate(movedR, movedC))) {
            return false;
        }

        PieceType enemy = PieceType.getEnemyType(currentTurn);
        if (field[movedR][movedC].getState() != enemy ) {
            return false;
        }

        // 次の座標に移動して相手のコマを挟んでいるか調べる
        movedR += vectorR;
        movedC += vectorC;
        while (isInsideField(new Coordinate(movedR, movedC))) {
            if (field[movedR][movedC].getState() == currentTurn) {
                return true;
            }
            if (field[movedR][movedC].isEmpty()) {
                break;
            }
            movedR += vectorR;
            movedC += vectorC;
        }

        return false;
    }

    /**
     * 白、黒、空きの数を返す.
     *
     * @return 白、黒、空きのコマの数のマップ
     */
    private Map<PieceType, Integer> getEachPiecesCnt() {
        Map<PieceType, Integer> PiecesCnt = new HashMap<>();
        PiecesCnt.put(PieceType.BLACK, 0);
        PiecesCnt.put(PieceType.WHITE, 0);
        PiecesCnt.put(PieceType.EMPTY, 0);

        // 各フィールドの数をカウントする
        for (int r = 0; r < ROW; r++) {
            for (int c = 0; c < COL; c++) {
                PieceType key = field[r][c].getState();
                int cnt = PiecesCnt.get(field[r][c].getState());
                PiecesCnt.put(key, ++cnt);
            }
        }
        return PiecesCnt;
    }

    /**
     * コマを置いた場所から見て指定された方向に向かって相手のコマをひっくり返していく.
     * <br>
     * このメソッドはすでに調べる方向の先に自分のコマがあることが判明していることが前提となっている
     * そのためフィールドの内部かどうかをわざわざ調べていない
     * @param coordinate ひっくり返す始点となる座標
     * @param vector ひっくり返す方向
     */
    private void flipBetweenOwnPieces(final Coordinate coordinate, final Vector vector) {
        int inpRow = coordinate.getRow();
        int inpCol = coordinate.getCol();
        int vectorR = vector.getVectorR();
        int vectorC = vector.getVectorC();

        int movedR = inpRow + vectorR;
        int movedC = inpCol + vectorC;

        // 自分のコマにたどり着くまで相手のコマをひっくり返していく
        while (field[movedR][movedC].getState() != currentTurn) {
            field[movedR][movedC].flip();

            movedR += vectorR;
            movedC += vectorC;
        }
    }

    /**
     * 座標を指定してフィールドの内部かどうかを判定する.
     *
     * @param coordinate 調べる対象の座標
     * @return フィールドの内部ならtrue, 外部ならfalse
     */
    private boolean isInsideField(Coordinate coordinate) {
        int inpRow = coordinate.getRow();
        int inpCol = coordinate.getCol();

        return 0 <= inpRow && inpRow < ROW && 0 <= inpCol && inpCol < COL;
    }
}
