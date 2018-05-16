package com.lifeistech.android.tournament;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class StartActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

    }
    public void newA(View v){
        Intent intent=new Intent(this,Set1Activity.class);
        startActivity(intent);
    }
    public void open(View v){
        Intent intent=new Intent(this,Login_Activity.class);
        startActivity(intent);

    }
}
