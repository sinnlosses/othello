package othello.strategy;

public enum GameMode {
    PLAYERS("1"),
    AI("2"),
    AIS("3");

    private String mode;

    GameMode(String mode) {
        this.mode = mode;
    }

    public String getMode() {
        return mode;
    }

}
