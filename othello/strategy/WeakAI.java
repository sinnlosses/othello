package othello.strategy;

import othello.Coordinate;
import othello.Board;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WeakAI implements StrategyInterface{
    private final int ROW = 8;
    private final int COL = 8;

    private Board othello;

    public WeakAI(Board othello) {
        this.othello = othello;
    }

    @Override
    public Coordinate decideCoordinate() {
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
