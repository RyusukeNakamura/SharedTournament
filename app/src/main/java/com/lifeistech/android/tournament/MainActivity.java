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
    String[] r1Winner;

    int done = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final int[] upSide = new int[2];
        final int[] bottomSide = new int[2];

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


        r1Winner = new String[4];

        //改善する必要あり４
        for (int i = 0; i < imageR1.length; i++) {

            try {
                //結果を読み込み
                DatabaseReference ref1Round = database.getReference("Round/Round1:" + i);

                final int j = i;
                ref1Round.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String w = String.valueOf(dataSnapshot.child("winner").getValue());
                        String l = (String) dataSnapshot.child("loser").getValue();
                        int upP = parseInt(dataSnapshot.child("up").getValue().toString());
                        int downP = parseInt(dataSnapshot.child("down").getValue().toString());
                        System.out.println("winner:" + w + "\nloser:" + l);


                        //勝者を格納
                        if (upP > downP) {
                            imageR1[j].setImageResource(R.drawable.ko_top_won);
                            r1Winner[j] = w;
                            done = j;
                        } else if (upP < downP) {
                            imageR1[j].setImageResource(R.drawable.ko_bottom_won);
                            r1Winner[j] = w;
                            done = j;
                        }
                        //imageR2の画像変更
                        if (done == j) {

                            if (j % 2 == 0) {
                                imageR2[j / 2].setImageResource(R.drawable.ko_top_done);
                                upSide[j / 2] = 1;
                            } else {
                                imageR2[j / 2].setImageResource(R.drawable.ko_bottom_done);
                                bottomSide[j / 2] = 1;
                            }
                            if (bottomSide[j / 2] == 1 && upSide[j / 2] == 1) {
                                imageR2[j / 2].setImageResource(R.drawable.ko_both_done);
                            }

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                refP.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        System.out.println("r1point=" + dataSnapshot.child(players[2*j]).child("r1point").getValue());
                        System.out.println("r2point=" + dataSnapshot.child(players[2*j]).child("r2point").getValue());
                        System.out.println("r3point=" + dataSnapshot.child(players[2*j]).child("r3point").getValue());

                        System.out.println("r1point=" + dataSnapshot.child(players[2*j+1]).child("r1point").getValue());
                        System.out.println("r2point=" + dataSnapshot.child(players[2*j+1]).child("r2point").getValue());
                        System.out.println("r3point=" + dataSnapshot.child(players[2*j+1]).child("r3point").getValue());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            } catch (Exception e) {
                Log.d("Status/", "player" + i + "はカラ");
            }
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

                    System.out.println("imageR2lllllllllllll");

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
