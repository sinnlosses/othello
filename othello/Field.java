package othello;

import java.util.HashMap;
import java.util.Map;

public class Field {

    /**
     * フィールドの行数
     */
    private final int ROW = 8;
    /**
     * フィールドの列数
     */
    private final int COL = 8;
    /**
     * フィールド本体
     */
    private Piece[][] field;
    /**
     * 現在の手番
     */
    private PieceType currentTurn;

    /**
     * コンストラクタ.
     * <p>
     * 行われることは以下
     * 1. フィールドの初期化
     * 2. プレイヤーの初期化
     */
    public Field() {
        field = new Piece[ROW][COL];

        for (int r = 0; r < ROW; r++) {
            for (int c = 0; c < COL; c++) {
                this.field[r][c] = new Piece();
            }
        }

        this.field[3][3].setState(PieceType.BLACK);
        this.field[4][3].setState(PieceType.WHITE);
        this.field[3][4].setState(PieceType.WHITE);
        this.field[4][4].setState(PieceType.BLACK);

        currentTurn = PieceType.WHITE;
    }

    /**
     * アルファベットを対応するindexの番号に変換する.
     *
     * <pre>a -> 0, b -> 1 ... h -> 7</pre>
     *
     * @param al 変換するアルファベット
     * @return 対応するindex番号
     */
    public static int alToInt(String al) {
        return "abcdefgh".indexOf(al.trim().toLowerCase());
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
        currentTurn = PieceType.getType(currentTurn.getValue()*-1);
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
        Map<PieceType, Integer> WhiteEmptyBlackCnt = getPiecesCnt();

        // White, Empty, Black、どれか1つでも0なら勝負がついたと判定できる
        for (int cnt : WhiteEmptyBlackCnt.values()) {
            if (cnt == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 白、黒、空きの数を返す.
     *
     * @return
     */
    private Map<PieceType, Integer> getPiecesCnt() {
        Map<PieceType, Integer> WhiteEmptyBlackCnt = new HashMap<>();
        WhiteEmptyBlackCnt.put(PieceType.BLACK, 0);
        WhiteEmptyBlackCnt.put(PieceType.WHITE, 0);
        WhiteEmptyBlackCnt.put(PieceType.EMPTY, 0);

        // 各フィールドの数をカウントする
        for (int r = 0; r < ROW; r++) {
            for (int c = 0; c < COL; c++) {
                PieceType key = field[r][c].getState();
                int cnt = WhiteEmptyBlackCnt.get(field[r][c].getState());
                WhiteEmptyBlackCnt.put(key, ++cnt);
            }
        }
        return WhiteEmptyBlackCnt;
    }

    public void printCurrentSituation() {
        Map<PieceType, Integer> piecesCnt = getPiecesCnt();
        System.out.println(PieceType.BLACK.getImage() + " : " + piecesCnt.get(PieceType.BLACK));
        System.out.println(PieceType.WHITE.getImage() + " : " + piecesCnt.get(PieceType.WHITE));
    }

    /**
     * 手番がコマを置くことができるかどうかを調べる.
     *
     * @return コマを置くことができればtrue, そうでなければfalse
     */
    public boolean canStillPutForCurrentTurn() {
        for (int r = 0; r < ROW; r++) {
            for (int c = 0; c < COL; c++) {
                if (canPutPiece(r, c)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * コマを置くことができるかどうかを判定する.
     *
     * 具体的には以下の3つを調べる
     * 1. フィールドの内部に置かれているかどうか(外部ならfalse)
     * 2. まだコマが置かれていない場所かどうか(すでに置かれていたらfalse)
     * 3. 挟むコマが存在するかどうか(挟むコマがないならfalse)
     *
     * @param inpRow 置くコマの行番号
     * @param inpCol 置くコマの列番号
     * @return コマを置くことができるかどうか
     */
    public boolean canPutPiece(int inpRow, int inpCol) {
        // フィールド外に置こうとした場合
        if (!isInsideField(inpRow, inpCol)) {
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
                if (!existOwnPieceAhead(r - inpRow, c - inpCol, inpRow, inpCol)) {
                    continue;
                }
                // 挟むコマがある場合
                return true;
            }
        }
        return false;
    }

    /**
     * 指定したコマの座標から見て挟んでいる相手のコマをひっくり返す.
     *
     * @param inpRow コマの行番号
     * @param inpCol コマの列番号
     */
    public void flipPiecesFromPlacedPiece(int inpRow, int inpCol) {
        for (int r = inpRow - 1; r <= inpRow + 1; r++) {
            for (int c = inpCol - 1; c <= inpCol + 1; c++) {
                // 自分のコマが調べる方向の先にあるか
                if (!existOwnPieceAhead(r - inpRow, c - inpCol, inpRow, inpCol)) {
                    continue;
                }
                // 挟むコマがあると判定された方向に向かって相手のコマをひっくり返す
                flipBetweenOwnPieces(r - inpRow, c - inpCol, inpRow, inpCol);
            }
        }
    }

    /**
     * コマを置いた場所から見て指定された方向の先に自分のコマがあるか調べる.
     *
     * @param vectorR 調べる行方向
     * @param vectorC 調べる列方向
     * @param inpRow 置く行番号
     * @param inpCol 置く列番号
     * @return 自分のコマがあるかどうか
     */
    private boolean existOwnPieceAhead(int vectorR, int vectorC, int inpRow, int inpCol) {

        // 1つとなりの状態が外部、または自分のコマならfalse
        int movedR = inpRow + vectorR;
        int movedC = inpCol + vectorC;

        if (!isInsideField(movedR, movedC)) {
            return false;
        }

        PieceType enemy = PieceType.getType(currentTurn.getValue()*-1);
        if (field[movedR][movedC].getState() != enemy ) {
            return false;
        }

        // 次の座標に移動して相手のコマを挟んでいるか調べる
        movedR += vectorR;
        movedC += vectorC;
        while (isInsideField(movedR, movedC)) {
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
     * コマを置いた場所から見て指定された方向に向かって相手のコマをひっくり返していく.
     *
     * このメソッドはすでに調べる方向の先に自分のコマがあることが判明していることが前提となっている
     * そのためフィールドの内部かどうかをわざわざ調べていない
     *
     * @param vectorR
     * @param vectorC
     * @param inpRow
     * @param inpCol
     */
    private void flipBetweenOwnPieces(int vectorR, int vectorC, int inpRow, int inpCol) {
        int movedR = inpRow + vectorR;
        int movedC = inpCol + vectorC;

        while (true) {
            if (this.field[movedR][movedC].getState() == currentTurn) {
                break;
            }

            field[movedR][movedC].flipPiece();

            movedR += vectorR;
            movedC += vectorC;
        }
    }

    /**
     * 指定した座標にコマを置く.
     *
     * @param inpRow 置く行番号
     * @param inpCol 置く列番号
     */
    public void putPiece(int inpRow, int inpCol) {
        field[inpRow][inpCol].setState(currentTurn);
    }

    /**
     * フィールドの描画を行う
     */
    public void printField() {
        System.out.println(toString());
    }

    /**
     * フィールドを文字列化して返す
     *
     * @return フィールド
     */
    @Override
    public String toString() {
        String[] alphabets = {"A", "B", "C", "D", "E", "F", "G", "H"};
        StringBuilder sb = new StringBuilder();

        // 列番号の並び
        sb.append(" 12345678");
        sb.append(System.lineSeparator());

        // 行番号と各フィールドの値が1行分の値
        for (int r = 0; r < ROW; r++) {
            sb.append(alphabets[r]);
            for (int c = 0; c < COL; c++) {
                sb.append(field[r][c]);
            }
            sb.append(System.lineSeparator());
        }

        return sb.toString();
    }

    /**
     * 座標を指定してフィールドの内部かどうかを判定する.
     *
     * @param inpRow 行番号
     * @param inpCol 列番号
     * @return フィールドの内部ならtrue, 外部ならfalse
     */
    private boolean isInsideField(int inpRow, int inpCol) {
        return 0 <= inpRow && inpRow < ROW && 0 <= inpCol && inpCol < COL;
    }
}
