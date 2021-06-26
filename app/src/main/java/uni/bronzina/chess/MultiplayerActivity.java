package uni.bronzina.chess;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

import static uni.bronzina.chess.IntroActivity.pPass;
import static uni.bronzina.chess.TheEngine.getPromoteToB;
import static uni.bronzina.chess.TheEngine.promoteToW;
import static uni.bronzina.chess.TheEngine.terminal;
import static uni.bronzina.chess.TheEngine.theBoard;
import static uni.bronzina.chess.TheUserInterface.drawBoardPiecesMulti;

public class MultiplayerActivity extends AppCompatActivity {

    private static final String TAG = "MultiplayerActivity";

    private String player = "";
    private String roomName = "";
    private String role = "";
    private String rule;
    private TextView textView;

    private FirebaseDatabase database;
    private DatabaseReference moveRef;
    private DatabaseReference turnRef;
    private DatabaseReference player1Ref;
    private FirebaseUser user;


    static ImageView x63,x62,x61,x60,x59,x58,x57,x56,x55,x54,x53,x52,x51,x50,x49,x48,x47,x46,x45,
            x44,x43,x42,x41,x40,x39,x38,x37,x36,x35,x34,x33,x32,x31,x30,x29,x28,x27,x26,x25,x24,
            x23,x22,x21,x20,x19,x18,x17,x16,x15,x14,x13,x12,x11,x10,x9,x8,x7,x6,x5,x4,x3,x2,x1,x0;

    static int[] imageViews = {R.id.p0,R.id.p1,R.id.p2,R.id.p3,R.id.p4,R.id.p5,R.id.p6,R.id.p7,R.id.p8,R.id.p9,
            R.id.p10,R.id.p11,R.id.p12,R.id.p13,R.id.p14,R.id.p15,R.id.p16,R.id.p17,R.id.p18,R.id.p19,
            R.id.p20,R.id.p21,R.id.p22,R.id.p23,R.id.p24,R.id.p25,R.id.p26,R.id.p27,R.id.p28,R.id.p29,
            R.id.p30,R.id.p31,R.id.p32,R.id.p33,R.id.p34,R.id.p35,R.id.p36,R.id.p37,R.id.p38,R.id.p39,
            R.id.p40,R.id.p41,R.id.p42,R.id.p43,R.id.p44,R.id.p45,R.id.p46,R.id.p47,R.id.p48,R.id.p49,
            R.id.p50,R.id.p51,R.id.p52,R.id.p53,R.id.p54,R.id.p55,R.id.p56,R.id.p57,R.id.p58,R.id.p59,
            R.id.p60,R.id.p61,R.id.p62,R.id.p63};

    static ImageView [] chessImages = {x0,x1,x2,x3,x4,x5,x6,x7,x8,x9,x10,x11,x12,x13,x14,x15,
            x16,x17,x18,x19,x20,x21,x22,x23,x24,x25,x26,x27,x28,x29,x30,x31,
            x32,x33,x34,x35,x36,x37,x38,x39,x40,x41,x42,x43,x44,x45,x46,x47,
            x48,x49,x50,x51,x52,x53,x54,x55,x56,x57,x58,x59,x60,x61,x62,x63};

    boolean wTurn, firstClick;
    String tryMove;

    static String moveOptions;
    static int firstNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveOptions="";
                if (!wTurn){
                    moveOptions= terminal("suggestMove,black");
                } else {
                    moveOptions= terminal("suggestMove,white");
                }

                try {
                    if (moveOptions.equals("K-0-0R,")) {
                        chessImages[6].setBackgroundResource(R.drawable.suggested);;
                    } else if (moveOptions.equals("K0-0-0,")) {
                        chessImages[2].setBackgroundResource(R.drawable.suggested);;
                    } else if (moveOptions.equals("k-0-0r,")) {
                        chessImages[62].setBackgroundResource(R.drawable.suggested);;
                    } else if (moveOptions.equals("k0-0-0,")) {
                        chessImages[58].setBackgroundResource(R.drawable.suggested);;
                    } else {
                        String temp = String.valueOf(moveOptions.charAt(3)) +
                                String.valueOf(moveOptions.charAt(4));
                        int highlightThis = Integer.parseInt(temp);
                        chessImages[highlightThis].setBackgroundResource(R.drawable.suggested);
                        temp = String.valueOf(moveOptions.charAt(1)) +
                                String.valueOf(moveOptions.charAt(2));
                        highlightThis = Integer.parseInt(temp);
                        chessImages[highlightThis].setBackgroundResource(R.drawable.suggested);
                    }

                } catch (Exception e) {
                    // Do nothing.
                    Log.i(TAG, e.toString());
                }

                Snackbar.make(view, "Engine suggests: "+ moveOptions, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        firstClick = false;

        moveOptions = "Move Options";

        // Declare all of our image views programmatically.
        for (int i=0; i<64; i++) {
            chessImages[i]=(ImageView)findViewById(imageViews[i]);
        } // checker board.

        //Start a new game.
        terminal("newGame");

        // Visually Draw the board....
        drawBoardPiecesMulti();

        database = FirebaseDatabase.getInstance("https://chess-32d01-default-rtdb.europe-west1.firebasedatabase.app/");

        user = FirebaseAuth.getInstance().getCurrentUser();
        player = user.getDisplayName();
        Log.i(TAG, player);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            roomName = extras.getString("roomName");
            if(roomName.equals(player)){
                role = "host";
            }
            else{
                role = "guest";
            }
            Log.i(TAG, role);
        }
        textView = findViewById(R.id.textView);
        if(role.equals("guest")){
            rule = player + ": Move white pieces!";
            textView.setText(rule);
            Drawable dark = getResources().getDrawable(R.drawable.dark);
            textView.setBackground(dark);
            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.wp, 0, 0, 0);
        }
        else{
            rule = player + ": Move black pieces!";
            textView.setText(rule);
            Drawable light = getResources().getDrawable(R.drawable.light);
            textView.setBackground(light);
            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bp, 0, 0, 0);
        }

        moveRef = database.getReference("rooms/" + roomName + "/move");
        moveRef.onDisconnect().removeValue();
        turnRef = database.getReference("rooms/" + roomName + "/turn");
        turnRef.setValue("host");
        addTurnListener();
        addMoveListener();

    }

    @Override
    public void onResume(){
        super.onResume();
        Log.i(TAG, "Resumed.");

    }

    public void moveablePiece (View view) {
            // Get the clicked squares tag to see what number it is.
            int number = Integer.parseInt(view.getTag().toString());
            String played;
            if (number < 10) {
                played = "0" + String.valueOf(number);
            } else {
                played = String.valueOf(number);
            }

            if (firstClick) {

                int minusNum = number - firstNum;
                int plusNum = firstNum - number;

                firstClick = false;
                String myMove = tryMove + played + String.valueOf(theBoard[number]);

                moveOptions = terminal("availMoves," + String.valueOf(wTurn));

                String[] separated = moveOptions.split(",");

                if (Arrays.asList(separated).contains(myMove)) {

                    String query = terminal("myMove," + myMove);
                    moveRef.setValue(myMove);
                    if(role.equals("host")){
                        turnRef.setValue("guest");
                    }
                    else{
                        turnRef.setValue("host");
                    }
                    drawBoardPiecesMulti();
                    wTurn = !wTurn;

                    /*if (!pPass) {
                        // Since we moved, if it is not pass and play, make the computer move.
                        moveOptions="";
                        if (!wTurn){
                            moveOptions= terminal("suggestMove,black");
                        } else {
                            moveOptions= terminal("suggestMove,white");
                        }
                        if (moveOptions.isEmpty()) {
                            staleOrCheckMate();
                        } else {
                            //getNextMove();
                            //addMoveListener();
                        }
                    }*/

                } else {

                    if (myMove.equalsIgnoreCase("K0406*")) {
                        myMove = "K-0-0R";
                    } else if (myMove.equalsIgnoreCase("K0402*")) {
                        myMove = "K0-0-0";
                    } else if (myMove.equalsIgnoreCase("k6062*")) {
                        myMove = "k-0-0r";
                    } else if (myMove.equalsIgnoreCase("k6058*")) {
                        myMove = "k0-0-0";
                    }


                    if (myMove.contains("P48") || myMove.contains("P49") || myMove.contains("P50") ||
                            myMove.contains("P51") || myMove.contains("P52") || myMove.contains("P53") ||
                            myMove.contains("P54") || myMove.contains("P55")) {
                        if (minusNum == 8) {
                            myMove = "Pu" + promoteToW + played + String.valueOf(theBoard[number]);
                        } else if (minusNum == 9) {
                            myMove = "Pr" + promoteToW + played + String.valueOf(theBoard[number]);
                        } else if (minusNum == 7) {
                            myMove = "Pl" + promoteToW + played + String.valueOf(theBoard[number]);
                        }
                    }

                    if (myMove.contains("p08") || myMove.contains("p09") || myMove.contains("p10") ||
                            myMove.contains("p11") || myMove.contains("p12") || myMove.contains("p13") ||
                            myMove.contains("p14") || myMove.contains("p15")) {
                        if (plusNum == 8) {
                            myMove = "pu" + getPromoteToB + played + String.valueOf(theBoard[number]);
                        } else if (plusNum == 7) {
                            myMove = "pr" + getPromoteToB + played + String.valueOf(theBoard[number]);
                        } else if (plusNum == 9) {
                            myMove = "pl" + getPromoteToB + played + String.valueOf(theBoard[number]);
                        }
                    }

                    if (myMove.contains("P32") || myMove.contains("P33") || myMove.contains("P34") ||
                            myMove.contains("P35") || myMove.contains("P36") || myMove.contains("P37") ||
                            myMove.contains("P38") || myMove.contains("P39")) {
                        if (minusNum == 9) {
                            myMove = "PER" + played + "p";
                        } else if (minusNum == 7) {
                            myMove = "PEL" + played + "p";
                        }
                    }

                    if (myMove.contains("p24") || myMove.contains("p25") || myMove.contains("p26") ||
                            myMove.contains("p27") || myMove.contains("p28") || myMove.contains("p29") ||
                            myMove.contains("p30") || myMove.contains("p31")) {
                        if (plusNum == 7) {
                            myMove = "per" + played + "P";
                        } else if (plusNum == 9) {
                            myMove = "pel" + played + "P";
                        }
                    }

                    if (Arrays.asList(separated).contains(myMove)) {

                        String query = terminal("myMove," + myMove);
                        moveRef.setValue(myMove);
                        if(role.equals("host")){
                            turnRef.setValue("guest");
                        }
                        else{
                            turnRef.setValue("host");
                        }
                        drawBoardPiecesMulti();
                        wTurn = !wTurn;

                        if (!pPass) {
                            // Since we moved, if it is not pass and play, make the computer move.
                            moveOptions = "";
                            if (!wTurn) {
                                moveOptions = terminal("suggestMove,black");
                            } else {
                                moveOptions = terminal("suggestMove,white");
                            }
                            if (moveOptions.isEmpty()) {
                                staleOrCheckMate();
                            } else {
                                addMoveListener();
                            }
                        }

                    }
                }
                tryMove = "";
                myMove = "";
                drawBoardPiecesMulti();

            } else {
                firstNum = number;
                try {
                    chessImages[firstNum].setBackgroundResource(R.drawable.highlight);
                    firstClick = true;
                    tryMove = String.valueOf(theBoard[number]) + played;
                    // Testing only // Log.i("WJH", tryMove);
                    String query = terminal("pieceMoves," + String.valueOf(theBoard[number]) +
                            "," + played);
                    String[] stringArray = query.split(",");
                    if (stringArray.length > 0) {
                        for (int i = 0; i < stringArray.length; i++) {

                            String temp = stringArray[i];
                            if (temp.equals("K-0-0R")) {
                                chessImages[6].setBackgroundResource(R.drawable.highlight);
                                ;
                            } else if (temp.equals("K0-0-0")) {
                                chessImages[2].setBackgroundResource(R.drawable.highlight);
                                ;
                            } else if (temp.equals("k-0-0r")) {
                                chessImages[62].setBackgroundResource(R.drawable.highlight);
                                ;
                            } else if (temp.equals("k0-0-0")) {
                                chessImages[58].setBackgroundResource(R.drawable.highlight);
                                ;
                            } else {
                                temp = String.valueOf(stringArray[i].charAt(3)) +
                                        String.valueOf(stringArray[i].charAt(4));
                                int highlightThis = Integer.parseInt(temp);
                                chessImages[highlightThis].setBackgroundResource(R.drawable.highlight);
                            }
                        }
                    }

                } catch (Exception e) {
                    // Do nothing.
                }
            }

            moveOptions = "";
            if (!wTurn) {
                moveOptions = terminal("suggestMove,black");
            } else {
                moveOptions = terminal("suggestMove,white");
            }
            if (moveOptions.isEmpty()) {
                staleOrCheckMate();
            }

    } // End clicked piece.

    public void staleOrCheckMate() {
        String status = terminal("checkmate");
        if (status.equalsIgnoreCase("1")) {
            status = "checkmate!";
        } else {
            status = "stalemate!";
        }
        String turnIs = "";
        if (wTurn) {turnIs="White is in ";} else {turnIs="Black is in ";}
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle(turnIs + status)
                .setMessage(
                        "Would you like to play a new game?")
                .setPositiveButton("View Board", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // Do nothing.

                    }
                })
                .setNegativeButton("New Game", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent myintent = new Intent(MultiplayerActivity.this, uni.bronzina.chess.IntroActivity.class);
                        startActivity(myintent);

                    }
                })
                .show(); // Make sure you show your popup or it wont work very well!

    }

    public void resetGame(View view) {
        // Call for a new game and redraw the board.

        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("New Game?")
                .setMessage(
                        "Would you like to play a new game?")
                .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // Do nothing.

                    }
                })
                .setNegativeButton("New Game", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Intent myintent = new Intent(MultiplayerActivity.this, uni.bronzina.chess.IntroActivity.class);
                        startActivity(myintent);
                        finish();

                    }
                })
                .show(); // Make sure you show your popup or it wont work very well!
    } // End reset game.


    private void addMoveListener (){
        moveRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String move = snapshot.getValue(String.class);
                moveOptions= terminal("availMoves,"+String.valueOf(wTurn));
                String[] separated = moveOptions.split(",");
                if (Arrays.asList(separated).contains(move)) {

                    String query = terminal("myMove," + move);
                    drawBoardPiecesMulti();
                    wTurn = !wTurn;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MultiplayerActivity.this, "Error in updating chess board!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addTurnListener(){
        turnRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String turn = snapshot.getValue(String.class);
                if(turn.equals(role)){
                    setEnableClick(true);
                    textView.setText(rule + "\n YOUR TURN");
                }
                else{
                    setEnableClick(false);
                    textView.setText(rule + "\n OPPONENT TURN");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MultiplayerActivity.this, "Error in making turn!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setEnableClick(boolean trueORfalse){
        ImageView iv;
        iv = (ImageView) findViewById(R.id.p0); iv.setClickable(trueORfalse);
        iv = (ImageView) findViewById(R.id.p1); iv.setClickable(trueORfalse);
        iv = (ImageView) findViewById(R.id.p2); iv.setClickable(trueORfalse);
        iv = (ImageView) findViewById(R.id.p3); iv.setClickable(trueORfalse);
        iv = (ImageView) findViewById(R.id.p4); iv.setClickable(trueORfalse);
        iv = (ImageView) findViewById(R.id.p5); iv.setClickable(trueORfalse);
        iv = (ImageView) findViewById(R.id.p6); iv.setClickable(trueORfalse);
        iv = (ImageView) findViewById(R.id.p7); iv.setClickable(trueORfalse);
        iv = (ImageView) findViewById(R.id.p8); iv.setClickable(trueORfalse);
        iv = (ImageView) findViewById(R.id.p9); iv.setClickable(trueORfalse);

        iv = (ImageView) findViewById(R.id.p10); iv.setClickable(trueORfalse);
        iv = (ImageView) findViewById(R.id.p11); iv.setClickable(trueORfalse);
        iv = (ImageView) findViewById(R.id.p12); iv.setClickable(trueORfalse);
        iv = (ImageView) findViewById(R.id.p13); iv.setClickable(trueORfalse);
        iv = (ImageView) findViewById(R.id.p14); iv.setClickable(trueORfalse);
        iv = (ImageView) findViewById(R.id.p15); iv.setClickable(trueORfalse);
        iv = (ImageView) findViewById(R.id.p16); iv.setClickable(trueORfalse);
        iv = (ImageView) findViewById(R.id.p17); iv.setClickable(trueORfalse);
        iv = (ImageView) findViewById(R.id.p18); iv.setClickable(trueORfalse);
        iv = (ImageView) findViewById(R.id.p19); iv.setClickable(trueORfalse);

        iv = (ImageView) findViewById(R.id.p20); iv.setClickable(trueORfalse);
        iv = (ImageView) findViewById(R.id.p21); iv.setClickable(trueORfalse);
        iv = (ImageView) findViewById(R.id.p22); iv.setClickable(trueORfalse);
        iv = (ImageView) findViewById(R.id.p23); iv.setClickable(trueORfalse);
        iv = (ImageView) findViewById(R.id.p24); iv.setClickable(trueORfalse);
        iv = (ImageView) findViewById(R.id.p25); iv.setClickable(trueORfalse);
        iv = (ImageView) findViewById(R.id.p26); iv.setClickable(trueORfalse);
        iv = (ImageView) findViewById(R.id.p27); iv.setClickable(trueORfalse);
        iv = (ImageView) findViewById(R.id.p28); iv.setClickable(trueORfalse);
        iv = (ImageView) findViewById(R.id.p29); iv.setClickable(trueORfalse);

        iv = (ImageView) findViewById(R.id.p30); iv.setClickable(trueORfalse);
        iv = (ImageView) findViewById(R.id.p31); iv.setClickable(trueORfalse);
        iv = (ImageView) findViewById(R.id.p32); iv.setClickable(trueORfalse);
        iv = (ImageView) findViewById(R.id.p33); iv.setClickable(trueORfalse);
        iv = (ImageView) findViewById(R.id.p34); iv.setClickable(trueORfalse);
        iv = (ImageView) findViewById(R.id.p35); iv.setClickable(trueORfalse);
        iv = (ImageView) findViewById(R.id.p36); iv.setClickable(trueORfalse);
        iv = (ImageView) findViewById(R.id.p37); iv.setClickable(trueORfalse);
        iv = (ImageView) findViewById(R.id.p38); iv.setClickable(trueORfalse);
        iv = (ImageView) findViewById(R.id.p39); iv.setClickable(trueORfalse);

        iv = (ImageView) findViewById(R.id.p40); iv.setClickable(trueORfalse);
        iv = (ImageView) findViewById(R.id.p41); iv.setClickable(trueORfalse);
        iv = (ImageView) findViewById(R.id.p42); iv.setClickable(trueORfalse);
        iv = (ImageView) findViewById(R.id.p43); iv.setClickable(trueORfalse);
        iv = (ImageView) findViewById(R.id.p44); iv.setClickable(trueORfalse);
        iv = (ImageView) findViewById(R.id.p45); iv.setClickable(trueORfalse);
        iv = (ImageView) findViewById(R.id.p46); iv.setClickable(trueORfalse);
        iv = (ImageView) findViewById(R.id.p47); iv.setClickable(trueORfalse);
        iv = (ImageView) findViewById(R.id.p48); iv.setClickable(trueORfalse);
        iv = (ImageView) findViewById(R.id.p49); iv.setClickable(trueORfalse);

        iv = (ImageView) findViewById(R.id.p50); iv.setClickable(trueORfalse);
        iv = (ImageView) findViewById(R.id.p51); iv.setClickable(trueORfalse);
        iv = (ImageView) findViewById(R.id.p52); iv.setClickable(trueORfalse);
        iv = (ImageView) findViewById(R.id.p53); iv.setClickable(trueORfalse);
        iv = (ImageView) findViewById(R.id.p54); iv.setClickable(trueORfalse);
        iv = (ImageView) findViewById(R.id.p55); iv.setClickable(trueORfalse);
        iv = (ImageView) findViewById(R.id.p56); iv.setClickable(trueORfalse);
        iv = (ImageView) findViewById(R.id.p57); iv.setClickable(trueORfalse);
        iv = (ImageView) findViewById(R.id.p58); iv.setClickable(trueORfalse);
        iv = (ImageView) findViewById(R.id.p59); iv.setClickable(trueORfalse);

        iv = (ImageView) findViewById(R.id.p60); iv.setClickable(trueORfalse);
        iv = (ImageView) findViewById(R.id.p61); iv.setClickable(trueORfalse);
        iv = (ImageView) findViewById(R.id.p62); iv.setClickable(trueORfalse);
        iv = (ImageView) findViewById(R.id.p63); iv.setClickable(trueORfalse);
    }

}