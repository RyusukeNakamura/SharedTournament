package com.lifeistech.android.sharedtournament;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
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

public class RoundDialogFragment extends DialogFragment {
    TextView upPlayer, downPlayer;
    EditText upScore, downScore, memo;
    int id, idU, idD;
    int round = 0;
    String userId, upP, downP, editable;
    String title;
    DatabaseReference refP;
    DatabaseReference refRound;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    Button ok,cancel;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        //カスタムダイアログのViewを取得
        View layout = LayoutInflater.from(getActivity())
                .inflate(R.layout.round_fragment, null);
        //紐づけ
        upPlayer = (TextView) layout.findViewById(R.id.upPlayer);
        downPlayer = (TextView) layout.findViewById(R.id.downPlayer);
        upScore = (EditText) layout.findViewById(R.id.upScore);
        downScore = (EditText) layout.findViewById(R.id.downScore);
        memo = (EditText) layout.findViewById(R.id.memo);
        ok=(Button)layout.findViewById(R.id.ok);
        cancel=(Button)layout.findViewById(R.id.cancel);

        id = getArguments().getInt("id");
        userId = getArguments().getString("userId");
        editable = getArguments().getString("editable");
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                positive();
                dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dismiss();
            }
        });


        //書き込み権限
        if (editable.equals("編集可能")) {
            upScore.setEnabled(true);
            downScore.setEnabled(true);
            memo.setEnabled(true);
            Log.d("auth", "writable");
        } else {
            upScore.setEnabled(false);
            downScore.setEnabled(false);
            memo.setEnabled(false);
            Log.d("auth", "disabled");
        }


        //EditTextで前回入力した値を取得
        getPrevious(1);
        getPrevious(2);
        getPrevious(3);

        //ダイアログを生成
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        Dialog dialog = new Dialog(getActivity());//super.OnCreateDialog(savedInstanceState)
        dialog.show();
        dialog.setTitle(title);
        dialog.getWindow().setLayout(width, 1000);
        dialog.setContentView(layout);

        return dialog;
        // AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.MyAlertDialogStyle);

        //ダイアログの設定---------------------------------------------------------------------------




                /*.setView(layout)
                .create();*/
    }
    public void positive (){
        try {
            Log.d("Click","positive");
            //2スコア
            int uScore = Integer.parseInt(upScore.getText().toString());
            int dScore = Integer.parseInt(downScore.getText().toString());

            //2選手名とメモの取得
            String uu = upPlayer.getText().toString();
            String dd = downPlayer.getText().toString();
            String mm = memo.getText().toString();

            Log.d("beforeMap","ok");
            //一回戦, ２選手のスコアを格納
            Map<String, Object> uSender = new HashMap<>();
            Map<String, Object> dSender = new HashMap<>();
            uSender.put("r" + round + "point", uScore);
            dSender.put("r" + round + "point", dScore);
            refRound = database.getReference(userId + "/Round/Round" + round + ":" + id);

            //RoundResultに結果を格納
            if (uScore > dScore) {
                Snackbar.make(MainActivity.layout, uu + " won", Snackbar.LENGTH_LONG).show();
                //勝った回数＝round数
                uSender.put("winPoint", round);
                refP.child("player" + idU).updateChildren(uSender);
                //負けたらround-1
                dSender.put("winPoint", round - 1);
                refP.child("player" + idD).updateChildren(dSender);
                RoundResult R = new RoundResult(uScore, dScore, uu, dd, mm);
                refRound.setValue(R);
            } else if (uScore < dScore) {
                Snackbar.make(MainActivity.layout, dd + " won", Snackbar.LENGTH_LONG).show();

                //負けround-1
                uSender.put("winPoint", round - 1);
                refP.child("player" + idU).updateChildren(uSender);
                //勝った回数＝round数
                dSender.put("winPoint", round);
                refP.child("player" + idD).updateChildren(dSender);

                RoundResult R = new RoundResult(uScore, dScore, dd, uu, mm);
                refRound.setValue(R);

            } else {
                Snackbar.make(MainActivity.layout, "勝敗を決めてください", Snackbar.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(getActivity(), "スコアを正しく入力してください", Toast.LENGTH_SHORT).show();
        }
    }
    public void onClick(DialogInterface dialog, int whichButton) {
    }

    public void onCancel(DialogInterface dialog) {
    }


    public void getPrevious(final int r) {
        refP = database.getReference(userId + "/Status");
        refRound = database.getReference(userId + "/Round");

        if (getArguments().getString((r - 1) + "rUpWinner") != null) {
            upP = getArguments().getString((r - 1) + "rUpWinner");
            downP = getArguments().getString((r - 1) + "rDownWinner");
            upPlayer.setText(upP);
            downPlayer.setText(downP);

            if (upP.indexOf("BYE") != -1) {
                upScore.setEnabled(false);
            }
            if (downP.indexOf("BYE") != -1) {
                downScore.setEnabled(false);
            }

            idU = getArguments().getInt(r + "rUpImage");
            idD = getArguments().getInt(r + "rDownImage");

            //playerStatus
            refP.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (Integer.parseInt(dataSnapshot.child("player" + idU).child("r" + r + "point").getValue().toString()) != 0) {
                        upScore.setText(dataSnapshot.child("player" + idU).child("r" + r + "point").getValue().toString());
                    }
                    if (Integer.parseInt(dataSnapshot.child("player" + idD).child("r" + r + "point").getValue().toString()) != 0) {
                        downScore.setText(dataSnapshot.child("player" + idD).child("r" + r + "point").getValue().toString());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            //RoundResultが変化したときの取得
            refRound.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("Round" + r + ":" + id).child("memo").getValue().toString() != null) {
                        memo.setText(dataSnapshot.child("Round" + r + ":" + id).child("memo").getValue().toString());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            round = r;
            title = r + "回戦";
        }
    }
}
