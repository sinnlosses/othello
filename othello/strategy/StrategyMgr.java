package othello.strategy;

import java.util.EnumMap;
import othello.Board;
import othello.Coordinate;
import othello.PieceType;

/**
 * ゲームモードを管理するクラス.
 * <p>
 * ゲームモードとは、各コマに対して誰がプレイを担当するか、 例えばプレイヤー同士行う場合や敵AIと行う場合などを指す.
 */
public class StrategyMgr {

  /**
   * それぞれのコマに対応する戦略を保持するマップ.
   */
  private EnumMap<PieceType, StrategyInterface> strategyForPiece;

  /**
   * 指定されたゲームモードに対応して, それぞれのコマのプレイヤーに対応する戦略の管理を行う.
   * <p>
   * 例えば黒のコマの戦略は人間が, 白のコマの戦略はAIが担当するなど.
   *
   * @param gameMode 本プログラムによって選ぶことができるゲームモードの選択肢
   * @throws IllegalArgumentException ゲームモードが存在しない場合
   */
  public StrategyMgr(GameMode gameMode) {
    strategyForPiece = new EnumMap<>(PieceType.class);
    switch (gameMode) {
      case PLAYERS:
        strategyForPiece.put(PieceType.BLACK, new Player());
        strategyForPiece.put(PieceType.WHITE, new Player());
        break;
      case WEAK_AI:
        strategyForPiece.put(PieceType.BLACK, new Player());
        strategyForPiece.put(PieceType.WHITE, new WeakAI());
        break;
      case NORMAL_AI:
        strategyForPiece.put(PieceType.BLACK, new Player());
        strategyForPiece.put(PieceType.WHITE, new NormalAI(PieceType.WHITE));
        break;
      case STRONG_AI:
        strategyForPiece.put(PieceType.BLACK, new Player());
        strategyForPiece.put(PieceType.WHITE, new StrongAI(PieceType.WHITE));
        break;
      case AIS:
        strategyForPiece.put(PieceType.BLACK, new WeakAI());
        strategyForPiece.put(PieceType.WHITE, new StrongAI(PieceType.WHITE));
        break;
      default:
        throw new IllegalArgumentException("指定したゲームモードはありません");
    }
  }

  /**
   * 手番に設定された戦略に基づきコマを置く座標を決定する.
   *
   * @param othello 盤面の状態を保持するオブジェクト
   * @return コマを置く座標
   */
  public Coordinate decideCoordinate(Board othello) {
    return strategyForPiece.get(othello.getCurrentTurn()).decideCoordinate(othello);
  }
}
