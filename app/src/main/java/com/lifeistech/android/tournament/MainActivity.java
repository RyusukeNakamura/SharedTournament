package com.lifeistech.android.tournament;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.provider.ContactsContract;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.Integer.getInteger;
import static java.lang.Integer.parseInt;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    static LinearLayout layout;
    public static TextView auth;
    static final int requestCodePassword = 0;


    ImageView[] imageR1, imageR2;
    ImageView imageR3, firstWinner;
    GridLayout gridLayout;
    TextView[] textView;

    String[] players;
    String[] r1Winner, r2Winner;
    List<String> winp;


    int[] upSide = {0, 0}, bottomSide = {0, 0};
    int finalist0 = -1, finalist1 = -1;
    int done1R = -1;
    int done2R = -1;
    int done3R = -1;

    int n = 0;

    String str, gName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = (TextView) findViewById(R.id.auth);

        layout = (LinearLayout) findViewById(R.id.linearLayout);
        Intent intent = getIntent();
        str = intent.getStringExtra("createdId");
        gName = intent.getStringExtra("gameName");
        Log.d("newCreatedID", str);
        Log.d("gameName", gName);

        //一人１トーナメント各自で作れる．あとで新しくつくるとき消すように保存．
        SharedPreferences pref=getSharedPreferences("disposeValue",MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();
        editor.putString("dispose",str);
        editor.commit();


        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(gName);
        actionBar.setSubtitle(str);


        //Set2,Loginで生成した配列を取得
        players = intent.getStringArrayExtra("players");
        Log.d("playersClass", players.getClass().toString());

        for (int i = 0; i < players.length; i++) {
            Log.d("player[]", players[i]);
        }

        DatabaseReference ref = database.getReference(str);


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

        //BYEは灰色にする
        for (int i = 0; i < textView.length; i++) {
            if (players[i].indexOf("BYE") != -1) {
                textView[i].setTextColor(Color.parseColor("#D3D3D3"));
                Log.d("matches", "bye");
            } else {
                textView[i].setTextColor(Color.parseColor("#000000"));
            }
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


        r1Winner = new String[4];
        r2Winner = new String[2];


        //メソッドに統一したい


        try {
            //結果を読み込み

            ref.child("Round").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d("1RonDataChanged", "datasnapshot");
                    for (int i = 0; i < imageR1.length; i++) {
                        //一回戦の結果取得
                        String w = String.valueOf(dataSnapshot.child("Round1:" + i + "/winner").getValue());
                        String l = (String) dataSnapshot.child("Round1:" + i + "/loser").getValue();
                        int upP = parseInt(dataSnapshot.child("Round1:" + i + "/up").getValue().toString());
                        int downP = parseInt(dataSnapshot.child("Round1:" + i + "/down").getValue().toString());
                        System.out.println("1Rwinner:" + w + "\n1Rloser:" + l);


                        //勝者を格納
                        if (upP > downP) {
                            imageR1[i].setBackgroundResource(R.drawable.one_topup);
                            Log.d("up>down", "imageR1up");
                            r1Winner[i] = w;
                            done1R = i;
                        } else if (upP < downP) {
                            imageR1[i].setBackgroundResource(R.drawable.one_bottomdown);
                            Log.d("up<down", "imageR1down");
                            r1Winner[i] = w;
                            done1R = i;
                        }
                        //imageR2の画像変更, 一回戦完了
                        if (done1R == i) {
                            if (i % 2 == 0) {
                                Log.d("imageR2", "changedTopDone" + i);
                                imageR2[i / 2].setBackgroundResource(R.drawable.two_top_done);
                                upSide[i / 2] = 1;
                            } else {
                                Log.d("imageR2", "changedBottomDone" + i);

                                imageR2[i / 2].setBackgroundResource(R.drawable.two_bottom_done);
                                bottomSide[i / 2] = 1;
                            }
                            if (bottomSide[i / 2] == 1 && upSide[i / 2] == 1) {
                                Log.d("一回戦", "両方完了！！！！");
                                imageR2[i / 2].setBackgroundResource(R.drawable.two_both_done);

                            }
                        }
                    }

                    for (int i = 0; i < imageR2.length; i++) {
                        //２回戦の結果取得
                        String w2 = String.valueOf(dataSnapshot.child("Round2:" + i + "/winner").getValue());
                        String l2 = (String) dataSnapshot.child("Round2:" + i + "/loser").getValue();
                        int upP2 = parseInt(dataSnapshot.child("Round2:" + i + "/up").getValue().toString());
                        int downP2 = parseInt(dataSnapshot.child("Round2:" + i + "/down").getValue().toString());
                        System.out.println("2Rwinner:" + w2 + "\n2Rloser:" + l2 + "\nj=" + i);


                        //勝者を格納
                        if (upP2 > downP2) {
                            imageR2[i].setBackgroundResource(R.drawable.two_topup);
                            Log.d("up>down", "r2wwwwwwwwwwwwwWinner" + i + ":" + w2);
                            r2Winner[i] = w2;
                            done2R = i;
                        } else if (upP2 < downP2) {
                            imageR2[i].setBackgroundResource(R.drawable.two_bottomdown);
                            Log.d("up<down", "r2Wwwwwwwwwwwwwwwwinner" + i + ":" + w2);
                            r2Winner[i] = w2;
                            done2R = i;
                        }
                        //imageR3の画像変更, ２回戦完了
                        if (done2R == i) {
                            if (i == 0) {
                                Log.d("imageR3", "changedTopDone");
                                imageR3.setBackgroundResource(R.drawable.three_top_done);
                                finalist0 = 1;
                            } else {
                                Log.d("imageR3", "changedBottomDone" + i);
                                imageR3.setBackgroundResource(R.drawable.three_bottom_done);
                                finalist1 = 1;
                            }
                            if (finalist0 == 1 && finalist1 == 1) {
                                imageR3.setBackgroundResource(R.drawable.three_both_done);
                            }

                        }


                        String w3 = String.valueOf(dataSnapshot.child("Round3:0/winner").getValue());
                        String l3 = (String) dataSnapshot.child("Round3:0/loser").getValue();
                        int upP3 = parseInt(dataSnapshot.child("Round3:0/up").getValue().toString());
                        int downP3 = parseInt(dataSnapshot.child("Round3:0/down").getValue().toString());
                        System.out.println("3Rwinner:" + w3 + "\n3Rloser:" + l3);

                        //勝者を格納
                        if (upP3 > downP3) {
                            imageR3.setBackgroundResource(R.drawable.three_topdown);
                            Log.d("up>down", "r3Winner0" + ":" + w3);
                            done3R = 0;
                        } else if (upP3 < downP3)

                        {
                            imageR3.setBackgroundResource(R.drawable.three_bottomup);
                            Log.d("up<down", "r3Winner0" + ":" + w3);
                            done3R = 0;
                        }
                        //imageR3の画像変更, ２回戦完了
                        if (done3R == 0) {
                            Log.d("imageR3", "firstWinner decided");
                            firstWinner.setImageResource(R.drawable.red_line);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            ref.child("Status").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Log.d("for完了", "ffffffff");

                    for (int i = 0; i < players.length; i++) {
                        if (players[i].equals(dataSnapshot.child("player" + i + "/name").getValue().toString())) {

                            switch (Integer.parseInt(dataSnapshot.child("player" + i + "/winPoint").getValue().toString())) {
                                case 1:
                                    textView[i].setBackgroundColor(Color.parseColor("#F9DAD2"));
                                    break;
                                case 2:
                                    textView[i].setBackgroundColor(Color.parseColor("#F98262"));
                                    break;
                                case 3:
                                    textView[i].setBackgroundColor(Color.parseColor("#FC3B00"));
                                    break;
                                default:
                                    textView[i].setBackgroundColor(Color.parseColor("#FFF5EE"));
                                    break;
                            }
                        }
                    }
                    winp = new ArrayList<String>();

                    for (int i = 0; i < players.length; i++) {
                        int n = Integer.parseInt(dataSnapshot.child("player" + i + "/winPoint").getValue().toString());
                        Log.d("int n", "n=" + n);
                        winp.add("best " + (int) (players.length / (Math.pow(2, n))) + "       " + players[i]);
                    }
                    Collections.sort(winp);
                    System.out.println(winp);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        } catch (Exception e) {
            Log.d("Status/", "playerはカラ");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //WriteLogIn
            case R.id.logIn:
                DialogFragment dialog2 = new WritableLogin();
                dialog2.setTargetFragment(null, requestCodePassword);
                Bundle args = new Bundle();
                args.putString("gameId", str);
                dialog2.setArguments(args);
                dialog2.show(getFragmentManager(), "writable");
                Log.d("writeLogin", "dialog");
                break;
            //ResultNow
            case R.id.result:

                Toast.makeText(this, "result", Toast.LENGTH_SHORT).show();
                DialogFragment dialogFragment = new ResultNowFragment();
                Bundle argument = new Bundle();
                argument.putString("userId", str);
                argument.putStringArray("players", winp.toArray(new String[0]));
                dialogFragment.setArguments(argument);
                dialogFragment.show(getFragmentManager(), "result");
                break;
            case R.id.dispose:
                try {
                    finish();
                    /*DatabaseReference ref = database.getReference(str);
                    ref.removeValue();*/
                } catch (Exception e) {
                    Log.d("dipose", "error??");
                }
                break;
        }

        return true;
    }


    //一回戦
    public void round1(View v) {

        DialogFragment dialog = new RoundDialogFragment();
        Bundle args = new Bundle();
        args.putString("userId", str);
        args.putString("editable", auth.getText().toString());


        for (int i = 0; i < imageR1.length; i++) {


            //クリックしたブロックを判別
            if (v == imageR1[i]) {

                args.putInt("id", i);
                args.putString("upPlayer", players[2 * i]);
                args.putString("downPlayer", players[2 * i + 1]);
                dialog.setArguments(args);
                dialog.show(getFragmentManager(), "round");
                break;
            }
        }
    }

    //２回戦
    public void round2(View v) {
        DialogFragment dialog = new RoundDialogFragment();
        Bundle args = new Bundle();
        args.putString("userId", str);
        args.putString("editable", auth.getText().toString());


        for (int i = 0; i < imageR2.length; i++) {
            if (v == imageR2[i]) {
                System.out.println("r1Winner=" + r1Winner[2 * i]);
                if (r1Winner[2 * i] != null && r1Winner[2 * i + 1] != null) {

                    args.putInt("id", i);


                    //上から何番目か
                    for (int j = 0; j < players.length; j++) {
                        //一度閉じてログインした後，==にすればifに引っかからなくなる．教訓
                        if (r1Winner[2 * i].equals(players[j])) {
                            args.putInt("imageR2up", j);
                        }
                        if (r1Winner[2 * i + 1].equals(players[j])) {
                            args.putInt("imageR2down", j);
                        }
                    }


                    args.putString("upR1winner", r1Winner[2 * i]);
                    args.putString("downR1winner", r1Winner[2 * i + 1]);
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

    //３回戦
    public void round3(View v) {
        DialogFragment dialog = new RoundDialogFragment();
        Bundle args = new Bundle();
        args.putString("userId", str);
        args.putString("editable", auth.getText().toString());


        System.out.println("firstWinner=");
        if (r2Winner[0] != null && r2Winner[1] != null) {

            for (int j = 0; j < players.length; j++) {
                if (r2Winner[0].equals(players[j])) {
                    Log.d("r1 r2 0win", "r1=" + r1Winner[0] + "r2=" + r2Winner[0]);
                    args.putInt("imageR3up", j);
                }
                if (r2Winner[1].equals(players[j])) {
                    Log.d("r1 r2 1win", "r1=" + r1Winner[1] + "r2=" + r2Winner[1]);

                    args.putInt("imageR3down", j);
                }
                Log.d("r1 r2 win", "in for");

            }


            args.putString("upR2winner", r2Winner[0]);
            args.putString("downR2winner", r2Winner[1]);
            dialog.setArguments(args);
            dialog.show(getFragmentManager(), "round");
            Log.d("dialog", "inMain");
        } else {
            Toast.makeText(getApplicationContext(), "下位の勝敗を決めて下さい", Toast.LENGTH_LONG).show();
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

    int back = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 戻るボタンの処理
            // 編集しているときにメモボタンを押したときは警告をする
            Snackbar.make(layout, "終了しますか？（データは保存されています）", Snackbar.LENGTH_LONG).setAction("YES", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            }).show();

            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
