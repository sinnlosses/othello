package othello;

import java.util.*;

/**
 * オセロのフィールドを構築するクラス.
 * <p>
 * 役割.
 * <ul>
 * <li>手番の管理</li>
 * <li>フィールドの状態管理</li>
 * <li>フィールドへのコマの設置</li>
 * <li>ログ管理</li>
 * </ul>
 *
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
     * a → 0, b → 1 ... h → 7
     *
     * @param alphabet 行番号に変換する対象のアルファベット
     * @return 対応する行番号(対応する行番号がない場合-1)
     */
    public static int toRowNumber(final String alphabet) {
        if (alphabet.trim().length() != 1) {
            return -1;
        }
        return ROW_ALPHABETS.indexOf(alphabet.trim().toUpperCase());
    }
    /**
     * 番号を対応する列番号に変換する.
     *
     * 1 → 0, 2 → 1, ..., 8 → 7
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
     * <p>
     * 初期化される項目は以下.
     * <ul>
     *     <li>フィールドの盤面ログ</li>
     *     <li>フィールド. オセロにおける初期状態に設定される.</li>
     *     <li>プレイヤーの手番.</li>
     * </ul>
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
     * 保持している盤面の状態を複製する.
     *
     * @return 現在の盤面の複製
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
     * 白、黒、空きそれぞれの数を返す.
     *
     * @return 白、黒、空きのコマの数を格納したオブジェクト
     */
    public Map<PieceType, Integer> getEachPiecesCnt() {
        Map<PieceType, Integer> PiecesCnt = new EnumMap<>(PieceType.class);
        // 初期化が必要. 初期化しなければフィールドに存在しないキーが登録されない.
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
     * ゲームが終了したかどうかを判定する.
     *
     * @return ゲームが終わった場合 {@code true}
     */
    public boolean isGameOver() {
        if (canPutForCurrentTurn()) {
            return false;
        }
        nextTurn();
        if (canPutForCurrentTurn()) {
            nextTurn();
            return false;
        }
        return true;
    }

    /**
     * 手番がコマを置ける座標があるか調べる.
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
    public void nextTurn() {
        currentTurn = PieceType.getEnemyType(currentTurn);
    }

    /**
     * 指定した座標にコマを置くことができるかどうかを判定する.
     *
     * <p>
     * フィールドの範囲外の座標を指定しても例外は排出しない.
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
        if (!getFieldPieceAt(coordinate).isEmpty()) {
            return false;
        }

        // 置いたコマから見て周囲8方向に挟むコマがあるかどうかを調べる
        for (Vector vector : Vector.values()) {
            // 自分のコマが調べる方向の先にあるか
            if (existOwnPieceAhead(coordinate, vector)) {
                return true;
            }
        }
        return false;
    }

    /**
     * コマを置く場合に必要な処理を行う.
     * 具体的には
     * <ol>
     *     <li>コマを指定の座標に置く</li>
     *     <li>挟んだコマをひっくり返す</li>
     *     <li>フィールドのログを取る</li>
     * </ol>
     *
     * @param coordinate 置く座標
     */
    public void processToPutPiece(final Coordinate coordinate) {
        putPiece(coordinate);
        flipPiecesFrom(coordinate);
        logField();
    }

    /**
     * コマを置いた結果を表示する.
     *
     * 具体的には
     * <ol>
     *     <li>フィールドの描画.</li>
     *     <li>置かれたコマの座標の情報の表示.</li>
     *     <li>現在のスコアの表示.</li>
     * </ol>
     *
     * @param coordinate コマの情報を表示する座標
     */
    public void printResult(Coordinate coordinate) {
        printField();
        printPutCoordinate(coordinate);
        printCurrentScores();
    }

    /**
     * 指定された座標の行と列の情報を表示する.
     *
     * @param coordinate 表示する座標
     */
    public void printPutCoordinate(Coordinate coordinate) {
        final String row = ROW_ALPHABETS.split("")[coordinate.getRow()];
        final String col = COL_NUMBERS.split("")[coordinate.getCol()];
        System.out.println(String.format("row = %s, col = %s", row, col));
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
     * 手番の情報を表示する.
     */
    public void printCurrentTurn() {
        System.out.println(getCurrentTurn() + "の手番です");
    }

    /**
     * ログをさかのぼりフィールドの状態を戻す.
     *
     * @param howMany いくつ前の状態に戻すか
     */
    public void goBack(int howMany) {
        for (int i = 0; i < howMany; i++) {
            if (fieldLogger.isEmpty()) {
                return;
            }
            currentTurn = PieceType.getEnemyType(currentTurn);
            field = fieldLogger.removeFirst();
        }
    }

    /**
     * フィールドを文字列化して返す.
     *
     * @return 行番号, 列番号, フィールドの状態を文字列として表現したもの
     */
    @Override
    public String toString() {

        final String lineSeparator = System.lineSeparator();
        final String[] rowAlphabets = ROW_ALPHABETS.split("");
        final String colNumbers = "  " + String.join(" ", COL_NUMBERS.split(""));

        StringBuilder sb = new StringBuilder();

        // 列番号の並び
        // 先頭の空白は行列の左上を指す
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
     * 指定した座標から見て指定した方向に関して相手のコマを挟んでいるかを調べる.
     *
     * @param coordinate 調べる根本となる座標
     * @param vector 調べる方向
     * @return 相手のコマを挟んでいる場合 {@code true}
     */
    private boolean existOwnPieceAhead(final Coordinate coordinate, final Vector vector) {
        // 調べる対象の座標. 入力の座標から指定した方向へ移動させる.
        Coordinate target = coordinate.move(vector);

        if (!isInsideField(target)) {
            return false;
        }
        if (getFieldPieceAt(target).getState() != PieceType.getEnemyType(currentTurn)) {
            // となりのコマは相手のコマでなければならない.
            return false;
        }
        target = target.move(vector);

        while (isInsideField(target)) {
            // 2つ以降離れている場合、コマが途切れている場合は相手のコマを挟んでいない.
            // 自分のコマである場合相手のコマを挟んでいる.
            if (getFieldPieceAt(target).getState() == PieceType.EMPTY) {
                return false;
            }
            if (getFieldPieceAt(target).getState() == currentTurn) {
                return true;
            }
            target = target.move(vector);
        }
        return false;
    }

    /**
     * 指定した座標にコマを置く.
     *
     * <p>
     * 前提として, 座標はフィールドの範囲内であること.
     * また, すでにコマが置かれていたとしてもエラーとならず、
     * 指定した座標のコマは現在の手番のコマの状態となる.
     *
     * @param coordinate コマを置く座標
     * @throws IllegalArgumentException フィールド外の座標を指定した場合
     */
    private void putPiece(final Coordinate coordinate) {
        if (!isInsideField(coordinate)) {
            throw new IllegalArgumentException("指定した座標には置けません");
        }
        getFieldPieceAt(coordinate).setState(currentTurn);
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
    private void flipPiecesFrom(final Coordinate coordinate) {
        for (Vector vector : Vector.values()) {
            if (existOwnPieceAhead(coordinate, vector)) {
                // 挟むコマがあると判定された方向に向かって相手のコマをひっくり返す
                flipBetweenOwnPieces(coordinate, vector);
            }
        }
    }

    /**
     * 座標と方向を指定して, 挟んでいる相手のコマをひっくり返す.
     *
     * <p>
     * このメソッドはすでに調べる方向の先に自分のコマがあることが判明していることが前提となっている.
     * そのためフィールドの外部に座標を指定するとエラーを排出する.
     *
     * @param coordinate ひっくり返す始点となる座標
     * @param vector ひっくり返す方向
     */
    private void flipBetweenOwnPieces(final Coordinate coordinate, final Vector vector) {
        // 移動していく座標の変数
        Coordinate target = coordinate.move(vector);

        // 自分のコマにたどり着くまで相手のコマをひっくり返していく
        while (getFieldPieceAt(target).getState() != currentTurn) {
            getFieldPieceAt(target).flip();
            target = target.move(vector);
        }
    }

    /**
     * 現在のフィールドを複製してログを保持するオブジェクトに登録する.
     */
    private void logField() {
        fieldLogger.addFirst(cloneField());
    }

    /**
     * 指定した座標におけるフィールドのコマを取得する.
     *
     * @param coordinate コマの状態を取得する対象の座標
     * @return コマの状態を保持するオブジェクト
     */
    private Piece getFieldPieceAt(Coordinate coordinate) {
        return field[coordinate.getRow()][coordinate.getCol()];
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
