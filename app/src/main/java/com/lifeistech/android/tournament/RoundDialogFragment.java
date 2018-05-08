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
    TextView upPlayer, downPlayer;
    EditText upScore, downScore, memo;
    int id;
    int round = 0;
    String upP;
    String downP;
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


        System.out.println("r1:" + getArguments().getString("upPlayer") + getArguments().getString("downPlayer"));
        System.out.println("r2:" + getArguments().getString("downR1winner") + getArguments().getString("upR1winner"));
        System.out.println("r3:" + getArguments().getString("downR2winner") + getArguments().getString("upR2winner"));

        //MainActivityから押されたブロックの階層

        //EditTextで前回入力した値を取得

        refP = database.getReference("Status");
        refRound = database.getReference("Round");



        //１回戦のダイアログ
        if (getArguments().getString("upPlayer") != null) {
            upP = getArguments().getString("upPlayer");
            downP = getArguments().getString("downPlayer");
            upPlayer.setText(upP);
            downPlayer.setText(downP);

            //playerStatus
            refP.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(Integer.parseInt(dataSnapshot.child(upP).child("r1point").getValue().toString())!=0) {
                        upScore.setText(dataSnapshot.child(upP).child("r1point").getValue().toString());
                        downScore.setText(dataSnapshot.child(downP).child("r1point").getValue().toString());
                        System.out.println("rrrrr1point=" + dataSnapshot.child(upP).child("r1point").getValue().toString());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            id = getArguments().getInt("imageR1id");

            //RoundResultが変化したときの取得
            refRound.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d("memo",dataSnapshot.child("Round1:"+id).child("memo").getValue().toString()+"aaa");
                    if (dataSnapshot.child("Round1:"+id).child("memo").getValue().toString() != null) {
                        memo.setText(dataSnapshot.child("Round1:"+id).child("memo").getValue().toString());
                        System.out.println("memo=" + dataSnapshot.child("Round1:"+id).child("memo").getValue().toString());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            round = 1;

        } else if (getArguments().getString("upR1winner") != null) {

            //2回戦のダイアログ

            upP = getArguments().getString("upR1winner");
            downP = getArguments().getString("downR1winner");
            upPlayer.setText(upP);
            downPlayer.setText(downP);

            //playerStatus
            refP.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(Integer.parseInt(dataSnapshot.child(upP).child("r2point").getValue().toString())!=0) {
                        upScore.setText(dataSnapshot.child(upP).child("r2point").getValue().toString());
                        downScore.setText(dataSnapshot.child(downP).child("r2point").getValue().toString());
                        System.out.println(upP + downP);
                        System.out.println("r2point=" + dataSnapshot.child(upP).child("r2point").getValue().toString());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            id = getArguments().getInt("imageR2id");

            //RoundResultが変化したときの取得
            refRound.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d("memo",dataSnapshot.child("Round2:"+id).child("memo").getValue().toString()+"2222");
                    if (dataSnapshot.child("Round2:"+id).child("memo").getValue().toString() != null) {
                        memo.setText(dataSnapshot.child("Round2:"+id).child("memo").getValue().toString());
                        System.out.println("memo=" + dataSnapshot.child("Round2:"+id).child("memo").getValue().toString());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            round = 2;
        }else{
            upP = getArguments().getString("upR2winner");
            downP = getArguments().getString("downR2winner");
            upPlayer.setText(upP);
            downPlayer.setText(downP);

            //playerStatus
            refP.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(Integer.parseInt(dataSnapshot.child(upP).child("r3point").getValue().toString())!=0) {
                        upScore.setText(dataSnapshot.child(upP).child("r3point").getValue().toString());
                        downScore.setText(dataSnapshot.child(downP).child("r3point").getValue().toString());
                        System.out.println("rrrrr3point=" + dataSnapshot.child(upP).child("r3point").getValue().toString());
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
                    Log.d("memo",dataSnapshot.child("Round3:").child("memo").getValue().toString()+"aaa");
                    if (dataSnapshot.child("Round3:").child("memo").getValue().toString() != null) {
                        memo.setText(dataSnapshot.child("Round3:").child("memo").getValue().toString());
                        System.out.println("memo=" + dataSnapshot.child("Round3:").child("memo").getValue().toString());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            round = 3;
        }
        System.out.println("ID=" + id);


        //ダイアログを生成
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        System.out.println("uS"+"\ndS");


        //ダイアログの設定
        return builder.setTitle("試合結果")
                .setIcon(R.drawable.ic_launcher_background)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("Clicked", "OK");
                        try {
                            //2スコア
                            int uScore = Integer.parseInt(upScore.getText().toString());
                            int dScore = Integer.parseInt(downScore.getText().toString());
                            System.out.println("uScore="+uScore+"\ndScore="+dScore);

                            //2選手名とメモの取得
                            String uu = upPlayer.getText().toString();
                            String dd = downPlayer.getText().toString();
                            String mm = memo.getText().toString();



                            //一回戦, ２選手のスコアを格納
                            if (round == 1) {
                                refRound = database.getReference("Round/Round1:" + id);
                                PlayersStatus psu = new PlayersStatus(uu, uScore, 0, 0);
                                refP.child(uu).setValue(psu);
                                PlayersStatus psd = new PlayersStatus(dd, dScore, 0, 0);
                                refP.child(dd).setValue(psd);
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
                            //2回戦. ２選手のスコアを格納child
                            else if (round == 2) {
                                System.out.println(uu + "---" + dd);

                                refRound = database.getReference("Round/Round2:");

                                Map<String, Object> sender = new HashMap<>();
                                sender.put("r3point", uScore);
                                refP.child(uu).updateChildren(sender);

                                Map<String, Object> sender2 = new HashMap<>();
                                sender2.put("r3point", dScore);
                                refP.child(dd).updateChildren(sender2);


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
                            }else{
                                System.out.println(uu + "---" + dd);

                                refRound = database.getReference("Round/Round3:");

                                Map<String, Object> sender = new HashMap<>();
                                sender.put("r3point", uScore);
                                refP.child(uu).updateChildren(sender);

                                Map<String, Object> sender2 = new HashMap<>();
                                sender2.put("r3point", dScore);
                                refP.child(dd).updateChildren(sender2);


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


                        } catch (Exception e) {
                            Log.d("error", "occured");
                        }
                    }
                })
                .setView(layout)
                .create();

    }
}
