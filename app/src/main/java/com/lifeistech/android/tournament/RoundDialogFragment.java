package com.lifeistech.android.tournament;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract;
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

import java.util.HashMap;
import java.util.Map;

public class RoundDialogFragment extends DialogFragment {
    TextView upPlayer, downPlayer, upR1Winner, downR1Winner;
    EditText upScore, downScore, memo;
    int id;
    int round = 0;


    //firebase 宣言
    FirebaseDatabase database = FirebaseDatabase.getInstance();


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


        System.out.println(getArguments().getString("upPlayer") + getArguments().getString("downPlayer"));
        System.out.println(getArguments().getString("downR1winner") + getArguments().getString("upR1winner"));


        //MainActivityから押されたブロックの階層

        //EditTextで前回入力した値を取得


        //１回戦のダイアログ
        if (getArguments().getString("upPlayer") != null) {
            upPlayer.setText(getArguments().getString("upPlayer"));
            downPlayer.setText(getArguments().getString("downPlayer"));
            id = getArguments().getInt("imageR1id");
            DatabaseReference refMsg = database.getReference("Status/player" + 2 * id);
            DatabaseReference refMsg2 = database.getReference("Status/player" + (2 * id + 1));
            DatabaseReference refRound= database.getReference("Round/Round1:" + id);

            //偶数番目のplayerStatus
            refMsg.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    upScore.setText(dataSnapshot.child("r1point").getValue().toString());
                    System.out.println(dataSnapshot.child("r1point").getValue().toString());

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            //奇数番目のplayerStatus
            refMsg2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    downScore.setText(dataSnapshot.child("r1point").getValue().toString());
                    System.out.println(dataSnapshot.child("r1point").getValue().toString());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            //RoundResultが変化したときの取得
            refRound.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("memo").getValue().toString() != null) {
                        memo.setText(dataSnapshot.child("memo").getValue().toString());
                        System.out.println(dataSnapshot.child("memo").getValue().toString());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            round = 1;

        } else {

            //2回戦のダイアログ
            upPlayer.setText(getArguments().getString("upR1winner"));
            downPlayer.setText(getArguments().getString("downR1winner"));
            id = getArguments().getInt("imageR2id");
            DatabaseReference refRound2 = database.getReference("Round/Round2:" + id);


            round = 2;
        }
        System.out.println("id=" + id);






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
                            int id1 = getArguments().getInt("imageR1id");
                            int id2 = getArguments().getInt("imageR2id");
                            System.out.println("idhhhhhhhhhhhhh" + id + "id2=" + id2);

                            //2選手名とメモの取得
                            String uu = upPlayer.getText().toString();
                            String dd = downPlayer.getText().toString();
                            String mm = memo.getText().toString();

                            DatabaseReference refP = database.getReference("Status/player" + 2 * id1);
                            DatabaseReference refP2 = database.getReference("Status/player" + (2 * id1 + 1));
                            DatabaseReference refRound = database.getReference("Round/Round1:" + id1);


                            //一回戦突破者を取得する必要あり！！！！！！！！！！！
                            DatabaseReference refW1=database.getReference();



                            DatabaseReference ref2Round = database.getReference("Round/Round2:" + id2);

                            //2選手のスコアを格納

                            //一回戦
                            if (round == 1) {
                                PlayersStatus psu = new PlayersStatus(uu, uScore, 0, 0);
                                refP.setValue(psu);
                                PlayersStatus psd = new PlayersStatus(dd, dScore, 0, 0);
                                refP2.setValue(psd);
                                //RoundResultに結果を格納
                                if (uScore > dScore) {
                                    Toast.makeText(getActivity(), uu + " won", Toast.LENGTH_LONG).show();
                                    RoundResult R = new RoundResult(uScore, dScore, uu, dd, mm);
                                    refRound.setValue(R);


                                } else if (uScore < dScore) {
                                    Toast.makeText(getActivity(), dd + " won", Toast.LENGTH_LONG).show();
                                    RoundResult R = new RoundResult(uScore, dScore, dd, uu, mm);
                                    refRound.setValue(R);

                                } else {
                                    Toast.makeText(getActivity(), "勝敗を決めてください", Toast.LENGTH_LONG).show();
                                }
                            }
                            //２回戦.  修正する必要あり！！！！！！！！！！
                            else if (round == 2) {
                                Map<String, Object> sender = new HashMap<>();
                                sender.put("r2point", uScore);
                                ref.updateChildren(sender);

                                Map<String, Object> sender2 = new HashMap<>();
                                sender2.put("r2point", dScore);
                                ref2.updateChildren(sender2);

                                //RoundResultに結果を格納
                                if (uScore > dScore) {
                                    Toast.makeText(getActivity(), uu + " won", Toast.LENGTH_LONG).show();
                                    RoundResult R = new RoundResult(uScore, dScore, uu, dd, mm);
                                    ref2Round.setValue(R);


                                } else if (uScore < dScore) {
                                    Toast.makeText(getActivity(), dd + " won", Toast.LENGTH_LONG).show();
                                    RoundResult R = new RoundResult(uScore, dScore, dd, uu, mm);
                                    ref2Round.setValue(R);

                                } else {
                                    Toast.makeText(getActivity(), "勝敗を決めてください", Toast.LENGTH_LONG).show();
                                }
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
