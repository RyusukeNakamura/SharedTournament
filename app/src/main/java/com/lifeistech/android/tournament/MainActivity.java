package com.lifeistech.android.tournament;

import android.app.DialogFragment;
import android.provider.ContactsContract;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

import static java.lang.Integer.parseInt;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();


    ImageView[] imageR1, imageR2;
    ImageView imageR3, firstWinner;
    GridLayout gridLayout;
    TextView[] textView;

    String[] players;
    String[] r1Winner, r2Winner;

    int[] upSide = {0, 0}, bottomSide = {0, 0};
    int finalist0 = -1, finalist1 = -1;
    int done1R = -1;
    int done2R = -1;
    int done3R = -1;

    final int WC = GridLayout.LayoutParams.WRAP_CONTENT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        players = new String[8];
        //ランダムに格納
        Random r = new Random();
        int n = 0;
        while (n != players.length) {
            int value = r.nextInt(players.length);
            if (players[value] == null) {
                players[value] = "player" + n;
                System.out.println(n + players[value]);
                n++;
            }
        }


        gridLayout = (GridLayout) findViewById(R.id.gridLayout);
        textView = new TextView[8];
        textView[0] = (TextView) findViewById(R.id.name1);
        textView[1] = (TextView) findViewById(R.id.name2);
        textView[2] = (TextView) findViewById(R.id.name3);
        textView[3] = (TextView) findViewById(R.id.name4);
        textView[4] = (TextView) findViewById(R.id.name5);
        textView[5] = (TextView) findViewById(R.id.name6);
        textView[6] = (TextView) findViewById(R.id.name7);
        textView[7] = (TextView) findViewById(R.id.name8);

        for (int i = 0; i < textView.length; i++) {
            textView[i].setText(players[i]);
        }

        imageR1 = new ImageView[4];
        imageR1[0] = (ImageView) findViewById(R.id.r1p12);
        imageR1[1] = (ImageView) findViewById(R.id.r1p34);
        imageR1[2] = (ImageView) findViewById(R.id.r1p56);
        imageR1[3] = (ImageView) findViewById(R.id.r1p78);

        imageR2 = new ImageView[2];
        imageR2[0] = (ImageView) findViewById(R.id.r2w12);
        imageR2[1] = (ImageView) findViewById(R.id.r2w34);

        imageR3 = (ImageView) findViewById(R.id.r3w12);
        firstWinner = (ImageView) findViewById(R.id.firstWinner);


        //dataBaseを宣言

        DatabaseReference refP = database.getReference("Status");
        DatabaseReference refRound = database.getReference("Round");


        //firebase のdatabaseを初期化
        for (int i = 0; i < players.length; i++) {
            refP.child("player" + i).setValue(new PlayersStatus("player" + i, 0, 0, 0));

            if (i % 2 == 0) {
                refRound.child("Round1:" + i / 2).setValue(new RoundResult(0, 0, "", "", ""));
            }
            if (i % 4 == 0) {
                refRound.child("Round2:" + i / 4).setValue(new RoundResult(0, 0, "", "", ""));
            }
            if (i % 8 == 0) {
                refRound.child("Round3:" + i / 8).setValue(new RoundResult(0, 0, "", "", ""));
            }

        }

        r1Winner = new String[4];
        r2Winner = new String[2];


        //改善する必要あり４


        try {
            //結果を読み込み

            //child何とかに帰る必要を今感じた．どうやるんだったっけ？？？？？？？？？？？？？？？
            refRound.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (int i = 0; i < imageR1.length; i++) {
                        //一回戦の結果取得
                        String w = String.valueOf(dataSnapshot.child("Round1:" + i).child("winner").getValue());
                        String l = (String) dataSnapshot.child("Round1:" + i).child("loser").getValue();
                        int upP = parseInt(dataSnapshot.child("Round1:" + i).child("up").getValue().toString());
                        int downP = parseInt(dataSnapshot.child("Round1:" + i).child("down").getValue().toString());
                        System.out.println("1Rwinner:" + w + "\n1Rloser:" + l);

                        //勝者を格納
                        if (upP > downP) {
                            imageR1[i].setBackgroundResource(R.drawable.ko_top_won);
                            // imageR1[j].setLayoutParams(new GridLayout.LayoutParams(WC,WC));
                            Log.d("up>down", "imageR1up");
                            r1Winner[i] = w;
                            done1R = i;
                        } else if (upP < downP) {
//                            imageR1[j].setImageResource(R.drawable.ko_bottom_won);
                            imageR1[i].setBackgroundResource(R.drawable.ko_bottom_won);
                            Log.d("up<down", "imageR1down");
                            r1Winner[i] = w;
                            done1R = i;
                        }
                        //imageR2の画像変更, 一回戦完了
                        if (done1R == i) {
                            if (i % 2 == 0) {
                                Log.d("imageR2", "changedTopDone" + i);
                                imageR2[i / 2].setBackgroundResource(R.drawable.ko_top_done);
                                upSide[i / 2] = 1;
                            } else {
                                Log.d("imageR2", "changedBottomDone" + i);

                                imageR2[i / 2].setBackgroundResource(R.drawable.ko_bottom_done);
                                bottomSide[i / 2] = 1;
                            }
                            if (bottomSide[i / 2] == 1 && upSide[i / 2] == 1) {
                                Log.d("一回戦", "両方完了！！！！");
                                imageR2[i / 2].setBackgroundResource(R.drawable.ko_both_done);

                            }
                        }
                    }

                    for (int j = 0; j < imageR2.length; j++) {
                        //２回戦の結果取得
                        String w2 = String.valueOf(dataSnapshot.child("Round2:" + j).child("winner").getValue());
                        String l2 = (String) dataSnapshot.child("Round2:" + j).child("loser").getValue();
                        int upP2 = parseInt(dataSnapshot.child("Round2:" + j).child("up").getValue().toString());
                        int downP2 = parseInt(dataSnapshot.child("Round2:" + j).child("down").getValue().toString());
                        System.out.println("2Rwinner:" + w2 + "\n2Rloser:" + l2 + "\nj=" + j);

                        //勝者を格納
                        if (upP2 > downP2) {
                            imageR2[j].setBackgroundResource(R.drawable.ko_top_won);
                            Log.d("up>down", "r2wwwwwwwwwwwwwWinner" + j + ":" + w2);
                            r2Winner[j] = w2;
                            done2R = j;
                        } else if (upP2 < downP2) {
                            imageR2[j].setBackgroundResource(R.drawable.ko_bottom_won);
                            Log.d("up<down", "r2Wwwwwwwwwwwwwwwwinner" + j + ":" + w2);
                            r2Winner[j] = w2;
                            done2R = j;
                        }
                        //imageR3の画像変更, ２回戦完了
                        if (done2R == j) {
                            if (j == 0) {
                                Log.d("imageR3", "changedTopDone");
                                imageR3.setBackgroundResource(R.drawable.ko_top_done);
                                finalist0 = 1;
                            } else {
                                Log.d("imageR3", "changedBottomDone" + j);
                                imageR3.setBackgroundResource(R.drawable.ko_bottom_done);
                                finalist1 = 1;
                            }
                            if (finalist0 == 1 && finalist1 == 1) {
                                imageR3.setBackgroundResource(R.drawable.ko_both_done);
                            }
                        }
                    }


                    String w3 = String.valueOf(dataSnapshot.child("Round3:0").child("winner").getValue());
                    String l3 = (String) dataSnapshot.child("Round3:0").child("loser").getValue();
                    int upP3 = parseInt(dataSnapshot.child("Round3:0").child("up").getValue().toString());
                    int downP3 = parseInt(dataSnapshot.child("Round3:0").child("down").getValue().toString());
                    System.out.println("3Rwinner:" + w3 + "\n3Rloser:" + l3);

                    //勝者を格納
                    if (upP3 > downP3) {
                        imageR3.setBackgroundResource(R.drawable.ko_top_won);
                        Log.d("up>down", "r3Winner0" + ":" + w3);
                        done3R = 0;
                    } else if (upP3 < downP3)

                    {
                        imageR3.setBackgroundResource(R.drawable.ko_bottom_won);
                        Log.d("up<down", "r3Winner0" + ":" + w3);
                        done3R = 0;
                    }
                    //imageR3の画像変更, ２回戦完了
                    if (done3R == 0) {
                        Log.d("imageR3", "firstWinner decided");
                        firstWinner.setBackgroundResource(R.drawable.red_line);
                    }
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            refP.addValueEventListener(new

                                               ValueEventListener() {
                                                   @Override
                                                   public void onDataChange(DataSnapshot dataSnapshot) {
//                        System.out.println("r1point=" + dataSnapshot.child(players[2 * j]).child("r1point").getValue());
//                        System.out.println("r2point=" + dataSnapshot.child(players[2 * j]).child("r2point").getValue());
//                        System.out.println("r3point=" + dataSnapshot.child(players[2 * j]).child("r3point").getValue());
//
//                        System.out.println("r1point=" + dataSnapshot.child(players[2 * j + 1]).child("r1point").getValue());
//                        System.out.println("r2point=" + dataSnapshot.child(players[2 * j + 1]).child("r2point").getValue());
//                        System.out.println("r3point=" + dataSnapshot.child(players[2 * j + 1]).child("r3point").getValue());
                                                   }

                                                   @Override
                                                   public void onCancelled(DatabaseError databaseError) {

                                                   }
                                               });


        } catch (Exception e) {
            Log.d("Status/", "playerはカラ");
        }

    }


    //一回戦
    public void round1(View v) {

        DialogFragment dialog = new RoundDialogFragment();
        Bundle args = new Bundle();

        for (int i = 0; i < imageR1.length; i++) {
            //クリックしたブロックを判別
            if (v == imageR1[i]) {
                args.putInt("imageR1id", i);
                args.putString("upPlayer", players[2 * i]);
                args.putString("downPlayer", players[2 * i + 1]);
                dialog.setArguments(args);
                dialog.show(getFragmentManager(), "round");
                break;
            }
            if (i % 2 == 0 && v == imageR2[i / 2]) {
                System.out.println("r1Winner=" + r1Winner[i]);
                if (r1Winner[i] != null && r1Winner[i + 1] != null) {

                    args.putInt("imageR2id", i / 2);
                    args.putString("upR1winner", r1Winner[i]);
                    args.putString("downR1winner", r1Winner[i + 1]);
                    dialog.setArguments(args);
                    dialog.show(getFragmentManager(), "round");
                } else {
                    System.out.println("r1Winnerが空です");
                    Toast.makeText(getApplicationContext(), "下位の勝敗を決めて下さい", Toast.LENGTH_LONG).show();
                }
                break;
            }
            if (v == imageR3 && i % 4 == 0) {
                System.out.println("firstWinner=");
                if (r2Winner[0] != null && r2Winner[1] != null) {

                    args.putString("upR2winner", r2Winner[0]);
                    args.putString("downR2winner", r2Winner[1]);
                    dialog.setArguments(args);
                    dialog.show(getFragmentManager(), "round");
                } else {
                    Toast.makeText(getApplicationContext(), "下位の勝敗を決めて下さい", Toast.LENGTH_LONG).show();
                }
            }

        }

    }


    public void status(View v) {
        for (int i = 0; i < textView.length; i++) {
            if (v == textView[i]) {
                DialogFragment dialog = new MyDialogFragment();

                Bundle args = new Bundle();
                args.putString("player", textView[i].getText().toString());
                dialog.setArguments(args);
                dialog.show(getFragmentManager(), "dialog");
                break;
            }
        }
    }


}
