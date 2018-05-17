package com.lifeistech.android.tournament;


import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class Set1Activity extends AppCompatActivity implements TextWatcher {
    EditText gameName, id, password;
    TextView confirmId, confirmPass;
    LinearLayout layout;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    int idFlag = 0;
    int idLength = 0;
    int pasFlag = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set1);

        gameName = (EditText) findViewById(R.id.gameName);
        id = (EditText) findViewById(R.id.id);
        password = (EditText) findViewById(R.id.password);
        confirmId = (TextView) findViewById(R.id.confirmId);
        confirmPass = (TextView) findViewById(R.id.confirmPass);


        id.addTextChangedListener(this);
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() <= 4) {
                    confirmPass.setTextColor(Color.RED);
                    confirmPass.setText("5文字以上入力して下さい");
                    pasFlag = 0;
                } else {
                    confirmPass.setTextColor(Color.GREEN);
                    confirmPass.setText("OK");
                    pasFlag = 1;
                }
            }
        });
        layout = (LinearLayout) findViewById(R.id.layout);

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        Log.d("\nafterEditable", s.toString());

        if (s.toString().length() <= 4) {
            confirmId.setTextColor(Color.RED);
            confirmId.setText("5文字以上入力して下さい");
            idLength = 0;
        }else{
            idLength=1;
        }

        DatabaseReference refNew = database.getReference(s.toString());
        refNew.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("onDataChange", "dataSnapshot");

                if (dataSnapshot.getValue() != null) {
                    confirmId.setTextColor(Color.RED);
                    confirmId.setText("そのIDはすでに存在します");
                    idFlag = 0;
                } else {
                    if (idLength != 0) {
                        confirmId.setTextColor(Color.GREEN);
                        confirmId.setText("OK");
                        idFlag = 1;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }


    public void newCreate(View v) {
        if (idFlag + pasFlag == 2) {

            Intent intent = new Intent(this, Set2Activity.class);
            intent.putExtra("className", id.getText().toString());
            intent.putExtra("gameName", gameName.getText().toString());
            intent.putExtra("writePassword", password.getText().toString());
            startActivity(intent);
            finish();
        } else {
            Log.d("iD", "入力してください");
            Toast.makeText(getApplicationContext(), "正しくIDを入力してください", Toast.LENGTH_LONG).show();
        }

    }
}
