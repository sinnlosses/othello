package othello.strategy;

import othello.Coordinate;
import othello.Board;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WeakAI implements StrategyInterface{
    /**
     * フィールドの行数.
     */
    private final int ROW = 8;
    /**
     * フィールドの列数.
     */
    private final int COL = 8;

    public WeakAI() {
        // 処理なし
    }

    /**
     * コマを置く座標をランダムに決定する.
     *
     * @param othello 盤面の情報を保持するオブジェクト.
     * @return コマを置く座標.
     */
    @Override
    public Coordinate decideCoordinate(final Board othello) {
        List<Coordinate> candidates = new ArrayList<>();
        for (int r = 0; r < ROW; r++) {
            for (int c = 0; c < COL; c++) {
                if (othello.canPutPiece(Coordinate.valueOf(r, c))) {
                    candidates.add(Coordinate.valueOf(r, c));
                }
            }
        }
        Collections.shuffle(candidates);
        return candidates.get(0);
    }
}
