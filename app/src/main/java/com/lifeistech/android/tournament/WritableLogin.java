package com.lifeistech.android.tournament;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

        View layout = LayoutInflater.from(getActivity())
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
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        wlp = writeLogPassword.getText().toString();
                        final Map<String, String> map = new HashMap<>();

                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {



                                if (wlp.equals(dataSnapshot.child("writePassword").getValue().toString())) {
                                    MainActivity.auth.setText("write/read");
//                                    Toast.makeText(getActivity(),"編集可能になりました!",Toast.LENGTH_LONG);



                                    Log.d("login", "success");/*
                                    Intent result = new Intent();

                                    if (getTargetFragment() != null) {
                                        getTargetFragment().onActivityResult(getTargetRequestCode(), MainActivity.RESULT_OK, result);
                                        Log.d("!null", "よくわからぬ");

                                        Log.d("intent", "activityResultOK");
                                    } else {
                                        Log.d("null", "よくわからぬ");

                                        try {
                                            PendingIntent pi = getActivity().createPendingResult(getTargetRequestCode(), result, PendingIntent.FLAG_ONE_SHOT);
                                            pi.send(Activity.RESULT_CANCELED);
                                        } catch (PendingIntent.CanceledException ex) {
                                            ex.printStackTrace();
                                        }
                                    }*/

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
