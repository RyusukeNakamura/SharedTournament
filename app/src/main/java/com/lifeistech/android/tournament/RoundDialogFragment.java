package com.lifeistech.android.tournament;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
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

public class RoundDialogFragment extends DialogFragment {
    TextView upPlayer, downPlayer;
    EditText upScore, downScore, memo;
    int id, idU, idD;
    int round = 0;
    String userId, upP, downP,editable;
    String title;
    DatabaseReference refP;
    DatabaseReference refRound;


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

        id = getArguments().getInt("id");
        Log.d("getArguments", "id=" + id);


        System.out.println("r1:" + getArguments().getString("upPlayer") + getArguments().getString("downPlayer"));
        System.out.println("r2:" + getArguments().getString("downR1winner") + getArguments().getString("upR1winner"));
        System.out.println("r3:" + getArguments().getString("downR2winner") + getArguments().getString("upR2winner"));


        userId = getArguments().getString("userId");
        Log.d("userId", userId);
        editable=getArguments().getString("editable");

        //EditTextで前回入力した値を取得

        //書き込み権限

        if(editable.equals("write/read")){
            upScore.setEnabled(true);
            downScore.setEnabled(true);
            memo.setEnabled(true);
            Log.d("auth","writable");
        }else{
            upScore.setEnabled(false);
            downScore.setEnabled(false);
            memo.setEnabled(false);
            Log.d("auth","disabled");

        }

        refP = database.getReference(userId + "/Status");
        refRound = database.getReference(userId + "/Round");

        Log.d("afterRefP,refRound", userId);

        //１回戦のダイアログ---------------------------------------------------------------------------
        if (getArguments().getString("upPlayer") != null) {
            upP = getArguments().getString("upPlayer");
            downP = getArguments().getString("downPlayer");
            upPlayer.setText(upP);
            downPlayer.setText(downP);


            idU = 2 * id;
            idD = 2 * id + 1;

            Log.d("1R idU,idD", idU + "," + idD);


            //playerStatus
            refP.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (Integer.parseInt(dataSnapshot.child("player" + idU).child("r1point").getValue().toString()) != 0) {
                        upScore.setText(dataSnapshot.child("player" + idU).child("r1point").getValue().toString());
                    }
                    if (Integer.parseInt(dataSnapshot.child("player" + idD).child("r1point").getValue().toString()) != 0) {
                        downScore.setText(dataSnapshot.child("player" + idD).child("r1point").getValue().toString());
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
                    //   Log.d("memo", dataSnapshot.child("Round1:" + id).child("memo").getValue().toString() + "1Rmemo");
                    if (dataSnapshot.child("Round1:" + id).child("memo").getValue().toString() != null) {
                        memo.setText(dataSnapshot.child("Round1:" + id).child("memo").getValue().toString());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            round = 1;
            title = "１回戦";

        } else if (getArguments().getString("upR1winner") != null) {

            //2回戦のダイアログ----------------------------------------------------------------------

            upP = getArguments().getString("upR1winner");
            downP = getArguments().getString("downR1winner");
            upPlayer.setText(upP);
            downPlayer.setText(downP);

            idU = getArguments().getInt("imageR2up");
            idD = getArguments().getInt("imageR2down");

            Log.d("2R idU,idD", idU + "," + idD);


            //playerStatus
            refP.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (Integer.parseInt(dataSnapshot.child("player" + idU).child("r2point").getValue().toString()) != 0) {
                        upScore.setText(dataSnapshot.child("player" + idU).child("r2point").getValue().toString());
                    }
                    if (Integer.parseInt(dataSnapshot.child("player" + idU).child("r2point").getValue().toString()) != 0) {
                        downScore.setText(dataSnapshot.child("player" + idD).child("r2point").getValue().toString());
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
                    //  Log.d("memo", dataSnapshot.child("Round2:" + id).child("memo").getValue().toString() + "2Rmemo");
                    if (dataSnapshot.child("Round2:" + id).child("memo").getValue().toString() != null) {
                        memo.setText(dataSnapshot.child("Round2:" + id).child("memo").getValue().toString());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            round = 2;
            title = "準決勝";
        } else if (getArguments().getString("upR2winner") != null) {

            Log.d("決勝", "dialog");

            upP = getArguments().getString("upR2winner");
            downP = getArguments().getString("downR2winner");
            upPlayer.setText(upP);
            downPlayer.setText(downP);

            idU = getArguments().getInt("imageR3up");
            idD = getArguments().getInt("imageR3down");
            Log.d("3R idU,idD", idU + "," + idD);


            //playerStatus
            refP.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (Integer.parseInt(dataSnapshot.child("player" + idU).child("r3point").getValue().toString()) != 0) {
                        upScore.setText(dataSnapshot.child("player" + idU).child("r3point").getValue().toString());
                    }
                    if (Integer.parseInt(dataSnapshot.child("player" + idU).child("r3point").getValue().toString()) != 0) {
                        downScore.setText(dataSnapshot.child("player" + idD).child("r3point").getValue().toString());
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
                    Log.d("memo", dataSnapshot.child("Round3:0").child("memo").getValue().toString() + "3Rmemo");
                    if (dataSnapshot.child("Round3:0").child("memo").getValue().toString() != null) {
                        memo.setText(dataSnapshot.child("Round3:0").child("memo").getValue().toString());

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            round = 3;
            title = "決勝戦";
        }
        System.out.println("ID=" + id);


        //ダイアログを生成
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        System.out.println("Dialog created.");


        //ダイアログの設定---------------------------------------------------------------------------
        return builder.setTitle(title)
                .setIcon(R.drawable.ic_launcher_background)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("Clicked", "OK");
                        try {
                            //2スコア
                            int uScore = Integer.parseInt(upScore.getText().toString());
                            int dScore = Integer.parseInt(downScore.getText().toString());
                            System.out.println("uScore=" + uScore + "\ndScore=" + dScore);


                            //2選手名とメモの取得
                            String uu = upPlayer.getText().toString();
                            String dd = downPlayer.getText().toString();
                            String mm = memo.getText().toString();

                            Log.d("uu+dd+memo", uu + dd + mm);

                            //一回戦, ２選手のスコアを格納

                            Map<String, Object> uSender = new HashMap<>();
                            Map<String, Object> dSender = new HashMap<>();


                            if (round == 1) {

                                uSender.put("r1point", uScore);
                                uSender.put("r2point", 0);
                                uSender.put("r3point", 0);

                                dSender.put("r1point", dScore);
                                dSender.put("r2point", 0);
                                dSender.put("r3point", 0);

                                refRound = database.getReference(userId + "/Round/Round1:" + id);
                                Log.d("refRound 1R", "id=" + id);
                            }
                            //2回戦. ２選手のスコアを格納child
                            else if (round == 2) {
                                System.out.println(uu + "---" + dd);


                                uSender.put("r2point", uScore);
                                uSender.put("r3point", 0);

                                dSender.put("r2point", dScore);
                                dSender.put("r3point", 0);

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                refRound = database.getReference(userId + "/Round/Round2:" + id);
                            } else {
                                System.out.println(uu + "---" + dd);

                                uSender.put("r3point", uScore);
                                dSender.put("r3point", dScore);

                                refRound = database.getReference(userId + "/Round/Round3:0");
                            }


                            //RoundResultに結果を格納
                            if (uScore > dScore) {
                                Snackbar.make(MainActivity.layout, uu + " won", Snackbar.LENGTH_LONG).show();

                                //勝った回数＝round数
                                uSender.put("winPoint",round);
                                refP.child("player" + idU).updateChildren(uSender);
                                //負けたらround-1
                                dSender.put("winPoint",round-1);
                                refP.child("player"+idD).updateChildren(dSender);


                                RoundResult R = new RoundResult(uScore, dScore, uu, dd, mm);
                                refRound.setValue(R);


                            } else if (uScore < dScore) {
                                Snackbar.make(MainActivity.layout, dd + " won", Snackbar.LENGTH_LONG).show();

                                //負けround-1
                                uSender.put("winPoint",round-1);
                                refP.child("player" + idU).updateChildren(uSender);
                                //勝った回数＝round数
                                dSender.put("winPoint",round);
                                refP.child("player"+idD).updateChildren(dSender);

                                RoundResult R = new RoundResult(uScore, dScore, dd, uu, mm);
                                refRound.setValue(R);

                            } else {
                                Snackbar.make(MainActivity.layout, "勝敗を決めてください", Snackbar.LENGTH_LONG).show();
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
