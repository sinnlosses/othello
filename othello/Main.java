package othello;

import othello.strategy.GameMode;
import othello.strategy.StrategyMgr;

/**
 * オセロのゲームを進行するメインクラス
 */
public class Main {
    public static void main(String[] args) {
        Board othello = new Board();

        System.out.println("ゲーム開始");
        StrategyMgr strategy = new StrategyMgr(GameMode.choiceGameMode());

        // 初期表示
        othello.printField();

        while (!othello.isGameOver()) {
            // 手番がコマを置けなければ手番を相手に移す
            if (!othello.canPutForCurrentTurn()) {
                othello.nextTurn();
            }

            // 手番の情報を表示する.
            othello.printCurrentTurn();

            // コマを置く座標の決定処理を行う.
            Coordinate coordinate = strategy.decideCoordinate(othello);

            // コマを置き, 挟んだコマをひっくり返す.
            othello.processToPutPiece(coordinate);

            // コマを置いた結果を表示する.
            othello.printResult(coordinate);

            othello.nextTurn();

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        System.out.println("ゲーム終了");
    }
}


