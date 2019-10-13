package othello;

/**
 * 方向を表すクラス.
 */
public class Vector {
    /**
     * 行方向
     */
    private int vectorR;
    /**
     * 列方向
     */
    private int vectorC;

    public Vector(int vectorR, int vectorC) {
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
