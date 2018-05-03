package com.lifeistech.android.tournament;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class RoundDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //カスタムダイアログのViewを取得
        View layout= LayoutInflater.from(getActivity())
                .inflate(R.layout.round_fragment,null);
        final TextView upPlayer=(TextView)layout.findViewById(R.id.upPlayer);
        final TextView downPlayer=(TextView)layout.findViewById(R.id.downPlayer);
        upPlayer.setText(getArguments().getString("upPlayer"));
        downPlayer.setText(getArguments().getString("downPlayer"));

        final EditText upScore=(EditText)layout.findViewById(R.id.upScore);
        final EditText downScore=(EditText)layout.findViewById(R.id.downScore);



        //ダイアログを生成
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String text=getArguments().getString("player");

        //ダイアログの設定
        return builder.setTitle(text)
                .setIcon(R.drawable.ic_launcher_background)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int uScore= Integer.parseInt(upScore.getText().toString());
                        int dScore=Integer.parseInt(downScore.getText().toString());
                        if(uScore>dScore){
                            Toast.makeText(getActivity(),upPlayer.getText().toString()+" win", Toast.LENGTH_LONG).show();
                        }else if(uScore<dScore){
                            Toast.makeText(getActivity(),downPlayer.getText().toString()+" win", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getActivity(),"draw", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setView(layout)
                .create();

    }
}
