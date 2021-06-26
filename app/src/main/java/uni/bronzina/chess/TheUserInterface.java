package uni.bronzina.chess;

import static uni.bronzina.chess.PlayActivity.chessImage;
import static uni.bronzina.chess.MultiplayerActivity.chessImages;
import static uni.bronzina.chess.TheEngine.theBoard;

public class TheUserInterface {

    public static void drawBoardPieces() {

        for (int i=0; i<64; i++) {
            chessImage[i].setBackgroundResource(R.drawable.dark);
            if (i==1 || i==3 || i==5 || i==7 ||
                    i==8 || i==10 || i==12 || i==14 ||
                    i==17 || i==19 || i==21 || i==23 ||
                    i==24 || i==26 || i==28 || i==30 ||
                    i==33 || i==35 || i==37 || i==39 ||
                    i==40 || i==42 || i==44 || i==46 ||
                    i==49 || i==51 || i==53 || i==55 ||
                    i==56 || i==58 || i==60 || i==62) {
                chessImage[i].setBackgroundResource(R.drawable.light);
            }

            switch (theBoard[i]) {
                case '*': chessImage[i].setImageResource(R.drawable.empty);
                    break;
                case 'P': chessImage[i].setImageResource(R.drawable.wp);
                    break;
                case 'R': chessImage[i].setImageResource(R.drawable.wr);;
                    break;
                case 'N': chessImage[i].setImageResource(R.drawable.wn);;
                    break;
                case 'B': chessImage[i].setImageResource(R.drawable.wb);;
                    break;
                case 'Q': chessImage[i].setImageResource(R.drawable.wq);;
                    break;
                case 'K': chessImage[i].setImageResource(R.drawable.wk);;
                    break;
                case 'p': chessImage[i].setImageResource(R.drawable.bp);
                    break;
                case 'r': chessImage[i].setImageResource(R.drawable.br);;
                    break;
                case 'n': chessImage[i].setImageResource(R.drawable.bn);;
                    break;
                case 'b': chessImage[i].setImageResource(R.drawable.bb);;
                    break;
                case 'q': chessImage[i].setImageResource(R.drawable.bq);;
                    break;
                case 'k': chessImage[i].setImageResource(R.drawable.bk);;
                    break;
            }
        } //Done for loop for drawing board.

    } // End draw board pieces

    public static void drawBoardPiecesMulti() {

        for (int i=0; i<64; i++) {
            chessImages[i].setBackgroundResource(R.drawable.dark);
            if (i==1 || i==3 || i==5 || i==7 ||
                    i==8 || i==10 || i==12 || i==14 ||
                    i==17 || i==19 || i==21 || i==23 ||
                    i==24 || i==26 || i==28 || i==30 ||
                    i==33 || i==35 || i==37 || i==39 ||
                    i==40 || i==42 || i==44 || i==46 ||
                    i==49 || i==51 || i==53 || i==55 ||
                    i==56 || i==58 || i==60 || i==62) {
                chessImages[i].setBackgroundResource(R.drawable.light);
            }

            switch (theBoard[i]) {
                case '*': chessImages[i].setImageResource(R.drawable.empty);
                    break;
                case 'P': chessImages[i].setImageResource(R.drawable.wp);
                    break;
                case 'R': chessImages[i].setImageResource(R.drawable.wr);;
                    break;
                case 'N': chessImages[i].setImageResource(R.drawable.wn);;
                    break;
                case 'B': chessImages[i].setImageResource(R.drawable.wb);;
                    break;
                case 'Q': chessImages[i].setImageResource(R.drawable.wq);;
                    break;
                case 'K': chessImages[i].setImageResource(R.drawable.wk);;
                    break;
                case 'p': chessImages[i].setImageResource(R.drawable.bp);
                    break;
                case 'r': chessImages[i].setImageResource(R.drawable.br);;
                    break;
                case 'n': chessImages[i].setImageResource(R.drawable.bn);;
                    break;
                case 'b': chessImages[i].setImageResource(R.drawable.bb);;
                    break;
                case 'q': chessImages[i].setImageResource(R.drawable.bq);;
                    break;
                case 'k': chessImages[i].setImageResource(R.drawable.bk);;
                    break;
            }
        } //Done for loop for drawing board.

    } // End draw board pieces

}

