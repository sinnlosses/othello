package othello.strategy;

/**
 * プレイできるゲームモードの列挙.
 */
public enum GameMode {
    PLAYERS("1"),
    WEAK_AI("2"),
    STRONG_AI("3"),
    AIS("4");

    public static void printCandidate() {
        System.out.println(String.format("%s: プレイヤー同士 %s: 対AI(弱) %s: 対AI(強) %s: AI同士(弱VS強)",
                PLAYERS.getMode(),
                WEAK_AI.getMode(),
                STRONG_AI.getMode(),
                AIS.getMode()));
        System.out.println(String.format("例: %s", GameMode.PLAYERS.getMode()));
    }

    /**
     * ゲームモードを文字列で表した情報.
     */
    private String mode;

    GameMode(String mode) {
        this.mode = mode;
    }

    /**
     * ゲームモードに対応する文字列を取得する.
     *
     * @return 対応番号.
     */
    public String getMode() {
        return mode;
    }

}
