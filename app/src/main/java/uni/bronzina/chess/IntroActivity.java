package uni.bronzina.chess;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class IntroActivity extends AppCompatActivity {

    static ImageView singleButton, doubleButton;
    static Boolean pBlack, pPass;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent = new Intent(IntroActivity.this, aboutActivity.class);
                startActivity(myintent);
            }
        });

        singleButton = (ImageView) findViewById(R.id.playSingleButton);
        doubleButton = (ImageView) findViewById(R.id.playDoubleButton);
        pBlack = false;
        pPass = false;
        PlayActivity.engineStrength=3;

    } // End on create.

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent myintent = new Intent(IntroActivity.this, SettingsActivity.class);
            startActivity(myintent);
            return true;
        }
        else if(id == R.id.action_logout){
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void playSingle (View singleButton) {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("Black or White?")
                .setMessage(
                        "Please choose to play as black or white.")
                .setPositiveButton("Black", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // Testing only //
                        Log.i("WJH", "Clicked Black.");

                        //what to do.
                        pBlack = true;
                        pPass = false;

                        Intent myintent = new Intent(IntroActivity.this, PlayActivity.class);
                        startActivity(myintent);

                    }
                })
                .setNegativeButton("White", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Log.i("WJH", "Clicked White.");

                        pBlack = false;
                        pPass = false;

                        Intent myintent = new Intent(IntroActivity.this, PlayActivity.class);
                        startActivity(myintent);

                    }
                })
                .show(); // Make sure you show your popup or it wont work very well!
    } // End playSingle

    public void playDouble (View doubleButton) {

        pBlack = false;
        pPass = true;
        Intent myintent = new Intent(IntroActivity.this, RoomActivity.class);
        startActivity(myintent);
    } // End playDouble

    private void logout(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(IntroActivity.this, SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}
