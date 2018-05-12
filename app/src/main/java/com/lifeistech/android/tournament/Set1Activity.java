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

public  class Set1Activity extends AppCompatActivity implements TextWatcher {
    EditText gameName, id;
    TextView confirmId;
    LinearLayout layout;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    int flag=0;
    int idLength=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set1);

        gameName = (EditText) findViewById(R.id.gameName);
        id = (EditText) findViewById(R.id.id);
        id.addTextChangedListener(this);
        confirmId=(TextView)findViewById(R.id.confirmId);
        layout = (LinearLayout) findViewById(R.id.layout);

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after){

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        idLength=start;
    }

    @Override
    public void afterTextChanged(Editable s) {
        Log.d("\nafterEditable",s.toString());

        if (s.toString().length() != 0 ) {
            DatabaseReference refNew = database.getReference(s.toString());
            refNew.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d("onDataChange", "dataSnapshot");

                    if (dataSnapshot.getValue() != null) {
                        confirmId.setTextColor(Color.RED);
                        confirmId.setText("そのIDはすでに存在します");
                        flag=0;

                    } else {
                        if(idLength>=3) {
                            confirmId.setTextColor(Color.GREEN);
                            confirmId.setText("OK");
                            flag = 1;
                        }else{
                            confirmId.setTextColor(Color.RED);
                            confirmId.setText("4文字以上入力して下さい");
                            flag=0;
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

        } else {



            Toast.makeText(getApplication(), "IDを入力してください", Toast.LENGTH_LONG).show();

        }


    }


    public void newCreate(View v) {
        if(flag!=0) {
            Intent intent = new Intent(this, Set2Activity.class);
            intent.putExtra("className", id.getText().toString());
            intent.putExtra("gameName",gameName.getText().toString());
            startActivity(intent);
            finish();
        }else{
            Log.d("iD","入力してください");
            Toast.makeText(getApplicationContext(), "正しくIDを入力してください", Toast.LENGTH_LONG).show();
        }

    }
}
