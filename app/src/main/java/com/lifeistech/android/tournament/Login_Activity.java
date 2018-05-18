package com.lifeistech.android.tournament;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
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
    EditText gameId;
    String gName, gId;
    String[] a;

    MyProgressFragment dialog;
     Handler handler;
    Thread t;


    Intent intent;

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);

        gameId = (EditText) findViewById(R.id.gameId);

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg){
                switch (msg.what){
                    case 1:
                        dialog.setProgress(dialog.getProgress()+1);
                        break;
                    case 0:
                        dialog.dismiss();
                        break;
                }
            }
        };



    }

    public void login(View view) {
        a = new String[8];
        gId = gameId.getText().toString();

        dialog=new MyProgressFragment();
        dialog.show(getFragmentManager(),"progress");

        t=new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<100;i++){
                    handler.sendEmptyMessage(1);
                    try{
                        Thread.sleep(50);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
                handler.sendEmptyMessage(0);
            }
        });
        t.start();



            DatabaseReference ref = database.getReference(gId);


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
                        finish();
                    Log.d("gameName",gName);
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
    @Override
    protected void onPause(){
        super.onPause();
        if(dialog!=null){
            dialog.dismiss();
        }
        t=null;
    }

}
