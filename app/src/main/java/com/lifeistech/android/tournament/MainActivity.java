package com.lifeistech.android.tournament;

import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    ImageView[] imageR1, imageR2;
    ImageView imageR3, winner;
    GridLayout gridLayout;
    TextView[] textView;

    String[] players;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        players = new String[8];
        Random r = new Random();
        int n = 0;
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
        winner = (ImageView) findViewById(R.id.winner);


    }

    public void round1(View v) {
        DialogFragment dialog = new RoundDialogFragment();

        Bundle args = new Bundle();
        for(int i=0;i<players.length;i++){
            if(v==imageR1[i]){
                args.putString("upPlayer", textView[2*i].getText().toString());
                    args.putString("downPlayer",textView[2*i+1].getText().toString());
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
