package com.lifeistech.android.tournament;

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

public class Set1Activity extends AppCompatActivity {
    EditText gameName,password;
    FirebaseDatabase database=FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set1);
        gameName=(EditText)findViewById(R.id.gameName);
        password=(EditText)findViewById(R.id.password);

    }
    public void newCreate(View v){
        DatabaseReference refNew = database.getReference(password.getText().toString());
        refNew.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Toast.makeText(getApplicationContext(),"change",Toast.LENGTH_SHORT).show();

                if(dataSnapshot.getValue()!=null){
                    Toast.makeText(getApplicationContext(),"そのパスワードはすでに存在します",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"OK",Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
