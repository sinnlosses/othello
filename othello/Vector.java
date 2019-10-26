package othello;

/**
 * 方向を表すクラス.
 */
public enum Vector {
    LEFT_UPPER(-1, -1),
    UP(0, -1),
    RIGHT_UPPER(1, -1),
    LEFT(-1, 0),
    RIGHT(1, 0),
    LEFT_DOWN(-1, 1),
    DOWN(0, 1),
    RIGHT_DOWN(1, 1);

    /**
     * 行方向
     */
    private int vectorR;
    /**
     * 列方向
     */
    private int vectorC;

    Vector(int vectorR, int vectorC) {
        this.vectorR = vectorR;
        this.vectorC = vectorC;
    }

    /**
     * 行方向を取得する.
     *
     * @return 行方向
     */
    public int getVectorR() {
        return vectorR;
    }

    /**
     * 列方向を取得する.
     *
     * @return 列方向
     */
    public int getVectorC() {
        return vectorC;
    }
}
