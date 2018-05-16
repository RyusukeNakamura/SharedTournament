package com.lifeistech.android.tournament;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login_Activity extends AppCompatActivity {
    EditText gameId, operatorId;
    String gName, gId;
    String[] a;
    int flag = 0;


    Intent intent;

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);

        gameId = (EditText) findViewById(R.id.gameId);
        operatorId = (EditText) findViewById(R.id.operatorId);

    }

    public void login(View view) {
        a = new String[8];
        gId = gameId.getText().toString();



            DatabaseReference ref = database.getReference(gId);
            Log.d("ref", "OK");


            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                    for (int i = 0; i < a.length; i++) {
                        a[i] = dataSnapshot.child("Status/player" + i + "/name").getValue().toString();
                        Log.d("player" + i, a[i]);
                    }
                    gName=dataSnapshot.child("gameName").getValue().toString();

                        intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("createdId", gId);
                        intent.putExtra("gameName", gName);
                        intent.putExtra("players", a);
                        startActivity(intent);
                    Log.d("gameName",gName);
                    flag = 1;
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "そのIDの試合は存在しません", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            Log.d("before", "intent");




    }

}
