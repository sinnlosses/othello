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

            System.out.println(othello.getCurrentTurn().toString() + "の手番です");

            // ここから入力処理
            int inputRow;
            int inputCol;
            while (true) {
                System.out.println("コマを置く座標の行と列を空白区切りで入力してください");
                System.out.println("行--->(a,b,c...,h), 列--->(1,2...,8)");
                System.out.println("例: a 1");

                String[] inputCoordinate = new Scanner(System.in).nextLine().split(" ");
                inputRow = Field.toRowNumber(inputCoordinate[0]);
                inputCol = Field.toColNumber(inputCoordinate[1]);

                // 入力が有効なら入力完了としてループを抜ける
                if (othello.canPutPiece(inputRow, inputCol)) {
                    break;
                }

                System.out.println("有効な入力ではありません. 正しく入力してください");
            }

            // コマを置き、挟んだコマをひっくり返す
            othello.putPiece(inputRow, inputCol);
            othello.flipPiecesFromPlaced(inputRow, inputCol);

            othello.printField();
            othello.printCurrentScores();
            othello.nextPlayer();
        }

        System.out.println("ゲーム終了");
    }

}


