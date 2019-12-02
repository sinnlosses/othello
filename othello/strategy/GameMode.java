package othello.strategy;

import java.util.Scanner;

/**
 * プレイできるゲームモードの列挙.
 */
public enum GameMode {
    /**
     * 人間同士で対戦するゲームモード
     */
    PLAYERS("1"),
    /**
     * 対AI(弱)と対戦するゲームモード
     */
    WEAK_AI("2"),
    /**
     * 対AI(並)と対戦するゲームモード
     */
    NORMAL_AI("3"),
    /**
     * 対AI(強)と対戦するゲームモード
     */
    STRONG_AI("4"),
    /**
     * AI同士と対戦するゲームモード
     */
    AIS("5");

    /**
     * ゲームモードを文字列で表した情報.
     */
    private String mode;

    GameMode(String mode) {
        this.mode = mode;
    }

    /**
     * ゲームのモードを標準入力により選択する.
     *
     * @return 入力に対応するゲームモード
     */
    public static GameMode choiceGameMode() {
        while (true) {
            System.out.println("プレイするモードを選択してください");

            System.out.println(String.format("%s: プレイヤー同士 %s: 対AI(弱) %s: 対AI(並) %s: 対AI(強) %s: AI同士(弱VS強)",
                    PLAYERS.mode,
                    WEAK_AI.mode,
                    NORMAL_AI.mode,
                    STRONG_AI.mode,
                    AIS.mode));
            System.out.println(String.format("例: %s", GameMode.PLAYERS.mode));

            String mode = new Scanner(System.in).nextLine().trim();

            // 入力がゲームのモードに存在していれば処理を抜ける.
            for (GameMode g : GameMode.values()) {
                if (g.mode.equals(mode)) {
                    return g;
                }
            }
            System.out.println("正しい選択肢を入力してください");
        }
    }

}
