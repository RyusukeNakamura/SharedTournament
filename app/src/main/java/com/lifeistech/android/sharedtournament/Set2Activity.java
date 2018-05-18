package com.lifeistech.android.sharedtournament;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Set2Activity extends AppCompatActivity {
    String className, sGameName,writePassword;
    int nullCount = 0;

    Intent intent;
    ListView listView;
    TextView confirmI, gameN,participantNumber;
    EditText[] editName;

    String[] strings;

    int n;


    FirebaseDatabase database = FirebaseDatabase.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set2);


        intent = getIntent();

        className = intent.getStringExtra("className");
        sGameName = intent.getStringExtra("gameName");

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setSubtitle(className);
        actionBar.setTitle(sGameName);

        writePassword=intent.getStringExtra("writePassword");

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

        int nn = 0;
        for (int i = 0; i < editName.length; i++) {
            if (editName[i].getText().toString().length() == 0) {
                nn++;
                Log.d("nn", "nn=" + nn);
            }
        }

        if (nn > editName.length -4) {
            Toast.makeText(getApplicationContext(), "選手名を4人以上入力して下さい", Toast.LENGTH_SHORT).show();
        } else {


            DatabaseReference reference = database.getReference(className);

            int bye = 1;

            //未記入ならばbyeを挿入する．最低4人はBYEではいけない .
            for (int i = 0; i < editName.length; i++) {
                if (editName[i].getText().toString().length() == 0) {
                    nullCount++;
                    if (nullCount % 2 == 1) {
                        strings[bye] = "BYE" + (2 * bye - 1);
                        Log.d("strings", bye + "," + (2 * bye - 1));
                    } else {
                        strings[strings.length - 1 - bye] = "BYE" + 2 * bye;
                        Log.d("strings", strings.length - 1 - bye + "," + 2 * bye);
                        bye++;
                    }

                }

            }
            Log.d("afterFor", "aaaaaaaaaaaa");

            n = 0;
            while (n != editName.length - nn) {

                //editName[]の文字列をランダムにstrings[]に格納
                Random r = new Random();
                int value = r.nextInt(editName.length);

                //値がなければ格納する．
                if (strings[value] == null) {
                    if (editName[n].getText().toString().length() != 0) {
                        strings[value] = editName[n].getText().toString();
                        System.out.println(editName[value].getText().toString() + n);
                        n++;
                        Log.d("value,n", value + "," + n);
                    }
                }
            }

            //試合名をfirebaseにあげる
            Map<String, String> map = new HashMap<String, String>();
            map.put("gameName", sGameName);
            map.put("writePassword",writePassword);
            reference.setValue(map);

            //status, RoundResultを初期化
            for (int i = 0; i < editName.length; i++) {
                int r1 = 0;
                if (strings[i].indexOf("BYE") != -1) {
                    r1 = -1;
                    Log.d("string" + i, "r1=" + r1);
                }
                Log.d("string" + i, "r1=" + r1);
                Log.d("string" + i, strings[i]);


                reference.child("Status").child("player" + i).setValue(new PlayersStatus(strings[i],0, r1, 0, 0));

                Log.d("string" + i, "eeeeeeeee" + r1);

                if (i % 2 == 0) {
                    reference.child("Round/Round1:" + i / 2).setValue(new RoundResult(0, 0, "", "", ""));
                }
                if (i % 4 == 0) {
                    reference.child("Round/Round2:" + i / 4).setValue(new RoundResult(0, 0, "", "", ""));
                }
                if (i % 8 == 0) {
                    reference.child("Round/Round3:" + i / 8).setValue(new RoundResult(0, 0, "", "", ""));
                }

            }
            //ユーザの端末にid名を保存
            SharedPreferences pref = getSharedPreferences("disposeValue", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("dispose", className);
            editor.commit();

            intent = new Intent(this, MainActivity.class);
            intent.putExtra("createdId", className);
            intent.putExtra("gameName", sGameName);
            intent.putExtra("players", strings);
            startActivity(intent);
            finish();
        }
    }
}


