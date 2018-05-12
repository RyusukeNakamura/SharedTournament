package com.lifeistech.android.tournament;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Set2Activity extends AppCompatActivity {
    String className;
    int nullCount = 0;

    Intent intent;
    ListView listView;
    TextView confirmI, gameN;
    EditText[] editName;

    String[] strings;

    int n;


    FirebaseDatabase database = FirebaseDatabase.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set2);


        intent = getIntent();

        confirmI = (TextView) findViewById(R.id.confirmI);
        gameN = (TextView) findViewById(R.id.gameN);
        className = intent.getStringExtra("className");
        confirmI.setText(className);
        gameN.setText(intent.getStringExtra("gameName"));

        editName = new EditText[8];
        editName[0] = (EditText) findViewById(R.id.editName0);
        editName[1] = (EditText) findViewById(R.id.editName1);
        editName[2] = (EditText) findViewById(R.id.editName2);
        editName[3] = (EditText) findViewById(R.id.editName3);
        editName[4] = (EditText) findViewById(R.id.editName4);
        editName[5] = (EditText) findViewById(R.id.editName5);
        editName[6] = (EditText) findViewById(R.id.editName6);
        editName[7] = (EditText) findViewById(R.id.editName7);

        strings = new String[editName.length];


    }

    public void createTournament(View v) {
        DatabaseReference reference = database.getReference(className);

        while (n != editName.length) {

            //editName[]の文字列をランダムにstrings[]に格納
            Random r = new Random();
            int value = r.nextInt(editName.length);

            if (strings[value] == null) {
                if (editName[n].getText().toString().length() != 0) {
                    strings[value] = editName[n].getText().toString();
                } else {
                    nullCount++;
                    Log.d("editText", "nullCount=" + nullCount);
                    strings[value] = "bye" + nullCount;
                }
                System.out.println(editName[value].toString() + n);
                n++;
            }
        }
        for (int i = 0; i < editName.length; i++) {
            reference.child("Status").child("player" + i).setValue(new PlayersStatus(strings[i], 0, 0, 0));

            if (i % 2 == 0) {
                reference.child("Round").child("Round1:" + i / 2).setValue(new RoundResult(0, 0, "", "", ""));
            }
            if (i % 4 == 0) {
                reference.child("Round").child("Round2:" + i / 4).setValue(new RoundResult(0, 0, "", "", ""));
            }
            if (i % 8 == 0) {
                reference.child("Round").child("Round3:" + i / 8).setValue(new RoundResult(0, 0, "", "", ""));
            }
//            Map<String,String> map=new HashMap<String,String>();
//            map.put("gameName",)
//            reference.setValue()
        }
        intent = new Intent(this, MainActivity.class);
        intent.putExtra("createdId", className);
        intent.putExtra("gameName", gameN.getText().toString());
        intent.putExtra("players",strings);
        startActivity(intent);
        finish();
    }
}


