package othello;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int inputRow, inputCol;
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

            while (true) {
                System.out.print("行を選択してください--->(a,b,c...,h)");
                inputRow = Field.toRowNumber(new Scanner(System.in).nextLine());

                System.out.print("列を選択してください--->(1,2...,8)");
                inputCol = Field.toColNumber(new Scanner(System.in).nextLine());

                // 入力が有効なら入力完了としてループを抜ける
                if (othello.canPutPiece(inputRow, inputCol)) {
                    break;
                }

                System.out.println("有効な入力ではありません. 正しく入力してください");
            }

            // コマを置けるかチェックして置ける場合は挟んだコマをひっくり返す
            if (othello.canPutPiece(inputRow, inputCol)) {
                othello.putPiece(inputRow, inputCol);
                othello.flipPiecesFromPlaced(inputRow, inputCol);
            } else {
                System.out.println("正しく入力してください");
                continue;
            }

            othello.printField();
            othello.printCurrentSituation();
            othello.nextPlayer();
        }

        System.out.println("ゲーム終了");
    }

}


