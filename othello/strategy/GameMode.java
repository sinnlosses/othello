package othello.strategy;

/**
 * プレイできるゲームモードの列挙.
 */
public enum GameMode {
    PLAYERS("1"),
    WEAK_AI("2"),
    STRONG_AI("3"),
    AIS("4");

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
