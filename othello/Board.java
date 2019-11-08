package othello;

import java.util.*;

/**
 * オセロのフィールドを構築するクラス.
 *
 * <p>
 * 役割.
 * <ul>
 * <li>現在のプレイヤーの管理</li>
 * <li>フィールドの状態管理</li>
 * <li>フィールドへのコマの設置</li>
 * <li>スコア管理</li>
 * </ul>
 *
 * </p>
 */
public class Board {
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
     * <p>
     * <pre>a → 0, b → 1 ... h → 7</pre>
     * </p>
     *
     * @param alphabetRow 変換するアルファベット
     * @return 対応する行番号(対応する行番号がない場合-1)
     */
    public static int toRowNumber(final String alphabetRow) {
        if (alphabetRow.trim().length() != 1) {
            return -1;
        }
        return ROW_ALPHABETS.indexOf(alphabetRow.trim().toUpperCase());
    }
    /**
     * 番号を対応する列番号に変換する.
     *
     * <p>
     * <pre> 1 → 0, 2 → 1, ..., 8 → 7</pre>
     * </p>
     *
     * @param col 変換対象の番号
     * @return 対応する列番号(対応する列番号が存在しない場合-1)
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

    private Deque<Piece[][]> fieldLogger;
    /**
     * 現在の手番.
     */
    private PieceType currentTurn;

    /**
     * オセロのフィールドを生成する.
     *
     * <p>
     * 初期化される項目は以下.
     * <ul>
     *     <li>フィールドの盤面ログ</li>
     *     <li>フィールド. オセロにおける初期状態に設定される.</li>
     *     <li>プレイヤーの手番.</li>
     * </ul>
     * </p>
     */
    public Board() {
        fieldLogger = new ArrayDeque<>();
        field = new Piece[ROW][COL];

        for (int r = 0; r < ROW; r++) {
            for (int c = 0; c < COL; c++) {
                field[r][c] = new Piece();
            }
        }

        field[3][3].setState(PieceType.WHITE);
        field[4][3].setState(PieceType.BLACK);
        field[3][4].setState(PieceType.BLACK);
        field[4][4].setState(PieceType.WHITE);

        currentTurn = PieceType.BLACK;
    }

    /**
     * 本インスタンスの複製を生成する.
     *
     * 複製時点のフィールドの状態と手番が保持される.
     *
     * @return 本インスタンスの複製
     */
    public Board cloneInstance() {
        Board newBoard = new Board();
        newBoard.field = cloneField();
        newBoard.currentTurn = getCurrentTurn();

        return newBoard;
    }

    /**
     * 本インスタンスのフィールドの状態を複製する.
     *
     * @return 現在のフィールドの状態の複製.
     */
    public Piece[][] cloneField() {
        Piece[][] newField = new Piece[ROW][COL];

        for (int r = 0; r < ROW; r++) {
            for (int c = 0; c < COL; c++) {
                newField[r][c] = new Piece();
                newField[r][c].setState(field[r][c].getState());
            }
        }

        return newField;
    }

    /**
     * 現在の手番を返す.
     *
     * @return 現在の手番を表すコマの種類
     */
    public PieceType getCurrentTurn() {
        return currentTurn;
    }

    /**
     * ゲームが終了したかどうかを判定する.
     *
     * @return ゲームが終わった場合 {@code true}
     */
    public boolean isGameOver() {
        Map<PieceType, Integer> PiecesCnt = getEachPiecesCnt();

        // White, Empty, Black、どれか1つでも0なら勝負がついたと判定できる
        // White, Blackが0なら片方がすべて取られたことを意味し、Emptyが0ならフィールドが埋まったことを意味する
        for (int cnt : PiecesCnt.values()) {
            if (cnt == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 手番がコマを置くことができるかどうかを調べる.
     *
     * @return コマを置くことができる場合 {@code true}
     */
    public boolean canPutForCurrentTurn() {
        for (int r = 0; r < ROW; r++) {
            for (int c = 0; c < COL; c++) {
                if (canPutPiece(Coordinate.valueOf(r, c))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 手番を次に移す.
     */
    public void nextPlayer() {
        currentTurn = Piece.getEnemyType(currentTurn);
    }

    /**
     * コマを置くことができるかどうかを判定する.
     *
     * <p>
     * 入力座標はフィールドの範囲外であってもよい(例外は排出しない).
     * </p>
     *
     * @param coordinate 置く座標
     * @return コマを置くことができる場合 {@code true}
     */
    public boolean canPutPiece(final Coordinate coordinate) {
        // フィールド外に置こうとした場合
        if (!isInsideField(coordinate)) {
            return false;
        }
        // すでにコマが置かれていた場合
        if (!field[coordinate.getRow()][coordinate.getCol()].isEmpty()) {
            return false;
        }

        // 置いたコマから見て周囲8方向に挟むコマがあるかどうかを調べる
        for (Vector vector : Vector.values()) {
            // 自分のコマが調べる方向の先にあるか
            if (!existOwnPieceAhead(coordinate, vector)) {
                continue;
            }
            // 挟むコマがある場合
            return true;
        }
        return false;
    }

    public void processToPlacePiece(final Coordinate coordinate) {
        placePiece(coordinate);
        flipPiecesFromPlaced(coordinate);
        logField();
    }

    /**
     * 指定した座標にコマを置く.
     *
     * <p>
     * 前提として, 座標はフィールドの範囲内であること.
     * また, すでにコマが置かれていたとしてもエラーとならず、
     * 指定した座標のコマは現在の手番のコマの状態となる.
     * </p>
     *
     * @param coordinate コマを置く座標
     */
    private void placePiece(final Coordinate coordinate) {
        field[coordinate.getRow()][coordinate.getCol()].setState(currentTurn);
    }

    /**
     * 指定したコマの座標から見て周囲8方向に対して自分のコマで挟んでいる相手のコマをひっくり返す.
     *
     * 前提として手番のコマを置いた後に使用すること.
     * 空の状態の座標を指定してもエラーは排出しない.
     * この場合でも周囲8方向の先に手番のコマがある場合
     * 相手のコマをひっくり返す.
     *
     * @param coordinate ひっくり返す始点となるコマの座標
     */
    private void flipPiecesFromPlaced(final Coordinate coordinate) {
        for (Vector vector : Vector.values()) {
            // 自分のコマが調べる方向の先にあるか
            if (!existOwnPieceAhead(coordinate, vector)) {
                continue;
            }
            // 挟むコマがあると判定された方向に向かって相手のコマをひっくり返す
            flipBetweenOwnPieces(coordinate, vector);
        }
    }

    /**
     * フィールドをスタックに保存する.
     */
    private void logField() {
        fieldLogger.addFirst(cloneField());
    }

    /**
     * ログをさかのぼりフィールドの状態を戻す.
     *
     * @param howMany いくつ前の状態に戻すか.
     */
    public void goBack(int howMany) {
        for (int i = 0; i < howMany; i++) {
            if (fieldLogger.isEmpty()) {
                return;
            }
            field = fieldLogger.removeFirst();
        }
    }

    /**
     * 指定された座標の行と列の情報を表示する.
     *
     * @param coordinate 表示したい座標
     */
    public void printPlacedCoordinate(Coordinate coordinate) {
        String row = ROW_ALPHABETS.split("")[coordinate.getRow()];
        String col = COL_NUMBERS.split("")[coordinate.getCol()];
        System.out.println(String.format("row = %s, col = %s", row, col));
    }

    /**
     * コマを置いた後の処理を実行する.
     *
     * 具体的には
     * <ol>
     *     <li>フィールドの描画.</li>
     *     <li>置かれたコマの座標の情報の表示.</li>
     *     <li>現在のスコアの表示.</li>
     *     <li>手番を相手に移す.</li>
     * </ol>
     *
     * @param coordinate コマの情報を表示する座標.
     */
    public void processAfterPlace(Coordinate coordinate) {
        printField();
        printPlacedCoordinate(coordinate);
        printCurrentScores();
        nextPlayer();
    }

    /**
     * フィールドの描画を行う.
     */
    public void printField() {
        System.out.println(toString());
    }

    /**
     * 現在の黒と白のコマの数をそれぞれ標準出力する.
     */
    public void printCurrentScores() {
        Map<PieceType, Integer> piecesCnt = getEachPiecesCnt();
        System.out.println(PieceType.BLACK + " : " + piecesCnt.get(PieceType.BLACK));
        System.out.println(PieceType.WHITE + " : " + piecesCnt.get(PieceType.WHITE));
    }

    /**
     * フィールドを文字列化して返す.
     *
     * @return フィールド
     */
    @Override
    public String toString() {

        final String lineSeparator = System.lineSeparator();
        final String[] rowAlphabets = ROW_ALPHABETS.split("");
        final String colNumbers = "  " + String.join(" ", COL_NUMBERS.split(""));

        StringBuilder sb = new StringBuilder();

        // 列番号の並び
        // 先頭の空白は行列の交差する部分を示す
        sb.append(colNumbers).append(lineSeparator);

        // 行番号と各フィールドの値が1行分の値
        // 例: A ● ● ● ● ● ● ● ● A
        for (int r = 0; r < ROW; r++) {
            sb.append(rowAlphabets[r]).append(" ");
            for (int c = 0; c < COL; c++) {
                sb.append(field[r][c]).append(" ");
            }
            sb.append(rowAlphabets[r]).append(lineSeparator);
        }
        sb.append(colNumbers).append(lineSeparator);

        return sb.toString();
    }

    /**
     * コマを置いた場所から見て指定された方向の先に自分のコマがあるか調べる.
     *
     * @param coordinate 置く座標
     * @param vector 調べる方向
     * @return 自分のコマがあるかどうか
     */
    private boolean existOwnPieceAhead(final Coordinate coordinate, final Vector vector) {

        // 入力に対してとなりのコマであるかどうかを判定する変数.
        boolean isNextToInput = true;

        int movedRow = coordinate.getRow() + vector.getVectorRow();
        int movedCol = coordinate.getCol() + vector.getVectorCol();
        while (isInsideField(Coordinate.valueOf(movedRow, movedCol))) {
            PieceType target = field[movedRow][movedCol].getState();

            if (isNextToInput) {
                // となりのコマは相手のコマでなければならない.
                if (target != Piece.getEnemyType(currentTurn)) {
                    break;
                }
                isNextToInput = false;

            } else {
                if (target == PieceType.EMPTY) {
                    break;
                }
                if (target == currentTurn) {
                    return true;
                }
            }

            movedRow += vector.getVectorRow();
            movedCol += vector.getVectorCol();
        }

        return false;
    }

    /**
     * 座標と方向を指定して, 挟んでいる相手のコマをひっくり返す.
     *
     * <p>
     * このメソッドはすでに調べる方向の先に自分のコマがあることが判明していることが前提となっている.
     * そのためフィールドの外部に座標を指定するとエラーを排出する.
     * </p>
     *
     * @param coordinate ひっくり返す始点となる座標
     * @param vector ひっくり返す方向
     */
    private void flipBetweenOwnPieces(final Coordinate coordinate, final Vector vector) {
        // 移動していく座標の変数
        int movedRow = coordinate.getRow() + vector.getVectorRow();
        int movedCol = coordinate.getCol() + vector.getVectorCol();

        // 自分のコマにたどり着くまで相手のコマをひっくり返していく
        while (field[movedRow][movedCol].getState() != currentTurn) {
            field[movedRow][movedCol].flip();

            movedRow += vector.getVectorRow();
            movedCol += vector.getVectorCol();
        }
    }

    /**
     * 白、黒、空きの数を返す.
     *
     * @return 白、黒、空きのコマの数のマップ
     */
    private Map<PieceType, Integer> getEachPiecesCnt() {
        Map<PieceType, Integer> PiecesCnt = new EnumMap<>(PieceType.class);
        for (PieceType pieceType : PieceType.values()) {
            PiecesCnt.put(pieceType, 0);
        }

        // 各フィールドの数をカウントする
        for (int r = 0; r < ROW; r++) {
            for (int c = 0; c < COL; c++) {
                PieceType key = field[r][c].getState();
                PiecesCnt.put(key, PiecesCnt.get(key) + 1);
            }
        }
        return PiecesCnt;
    }

    /**
     * 座標を指定してフィールドの内部かどうかを判定する.
     *
     * @param coordinate 調べる対象の座標
     * @return フィールドの内部の場合 {@code true}
     */
    private boolean isInsideField(Coordinate coordinate) {
        final int inputRow = coordinate.getRow();
        final int inputCol = coordinate.getCol();

        return 0 <= inputRow && inputRow < ROW && 0 <= inputCol && inputCol < COL;
    }
}
