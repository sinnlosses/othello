package othello;

import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        int inputR, inputC;
        Field othello = new Field();

        // 初期表示
        othello.printField();

        while (!othello.isGameOver()){
            // 手番がコマを置けなければ手番を相手に移す
            if ( othello.canStillPutForCurrentTurn() ){
                othello.nextPlayer();
                continue;
            }

            System.out.println(othello.getCurrentTurn().getImage() + "の手番です-- ");

            System.out.print("行を選択してください--->(a,b,c...)");
            inputR = Field.alToInt( new Scanner(System.in).nextLine() );

            System.out.print("列を選択してください--->(0,1,2...)");
            inputC = new Scanner(System.in).nextInt()-1;

            // コマを置けるかチェックして置ける場合は挟んだコマをひっくり返す
            if (othello.canPutPiece(inputR, inputC)){
                othello.putPiece(inputR, inputC);
                othello.flipPiecesFromPlacedPiece(inputR, inputC);
            } else{
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


