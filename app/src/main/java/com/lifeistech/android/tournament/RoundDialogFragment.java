package com.lifeistech.android.tournament;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
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

import org.w3c.dom.Text;

public class RoundDialogFragment extends DialogFragment {


    //firebase 宣言
    FirebaseDatabase database = FirebaseDatabase.getInstance();


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        //カスタムダイアログのViewを取得
        View layout = LayoutInflater.from(getActivity())
                .inflate(R.layout.round_fragment, null);
        final TextView upPlayer = (TextView) layout.findViewById(R.id.upPlayer);
        final TextView downPlayer = (TextView) layout.findViewById(R.id.downPlayer);
        upPlayer.setText(getArguments().getString("upPlayer"));
        downPlayer.setText(getArguments().getString("downPlayer"));

        final EditText upScore = (EditText) layout.findViewById(R.id.upScore);
        final EditText downScore = (EditText) layout.findViewById(R.id.downScore);

        int id = getArguments().getInt("id");
        DatabaseReference refMsg = database.getReference("result" + id);

        database.getReference("result" + id);
        refMsg.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //ダイアログを生成
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String text = getArguments().getString("player");

        //ダイアログの設定
        return builder.setTitle(text)
                .setIcon(R.drawable.ic_launcher_background)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("Clicked", "OK");
                        try {
                            //2スコア
                            int uScore = Integer.parseInt(upScore.getText().toString());
                            int dScore = Integer.parseInt(downScore.getText().toString());
                            //クリックしたブロックid
                            int id = getArguments().getInt("id");

                            //2選手名
                            String uu=upPlayer.getText().toString();
                            String dd=downPlayer.getText().toString();

                            DatabaseReference refMsg = database.getReference("result" + 2 * id);
                            DatabaseReference refMsg2 = database.getReference("result" + (2 * id + 1));
                            DatabaseReference refRound=database.getReference("Round1");

                            //2選手のスコアを格納
                            PlayersStatus psu = new PlayersStatus(uu, uScore, 0, 0);
                            refMsg.setValue(psu);
                            PlayersStatus psd = new PlayersStatus(dd, dScore, 0, 0);
                            refMsg2.setValue(psd);

                            //試合を記録
                            if (uScore > dScore) {
                                Toast.makeText(getActivity(), uu + " won", Toast.LENGTH_LONG).show();
                                RoundResult R=new RoundResult(uScore,dScore,uu,dd);
                                refRound.setValue(R);


                            } else if (uScore < dScore) {
                                Toast.makeText(getActivity(), dd + " won", Toast.LENGTH_LONG).show();
                                RoundResult R=new RoundResult(uScore,dScore,dd,uu);
                                refRound.setValue(R);

                            } else {
                                Toast.makeText(getActivity(), "勝敗を決めてください", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            Log.d("error", "occured");
                        }
                    }
                })
                .setView(layout)
                .create();

    }
}
