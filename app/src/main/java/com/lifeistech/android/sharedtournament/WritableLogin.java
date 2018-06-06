package com.lifeistech.android.sharedtournament;

import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class WritableLogin extends DialogFragment {
    TextView gText;
    EditText writeLogPassword;
    String gId, wlp;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final View layout = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_write_login, null);

        writeLogPassword = (EditText) layout.findViewById(R.id.writeLogPassword);
        gText = (TextView) layout.findViewById(R.id.gText);
        gId = getArguments().getString("gameId");
        gText.setText(gId);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference reference = database.getReference(gId);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        return builder.setTitle("結果編集ログイン")
                .setMessage("パスワードを入力して下さい")
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        wlp = writeLogPassword.getText().toString();
                        final Map<String, String> map = new HashMap<>();

                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {


                                if (wlp.equals(dataSnapshot.child("writePassword").getValue().toString())) {
                                    MainActivity.auth.setText("編集可能");
                                    MainActivity.logOnOff=1;

                                } else {
                                    Log.d("login", "failed");

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }
                })
                .setView(layout)
                .create();
    }
}
