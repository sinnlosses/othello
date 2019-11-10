package othello;

import othello.strategy.GameMode;
import othello.strategy.StrategyMgr;

import java.util.Scanner;

/**
 * オセロのゲームを進行するメインクラス
 */
public class Main {
    public static void main(String[] args) {
        Board othello = new Board();

        System.out.println("ゲーム開始");
        StrategyMgr strategy = new StrategyMgr(choiceGameMode());

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
            othello.processToPlacePiece(coordinate);

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

    /**
     * ゲームのモードを標準入力により選択する.
     *
     * @return 入力に対応するゲームモード.
     */
    private static GameMode choiceGameMode() {
        while (true) {
            System.out.println("プレイするモードを選択してください");
            System.out.println(String.format("%s: プレイヤー同士 %s: 対AI(弱) %s: 対AI(強) %s: AI同士(弱VS強)",
                    GameMode.PLAYERS.getMode(),
                    GameMode.WEAK_AI.getMode(),
                    GameMode.STRONG_AI.getMode(),
                    GameMode.AIS.getMode()));
            System.out.println(String.format("例: %s", GameMode.PLAYERS.getMode()));

            String mode = new Scanner(System.in).nextLine().trim();

            // 入力がゲームのモードに存在していれば処理を抜ける.
            for (GameMode g : GameMode.values()) {
                if (g.getMode().equals(mode)) {
                    return g;
                }
            }
            System.out.println("正しい選択肢を入力してください");
        }
    }
}


