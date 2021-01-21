package com.supertridents.learn.digital.marketing.quiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.ankushgrover.hourglass.Hourglass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

import static com.supertridents.learn.digital.marketing.quiz.MainActivity.LEVEL;
import static com.supertridents.learn.digital.marketing.quiz.MainActivity.coins;

public class
GameActivity extends AppCompatActivity implements View.OnClickListener {

    final String CORRECT="CORRECT";
    final String ANS="ANS";
    TextView question,op1,op2,op3,op4;
    List<QuestionItem> questionItems;
    int currentQuestion = 0,level;
    int correct = 0,wrong = 0,i = 1;
    TextView current,total,time;
    CardView pause,fifty,swap,fifty2,doubleDip,doubleDip2;
    CountDownTimer cTimer = null;
    int isDoubledip = 0,isfifty=0,isswap=0;
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getSupportActionBar().hide();
        SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
        editor.commit();
//        QuestionFragment fragment = new QuestionFragment();
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.screen,fragment);
//        transaction.commit();
        question = findViewById(R.id.question);
        op1 = findViewById(R.id.option_1);
        op2 = findViewById(R.id.option_2);
        op3 = findViewById(R.id.option_3);
        op4 = findViewById(R.id.option_4);
        pause = findViewById(R.id.gamepause);
        fifty = findViewById(R.id.fifty);
        fifty2 = findViewById(R.id.fifty2);
        swap  = findViewById(R.id.swap);
        time = findViewById(R.id.timer);
        doubleDip = findViewById(R.id.doubleDip);
        doubleDip2 = findViewById(R.id.doubleDip2);

        loadQuestions();
        Collections.shuffle(questionItems);
        setQuestionScreen(currentQuestion);
        total = findViewById(R.id.total);
        current =findViewById(R.id.current);
        total.setText("3");
        current.setText(i+"/");

        op1.setOnClickListener(this);
        op2.setOnClickListener(this);
        op3.setOnClickListener(this);
        op4.setOnClickListener(this);
        pause.setOnClickListener(this);
        Intent intent = getIntent();
        level = intent.getIntExtra("level",0);
        TextView lvl = findViewById(R.id.lvltxt);
        lvl.setText("Level "+String.valueOf(level));

        SharedPreferences preferences = getSharedPreferences(LEVEL,MODE_PRIVATE);
        final int[] coin = {preferences.getInt(String.valueOf(coins), 1)};
        TextView cointext = findViewById(R.id.coins);
        cointext.setText(String.valueOf(coin[0]));

        //Lifelines
        fifty.setOnClickListener(v -> {

            final Dialog dialog2 = new Dialog(GameActivity.this);
            dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
            dialog2.setContentView(R.layout.fifty);
            dialog2.setCancelable(true);

            WindowManager.LayoutParams lp2 = new WindowManager.LayoutParams();
            lp2.copyFrom(dialog2.getWindow().getAttributes());
            lp2.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp2.height = WindowManager.LayoutParams.WRAP_CONTENT;

            (dialog2.findViewById(R.id.fyes)).setOnClickListener(v1 -> {
                isfifty=1;
                String ans = questionItems.get(currentQuestion).getCorrect();
                if(coin[0] >=100) {
                    if (ans.equals(op1.getText())) {
                        op1.setBackground(getResources().getDrawable(R.drawable.box_unselected));
                        op3.setBackground(getResources().getDrawable(R.drawable.box_unselected));
                    } else if (ans.equals(op2.getText())) {
                        op2.setBackground(getResources().getDrawable(R.drawable.box_unselected));
                        op4.setBackground(getResources().getDrawable(R.drawable.box_unselected));
                    } else if (ans.equals(op3.getText())) {
                        op3.setBackground(getResources().getDrawable(R.drawable.box_unselected));
                        op2.setBackground(getResources().getDrawable(R.drawable.box_unselected));
                    } else if (ans.equals(op4.getText())) {
                        op3.setBackground(getResources().getDrawable(R.drawable.box_unselected));
                        op4.setBackground(getResources().getDrawable(R.drawable.box_unselected));
                    }
                    coin[0] = coin[0] -100;
                    SharedPreferences.Editor coinseditor = getSharedPreferences(MainActivity.LEVEL,MODE_PRIVATE).edit();
                    coinseditor.putInt(String.valueOf(coins), coin[0]);
                    coinseditor.apply();
                    coinseditor.commit();
                    cointext.setText(String.valueOf(coin[0]));
                    fifty.setClickable(false);
                    fifty.setForeground(getResources().getDrawable(R.drawable.ic_close));
                }else {
                    Toast.makeText(this, "No Coins", Toast.LENGTH_SHORT).show();
                }

                dialog2.dismiss();
            });
            (dialog2.findViewById(R.id.fno)).setOnClickListener(v1 -> {
                dialog2.dismiss();
            });

            dialog2.show();
            dialog2.getWindow().setAttributes(lp2);
        });

        doubleDip.setOnClickListener(v -> {

            final Dialog dialog2 = new Dialog(GameActivity.this);
            dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
            dialog2.setContentView(R.layout.doubledip);
            dialog2.setCancelable(true);

            WindowManager.LayoutParams lp2 = new WindowManager.LayoutParams();
            lp2.copyFrom(dialog2.getWindow().getAttributes());
            lp2.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp2.height = WindowManager.LayoutParams.WRAP_CONTENT;

            (dialog2.findViewById(R.id.dyes)).setOnClickListener(v1 -> {
                String ans = questionItems.get(currentQuestion).getCorrect();
                isfifty = 2;

                if(coin[0] >=100) {

                    op1.setOnClickListener(v4 -> checkDoubledip(op1));
                    op2.setOnClickListener(v4 -> checkDoubledip(op2));
                    op3.setOnClickListener(v4 -> checkDoubledip(op3));
                    op4.setOnClickListener(v4 -> checkDoubledip(op4));
                    coin[0] = coin[0] -100;
                    SharedPreferences.Editor coinseditor = getSharedPreferences(MainActivity.LEVEL,MODE_PRIVATE).edit();
                    coinseditor.putInt(String.valueOf(coins), coin[0]);
                    coinseditor.apply();
                    coinseditor.commit();
                    cointext.setText(String.valueOf(coin[0]));
                    doubleDip.setClickable(false);
                    doubleDip.setForeground(getResources().getDrawable(R.drawable.ic_close));
                }else {
                    Toast.makeText(this, "No Coins", Toast.LENGTH_SHORT).show();
                }

                dialog2.dismiss();
            });
            (dialog2.findViewById(R.id.dno)).setOnClickListener(v1 -> {
                dialog2.dismiss();
            });

            dialog2.show();
            dialog2.getWindow().setAttributes(lp2);
        });

        swap.setOnClickListener(v -> {

            final Dialog dialog2 = new Dialog(GameActivity.this);
            dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
            dialog2.setContentView(R.layout.swap);
            dialog2.setCancelable(true);

            WindowManager.LayoutParams lp2 = new WindowManager.LayoutParams();
            lp2.copyFrom(dialog2.getWindow().getAttributes());
            lp2.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp2.height = WindowManager.LayoutParams.WRAP_CONTENT;

            (dialog2.findViewById(R.id.syes)).setOnClickListener(v1 -> {

                if(coin[0] >=100) {

                    setRandomQuestionScreen(7);

                    op1.setOnClickListener(v4 -> checkRandomAnswer(op1));
                    op2.setOnClickListener(v4 -> checkRandomAnswer(op2));
                    op3.setOnClickListener(v4 -> checkRandomAnswer(op3));
                    op4.setOnClickListener(v4 -> checkRandomAnswer(op4));
                    coin[0] = coin[0] -100;
                    SharedPreferences.Editor coinseditor = getSharedPreferences(MainActivity.LEVEL,MODE_PRIVATE).edit();
                    coinseditor.putInt(String.valueOf(coins), coin[0]);
                    coinseditor.apply();
                    coinseditor.commit();
                    cointext.setText(String.valueOf(coin[0]));
                    swap.setClickable(false);
                    swap.setForeground(getResources().getDrawable(R.drawable.ic_close));


//                        fifty.setVisibility(View.INVISIBLE);
//                        fifty2.setVisibility(View.VISIBLE);
//                        fifty2.setOnClickListener(v2 -> {
//
//                            final Dialog dialogf = new Dialog(GameActivity.this);
//                            dialogf.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
//                            dialogf.setContentView(R.layout.fifty);
//                            dialogf.setCancelable(true);
//
//                            WindowManager.LayoutParams lpf = new WindowManager.LayoutParams();
//                            lpf.copyFrom(dialogf.getWindow().getAttributes());
//                            lpf.width = WindowManager.LayoutParams.MATCH_PARENT;
//                            lpf.height = WindowManager.LayoutParams.WRAP_CONTENT;
//
//                            (dialogf.findViewById(R.id.fyes)).setOnClickListener(v3 -> {
//                                String ans = questionItems.get(7).getCorrect();
//                                if (coin[0] >= 100) {
//                                    if (ans.equals(op1.getText())) {
//                                        op1.setBackground(getResources().getDrawable(R.drawable.box_unselected));
//                                        op3.setBackground(getResources().getDrawable(R.drawable.box_unselected));
//                                    } else if (ans.equals(op2.getText())) {
//                                        op2.setBackground(getResources().getDrawable(R.drawable.box_unselected));
//                                        op4.setBackground(getResources().getDrawable(R.drawable.box_unselected));
//                                    } else if (ans.equals(op3.getText())) {
//                                        op3.setBackground(getResources().getDrawable(R.drawable.box_unselected));
//                                        op2.setBackground(getResources().getDrawable(R.drawable.box_unselected));
//                                    } else if (ans.equals(op4.getText())) {
//                                        op3.setBackground(getResources().getDrawable(R.drawable.box_unselected));
//                                        op4.setBackground(getResources().getDrawable(R.drawable.box_unselected));
//                                    }
//                                    op1.setOnClickListener(v4 -> checkRandomAnswer(op1));
//                                    op2.setOnClickListener(v4 -> checkRandomAnswer(op2));
//                                    op3.setOnClickListener(v4 -> checkRandomAnswer(op3));
//                                    op4.setOnClickListener(v4 -> checkRandomAnswer(op4));
//                                    coin[0] = coin[0] - 100;
//                                    SharedPreferences.Editor coinsseditor = getSharedPreferences(MainActivity.LEVEL, MODE_PRIVATE).edit();
//                                    coinsseditor.putInt(String.valueOf(coins), coin[0]);
//                                    coinsseditor.apply();
//                                    coinsseditor.commit();
//                                    cointext.setText(String.valueOf(coin[0]));
//                                    fifty2.setClickable(false);
//                                    fifty2.setForeground(getResources().getDrawable(R.drawable.ic_close));
//                                } else {
//                                    Toast.makeText(this, "No Coins", Toast.LENGTH_SHORT).show();
//                                }
//
//                                dialogf.dismiss();
//                            });
//                            (dialogf.findViewById(R.id.fno)).setOnClickListener(v3 -> {
//                                dialogf.dismiss();
//                            });
//
//                            dialogf.show();
//                            dialogf.getWindow().setAttributes(lp2);
//                        });
//
//                        doubleDip.setVisibility(View.INVISIBLE);
//                        doubleDip2.setVisibility(View.VISIBLE);
//                        doubleDip2.setOnClickListener(v2 -> {
//
//                            final Dialog ddialog2 = new Dialog(GameActivity.this);
//                            ddialog2.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
//                            ddialog2.setContentView(R.layout.doubledip);
//                            ddialog2.setCancelable(true);
//
//                            WindowManager.LayoutParams dlp2 = new WindowManager.LayoutParams();
//                            dlp2.copyFrom(ddialog2.getWindow().getAttributes());
//                            dlp2.width = WindowManager.LayoutParams.MATCH_PARENT;
//                            dlp2.height = WindowManager.LayoutParams.WRAP_CONTENT;
//
//                            (ddialog2.findViewById(R.id.dyes)).setOnClickListener(v3 -> {
//                                String ans = questionItems.get(7).getCorrect();
//                                if (coin[0] >= 100) {
//
//                                    op1.setOnClickListener(v4 -> checkRandomDoubledip(op1));
//                                    op2.setOnClickListener(v4 -> checkRandomDoubledip(op2));
//                                    op3.setOnClickListener(v4 -> checkRandomDoubledip(op3));
//                                    op4.setOnClickListener(v4 -> checkRandomDoubledip(op4));
//                                    //isDoubledip = 2;
//                                    coin[0] = coin[0] - 100;
//                                    SharedPreferences.Editor dcoinseditor = getSharedPreferences(MainActivity.LEVEL, MODE_PRIVATE).edit();
//                                    dcoinseditor.putInt(String.valueOf(coins), coin[0]);
//                                    dcoinseditor.apply();
//                                    dcoinseditor.commit();
//                                    cointext.setText(String.valueOf(coin[0]));
//                                    doubleDip2.setClickable(false);
//                                    doubleDip2.setForeground(getResources().getDrawable(R.drawable.ic_close));
//                                } else {
//                                    Toast.makeText(this, "No Coins", Toast.LENGTH_SHORT).show();
//                                }
//
//                                ddialog2.dismiss();
//                            });
//                            (ddialog2.findViewById(R.id.dno)).setOnClickListener(v3 -> {
//                                ddialog2.dismiss();
//                            });
//
//                            ddialog2.show();
//                            ddialog2.getWindow().setAttributes(lp2);
//                        });

                }else {
                    Toast.makeText(this, "No Coins", Toast.LENGTH_SHORT).show();
                }

                dialog2.dismiss();
            });
            (dialog2.findViewById(R.id.sno)).setOnClickListener(v1 -> {
                dialog2.dismiss();
            });

            dialog2.show();
            dialog2.getWindow().setAttributes(lp2);
        });

        
    }


    Hourglass hourglass = new Hourglass(60000, 1000) {
        @Override
        public void onTimerTick(long timeRemaining) {
            // Update UI
            time.setText(String.valueOf(timeRemaining / 1000));
        }

        @Override
        public void onTimerFinish() {
            // Timer finished
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // gif.setVisibility(View.INVISIBLE);

                            //Toast.makeText(GameActivity.this, "Game Over", Toast.LENGTH_SHORT).show();
                            final Dialog dialog2 = new Dialog(GameActivity.this);
                            dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                            dialog2.setContentView(R.layout.finish);
                            dialog2.setCancelable(true);

                            WindowManager.LayoutParams lp2 = new WindowManager.LayoutParams();
                            lp2.copyFrom(dialog2.getWindow().getAttributes());
                            lp2.width = WindowManager.LayoutParams.MATCH_PARENT;
                            lp2.height = WindowManager.LayoutParams.MATCH_PARENT;

                            TextView lvl = dialog2.findViewById(R.id.flevel);
                            lvl.setText("Level "+String.valueOf(level));
                            (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_off);
                            (dialog2.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_off);
                            (dialog2.findViewById(R.id.fstar3)).setBackgroundResource(R.drawable.star_off);

                            (dialog2.findViewById(R.id.fnext)).setOnClickListener(v2 -> {
                                SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
                                editor.clear();
                                editor.apply();
                                editor.commit();
                                startActivity(new Intent(GameActivity.this,QuestionActivity.class));
                            });

                            (dialog2.findViewById(R.id.fhome)).setOnClickListener(v2 -> {
                                SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
                                editor.clear();
                                editor.apply();
                                editor.commit();
                                startActivity(new Intent(GameActivity.this,MainActivity.class));
                            });
                            (dialog2.findViewById(R.id.frestart)).setOnClickListener(v2 -> {
                                SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
                                editor.clear();
                                editor.apply();
                                editor.commit();
                                startActivity(getIntent());
                            });
                            SharedPreferences preferences = getSharedPreferences(CORRECT,MODE_PRIVATE);
                            int c = preferences.getInt(ANS,0);

                            if(c==1){
                                (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                            }else if(c==2){
                                (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                                (dialog2.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_on);
                            }else if(c==3){
                                (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                                (dialog2.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_on);
                                (dialog2.findViewById(R.id.fstar3)).setBackgroundResource(R.drawable.star_on);
                            }
                            dialog2.show();
                            dialog2.getWindow().setAttributes(lp2);

                    }
                },100);
                //Toast.makeText(GameActivity.this, "Time Over", Toast.LENGTH_SHORT).show();


        }
    };
    @SuppressLint("UseCompatLoadingForDrawables")
    private void checkAnswer(TextView selected) {
        String selectedAnswer = selected.getText().toString();
        if(selectedAnswer.equals(questionItems.get(currentQuestion).getCorrect())){
            selected.setBackground(getResources().getDrawable(R.drawable.option_right));
            correct++;

            //gif.setVisibility(View.VISIBLE);
            clickable(false);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                   // gif.setVisibility(View.INVISIBLE);
                    reset();
                    clickable(true);

                    if(currentQuestion<2){
                        currentQuestion++;
                        i++;
                        current.setText(i+"/");
                         setQuestionScreen(currentQuestion);
                         clickable(true);
                          reset();
                     }else {
                        //Toast.makeText(GameActivity.this, "Game Over", Toast.LENGTH_SHORT).show();
                        final Dialog dialog2 = new Dialog(GameActivity.this);
                        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                        dialog2.setContentView(R.layout.finish);
                        dialog2.setCancelable(true);

                        WindowManager.LayoutParams lp2 = new WindowManager.LayoutParams();
                        lp2.copyFrom(dialog2.getWindow().getAttributes());
                        lp2.width = WindowManager.LayoutParams.MATCH_PARENT;
                        lp2.height = WindowManager.LayoutParams.MATCH_PARENT;

                        TextView lvl = dialog2.findViewById(R.id.flevel);
                        lvl.setText("Level "+String.valueOf(level));
                        (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_off);
                        (dialog2.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_off);
                        (dialog2.findViewById(R.id.fstar3)).setBackgroundResource(R.drawable.star_off);

                        (dialog2.findViewById(R.id.fnext)).setOnClickListener(v2 -> {
                            SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
                            editor.clear();
                            editor.apply();
                            editor.commit();
                            startActivity(new Intent(GameActivity.this,QuestionActivity.class));
                        });

                        (dialog2.findViewById(R.id.fhome)).setOnClickListener(v2 -> {
                            SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
                            editor.clear();
                            editor.apply();
                            editor.commit();
                            startActivity(new Intent(GameActivity.this,MainActivity.class));
                        });
                        (dialog2.findViewById(R.id.frestart)).setOnClickListener(v2 -> {
                            SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
                            editor.clear();
                            editor.apply();
                            editor.commit();
                            startActivity(getIntent());
                        });
                        SharedPreferences preferences = getSharedPreferences(CORRECT,MODE_PRIVATE);
                        int c = preferences.getInt(ANS,0);

                        if(c==1){
                            (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                        }else if(c==2){
                            (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                            (dialog2.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_on);
                        }else if(c==3){
                            (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                            (dialog2.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_on);
                            (dialog2.findViewById(R.id.fstar3)).setBackgroundResource(R.drawable.star_on);
                        }
                        dialog2.show();
                        dialog2.getWindow().setAttributes(lp2);
                     }
                    }
            },800);

        } else {
            selected.setBackground(getResources().getDrawable(R.drawable.option_wrong));
            wrong++;
            final Dialog dialog = new Dialog(GameActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
            dialog.setContentView(R.layout.wrong);
            dialog.setCancelable(true);

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            (dialog.findViewById(R.id.lifeline)).setOnClickListener(v ->{
                reset();
                clickable(true);
                        dialog.dismiss();
            });

            (dialog.findViewById(R.id.closewrong)).setOnClickListener(v -> {
                reset();
                if(currentQuestion<2){
                    currentQuestion++;
                    i++;
                    current.setText(i+"/");
                    setQuestionScreen(currentQuestion);
                    clickable(true);
                    reset();
                }else {
                    final Dialog dialog2 = new Dialog(GameActivity.this);
                    dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                    dialog2.setContentView(R.layout.finish);
                    dialog2.setCancelable(true);

                    WindowManager.LayoutParams lp2 = new WindowManager.LayoutParams();
                    lp2.copyFrom(dialog2.getWindow().getAttributes());
                    lp2.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp2.height = WindowManager.LayoutParams.MATCH_PARENT;

                    TextView lvl = dialog2.findViewById(R.id.flevel);
                    lvl.setText("Level "+String.valueOf(level));
                    (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_off);
                    (dialog2.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_off);
                    (dialog2.findViewById(R.id.fstar3)).setBackgroundResource(R.drawable.star_off);

                    (dialog2.findViewById(R.id.fnext)).setOnClickListener(v2 -> {
                        SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
                        editor.clear();
                        editor.apply();
                        editor.commit();
                        startActivity(new Intent(GameActivity.this,QuestionActivity.class));
                    });

                    (dialog2.findViewById(R.id.fhome)).setOnClickListener(v2 -> {
                        SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
                        editor.clear();
                        editor.apply();
                        editor.commit();
                        startActivity(new Intent(GameActivity.this,MainActivity.class));
                    });
                    (dialog2.findViewById(R.id.frestart)).setOnClickListener(v2 -> {
                        SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
                        editor.clear();
                        editor.apply();
                        editor.commit();
                        startActivity(getIntent());
                    });
                    SharedPreferences preferences = getSharedPreferences(CORRECT,MODE_PRIVATE);
                    int c = preferences.getInt(ANS,0);

                    Toast.makeText(this, "Correct "+c, Toast.LENGTH_SHORT).show();
                    if(c==1){
                        (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                    }else if(c==2){
                        (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                        (dialog2.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_on);
                    }else if(c==3){
                        (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                        (dialog2.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_on);
                        (dialog2.findViewById(R.id.fstar3)).setBackgroundResource(R.drawable.star_on);
                    }
                    dialog2.show();
                    dialog2.getWindow().setAttributes(lp2);

                }
                dialog.dismiss();
            });

            dialog.show();
            dialog.getWindow().setAttributes(lp);
        }
        SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
        editor.putInt(ANS, correct);
        editor.apply();
        editor.commit();

    }

    private void checkRandomDoubledip(TextView selected) {
        String selectedAnswer = selected.getText().toString();
        if(selectedAnswer.equals(questionItems.get(7).getCorrect())){
            selected.setBackground(getResources().getDrawable(R.drawable.option_right));
            correct++;

            //gif.setVisibility(View.VISIBLE);
            clickable(false);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                   // gif.setVisibility(View.INVISIBLE);
                    reset();
                    clickable(true);

                    if(currentQuestion<2){
                        currentQuestion++;
                        i++;
                        current.setText(i+"/");
                         setQuestionScreen(currentQuestion);
                         clickable(true);
                          reset();
                     }else {
                        //Toast.makeText(GameActivity.this, "Game Over", Toast.LENGTH_SHORT).show();
                        final Dialog dialog2 = new Dialog(GameActivity.this);
                        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                        dialog2.setContentView(R.layout.finish);
                        dialog2.setCancelable(true);

                        WindowManager.LayoutParams lp2 = new WindowManager.LayoutParams();
                        lp2.copyFrom(dialog2.getWindow().getAttributes());
                        lp2.width = WindowManager.LayoutParams.MATCH_PARENT;
                        lp2.height = WindowManager.LayoutParams.MATCH_PARENT;

                        TextView lvl = dialog2.findViewById(R.id.flevel);
                        lvl.setText("Level "+String.valueOf(level));
                        (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_off);
                        (dialog2.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_off);
                        (dialog2.findViewById(R.id.fstar3)).setBackgroundResource(R.drawable.star_off);

                        (dialog2.findViewById(R.id.fnext)).setOnClickListener(v2 -> {
                            SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
                            editor.clear();
                            editor.apply();
                            editor.commit();
                            startActivity(new Intent(GameActivity.this,QuestionActivity.class));
                        });

                        (dialog2.findViewById(R.id.fhome)).setOnClickListener(v2 -> {
                            SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
                            editor.clear();
                            editor.apply();
                            editor.commit();
                            startActivity(new Intent(GameActivity.this,MainActivity.class));
                        });
                        (dialog2.findViewById(R.id.frestart)).setOnClickListener(v2 -> {
                            SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
                            editor.clear();
                            editor.apply();
                            editor.commit();
                            startActivity(getIntent());
                        });
                        SharedPreferences preferences = getSharedPreferences(CORRECT,MODE_PRIVATE);
                        int c = preferences.getInt(ANS,0);

                        if(c==1){
                            (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                        }else if(c==2){
                            (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                            (dialog2.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_on);
                        }else if(c==3){
                            (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                            (dialog2.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_on);
                            (dialog2.findViewById(R.id.fstar3)).setBackgroundResource(R.drawable.star_on);
                        }
                        dialog2.show();
                        dialog2.getWindow().setAttributes(lp2);
                     }
                    }
            },800);

        } else {
            selected.setBackground(getResources().getDrawable(R.drawable.option_wrong));
            Toast.makeText(this, "Double Dip", Toast.LENGTH_SHORT).show();
           clickable(true);

            if(selectedAnswer.equals(questionItems.get(7).getCorrect())) {
                selected.setBackground(getResources().getDrawable(R.drawable.option_right));
                correct++;

                //gif.setVisibility(View.VISIBLE);
                clickable(false);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // gif.setVisibility(View.INVISIBLE);
                        reset();
                        clickable(true);

                        if (currentQuestion < 2) {
                            currentQuestion++;
                            i++;
                            current.setText(i + "/");
                            setQuestionScreen(currentQuestion);
                            clickable(true);
                            reset();
                        } else {
                            //Toast.makeText(GameActivity.this, "Game Over", Toast.LENGTH_SHORT).show();
                            final Dialog dialog2 = new Dialog(GameActivity.this);
                            dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                            dialog2.setContentView(R.layout.finish);
                            dialog2.setCancelable(true);

                            WindowManager.LayoutParams lp2 = new WindowManager.LayoutParams();
                            lp2.copyFrom(dialog2.getWindow().getAttributes());
                            lp2.width = WindowManager.LayoutParams.MATCH_PARENT;
                            lp2.height = WindowManager.LayoutParams.MATCH_PARENT;

                            TextView lvl = dialog2.findViewById(R.id.flevel);
                            lvl.setText("Level " + String.valueOf(level));
                            (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_off);
                            (dialog2.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_off);
                            (dialog2.findViewById(R.id.fstar3)).setBackgroundResource(R.drawable.star_off);

                            (dialog2.findViewById(R.id.fnext)).setOnClickListener(v2 -> {
                                SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
                                editor.clear();
                                editor.apply();
                                editor.commit();
                                startActivity(new Intent(GameActivity.this, QuestionActivity.class));
                            });

                            (dialog2.findViewById(R.id.fhome)).setOnClickListener(v2 -> {
                                SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
                                editor.clear();
                                editor.apply();
                                editor.commit();
                                startActivity(new Intent(GameActivity.this, MainActivity.class));
                            });
                            (dialog2.findViewById(R.id.frestart)).setOnClickListener(v2 -> {
                                SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
                                editor.clear();
                                editor.apply();
                                editor.commit();
                                startActivity(getIntent());
                            });
                            SharedPreferences preferences = getSharedPreferences(CORRECT, MODE_PRIVATE);
                            int c = preferences.getInt(ANS, 0);

                            if (c == 1) {
                                (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                            } else if (c == 2) {
                                (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                                (dialog2.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_on);
                            } else if (c == 3) {
                                (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                                (dialog2.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_on);
                                (dialog2.findViewById(R.id.fstar3)).setBackgroundResource(R.drawable.star_on);
                            }
                            dialog2.show();
                            dialog2.getWindow().setAttributes(lp2);
                        }
                    }
                }, 800);
            }

        }
        SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
        editor.putInt(ANS, correct);
        editor.apply();
        editor.commit();

    }
    private void checkDoubledip(TextView selected) {
        String selectedAnswer = selected.getText().toString();
        if(selectedAnswer.equals(questionItems.get(currentQuestion).getCorrect())){
            selected.setBackground(getResources().getDrawable(R.drawable.option_right));
            correct++;

            //gif.setVisibility(View.VISIBLE);
            clickable(false);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                   // gif.setVisibility(View.INVISIBLE);
                    reset();
                    clickable(true);

                    if(currentQuestion<2){
                        currentQuestion++;
                        i++;
                        current.setText(i+"/");
                         setQuestionScreen(currentQuestion);
                         clickable(true);
                          reset();
                     }else {
                        //Toast.makeText(GameActivity.this, "Game Over", Toast.LENGTH_SHORT).show();
                        final Dialog dialog2 = new Dialog(GameActivity.this);
                        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                        dialog2.setContentView(R.layout.finish);
                        dialog2.setCancelable(true);

                        WindowManager.LayoutParams lp2 = new WindowManager.LayoutParams();
                        lp2.copyFrom(dialog2.getWindow().getAttributes());
                        lp2.width = WindowManager.LayoutParams.MATCH_PARENT;
                        lp2.height = WindowManager.LayoutParams.MATCH_PARENT;

                        TextView lvl = dialog2.findViewById(R.id.flevel);
                        lvl.setText("Level "+String.valueOf(level));
                        (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_off);
                        (dialog2.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_off);
                        (dialog2.findViewById(R.id.fstar3)).setBackgroundResource(R.drawable.star_off);

                        (dialog2.findViewById(R.id.fnext)).setOnClickListener(v2 -> {
                            SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
                            editor.clear();
                            editor.apply();
                            editor.commit();
                            startActivity(new Intent(GameActivity.this,QuestionActivity.class));
                        });

                        (dialog2.findViewById(R.id.fhome)).setOnClickListener(v2 -> {
                            SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
                            editor.clear();
                            editor.apply();
                            editor.commit();
                            startActivity(new Intent(GameActivity.this,MainActivity.class));
                        });
                        (dialog2.findViewById(R.id.frestart)).setOnClickListener(v2 -> {
                            SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
                            editor.clear();
                            editor.apply();
                            editor.commit();
                            startActivity(getIntent());
                        });
                        SharedPreferences preferences = getSharedPreferences(CORRECT,MODE_PRIVATE);
                        int c = preferences.getInt(ANS,0);

                        if(c==1){
                            (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                        }else if(c==2){
                            (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                            (dialog2.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_on);
                        }else if(c==3){
                            (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                            (dialog2.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_on);
                            (dialog2.findViewById(R.id.fstar3)).setBackgroundResource(R.drawable.star_on);
                        }
                        dialog2.show();
                        dialog2.getWindow().setAttributes(lp2);
                     }
                    }
            },800);

        } else {
            selected.setBackground(getResources().getDrawable(R.drawable.option_wrong));
            Toast.makeText(this, "Double Dip", Toast.LENGTH_SHORT).show();
           clickable(true);

            if(selectedAnswer.equals(questionItems.get(7).getCorrect())) {
                selected.setBackground(getResources().getDrawable(R.drawable.option_right));
                correct++;

                //gif.setVisibility(View.VISIBLE);
                clickable(false);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // gif.setVisibility(View.INVISIBLE);
                        reset();
                        clickable(true);

                        if (currentQuestion < 2) {
                            currentQuestion++;
                            i++;
                            current.setText(i + "/");
                            setQuestionScreen(currentQuestion);
                            clickable(true);
                            reset();
                        } else {
                            //Toast.makeText(GameActivity.this, "Game Over", Toast.LENGTH_SHORT).show();
                            final Dialog dialog2 = new Dialog(GameActivity.this);
                            dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                            dialog2.setContentView(R.layout.finish);
                            dialog2.setCancelable(true);

                            WindowManager.LayoutParams lp2 = new WindowManager.LayoutParams();
                            lp2.copyFrom(dialog2.getWindow().getAttributes());
                            lp2.width = WindowManager.LayoutParams.MATCH_PARENT;
                            lp2.height = WindowManager.LayoutParams.MATCH_PARENT;

                            TextView lvl = dialog2.findViewById(R.id.flevel);
                            lvl.setText("Level " + String.valueOf(level));
                            (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_off);
                            (dialog2.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_off);
                            (dialog2.findViewById(R.id.fstar3)).setBackgroundResource(R.drawable.star_off);

                            (dialog2.findViewById(R.id.fnext)).setOnClickListener(v2 -> {
                                SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
                                editor.clear();
                                editor.apply();
                                editor.commit();
                                startActivity(new Intent(GameActivity.this, QuestionActivity.class));
                            });

                            (dialog2.findViewById(R.id.fhome)).setOnClickListener(v2 -> {
                                SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
                                editor.clear();
                                editor.apply();
                                editor.commit();
                                startActivity(new Intent(GameActivity.this, MainActivity.class));
                            });
                            (dialog2.findViewById(R.id.frestart)).setOnClickListener(v2 -> {
                                SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
                                editor.clear();
                                editor.apply();
                                editor.commit();
                                startActivity(getIntent());
                            });
                            SharedPreferences preferences = getSharedPreferences(CORRECT, MODE_PRIVATE);
                            int c = preferences.getInt(ANS, 0);

                            if (c == 1) {
                                (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                            } else if (c == 2) {
                                (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                                (dialog2.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_on);
                            } else if (c == 3) {
                                (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                                (dialog2.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_on);
                                (dialog2.findViewById(R.id.fstar3)).setBackgroundResource(R.drawable.star_on);
                            }
                            dialog2.show();
                            dialog2.getWindow().setAttributes(lp2);
                        }
                    }
                }, 800);
            }

        }
        SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
        editor.putInt(ANS, correct);
        editor.apply();
        editor.commit();

    }

    private void checkRandomAnswer(TextView selected) {
        String selectedAnswer = selected.getText().toString();
        if(selectedAnswer.equals(questionItems.get(7).getCorrect())){
            selected.setBackground(getResources().getDrawable(R.drawable.option_right));
            correct++;

            //gif.setVisibility(View.VISIBLE);
            clickable(false);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                   // gif.setVisibility(View.INVISIBLE);
                    reset();
                    clickable(true);

                    if(currentQuestion<2){
                        currentQuestion++;
                        i++;
                        current.setText(i+"/");
                         setRandomQuestionScreen(currentQuestion);
                        op1.setOnClickListener(v4 -> checkAnswer(op1));
                        op2.setOnClickListener(v4 -> checkAnswer(op2));
                        op3.setOnClickListener(v4 -> checkAnswer(op3));
                        op4.setOnClickListener(v4 -> checkAnswer(op4));
                         clickable(true);
                          reset();

                     }else {
                        //Toast.makeText(GameActivity.this, "Game Over", Toast.LENGTH_SHORT).show();
                        final Dialog dialog2 = new Dialog(GameActivity.this);
                        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                        dialog2.setContentView(R.layout.finish);
                        dialog2.setCancelable(true);

                        WindowManager.LayoutParams lp2 = new WindowManager.LayoutParams();
                        lp2.copyFrom(dialog2.getWindow().getAttributes());
                        lp2.width = WindowManager.LayoutParams.MATCH_PARENT;
                        lp2.height = WindowManager.LayoutParams.MATCH_PARENT;

                        TextView lvl = dialog2.findViewById(R.id.flevel);
                        lvl.setText("Level "+String.valueOf(level));
                        (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_off);
                        (dialog2.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_off);
                        (dialog2.findViewById(R.id.fstar3)).setBackgroundResource(R.drawable.star_off);

                        (dialog2.findViewById(R.id.fnext)).setOnClickListener(v2 -> {
                            SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
                            editor.clear();
                            editor.apply();
                            editor.commit();
                            startActivity(new Intent(GameActivity.this,QuestionActivity.class));
                        });

                        (dialog2.findViewById(R.id.fhome)).setOnClickListener(v2 -> {
                            SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
                            editor.clear();
                            editor.apply();
                            editor.commit();
                            startActivity(new Intent(GameActivity.this,MainActivity.class));
                        });
                        (dialog2.findViewById(R.id.frestart)).setOnClickListener(v2 -> {
                            SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
                            editor.clear();
                            editor.apply();
                            editor.commit();
                            startActivity(getIntent());
                        });
                        SharedPreferences preferences = getSharedPreferences(CORRECT,MODE_PRIVATE);
                        int c = preferences.getInt(ANS,0);

                        if(c==1){
                            (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                        }else if(c==2){
                            (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                            (dialog2.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_on);
                        }else if(c==3){
                            (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                            (dialog2.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_on);
                            (dialog2.findViewById(R.id.fstar3)).setBackgroundResource(R.drawable.star_on);
                        }
                        dialog2.show();
                        dialog2.getWindow().setAttributes(lp2);
                     }
                    }
            },800);

        } else {
            selected.setBackground(getResources().getDrawable(R.drawable.option_wrong));
            wrong++;
            final Dialog dialog = new Dialog(GameActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
            dialog.setContentView(R.layout.wrong);
            dialog.setCancelable(true);

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            (dialog.findViewById(R.id.lifeline)).setOnClickListener(v ->{
                reset();
                clickable(true);
                        dialog.dismiss();
            });

            (dialog.findViewById(R.id.closewrong)).setOnClickListener(v -> {
                reset();
                if(currentQuestion<2){
                    currentQuestion++;
                    i++;
                    current.setText(i+"/");
                    setQuestionScreen(currentQuestion);
                    clickable(true);
                    reset();
                }else {
                    final Dialog dialog2 = new Dialog(GameActivity.this);
                    dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                    dialog2.setContentView(R.layout.finish);
                    dialog2.setCancelable(true);

                    WindowManager.LayoutParams lp2 = new WindowManager.LayoutParams();
                    lp2.copyFrom(dialog2.getWindow().getAttributes());
                    lp2.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp2.height = WindowManager.LayoutParams.MATCH_PARENT;

                    TextView lvl = dialog2.findViewById(R.id.flevel);
                    lvl.setText("Level "+String.valueOf(level));
                    (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_off);
                    (dialog2.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_off);
                    (dialog2.findViewById(R.id.fstar3)).setBackgroundResource(R.drawable.star_off);

                    (dialog2.findViewById(R.id.fnext)).setOnClickListener(v2 -> {
                        SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
                        editor.clear();
                        editor.apply();
                        editor.commit();
                        startActivity(new Intent(GameActivity.this,QuestionActivity.class));
                    });

                    (dialog2.findViewById(R.id.fhome)).setOnClickListener(v2 -> {
                        SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
                        editor.clear();
                        editor.apply();
                        editor.commit();
                        startActivity(new Intent(GameActivity.this,MainActivity.class));
                    });
                    (dialog2.findViewById(R.id.frestart)).setOnClickListener(v2 -> {
                        SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
                        editor.clear();
                        editor.apply();
                        editor.commit();
                        startActivity(getIntent());
                    });
                    SharedPreferences preferences = getSharedPreferences(CORRECT,MODE_PRIVATE);
                    int c = preferences.getInt(ANS,0);

                    Toast.makeText(this, "Correct "+c, Toast.LENGTH_SHORT).show();
                    if(c==1){
                        (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                    }else if(c==2){
                        (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                        (dialog2.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_on);
                    }else if(c==3){
                        (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                        (dialog2.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_on);
                        (dialog2.findViewById(R.id.fstar3)).setBackgroundResource(R.drawable.star_on);
                    }
                    dialog2.show();
                    dialog2.getWindow().setAttributes(lp2);

                }
                dialog.dismiss();
            });

            dialog.show();
            dialog.getWindow().setAttributes(lp);
        }
        SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
        editor.putInt(ANS, correct);
        editor.apply();
        editor.commit();

    }
    //set question
    private void  setQuestionScreen(int n){
        if(n<3) {
            hourglass.startTimer();
            question.setText(questionItems.get(n).getQuestion());
            op1.setText(questionItems.get(n).getOp1());
            op2.setText(questionItems.get(n).getOp2());
            op3.setText(questionItems.get(n).getOp3());
            op4.setText(questionItems.get(n).getOp4());
        }
        else{
            Toast.makeText(this, "Game Over", Toast.LENGTH_SHORT).show();
        }
    }

    private void  setRandomQuestionScreen(int n){

            question.setText(questionItems.get(n).getQuestion());
            op1.setText(questionItems.get(n).getOp1());
            op2.setText(questionItems.get(n).getOp2());
            op3.setText(questionItems.get(n).getOp3());
            op4.setText(questionItems.get(n).getOp4());
        }

    //load questions
    private  void loadQuestions(){
        questionItems = new ArrayList<>();
        String jsonstr = loadJSON("quizz.json");
        try{
            JSONObject jsonobject = new JSONObject(jsonstr);
            JSONArray questions = jsonobject.getJSONArray("questions");
            for(int i = 0; i<questions.length();i++){
                JSONObject question = questions.getJSONObject(i);
                String questionString = question.getString("q");
                String op1String = question.getString("op1");
                String op2String = question.getString("op2");
                String op3String = question.getString("op3");
                String op4String = question.getString("op4");
                String answerString = question.getString("ans");
                questionItems.add(new QuestionItem(
                   questionString,
                   op1String,
                   op2String,
                   op3String,
                   op4String,
                   answerString
                ));

            }
        }catch (JSONException e){
            e.printStackTrace();
        }

    }

    //load json file
    private String loadJSON(String file){
        String json="";
        try {
            InputStream is = getAssets().open(file);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json=new String(buffer,"UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    private void clickable(Boolean a) {
        op1.setClickable(a);
        op2.setClickable(a);
        op3.setClickable(a);
        op4.setClickable(a);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void reset() {
        op4.setBackground(getResources().getDrawable(R.drawable.option_background));
        op3.setBackground(getResources().getDrawable(R.drawable.option_background));
        op2.setBackground(getResources().getDrawable(R.drawable.option_background));
        op1.setBackground(getResources().getDrawable(R.drawable.option_background));
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.option_1:
                checkAnswer(op1);
                break;
            case R.id.option_2:
               checkAnswer(op2);
                break;
            case R.id.option_3:
                checkAnswer(op3);
                break;
            case R.id.option_4:
                checkAnswer(op4);
                break;

            default:
                
        }

        switch (v.getId()){
            case R.id.gamepause:
            {
                hourglass.pauseTimer();
                SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
                //Toast.makeText(GameActivity.this, "pause", Toast.LENGTH_SHORT).show();
                final Dialog dialog = new Dialog(GameActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                dialog.setContentView(R.layout.pause);
                dialog.setCancelable(true);

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;

                (dialog.findViewById(R.id.pstar1)).setBackgroundResource(R.drawable.star_off);
                (dialog.findViewById(R.id.pstar2)).setBackgroundResource(R.drawable.star_off);
                (dialog.findViewById(R.id.pstar3)).setBackgroundResource(R.drawable.star_off);

                (dialog.findViewById(R.id.resume)).setOnClickListener(v1 -> {
                    hourglass.resumeTimer();
                    dialog.dismiss();
                });
                (dialog.findViewById(R.id.restart)).setOnClickListener(v2->{
                    //SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
                    editor.clear();
                    editor.apply();
                    editor.commit();
                    startActivity(getIntent());
                    //this.recreate();
                });
                SharedPreferences preferences = getSharedPreferences(CORRECT,MODE_PRIVATE);
                int c = preferences.getInt(ANS,0);
                (dialog.findViewById(R.id.pexit)).setOnClickListener(v3->{
                    //SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
                    editor.clear();
                    editor.apply();
                    editor.commit();
                    startActivity(new Intent(GameActivity.this, MainActivity.class));

                });


                if(c==1){
                    (dialog.findViewById(R.id.pstar1)).setBackgroundResource(R.drawable.star_on);
                }else if(c==2){
                    (dialog.findViewById(R.id.pstar1)).setBackgroundResource(R.drawable.star_on);
                    (dialog.findViewById(R.id.pstar2)).setBackgroundResource(R.drawable.star_on);
                }else if(c==3){
                    (dialog.findViewById(R.id.pstar1)).setBackgroundResource(R.drawable.star_on);
                    (dialog.findViewById(R.id.pstar2)).setBackgroundResource(R.drawable.star_on);
                    (dialog.findViewById(R.id.pstar3)).setBackgroundResource(R.drawable.star_on);
                }

                dialog.show();
                dialog.getWindow().setAttributes(lp);
            }
        }
    }

}