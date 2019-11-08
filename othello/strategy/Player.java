package othello.strategy;

import othello.Coordinate;
import othello.Board;

import java.util.Scanner;

public class Player implements StrategyInterface{

    /**
     * コマの状況を保持しているフィールド
     */
    private Board othello;

    public Player() {
        // 処理なし
    }

    /**
     * コマを置く座標をプレイヤーに標準入力させる.
     *
     * @return プレイヤーが入力し、コマを置けることが保証された座標
     */
    @Override
    public Coordinate decideCoordinate(Board othello) {
        while (true) {
            System.out.println("コマを置く座標の行と列を空白区切りで入力してください");
            System.out.println("行--->(a,b,c,...,h), 列--->(1,2,3,...,8)");
            System.out.println("例: a 1");

            String[] inputCoordinate = new Scanner(System.in).nextLine().split(" ");

            if (inputCoordinate.length != 2) {
                continue;
            }
            int inputRow = Board.toRowNumber(inputCoordinate[0]);
            int inputCol = Board.toColNumber(inputCoordinate[1]);

            // 入力が有効なら入力完了としてループを抜ける
            if (othello.canPutPiece(Coordinate.valueOf(inputRow, inputCol))) {
                return Coordinate.valueOf(inputRow, inputCol);
            }
            System.out.println("有効な入力ではありません. 正しく入力してください");
        }
    }
}