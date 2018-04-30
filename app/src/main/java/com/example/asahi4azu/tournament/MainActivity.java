package com.example.asahi4azu.tournament;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    ImageView[] imageR1, imageR2;
    ImageView imageR3, winner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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


}
