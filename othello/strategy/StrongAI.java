package othello.strategy;

import othello.Coordinate;
import othello.Board;
import othello.Piece;
import othello.PieceType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static java.lang.Math.max;

/**
 * アルファベータ法による評価値の計算用クラス.
 */
public class StrongAI implements StrategyInterface{
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
    private final int[][] BP = {{45, -11, 4, -1, -1, 4, -11, 45},
                                {-11, -16, -1, -3, -3, 2, -16, -11},
                                {4, -1, 2, -1, -1, 2, -1, 4},
                                {-1, -3, -1, 0, 0, -1, -3, -1},
                                {-1, -3, -1, 0, 0, -1, -3, -1},
                                {4, -1, 2, -1, -1, 2, -1, 4},
                                {-11, -16, -1, -3, -3, 2, -16, -11},
                                {45, -11, 4, -1, -1, 4, -11, 45}
    };

    public StrongAI() {
        // 処理なし
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
        List<Coordinate> candidates = extractCandidates(clone);

        // 評価値の初期値を最小の値として設定する.
        int evalMax = Integer.MIN_VALUE;

        // 置くことができる座標それぞれの評価値を求め最も評価値の高い座標を選出する.
        for (Coordinate candidate : candidates) {
            clone.processToPlacePiece(candidate);
            int eval = alphaBeta(clone, 6, Integer.MIN_VALUE, Integer.MAX_VALUE);
            clone.goBack(1);
            if (eval > evalMax) {
                result = candidate;
                evalMax = eval;
            }
        }
        return result;
    }

    /**
     * アルファベータ法(ネガアルファ法)による座標の選定を行う.
     *
     * @param othello 評価する盤面を保持するオブジェクト.
     * @param depth 深さ制限.
     * @param alpha 関心の最小値.
     * @param beta 関心の最大値.
     * @return 評価値
     */
    private int alphaBeta(Board othello, final int depth, int alpha, int beta) {
        List<Coordinate> candidates = extractCandidates(othello);
        if (depth <= 0 || candidates.isEmpty()) {
            return evaluate(othello);
        }

        for (Coordinate candidate : candidates) {
            othello.processToPlacePiece(candidate);
            alpha = max(alpha, -alphaBeta(othello,depth-1, -beta, -alpha));
            othello.goBack(1);
            if (alpha >= beta) {
                break;
            }
        }
        return alpha;
    }

    /**
     * オセロの盤面を評価する.
     *
     * @param othello 評価対象の盤面の情報を保持するオブジェクト.
     * @return 盤面の評価値.
     */
    private int evaluate(Board othello) {
        Piece[][] field = othello.cloneField();

        // 盤位置(BP)の評価値を計算する.
        int scoreBP = calcBoardPosition(field, othello.getCurrentTurn());

        // 候補数(CN)の評価値を計算する.
        int scoreCN = (extractCandidates(othello).size() + new Random().nextInt(2)*2);

        return scoreBP*3 + scoreCN*10;
    }

    /**
     * 可能な手をすべて取得する.
     *
     * @param othello フィールドの盤面を保持するオブジェクト.
     * @return すべての可能な手.
     */
    private List<Coordinate> extractCandidates(Board othello) {
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
     * 盤面を盤位置に基づいて全体評価する.
     *
     * 評価値を計算するために使用される.
     *
     * @param field 盤面
     * @param me 自分のコマの種類
     * @return 評価値
     */
    private int calcBoardPosition(Piece[][] field, PieceType me) {
        int scoreBP = 0;
        for (int r = 0; r < ROW; r++) {
            for (int c = 0; c < COL; c++) {
                scoreBP += BP[r][c]*toIntForPiece(field[r][c], me);
            }
        }
        return scoreBP;
    }

    /**
     * コマの状態を判定しコマ単位の評価値を計算する.
     *
     * @param target 対象のコマ
     * @param me 自分のコマの種類
     * @return 評価値
     */
    private int toIntForPiece(Piece target, PieceType me) {
        if (target.getState() == me) {
            return 1;
        } else if (target.isEmpty()) {
            return 0;
        } else {
            return -1;
        }
    }
}
