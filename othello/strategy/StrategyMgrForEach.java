package othello.strategy;

import othello.Coordinate;
import othello.Board;
import othello.PieceType;

import java.util.EnumMap;

/**
 * オセロにおいて, 誰がプレイするのかを定義するクラス.
 *
 * オセロをするにあたって, プレイヤー同士行う場合や敵AIと行う場合などの
 * ゲームモードを管理する.
 */
public class StrategyMgrForEach {
    /**
     * それぞれのコマに対応する戦略を保持するマップ.
     */
    private EnumMap<PieceType, StrategyInterface> strategyForPiece;

    /**
     * それぞれのコマに対応する戦略を初期化を行う.
     *
     * @param othello 盤の情報を保持しているフィールド.
     * @param gameMode 本プログラムによって選ぶことができるゲームモードの選択肢.
     */
    public StrategyMgrForEach(Board othello, GameMode gameMode) {
        strategyForPiece = new EnumMap<>(PieceType.class);
        switch (gameMode) {
            case PLAYERS:
                strategyForPiece.put(PieceType.BLACK, new Player(othello));
                strategyForPiece.put(PieceType.WHITE, new Player(othello));
                break;
            case WEAK_AI:
                strategyForPiece.put(PieceType.BLACK, new Player(othello));
                strategyForPiece.put(PieceType.WHITE, new WeakAI(othello));
                break;
            case AIS:
                strategyForPiece.put(PieceType.BLACK, new WeakAI(othello));
                strategyForPiece.put(PieceType.WHITE, new WeakAI(othello));
                break;
        }
    }

    /**
     * 戦略に基づきコマを置く座標を決定する.
     *
     * @param currentTurn 手番.
     * @return コマを置く座標.
     */
    public Coordinate decideCoordinate(PieceType currentTurn) {
        return strategyForPiece.get(currentTurn).decideCoordinate();
    }
}
