package othello.strategy;

import othello.Coordinate;
import othello.Board;
import othello.PieceType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * アルファベータ法による評価値の計算用クラス.
 */
public class NormalAI implements StrategyInterface{
    /**
     * フィールドの行数.
     */
    private final int ROW = 8;
    /**
     * フィールドの列数.
     */
    private final int COL = 8;
    /**
     * 自分のコマの種類.
     */
    private final PieceType me;

    public NormalAI(PieceType me) {
        this.me = me;
    }

    /**
     * コマを置く座標を決定する.
     *
     * @param othello 現在の状態を保持したオブジェクト.
     * @return 評価値の最も高い座標.
     */
    @Override
    public Coordinate decideCoordinate(Board othello) {
        Board clone = othello.cloneInstance();
        Coordinate result = Coordinate.valueOf(-1, -1);
        List<Coordinate> candidates = getCandidates(clone);

        // 評価値の初期値を最小の値として設定する.
        int evalMax = Integer.MIN_VALUE;

        // 置くことができる座標それぞれの評価値を求め最も評価値の高い座標を選出する.
        for (Coordinate candidate : candidates) {
            clone.processToPutPiece(candidate);
            int eval = alphaBeta(clone, 5, Integer.MIN_VALUE, Integer.MAX_VALUE);
            clone.goBack(1);
            if (eval > evalMax) {
                result = candidate;
                evalMax = eval;
            }
        }
        return result;
    }

    /**
     * オセロの盤面を評価する.
     *
     * @param othello 評価対象の盤面の情報を保持するオブジェクト.
     * @return 盤面の評価値.
     */
    protected int evaluate(Board othello) {
        return calcHavingNumber(othello);
    }

    /**
     * コマを置くことが可能な座標をすべて取得する.
     *
     * @param othello フィールドの盤面を保持するオブジェクト.
     * @return すべての可能な手.
     */
    protected List<Coordinate> getCandidates(Board othello) {
        List<Coordinate> coordinates = new ArrayList<>();
        for (int r = 0; r < ROW; r++) {
            for (int c = 0; c < COL; c++) {
                if (othello.canPutPiece(Coordinate.valueOf(r, c))) {
                    coordinates.add(Coordinate.valueOf(r, c));
                }
            }
        }
        return coordinates;
    }

    /**
     * アルファベータ法による座標の選定を行う.
     *
     * @param othello 評価する盤面を保持するオブジェクト.
     * @param depth 深さ制限.
     * @param alpha α値. このノードの評価値は必ずα値以上となる.
     * @param beta β値. このノードの評価値は必ずβ値以下となる.
     * @return 評価値
     */
    private int alphaBeta(Board othello, final int depth, int alpha, int beta) {
        List<Coordinate> candidates = getCandidates(othello);
        if (depth <= 0 || candidates.isEmpty()) {
            return evaluate(othello);
        }

        int upperBound = Integer.MAX_VALUE;
        int lowerBound = Integer.MIN_VALUE;

        for (Coordinate candidate : candidates) {
            othello.processToPutPiece(candidate);
            othello.nextTurn();

            // 子ノードの評価値を計算する.
            int childValue = alphaBeta(othello, depth-1, alpha, beta);

            if (othello.getCurrentTurn() != me) {
                // 自分のノードの場合は子ノードの最大値を求める.
                if (childValue > lowerBound) {
                    lowerBound = childValue;
                    alpha = childValue;
                }
                if (lowerBound > beta) {
                    // βカット
                    othello.goBack(1);
                    return childValue;
                }
            } else {
                // 相手のノードの場合は子ノードの最小値を求める.
                if (childValue < upperBound) {
                    upperBound = childValue;
                    beta = childValue;
                }
                if (upperBound < alpha) {
                    // αカット
                    othello.goBack(1);
                    return childValue;
                }
            }
            othello.goBack(1);
        }
        if (othello.getCurrentTurn() == me) {
            return alpha;
        }
        return beta;
    }

    /**
     * 自石と相手の石から評価値を計算する.
     *
     * @param othello 盤面の状態を保持するオブジェクト.
     * @return 評価値.
     */
    protected int calcHavingNumber(Board othello) {
        Map<PieceType, Integer> havingScores = othello.getEachPiecesCnt();
        return havingScores.get(me);
    }
}
