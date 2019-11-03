package othello;

import java.util.Scanner;

/**
 * オセロのゲームを進行するメインクラス
 */
public class Main {
    public static void main(String[] args) {
        Field othello = new Field();

        // 初期表示
        othello.printField();

        while (!othello.isGameOver()) {
            // 手番がコマを置けなければ手番を相手に移す
            if (!othello.canPutForCurrentTurn()) {
                othello.nextPlayer();
                continue;
            }
            System.out.println(othello.getCurrentTurn() + "の手番です");

            // コマを置くことができる座標が指定されるまでプレイヤーが入力処理を行う
            Coordinate coordinate = inputCoordinateByPlayer(othello);

            // コマを置き, 挟んだコマをひっくり返す
            othello.putPiece(coordinate);
            othello.flipPiecesFromPlaced(coordinate);

            // コマを置いた結果を表示する
            othello.printField();
            othello.printCurrentScores();
            othello.nextPlayer();
        }

        System.out.println("ゲーム終了");
    }

    /**
     * コマを置く座標をプレイヤーに標準入力させる.
     *
     * @param othello コマの状況を保持しているフィールド
     * @return プレイヤーが入力し、コマを置けることが保証された座標
     */
    private static Coordinate inputCoordinateByPlayer(Field othello) {
        while (true) {
            System.out.println("コマを置く座標の行と列を空白区切りで入力してください");
            System.out.println("行--->(a,b,c,...,h), 列--->(1,2,3,...,8)");
            System.out.println("例: a 1");

            String[] inputCoordinate = new Scanner(System.in).nextLine().split(" ");

            if (inputCoordinate.length != 2) {
                continue;
            }
            int inputRow = Field.toRowNumber(inputCoordinate[0]);
            int inputCol = Field.toColNumber(inputCoordinate[1]);

            // 入力が有効なら入力完了としてループを抜ける
            if (othello.canPutPiece(Coordinate.valueOf(inputRow, inputCol))) {
                return Coordinate.valueOf(inputRow, inputCol);
            }
            System.out.println("有効な入力ではありません. 正しく入力してください");
        }
    }
}


