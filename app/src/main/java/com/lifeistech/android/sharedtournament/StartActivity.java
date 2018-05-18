package com.lifeistech.android.sharedtournament;

import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.FirebaseDatabase;

public class StartActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Log.d("startActivity", "start");

    }

    public void newA(View v) {
        SharedPreferences pref = getSharedPreferences("disposeValue", MODE_PRIVATE);
        String oldId = pref.getString("dispose", null);
        if(oldId!=null) {
            DialogFragment dialog = new DeleteConfirmationFragment();
            Bundle args=new Bundle();
            args.putString("oldId", oldId);
            dialog.setArguments(args);
            dialog.show(getFragmentManager(), "deleteOld");
            //一度削除したらoldId削除すべきだが，頭が回らない．
            Log.d("oldOne", oldId);
        }else{
            Intent intent = new Intent(this, Set1Activity.class);
            startActivity(intent);
        }


    }

    public void open(View v) {
        Intent intent = new Intent(this, Login_Activity.class);
        startActivity(intent);

    }
}
