package othello.strategy;

import othello.Coordinate;
import othello.Field;
import othello.PieceType;

import java.util.EnumMap;

/**
 * オセロにおいて, 誰がプレイするのかを定義するクラス.
 *
 * オセロをするにあたって, プレイヤー同士行う場合や敵AIと行う場合などの
 * ゲームモードを管理する.
 */
public class Strategy{
    /**
     * それぞれのコマに対応する戦略を保持するマップ.
     */
    private EnumMap<PieceType, StrategyInterface> strategyMgr;

    /**
     * それぞれのコマに対応する戦略を初期化を行う.
     *
     * @param othello 盤の情報を保持しているフィールド.
     * @param gameMode 本プログラムによって選ぶことができるゲームモードの選択肢.
     */
    public Strategy(Field othello, GameMode gameMode) {
        strategyMgr = new EnumMap<>(PieceType.class);
        switch (gameMode) {
            case PLAYERS:
                strategyMgr.put(PieceType.BLACK, new Player(othello));
                strategyMgr.put(PieceType.WHITE, new Player(othello));
                break;
            case WEAK_AI:
                strategyMgr.put(PieceType.BLACK, new Player(othello));
                strategyMgr.put(PieceType.WHITE, new WeakAI(othello));
                break;
            case AIS:
                strategyMgr.put(PieceType.BLACK, new WeakAI(othello));
                strategyMgr.put(PieceType.WHITE, new WeakAI(othello));
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
        return strategyMgr.get(currentTurn).decideCoordinate();
    }
}
