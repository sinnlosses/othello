package othello.strategy;

import othello.Coordinate;
import othello.Board;
import othello.Piece;
import othello.PieceType;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

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
        List<Coordinate> candidates = extractPossibleChoice(clone);

        // 評価値の初期値を最小の値として設定する.
        int evalMax = Integer.MIN_VALUE;

        // 置くことができる座標それぞれの評価値を求め最も評価値の高い座標を選出する.
        for (Coordinate candidate : candidates) {
            clone.processToPlacePiece(candidate);
            int eval = alphabeta(clone, 6, Integer.MIN_VALUE, Integer.MAX_VALUE);
            clone.goBack(1);
            if (eval > evalMax) {
                result = candidate;
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
    private int alphabeta(Board othello, final int depth, int alpha, int beta) {
        List<Coordinate> candidates = extractPossibleChoice(othello);
        if (depth <= 0 || candidates.isEmpty()) {
            return evaluate(othello);
        }

        for (Coordinate candidate : candidates) {
            othello.processToPlacePiece(candidate);
            alpha = max(alpha, -alphabeta(othello,depth-1, -beta, -alpha));
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
     * @return 盤面の評価値
     */
    private int evaluate(Board othello) {
        Piece[][] field = othello.cloneField();
        EnumMap<PieceType, Integer> bePutCnt = new EnumMap<>(PieceType.class);
        for (PieceType p : PieceType.values()) {
            bePutCnt.put(p, 0);
        }
        for (int r = 0; r < ROW; r++) {
            for (int c = 0; c < COL; c++) {
                int cnt = bePutCnt.get(field[r][c].getState());
                bePutCnt.put(field[r][c].getState(), cnt + 1);
            }
        }

        PieceType enemy = Piece.getEnemyType(othello.getCurrentTurn());
        int scoreA = bePutCnt.get(othello.getCurrentTurn()) - bePutCnt.get(enemy);
        int scoreB = 0;
        if (field[0][0].getState() == othello.getCurrentTurn()) {
            scoreB += 10;
        }
        if (field[7][0].getState() == othello.getCurrentTurn()) {
            scoreB += 10;
        }
        if (field[0][7].getState() == othello.getCurrentTurn()) {
            scoreB += 10;
        }
        if (field[7][7].getState() == othello.getCurrentTurn()) {
            scoreB += 10;
        }

        return scoreA + 3 * scoreB;
    }

    /**
     * 可能な手をすべて取得する.
     *
     * @param othello フィールドの盤面を保持するオブジェクト.
     * @return すべての可能な手.
     */
    private List<Coordinate> extractPossibleChoice(Board othello) {
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
}
