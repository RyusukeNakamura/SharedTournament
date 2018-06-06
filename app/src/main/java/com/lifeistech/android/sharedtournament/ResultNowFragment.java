package com.lifeistech.android.sharedtournament;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultNowFragment extends DialogFragment {
    String[] s;
    FirebaseDatabase database = FirebaseDatabase.getInstance();


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Map<String, Integer> map = new HashMap<>();


        DatabaseReference reference = database.getReference(getArguments().getString("userId"));
        s = new String[8];
        s = getArguments().getStringArray("players");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        return builder.setTitle("現在のランキング")
                .setItems(s, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .create();

    }
}
