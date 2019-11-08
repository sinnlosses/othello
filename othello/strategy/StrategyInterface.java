package othello.strategy;

import othello.Board;
import othello.Coordinate;

/**
 * オセロのコマを置く座標を決定するインターフェース.
 */
public interface StrategyInterface {
    Coordinate decideCoordinate(final Board othello);
}
