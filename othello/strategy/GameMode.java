package othello.strategy;

public enum GameMode {
    PLAYERS("1"),
    WEAK_AI("2"),
    STRONG_AI("3"),
    AIS("4");

    private String mode;

    GameMode(String mode) {
        this.mode = mode;
    }

    public String getMode() {
        return mode;
    }

}
