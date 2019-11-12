package othello.strategy;

import othello.Board;
import othello.Piece;
import othello.PieceType;

/**
 * アルファベータ法による評価値の計算用クラス.
 *
 * 継承元から評価関数を変更し, 詳細に盤面を評価するように改善されている.
 */
public class StrongAI extends NormalAI {
    /**
     * フィールドの行数.
     */
    private final int ROW = 8;
    /**
     * フィールドの列数.
     */
    private final int COL = 8;
    /**
     * 盤面評価に使用される盤位置の評価値
     */
    private final int[][] PositionScore = {{45, -11, 4, -1, -1, 4, -11, 45},
                                            {-11, -16, -1, -3, -3, 2, -16, -11},
                                            {4, -1, 2, -1, -1, 2, -1, 4},
                                            {-1, -3, -1, 0, 0, -1, -3, -1},
                                            {-1, -3, -1, 0, 0, -1, -3, -1},
                                            {4, -1, 2, -1, -1, 2, -1, 4},
                                            {-11, -16, -1, -3, -3, 2, -16, -11},
                                            {45, -11, 4, -1, -1, 4, -11, 45}
    };

    /**
     * 自分のコマの種類.
     */
    private final PieceType me;

    public StrongAI(PieceType me) {
        super(me);
        this.me = me;
    }

    /**
     * オセロの盤面を評価する.
     *
     * @param othello 評価対象の盤面の情報を保持するオブジェクト
     * @return 盤面の評価値
     */
    @Override
    protected int evaluate(Board othello) {
        Piece[][] field = othello.cloneField();

        // 盤位置(BoardPosition)の評価値を計算する.
        int scoreBP = calcBoardPosition(field);

        // 候補数(CandidateNumber)の評価値を計算する.
        int scoreCN = calcCandidatesNumber(othello);

        // 自石の数(HavingNumber)に基づいて評価値を計算する.
        int scoreHN = super.evaluate(othello);

        return scoreBP*3 + scoreCN*10 + scoreHN*3;
    }

    /**
     * 盤面を盤位置に基づいて全体評価する.
     *
     * 評価値を計算するために使用される.
     *
     * @param field 盤面
     * @return 評価値
     */
    private int calcBoardPosition(Piece[][] field) {
        int scoreBP = 0;
        for (int r = 0; r < ROW; r++) {
            for (int c = 0; c < COL; c++) {
                scoreBP += PositionScore[r][c]*toIntForPiece(field[r][c]);
            }
        }
        return scoreBP;
    }

    /**
     * 自分が置くことができるコマの数を返す.
     *
     * @param othello 盤面の状態を保持するオブジェクト
     * @return 置くことが可能なコマの数
     */
    private int calcCandidatesNumber(Board othello) {
        return super.getCandidates(othello).size();
    }

    /**
     * コマの状態を判定しコマ単位の評価値を計算する.
     *
     * @param target 対象のコマ
     * @return 評価値
     */
    private int toIntForPiece(Piece target) {
        if (target.getState() == me) {
            return 1;
        } else if (target.isEmpty()) {
            return 0;
        } else {
            return -1;
        }
    }
}