package com.lifeistech.android.tournament;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MyDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //カスタムダイアログのViewを取得
        View layout= LayoutInflater.from(getActivity())
                .inflate(R.layout.player_fragment,null);

        //ダイアログを生成
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String text=getArguments().getString("player");

        //ダイアログの設定
        return builder.setTitle(text)
                .setIcon(R.drawable.ic_launcher_background)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Snackbar.make(new MainActivity().layout,"completed", Toast.LENGTH_LONG).show();
                    }
                })
                .setView(layout)
                .create();
    }
}
