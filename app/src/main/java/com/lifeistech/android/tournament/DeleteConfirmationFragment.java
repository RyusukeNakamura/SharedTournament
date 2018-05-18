package com.lifeistech.android.tournament;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DeleteConfirmationFragment extends DialogFragment {
    String old;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        old = getArguments().getString("oldId");
        Log.d(old,old);

        return builder.setTitle(old)
                .setMessage("上記のデータが消去されますがよろしいですか")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        database.getReference(old).removeValue();
                        Toast.makeText(getActivity(), "削除しました", Toast.LENGTH_LONG).show();
                        Log.d("before", "intent");
                        Intent intent = new Intent(getActivity(), Set1Activity.class);
                        startActivity(intent);
                        Log.d("after", "intent");
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create();
    }
}
