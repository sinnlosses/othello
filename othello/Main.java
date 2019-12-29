package othello;

import othello.strategy.GameMode;
import othello.strategy.StrategyMgr;

/**
 * オセロのゲームを進行するメインクラス.
 */
public class Main {

  public static void main(String[] args) {
    Board othello = new Board();

    System.out.println("ゲーム開始");
    StrategyMgr strategyMgr = new StrategyMgr(GameMode.choiceGameMode());

    // 初期表示
    othello.printField();

    while (!othello.isGameOver()) {
      if (!othello.canPutForCurrentTurn()) {
        othello.nextTurn();
      }

      othello.printCurrentTurn();

      // 手番ごとに保持した戦略に基づきコマを置く座標の決定処理を行う.
      Coordinate coordinate = strategyMgr.decideCoordinate(othello);

      // コマを置き, 挟んだコマをひっくり返す.
      othello.processToPutPiece(coordinate);

      othello.printResult(coordinate);
      othello.nextTurn();

      try {
        Thread.sleep(200);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

    }

    System.out.println("ゲーム終了");
  }
}


