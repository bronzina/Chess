package uni.bronzina.chess;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RoomActivity extends AppCompatActivity {

    ListView listView;
    Button button;

    List<String> roomsList;

    String playerName;
    String roomName;

    FirebaseDatabase database;
    DatabaseReference roomRef;
    DatabaseReference roomsRef;

    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        database = FirebaseDatabase.getInstance("https://chess-32d01-default-rtdb.europe-west1.firebasedatabase.app/");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            playerName = firebaseUser.getDisplayName();
            roomName = playerName;
        } else {
           playerName = "";
           roomName = "";
        }

        listView = findViewById(R.id.listView);
        button = findViewById(R.id.button);

        //All existing rooms
        roomsList = new ArrayList<>();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setText("CREATING ROOM");
                button.setEnabled(false);
                roomName = playerName;
                roomRef = database.getReference("rooms/" + roomName + "/player1");
                roomRef.onDisconnect().setValue("");
                addRoomEventListener();
                roomRef.setValue(playerName);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                roomName = roomsList.get(position);
                roomRef = database.getReference("rooms/" + roomName + "/player1");
                addUserEventListener();
                addRoomEventListener();
                //roomRef.setValue(playerName);
            }
        });

        addRoomsEventListener();
    }

    private void addUserEventListener(){
        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String user = snapshot.getValue(String.class);
                FirebaseUser player = FirebaseAuth.getInstance().getCurrentUser();
                if(user != player.getDisplayName()) {
                    DatabaseReference player2Ref = database.getReference("rooms/" + roomName + "/player2");
                    player2Ref.setValue(playerName);
                    player2Ref.onDisconnect().setValue("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //
            }
        });
    }

    private void addRoomEventListener(){
        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                button.setText("CREATE ROOM");
                button.setEnabled(true);
                Intent intent = new Intent(getApplicationContext(), MultiplayerActivity.class);
                intent.putExtra("roomName", roomName);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                button.setText("CREATE ROOM");
                button.setEnabled(true);
                Toast.makeText(RoomActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addRoomsEventListener(){
        roomsRef = database.getReference("rooms");
        roomsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                roomsList.clear();
                Iterable<DataSnapshot> rooms = dataSnapshot.getChildren();
                for(DataSnapshot snapshot : rooms){
                    roomsList.add(snapshot.getKey());

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(RoomActivity.this, android.R.layout.simple_list_item_1, roomsList);
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Error - nothing
            }
        });
    }
}