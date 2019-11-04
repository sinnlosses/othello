package othello;

import othello.strategy.GameMode;
import othello.strategy.StrategyMgrForEach;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * オセロのゲームを進行するメインクラス
 */
public class Main {
    public static void main(String[] args) {
        Board othello = new Board();

        // 初期表示
        othello.printField();
        GameMode gameMode = choiceGameMode();
        StrategyMgrForEach strategy = new StrategyMgrForEach(othello, gameMode);

        while (!othello.isGameOver()) {
            // 手番がコマを置けなければ手番を相手に移す
            if (!othello.canPutForCurrentTurn()) {
                othello.nextPlayer();
                continue;
            }
            System.out.println(othello.getCurrentTurn() + "の手番です");

            // コマを置く座標の入力処理を行う
            Coordinate coordinate = strategy.decideCoordinate(othello.getCurrentTurn());

            // コマを置き, 挟んだコマをひっくり返す
            othello.processToPlacePiece(coordinate);

            // コマを置いた結果を表示する
            othello.printField();
            othello.printCurrentScores();
            othello.nextPlayer();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("ゲーム終了");
    }

    /**
     * ゲームのモードを選択する.
     *
     * @return 入力に対応するゲームモードの列挙型.
     */
    private static GameMode choiceGameMode() {
        String players = "1";
        String weakAI = "2";
        String AIs = "3";

        Map<String, GameMode> gameModes = new HashMap<>();
        gameModes.put(players, GameMode.PLAYERS);
        gameModes.put(weakAI, GameMode.WEAK_AI);
        gameModes.put(AIs, GameMode.AIS);

        while (true) {
            System.out.println("プレイするモードを選択してください");
            System.out.println(String.format("%s: プレイヤー同士 %s: 対AI(弱) %s: 対AI(強)",
                    players,
                    weakAI,
                    AIs));
            System.out.println(String.format("例: %s", players));

            String mode = new Scanner(System.in).nextLine().trim();

            // 入力がゲームのモードに存在していれば処理を抜ける.
            if (gameModes.containsKey(mode)) {
                return gameModes.get(mode);
            }
            System.out.println("正しい選択肢を入力してください");
        }
    }
}


