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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();


    ImageView[] imageR1, imageR2;
    ImageView imageR3, firstWinner;
    GridLayout gridLayout;
    TextView[] textView;

    String[] players;

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
        System.out.println("nande?");
        while (n != players.length) {
            int value = r.nextInt(players.length);
            if (players[value] == null) {
                players[value] = "Player" + n;
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


        //選手の人数分のdataBaseを宣言
        DatabaseReference[] ref = new DatabaseReference[8];


        //改善する必要あり４
        for (int i = 0; i < 4; i++) {

            if (database.getReference("result" + 2 * i + 1) != null) {
                ref[2 * i] = database.getReference("result" + 2 * i);
                ref[2 * i + 1] = database.getReference("result" + (2 * i + 1));


                ref[2 * i].addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                            System.out.println(dataSnapshot.child("r1point").getValue());
                            System.out.println(dataSnapshot.child("r2point").getValue());
                            System.out.println(dataSnapshot.child("r3point").getValue());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                ref[2 * i + 1].addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        System.out.println(dataSnapshot.child("r1point").getValue());
                        System.out.println(dataSnapshot.child("r2point").getValue());
                        System.out.println(dataSnapshot.child("r3point").getValue());


                        //iが使えなかったから仕方なくjのfor文　i使うならfinalいるらしい
//                        for (int j = 0; j < 4; j++) {
//
//                            System.out.println("j:" + j);
//
//                            if (dataSnapshot.getValue(RoundResult.class).getR1point() == textView[2 * j].getText().toString()) {
//                                imageR1[j].setImageResource(R.drawable.ko_top_won);
////                                imageR1[j].setLayoutParams(new LinearLayout.LayoutParams(
////                                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
//
//                                System.out.println("gusu");
//
//                                done = j;
//
//                            } else if (dataSnapshot.getValue(RoundResult.class).getWinner() == textView[2 * j + 1].getText().toString()) {
//                                imageR1[j].setImageResource(R.drawable.ko_bottom_won);
//
//
//
//                                System.out.println("kisu");
//
//                                done = j;
//                            }
//                            if (done == j) {
//
//
//                                if (j % 2 == 0) {
//                                    imageR2[j / 2].setImageResource(R.drawable.ko_top_done);
//                                    upSide[j / 2] = 1;
//                                } else {
//                                    imageR2[j / 2].setImageResource(R.drawable.ko_bottom_done);
//                                    bottomSide[j / 2] = 1;
//                                }
//                                if (upSide[j/2] == 1 && bottomSide[j/2] == 1) {
//                                    imageR2[j / 2].setImageResource(R.drawable.ko_both_done);
//
//
//                                }
//
//                            }
//                            System.out.println("for matsubi");
//                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            } else {
                Log.d("result", "result" + i + "はカラ");
            }
        }
    }

    public void round1(View v) {

        DialogFragment dialog = new RoundDialogFragment();
        Bundle args = new Bundle();

        for (int i = 0; i < imageR1.length; i++) {
            if (v == imageR1[i]) {
                args.putInt("id", i);
                args.putString("upPlayer", players[2 * i]);
                args.putString("downPlayer", players[2 * i + 1]);
                break;
            }
        }
        dialog.setArguments(args);
        dialog.show(getFragmentManager(), "round");
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
