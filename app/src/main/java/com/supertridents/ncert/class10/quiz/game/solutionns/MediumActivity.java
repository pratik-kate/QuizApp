
package com.supertridents.ncert.class10.quiz.game.solutionns;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static com.supertridents.ncert.class10.quiz.game.solutionns.MainActivity.LEVEL;
import static com.supertridents.ncert.class10.quiz.game.solutionns.MainActivity.SCORE;
import static com.supertridents.ncert.class10.quiz.game.solutionns.MainActivity.coins;

public class MediumActivity extends AppCompatActivity implements View.OnClickListener, OnUserEarnedRewardListener {

    final String CORRECT="CORRECT";
    final String ANS="ANS";
    TextView question,op1,op2,op3,op4;
    List<QuestionItem> questionItems;
    int currentQuestion = 0,level,clvl;
    int correct = 0,wrong = 0,i = 1;
    TextView current,total,time;
    CardView pause,fifty,swap,fifty2,doubleDip,doubleDip2;
    CountDownTimer cTimer = null;
    int isDoubledip = 0,isfifty=0,isswap=0;
    Boolean dip= true,d=true,f=true;
    ProgressBar mbar;
    private static final long START_TIME_IN_MILLIS = 30000;
    private static final long TIME = 17000;
    private static final long START_TIME_IN_MILLI = 5000;
    private CountDownTimer mCountDownTimer,countDownTimer;
    private long TimeLeftInMillis = START_TIME_IN_MILLI;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    Boolean doubledip=true,ffifty=true,sswap=true;
    Boolean[] stateAlive = {true};
    Boolean[] used ={true};
    Boolean[] timer ={true};

    private InterstitialAd mInterstitialAd;
    AdRequest adRequest;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medium);
        getSupportActionBar().hide();

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                //Toast.makeText(EraserActivity.this, "full ad", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
                super.onAdFailedToLoad(adError);
                mInterstitialAd.loadAd(adRequest);
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
            }
        });
        SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
        editor.commit();
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
        mbar = findViewById(R.id.mediumbar);
        mbar.setMax(15);
        mbar.setProgress(1);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(2000);
        //alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        question.startAnimation(alphaAnimation);

        SharedPreferences levelpreferences = getSharedPreferences(MainActivity.LEVEL,MODE_PRIVATE);
        clvl = levelpreferences.getInt(MainActivity.CURRENT,1);

        loadQuestions();
        Collections.shuffle(questionItems);
        setQuestionScreen(currentQuestion);
        total = findViewById(R.id.total);
        current =findViewById(R.id.current);
        total.setText(String.valueOf(questionItems.size()));
        current.setText(i+"/");


        TranslateAnimation animop1 = new TranslateAnimation(-1000,0,0,0);
        animop1.setDuration(1000);
        op1.startAnimation(animop1);

        TranslateAnimation animop2 = new TranslateAnimation(1000,0,0,0);
        animop2.setDuration(1000);
        op2.startAnimation(animop2);

        TranslateAnimation animop3 = new TranslateAnimation(-1000,0,0,0);
        animop3.setDuration(1000);
        op3.startAnimation(animop3);

        TranslateAnimation animop4 = new TranslateAnimation(1000,0,0,0);
        animop4.setDuration(1000);
        op4.startAnimation(animop4);

        TranslateAnimation btnanim = new TranslateAnimation(0,0,1000,0);
        btnanim.setDuration(1000);
        swap.setAnimation(btnanim);
        doubleDip.setAnimation(btnanim);
        doubleDip2.setAnimation(btnanim);
        fifty.setAnimation(btnanim);
        fifty2.setAnimation(btnanim);


        op1.setOnClickListener(this);
        op2.setOnClickListener(this);
        op3.setOnClickListener(this);
        op4.setOnClickListener(this);
        pause.setOnClickListener(this);
//        Intent intent = getIntent();
//        level = intent.getIntExtra("level",0);
        TextView lvl = findViewById(R.id.lvltxt);
        lvl.setText("Normal");

        SharedPreferences preferences = getSharedPreferences(LEVEL,MODE_PRIVATE);
        final int[] coin = {preferences.getInt(String.valueOf(coins), 1)};
        TextView cointext = findViewById(R.id.coins);
        cointext.setText(String.valueOf(coin[0]));

        //Lifelines
        fifty.setOnClickListener(v -> {

            pauseTimer();
            if(ffifty){
                final Dialog dialog2 = new Dialog(MediumActivity.this);
                dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                dialog2.setContentView(R.layout.fifty);
                dialog2.setCancelable(true);

                WindowManager.LayoutParams lp2 = new WindowManager.LayoutParams();
                lp2.copyFrom(dialog2.getWindow().getAttributes());
                lp2.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp2.height = WindowManager.LayoutParams.WRAP_CONTENT;

                (dialog2.findViewById(R.id.fyes)).setOnClickListener(v1 -> {
                    ffifty=false;
                    isfifty = 1;
                    startTimer(mTimeLeftInMillis);
                    String ans = questionItems.get(currentQuestion).getCorrect();
                    if (coin[0] >= 100) {

                        f = false;
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
                        coin[0] = coin[0] - 100;
                        SharedPreferences.Editor coinseditor = getSharedPreferences(MainActivity.LEVEL, MODE_PRIVATE).edit();
                        coinseditor.putInt(String.valueOf(coins), coin[0]);
                        coinseditor.apply();
                        coinseditor.commit();
                        cointext.setText(String.valueOf(coin[0]));
                        // fifty.setClickable(false);
                        fifty.setForeground(getResources().getDrawable(R.drawable.ic_close));
                    } else {
                        Toast.makeText(this, "No Coins", Toast.LENGTH_SHORT).show();
                    }

                    dialog2.dismiss();
                });
                (dialog2.findViewById(R.id.fno)).setOnClickListener(v1 -> {
                    startTimer(mTimeLeftInMillis);
                    dialog2.dismiss();

                });

                dialog2.show();
                dialog2.getWindow().setAttributes(lp2);
            }else {
                final Dialog dialog2 = new Dialog(MediumActivity.this);
                dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                dialog2.setContentView(R.layout.alert);
                dialog2.setCancelable(true);
                WindowManager.LayoutParams lp2 = new WindowManager.LayoutParams();
                lp2.copyFrom(dialog2.getWindow().getAttributes());
                lp2.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp2.height = WindowManager.LayoutParams.WRAP_CONTENT;
                TextView text = dialog2.findViewById(R.id.atext);
                text.setText("Watch Ad to use extra lifeline?");
                (dialog2.findViewById(R.id.yes)).setOnClickListener(v1 -> {
                    if(Constants.rewardedAd.isLoaded()){
                        Constants.rewardedAd.show(MediumActivity.this, new RewardedAdCallback() {
                            @Override
                            public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                startTimer(mTimeLeftInMillis);
                                String ans = questionItems.get(currentQuestion).getCorrect();
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
                                //Toast.makeText(MediumActivity.this, "done", Toast.LENGTH_SHORT).show();
                                Constants.loadRewardedAd(MediumActivity.this);
                            }
                        });
                    }
                    else{
//                        startTimer(mTimeLeftInMillis);
//                        Toast.makeText(MediumActivity.this, "Please Wait, Ad is loading", Toast.LENGTH_SHORT).show();
                    }
                    dialog2.dismiss();
                });
                (dialog2.findViewById(R.id.no)).setOnClickListener(v1 -> {
                    startTimer(mTimeLeftInMillis);
                    dialog2.dismiss();

                });
                dialog2.show();
                dialog2.getWindow().setAttributes(lp2);

            }
        });

        doubleDip.setOnClickListener(v -> {
            pauseTimer();
            if(doubledip) {
                final Boolean[] a = {true};
                final Dialog dialog2 = new Dialog(MediumActivity.this);
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

                    startTimer(mTimeLeftInMillis);
                    if (coin[0] >= 100) {

                        doubledip=false;
                        d = false;
                        if (dip) {
                            op1.setOnClickListener(v4 -> checkDoubledip(op1));
                            op2.setOnClickListener(v4 -> checkDoubledip(op2));
                            op3.setOnClickListener(v4 -> checkDoubledip(op3));
                            op4.setOnClickListener(v4 -> checkDoubledip(op4));
                        } else {
                            op1.setOnClickListener(v4 -> {
                                checkAnswer(op1);
                            });
                            op2.setOnClickListener(v4 -> {
                                checkAnswer(op2);
                            });
                            op3.setOnClickListener(v4 -> {
                                checkAnswer(op3);
                            });
                            op4.setOnClickListener(v4 -> {
                                checkAnswer(op4);
                            });
                        }
                        dip = false;
                        coin[0] = coin[0] - 100;
                        SharedPreferences.Editor coinseditor = getSharedPreferences(MainActivity.LEVEL, MODE_PRIVATE).edit();
                        coinseditor.putInt(String.valueOf(coins), coin[0]);
                        coinseditor.apply();
                        coinseditor.commit();
                        cointext.setText(String.valueOf(coin[0]));
                        //doubleDip.setClickable(false);
                        doubleDip.setForeground(getResources().getDrawable(R.drawable.ic_close));
                    } else {
                        Toast.makeText(this, "No Coins", Toast.LENGTH_SHORT).show();
                    }

                    dialog2.dismiss();
                });
                (dialog2.findViewById(R.id.dno)).setOnClickListener(v1 -> {
                    startTimer(mTimeLeftInMillis);
                    dialog2.dismiss();
                });

                dialog2.show();
                dialog2.getWindow().setAttributes(lp2);
            }else {
                final Dialog dialog2 = new Dialog(MediumActivity.this);
                dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                dialog2.setContentView(R.layout.alert);
                dialog2.setCancelable(true);
                WindowManager.LayoutParams lp2 = new WindowManager.LayoutParams();
                lp2.copyFrom(dialog2.getWindow().getAttributes());
                lp2.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp2.height = WindowManager.LayoutParams.WRAP_CONTENT;
                TextView text = dialog2.findViewById(R.id.atext);
                text.setText("Watch Ad to use extra lifeline?");
                (dialog2.findViewById(R.id.yes)).setOnClickListener(v1 -> {
                    if(Constants.rewardedAd.isLoaded()){
                        Constants.rewardedAd.show(MediumActivity.this, new RewardedAdCallback() {
                            @Override
                            public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                startTimer(mTimeLeftInMillis);

                                op1.setOnClickListener(v4 -> checkDoubledip(op1));
                                op2.setOnClickListener(v4 -> checkDoubledip(op2));
                                op3.setOnClickListener(v4 -> checkDoubledip(op3));
                                op4.setOnClickListener(v4 -> checkDoubledip(op4));
                                //Toast.makeText(MediumActivity.this, "done", Toast.LENGTH_SHORT).show();
                                Constants.loadRewardedAd(MediumActivity.this);
                            }
                        });
                    }
                    else{
//                        startTimer(mTimeLeftInMillis);
//                        Toast.makeText(MediumActivity.this, "Please Wait, Ad is loading", Toast.LENGTH_SHORT).show();
                    }
                    dialog2.dismiss();
                });
                (dialog2.findViewById(R.id.no)).setOnClickListener(v1 -> {
                    startTimer(mTimeLeftInMillis);
                    dialog2.dismiss();

                });
                dialog2.show();
                dialog2.getWindow().setAttributes(lp2);

            }
        });

        swap.setOnClickListener(v -> {
            pauseTimer();
            if(sswap) {
                final Dialog dialog2 = new Dialog(MediumActivity.this);
                dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                dialog2.setContentView(R.layout.swap);
                dialog2.setCancelable(true);

                WindowManager.LayoutParams lp2 = new WindowManager.LayoutParams();
                lp2.copyFrom(dialog2.getWindow().getAttributes());
                lp2.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp2.height = WindowManager.LayoutParams.WRAP_CONTENT;

                (dialog2.findViewById(R.id.syes)).setOnClickListener(v1 -> {

                    sswap=false;
                    startTimer(mTimeLeftInMillis);
                    if (coin[0] >= 100) {

                        setRandomQuestionScreen(7);
                        op1.setOnClickListener(v4 -> checkRandomAnswer(op1));
                        op2.setOnClickListener(v4 -> checkRandomAnswer(op2));
                        op3.setOnClickListener(v4 -> checkRandomAnswer(op3));
                        op4.setOnClickListener(v4 -> checkRandomAnswer(op4));
                        coin[0] = coin[0] - 100;
                        SharedPreferences.Editor coinseditor = getSharedPreferences(MainActivity.LEVEL, MODE_PRIVATE).edit();
                        coinseditor.putInt(String.valueOf(coins), coin[0]);
                        coinseditor.apply();
                        coinseditor.commit();
                        cointext.setText(String.valueOf(coin[0]));
                        //swap.setClickable(false);
                        swap.setForeground(getResources().getDrawable(R.drawable.ic_close));
                        if (f) {
                            fifty.setVisibility(View.INVISIBLE);
                            fifty2.setVisibility(View.VISIBLE);
                            fifty2.setOnClickListener(v2 -> {

                                final Dialog dialogf = new Dialog(MediumActivity.this);
                                dialogf.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                                dialogf.setContentView(R.layout.fifty);
                                dialogf.setCancelable(true);

                                WindowManager.LayoutParams lpf = new WindowManager.LayoutParams();
                                lpf.copyFrom(dialogf.getWindow().getAttributes());
                                lpf.width = WindowManager.LayoutParams.MATCH_PARENT;
                                lpf.height = WindowManager.LayoutParams.WRAP_CONTENT;

                                (dialogf.findViewById(R.id.fyes)).setOnClickListener(v3 -> {

                                    String ans = questionItems.get(7).getCorrect();
                                    if (coin[0] >= 100) {
                                        fifty2.setVisibility(View.INVISIBLE);
                                        fifty.setVisibility(View.VISIBLE);
                                        fifty.setForeground(getResources().getDrawable(R.drawable.ic_close));
                                        fifty.setClickable(false);
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
                                        op1.setOnClickListener(v4 -> checkRandomAnswer(op1));
                                        op2.setOnClickListener(v4 -> checkRandomAnswer(op2));
                                        op3.setOnClickListener(v4 -> checkRandomAnswer(op3));
                                        op4.setOnClickListener(v4 -> checkRandomAnswer(op4));
                                        coin[0] = coin[0] - 100;
                                        SharedPreferences.Editor coinsseditor = getSharedPreferences(MainActivity.LEVEL, MODE_PRIVATE).edit();
                                        coinsseditor.putInt(String.valueOf(coins), coin[0]);
                                        coinsseditor.apply();
                                        coinsseditor.commit();
                                        cointext.setText(String.valueOf(coin[0]));
                                        fifty2.setClickable(false);
                                        fifty2.setForeground(getResources().getDrawable(R.drawable.ic_close));
                                    } else {
                                        Toast.makeText(this, "No Coins", Toast.LENGTH_SHORT).show();
                                    }

                                    dialogf.dismiss();
                                });
                                (dialogf.findViewById(R.id.fno)).setOnClickListener(v3 -> {
                                    dialogf.dismiss();
                                });

                                dialogf.show();
                                dialogf.getWindow().setAttributes(lp2);
                            });
                        }

                        if (d) {
                            doubleDip.setVisibility(View.INVISIBLE);
                            doubleDip2.setVisibility(View.VISIBLE);
                            doubleDip2.setOnClickListener(v2 -> {

                                final Dialog ddialog2 = new Dialog(MediumActivity.this);
                                ddialog2.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                                ddialog2.setContentView(R.layout.doubledip);
                                ddialog2.setCancelable(true);

                                WindowManager.LayoutParams dlp2 = new WindowManager.LayoutParams();
                                dlp2.copyFrom(ddialog2.getWindow().getAttributes());
                                dlp2.width = WindowManager.LayoutParams.MATCH_PARENT;
                                dlp2.height = WindowManager.LayoutParams.WRAP_CONTENT;

                                (ddialog2.findViewById(R.id.dyes)).setOnClickListener(v3 -> {
                                    String ans = questionItems.get(7).getCorrect();
                                    if (coin[0] >= 100) {

                                        doubleDip2.setVisibility(View.INVISIBLE);
                                        doubleDip.setVisibility(View.VISIBLE);
                                        doubleDip.setForeground(getResources().getDrawable(R.drawable.ic_close));
                                        doubleDip.setClickable(false);
                                        op1.setOnClickListener(v4 -> checkRandomDoubledip(op1));
                                        op2.setOnClickListener(v4 -> checkRandomDoubledip(op2));
                                        op3.setOnClickListener(v4 -> checkRandomDoubledip(op3));
                                        op4.setOnClickListener(v4 -> checkRandomDoubledip(op4));
                                        //isDoubledip = 2;
                                        coin[0] = coin[0] - 100;
                                        SharedPreferences.Editor dcoinseditor = getSharedPreferences(MainActivity.LEVEL, MODE_PRIVATE).edit();
                                        dcoinseditor.putInt(String.valueOf(coins), coin[0]);
                                        dcoinseditor.apply();
                                        dcoinseditor.commit();
                                        cointext.setText(String.valueOf(coin[0]));
                                        doubleDip2.setClickable(false);
                                        doubleDip2.setForeground(getResources().getDrawable(R.drawable.ic_close));
                                    } else {
                                        Toast.makeText(this, "No Coins", Toast.LENGTH_SHORT).show();
                                    }

                                    op1.setOnClickListener(v4 -> {
                                        checkAnswer(op1);
                                    });
                                    op2.setOnClickListener(v4 -> {
                                        checkAnswer(op2);
                                    });
                                    op3.setOnClickListener(v4 -> {
                                        checkAnswer(op3);
                                    });
                                    op4.setOnClickListener(v4 -> {
                                        checkAnswer(op4);
                                    });
                                    ddialog2.dismiss();
                                });
                                (ddialog2.findViewById(R.id.dno)).setOnClickListener(v3 -> {
                                    ddialog2.dismiss();
                                });

                                ddialog2.show();
                                ddialog2.getWindow().setAttributes(lp2);
                            });
                        }

                    } else {
                        Toast.makeText(this, "No Coins", Toast.LENGTH_SHORT).show();
                    }
                    dialog2.dismiss();
                });
                (dialog2.findViewById(R.id.sno)).setOnClickListener(v1 -> {
                    startTimer(mTimeLeftInMillis);
                    dialog2.dismiss();
                });

                dialog2.show();
                dialog2.getWindow().setAttributes(lp2);
            }
            else {
                final Dialog dialog2 = new Dialog(MediumActivity.this);
                dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                dialog2.setContentView(R.layout.alert);
                dialog2.setCancelable(true);
                WindowManager.LayoutParams lp2 = new WindowManager.LayoutParams();
                lp2.copyFrom(dialog2.getWindow().getAttributes());
                lp2.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp2.height = WindowManager.LayoutParams.WRAP_CONTENT;
                TextView text = dialog2.findViewById(R.id.atext);
                text.setText("Watch Ad to use extra lifeline?");
                (dialog2.findViewById(R.id.yes)).setOnClickListener(v1 -> {
                    if(Constants.rewardedAd.isLoaded()){
                        Constants.rewardedAd.show(MediumActivity.this, new RewardedAdCallback() {
                            @Override
                            public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                startTimer(mTimeLeftInMillis);
                                setRandomQuestionScreen(7);
                                op1.setOnClickListener(v4 -> checkRandomAnswer(op1));
                                op2.setOnClickListener(v4 -> checkRandomAnswer(op2));
                                op3.setOnClickListener(v4 -> checkRandomAnswer(op3));
                                op4.setOnClickListener(v4 -> checkRandomAnswer(op4));
                                //Toast.makeText(MediumActivity.this, "done", Toast.LENGTH_SHORT).show();
                                Constants.loadRewardedAd(MediumActivity.this);
                                startTimer(mTimeLeftInMillis);
                            }
                        });
                    }
                    else{
//                        startTimer(mTimeLeftInMillis);
//                        Toast.makeText(MediumActivity.this, "Please Wait, Ad is loading", Toast.LENGTH_SHORT).show();
                    }
                    dialog2.dismiss();
                });
                (dialog2.findViewById(R.id.no)).setOnClickListener(v1 -> {
                    startTimer(mTimeLeftInMillis);
                    dialog2.dismiss();

                });
                dialog2.show();
                dialog2.getWindow().setAttributes(lp2);


            }
        });

    }

    @Override
    public void onBackPressed() {
        pauseTimer();
        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("Do you want to quit the game?")
                .setPositiveButton("Yes", (arg0, arg1) -> {
                    super.onBackPressed();
                    finish();
                })
                .setNegativeButton("No", (arg0, arg1) -> {arg0.dismiss();startTimer(mTimeLeftInMillis);})
                .show();
    }
    @Override
    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
        Toast.makeText(this, "Rewarded", Toast.LENGTH_SHORT).show();
    }

    private void showinterstitial(){
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }
    }
    private void showInterstitial() {

        try{
        Constants.rewardedInterstitialAd.show(MediumActivity.this,rewardItem -> {
            SharedPreferences preferences = getSharedPreferences(MainActivity.LEVEL,MODE_PRIVATE);
            final int[] credits = {preferences.getInt(String.valueOf(MainActivity.coins), 0)};
            //Toast.makeText(this, "Reward", Toast.LENGTH_SHORT).show();
            credits[0] = credits[0] + 500;
            SharedPreferences.Editor editor = getSharedPreferences(MainActivity.LEVEL,MODE_PRIVATE).edit();
            editor.putInt(String.valueOf(MainActivity.coins),credits[0]);
            editor.apply();
            editor.commit();
            Toast.makeText(MediumActivity.this, "500 Coins Won", Toast.LENGTH_SHORT).show();
            Constants.loadAd(this);
        });
        }catch (Exception e){
            //Toast.makeText(this, "Please Wait Ad is loading", Toast.LENGTH_SHORT).show();
        }

    }
    //timer
    private void startTimer(long ms) {
        mCountDownTimer = new CountDownTimer(ms, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }
            @Override
            public void onFinish() {
                mTimerRunning = false;
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(timer[0]) {
                            final Dialog dialog2 = new Dialog(MediumActivity.this);
                            dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                            dialog2.setContentView(R.layout.alert);
                            dialog2.setCancelable(true);
                            WindowManager.LayoutParams lp2 = new WindowManager.LayoutParams();
                            lp2.copyFrom(dialog2.getWindow().getAttributes());
                            lp2.width = WindowManager.LayoutParams.MATCH_PARENT;
                            lp2.height = WindowManager.LayoutParams.WRAP_CONTENT;
                            TextView text = dialog2.findViewById(R.id.atext);
                            text.setText("Watch Ad To Get 15s More");
                            (dialog2.findViewById(R.id.yes)).setOnClickListener(v1 -> {
                                timer[0]=false;
                                if (Constants.rewardedAd.isLoaded()) {
                                    Constants.rewardedAd.show(MediumActivity.this, new RewardedAdCallback() {
                                        @Override
                                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                            startTimer(TIME);
                                            //Toast.makeText(MediumActivity.this, "done", Toast.LENGTH_SHORT).show();
                                            Constants.loadRewardedAd(MediumActivity.this);
                                        }
                                    });
                                } else {

                                    //startTimer(mTimeLeftInMillis);
                                    //Toast.makeText(MediumActivity.this, "Please Wait, Ad is loading", Toast.LENGTH_SHORT).show();
                                }
                                dialog2.dismiss();
                            });
                            (dialog2.findViewById(R.id.no)).setOnClickListener(v1 -> {
                                //startTimer(mTimeLeftInMillis);
                                if (currentQuestion == 14) {
                                    final Dialog dialog = new Dialog(MediumActivity.this, R.style.PauseDialog);
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                                    dialog.setContentView(R.layout.finish);
                                    dialog.setCancelable(true);
                                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                    lp.copyFrom(dialog.getWindow().getAttributes());
                                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                                    lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                                    TextView lvl = dialog.findViewById(R.id.flevel);
                                    lvl.setText("Easy");
                                    (dialog.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_off);
                                    (dialog.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_off);
                                    (dialog.findViewById(R.id.fstar3)).setBackgroundResource(R.drawable.star_off);
                                    (dialog.findViewById(R.id.fnext)).setOnClickListener(v2 -> {
                                        SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
                                        editor.clear();
                                        editor.apply();
                                        editor.commit();
                                        startActivity(new Intent(MediumActivity.this, PlayActivity.class));
                                    });
                                    (dialog.findViewById(R.id.fhome)).setOnClickListener(v2 -> {
                                        SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
                                        editor.clear();
                                        editor.apply();
                                        editor.commit();
                                        startActivity(new Intent(MediumActivity.this, MainActivity.class));
                                    });
                                    (dialog.findViewById(R.id.frestart)).setOnClickListener(v2 -> {
                                        SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
                                        editor.clear();
                                        editor.apply();
                                        editor.commit();
                                        startActivity(getIntent());
                                    });
                                    SharedPreferences preferences = getSharedPreferences(CORRECT, MODE_PRIVATE);
                                    int c = preferences.getInt(ANS, 0);

                                    //gamefinish
                                    TextView scoretxt = (dialog.findViewById(R.id.scoretext));
                                    if (c < 6) {
                                        (dialog.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                                        scoretxt.setText(String.valueOf(c * 100));
                                    } else if (c <= 14) {
                                        (dialog.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                                        (dialog.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_on);
                                        scoretxt.setText(String.valueOf(c * 100));
                                        //edit.putInt(MainActivity.CURRENT,1);

                                    } else if (c == 15) {
                                        (dialog.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                                        (dialog.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_on);
                                        (dialog.findViewById(R.id.fstar3)).setBackgroundResource(R.drawable.star_on);
                                        scoretxt.setText(String.valueOf(c * 100));
                                    }
                                    TranslateAnimation anim = new TranslateAnimation(0, 0, 0, 0);
                                    anim.setDuration(1000);

                                    dialog.show();
                                    dialog.getWindow().setAttributes(lp);
                                    showInterstitial();
                                } else {
                                    //startTimer(mTimeLeftInMillis);
                                    currentQuestion++;
                                    i++;
                                    current.setText(i + "/");
                                    setQuestionScreen(currentQuestion);
                                    clickable(true);
                                    reset();
                                }
                                dialog2.dismiss();

                            });
                            dialog2.show();
                            dialog2.getWindow().setAttributes(lp2);
                        }else{
                            if (currentQuestion == 14) {

                                final Dialog dialog = new Dialog(MediumActivity.this, R.style.PauseDialog);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                                dialog.setContentView(R.layout.finish);
                                dialog.setCancelable(true);
                                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                lp.copyFrom(dialog.getWindow().getAttributes());
                                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                                TextView lvl = dialog.findViewById(R.id.flevel);
                                lvl.setText("Easy");
                                (dialog.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_off);
                                (dialog.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_off);
                                (dialog.findViewById(R.id.fstar3)).setBackgroundResource(R.drawable.star_off);
                                (dialog.findViewById(R.id.fnext)).setOnClickListener(v2 -> {
                                    SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
                                    editor.clear();
                                    editor.apply();
                                    editor.commit();
                                    startActivity(new Intent(MediumActivity.this, PlayActivity.class));
                                });
                                (dialog.findViewById(R.id.fhome)).setOnClickListener(v2 -> {
                                    SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
                                    editor.clear();
                                    editor.apply();
                                    editor.commit();
                                    startActivity(new Intent(MediumActivity.this, MainActivity.class));
                                });
                                (dialog.findViewById(R.id.frestart)).setOnClickListener(v2 -> {
                                    SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
                                    editor.clear();
                                    editor.apply();
                                    editor.commit();
                                    startActivity(getIntent());
                                });
                                SharedPreferences preferences = getSharedPreferences(CORRECT, MODE_PRIVATE);
                                int c = preferences.getInt(ANS, 0);

                                //gamefinish
                                TextView scoretxt = (dialog.findViewById(R.id.scoretext));
                                if (c < 6) {
                                    (dialog.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                                    scoretxt.setText(String.valueOf(c * 100));
                                } else if (c <= 14) {
                                    (dialog.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                                    (dialog.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_on);
                                    scoretxt.setText(String.valueOf(c * 100));
                                    //edit.putInt(MainActivity.CURRENT,1);

                                } else if (c == 15) {
                                    (dialog.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                                    (dialog.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_on);
                                    (dialog.findViewById(R.id.fstar3)).setBackgroundResource(R.drawable.star_on);
                                    scoretxt.setText(String.valueOf(c * 100));
                                }
                                TranslateAnimation anim = new TranslateAnimation(0, 0, 0, 0);
                                anim.setDuration(1000);

                                dialog.show();
                                dialog.getWindow().setAttributes(lp);
                                showInterstitial();
                            } else {
                                startTimer(mTimeLeftInMillis);
                                currentQuestion++;
                                i++;
                                current.setText(i + "/");
                                setQuestionScreen(currentQuestion);
                                clickable(true);
                                reset();
                            }
                        }
                    }
                },100);
            }
        }.start();
        mTimerRunning = true;
//        mButtonStartPause.setText("pause");
//        mButtonReset.setVisibility(View.INVISIBLE);
    }
    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
    }
    private void resetTimer() {
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();
    }
    private void updateCountDownText() {
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d", seconds);
        time.setText(timeLeftFormatted);
    }



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

                    if(currentQuestion<questionItems.size()-1){
                        currentQuestion++;
                        i++;
                        current.setText(i+"/");
                        setQuestionScreen(currentQuestion);
                        clickable(true);
                        reset();
                    }else {

                        final Dialog dialog = new Dialog(MediumActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                        dialog.setContentView(R.layout.alert);
                        dialog.setCancelable(true);
                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        lp.copyFrom(dialog.getWindow().getAttributes());
                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                        TextView text = dialog.findViewById(R.id.atext);
                        text.setText("Congrats! You Completed The Level. watch this video to get 500 extra coins");
                        TextView a = dialog.findViewById(R.id.yes);
                        TextView b = dialog.findViewById(R.id.no);

                        countDownTimer = new CountDownTimer(TimeLeftInMillis, 5000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                TimeLeftInMillis=millisUntilFinished;
                                int seconds = (int) (TimeLeftInMillis / 1000) % 60;
                                String timeLeftFormatted = String.format(Locale.getDefault(), "%02d", seconds);
                                TextView a = dialog.findViewById(R.id.yes);
                                a.setText("Ad will start in 5sec");

                                //updateCountDown();
                            }
                            @Override
                            public void onFinish() {
                                mTimerRunning = false;
                                if(stateAlive[0]){showInterstitial();}
                                else{
                                    Toast.makeText(MediumActivity.this, "You Won", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }.start();
                        (dialog.findViewById(R.id.no)).setOnClickListener(v1 -> {
                            stateAlive[0] =false;

                            //finish();
                            startTimer(mTimeLeftInMillis);
                            final Dialog dialog2 = new Dialog(MediumActivity.this,R.style.PauseDialog);
                            dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                            dialog2.setContentView(R.layout.finish);
                            dialog2.setCancelable(true);
                            WindowManager.LayoutParams lp2 = new WindowManager.LayoutParams();
                            lp2.copyFrom(dialog2.getWindow().getAttributes());
                            lp2.width = WindowManager.LayoutParams.MATCH_PARENT;
                            lp2.height = WindowManager.LayoutParams.MATCH_PARENT;
                            TextView lvl = dialog2.findViewById(R.id.flevel);
                            lvl.setText("Easy");
                            (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_off);
                            (dialog2.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_off);
                            (dialog2.findViewById(R.id.fstar3)).setBackgroundResource(R.drawable.star_off);
                            SharedPreferences.Editor clvleditor = getSharedPreferences(MainActivity.LEVEL,MODE_PRIVATE).edit();
                            clvleditor.putInt(MainActivity.CURRENT,clvl);
                            clvleditor.apply();
                            clvleditor.commit();
                            (dialog2.findViewById(R.id.fnext)).setOnClickListener(v2 -> {
                                SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
                                editor.clear();
                                editor.apply();
                                editor.commit();
                                startActivity(new Intent(MediumActivity.this,PlayActivity.class));
                            });
                            (dialog2.findViewById(R.id.fhome)).setOnClickListener(v2 -> {
                                SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
                                editor.clear();
                                editor.apply();
                                editor.commit();
                                startActivity(new Intent(MediumActivity.this,MainActivity.class));
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
                            SharedPreferences.Editor edit = getSharedPreferences(LEVEL,MODE_PRIVATE).edit();
                            int score = getSharedPreferences(LEVEL,MODE_PRIVATE).getInt(SCORE,0);
                            TextView scoretxt = (dialog2.findViewById(R.id.scoretext));
                            if(c<6){
                                (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                                score = score+(c*100);
                                edit.putInt(MainActivity.SCORE,score);
                                scoretxt.setText(String.valueOf(c*100));
                            }else if(c<=14){
                                (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                                (dialog2.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_on);
                                score = score+(c*100);
                                edit.putInt(MainActivity.SCORE,score);
                                scoretxt.setText(String.valueOf(c*100));
                                edit.putInt(MainActivity.CURRENT,1);
                            }else if(c==15){
                                (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                                (dialog2.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_on);
                                (dialog2.findViewById(R.id.fstar3)).setBackgroundResource(R.drawable.star_on);
                                score = score+(c*100);
                                edit.putInt(MainActivity.SCORE,score);
                                scoretxt.setText(String.valueOf(c*100));
                            }
                            edit.apply();
                            edit.commit();
                            dialog2.show();
                            dialog2.getWindow().setAttributes(lp2);
                            showinterstitial();
                            dialog.dismiss();
                        });
                        dialog.show();
                        dialog.getWindow().setAttributes(lp);
                    }
                }
            },800);

        } else {
            selected.setBackground(getResources().getDrawable(R.drawable.option_wrong));
            wrong++;
            final boolean[] stateAlive = {true};
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    reset();
                    pauseTimer();
                    if(used[0]) {
                        used[0]=false;
                        final Dialog dialog = new Dialog(MediumActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                        dialog.setContentView(R.layout.alert);
                        dialog.setCancelable(true);
                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        lp.copyFrom(dialog.getWindow().getAttributes());
                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                        TextView text = dialog.findViewById(R.id.atext);
                        text.setText("Watch This video and get chance");
                        TextView a = dialog.findViewById(R.id.yes);
                        countDownTimer = new CountDownTimer(TimeLeftInMillis, 5000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                TimeLeftInMillis = millisUntilFinished;
                                int seconds = (int) (TimeLeftInMillis / 1000) % 60;
                                String timeLeftFormatted = String.format(Locale.getDefault(), "%02d", seconds);
                                TextView a = dialog.findViewById(R.id.yes);
                                a.setText("Ad will start in 5sec");


                                //updateCountDown();
                            }

                            @Override
                            public void onFinish() {
                                mTimerRunning = false;

                                if (Constants.rewardedAd.isLoaded() && stateAlive[0]) {
                                    Constants.rewardedAd.show(MediumActivity.this, new RewardedAdCallback() {
                                        @Override
                                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                            startTimer(mTimeLeftInMillis);
                                            //Toast.makeText(MediumActivity.this, "done", Toast.LENGTH_SHORT).show();
                                            Constants.loadRewardedAd(MediumActivity.this);
                                            dialog.dismiss();
                                        }
                                    });
                                } else {
//                                    startTimer(mTimeLeftInMillis);
//                                    Toast.makeText(MediumActivity.this, "Please Wait, Ad is loading", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }.start();
                        (dialog.findViewById(R.id.no)).setOnClickListener(v1 -> {
                            stateAlive[0] = false;

                            //finish();
                            //startTimer(mTimeLeftInMillis);
                            final Dialog dialog2 = new Dialog(MediumActivity.this, R.style.PauseDialog);
                            dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                            dialog2.setContentView(R.layout.finish);
                            dialog2.setCancelable(true);
                            WindowManager.LayoutParams lp2 = new WindowManager.LayoutParams();
                            lp2.copyFrom(dialog2.getWindow().getAttributes());
                            lp2.width = WindowManager.LayoutParams.MATCH_PARENT;
                            lp2.height = WindowManager.LayoutParams.MATCH_PARENT;
                            TextView lvl = dialog2.findViewById(R.id.flevel);
                            lvl.setText("Easy");
                            (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_off);
                            (dialog2.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_off);
                            (dialog2.findViewById(R.id.fstar3)).setBackgroundResource(R.drawable.star_off);
                            SharedPreferences.Editor clvleditor = getSharedPreferences(MainActivity.LEVEL, MODE_PRIVATE).edit();
                            clvleditor.putInt(MainActivity.CURRENT, clvl);
                            clvleditor.apply();
                            clvleditor.commit();
                            (dialog2.findViewById(R.id.fnext)).setOnClickListener(v2 -> {
                                SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
                                editor.clear();
                                editor.apply();
                                editor.commit();
                                startActivity(new Intent(MediumActivity.this, PlayActivity.class));
                            });
                            (dialog2.findViewById(R.id.fhome)).setOnClickListener(v2 -> {
                                SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
                                editor.clear();
                                editor.apply();
                                editor.commit();
                                startActivity(new Intent(MediumActivity.this, MainActivity.class));
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
                            SharedPreferences.Editor edit = getSharedPreferences(LEVEL, MODE_PRIVATE).edit();
                            int score = getSharedPreferences(LEVEL, MODE_PRIVATE).getInt(SCORE, 0);
                            TextView scoretxt = (dialog2.findViewById(R.id.scoretext));
                            if (c < 6) {
                                (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                                score = score + (c * 100);
                                edit.putInt(MainActivity.SCORE, score);
                                scoretxt.setText(String.valueOf(c * 100));
                            } else if (c <= 14) {
                                (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                                (dialog2.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_on);
                                score = score + (c * 100);
                                edit.putInt(MainActivity.SCORE, score);
                                scoretxt.setText(String.valueOf(c * 100));
                                edit.putInt(MainActivity.CURRENT, 1);
                            } else if (c == 15) {
                                (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                                (dialog2.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_on);
                                (dialog2.findViewById(R.id.fstar3)).setBackgroundResource(R.drawable.star_on);
                                score = score + (c * 100);
                                edit.putInt(MainActivity.SCORE, score);
                                scoretxt.setText(String.valueOf(c * 100));
                            }
                            edit.apply();
                            edit.commit();
                            dialog2.show();
                            dialog2.getWindow().setAttributes(lp2);
                            showinterstitial();
                            dialog.dismiss();

                        });
                        dialog.show();
                        dialog.getWindow().setAttributes(lp);
                    }else{
                        stateAlive[0] = false;

                        //finish();
                        //startTimer(mTimeLeftInMillis);
                        final Dialog dialog2 = new Dialog(MediumActivity.this, R.style.PauseDialog);
                        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                        dialog2.setContentView(R.layout.finish);
                        dialog2.setCancelable(true);
                        WindowManager.LayoutParams lp2 = new WindowManager.LayoutParams();
                        lp2.copyFrom(dialog2.getWindow().getAttributes());
                        lp2.width = WindowManager.LayoutParams.MATCH_PARENT;
                        lp2.height = WindowManager.LayoutParams.MATCH_PARENT;
                        TextView lvl = dialog2.findViewById(R.id.flevel);
                        lvl.setText("Easy");
                        (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_off);
                        (dialog2.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_off);
                        (dialog2.findViewById(R.id.fstar3)).setBackgroundResource(R.drawable.star_off);
                        SharedPreferences.Editor clvleditor = getSharedPreferences(MainActivity.LEVEL, MODE_PRIVATE).edit();
                        clvleditor.putInt(MainActivity.CURRENT, clvl);
                        clvleditor.apply();
                        clvleditor.commit();
                        (dialog2.findViewById(R.id.fnext)).setOnClickListener(v2 -> {
                            SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
                            editor.clear();
                            editor.apply();
                            editor.commit();
                            startActivity(new Intent(MediumActivity.this, PlayActivity.class));
                        });
                        (dialog2.findViewById(R.id.fhome)).setOnClickListener(v2 -> {
                            SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
                            editor.clear();
                            editor.apply();
                            editor.commit();
                            startActivity(new Intent(MediumActivity.this, MainActivity.class));
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
                        SharedPreferences.Editor edit = getSharedPreferences(LEVEL, MODE_PRIVATE).edit();
                        int score = getSharedPreferences(LEVEL, MODE_PRIVATE).getInt(SCORE, 0);
                        TextView scoretxt = (dialog2.findViewById(R.id.scoretext));
                        if (c < 6) {
                            (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                            score = score + (c * 100);
                            edit.putInt(MainActivity.SCORE, score);
                            scoretxt.setText(String.valueOf(c * 100));
                        } else if (c <= 14) {
                            (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                            (dialog2.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_on);
                            score = score + (c * 100);
                            edit.putInt(MainActivity.SCORE, score);
                            scoretxt.setText(String.valueOf(c * 100));
                            edit.putInt(MainActivity.CURRENT, 1);
                        } else if (c == 15) {
                            (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                            (dialog2.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_on);
                            (dialog2.findViewById(R.id.fstar3)).setBackgroundResource(R.drawable.star_on);
                            score = score + (c * 100);
                            edit.putInt(MainActivity.SCORE, score);
                            scoretxt.setText(String.valueOf(c * 100));
                        }
                        edit.apply();
                        edit.commit();
                        dialog2.show();
                        dialog2.getWindow().setAttributes(lp2);
                        showinterstitial();

                    }
                    /////

                }
            },800);

//                dialog.dismiss();
//            }

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

                        final Dialog dialog = new Dialog(MediumActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                        dialog.setContentView(R.layout.alert);
                        dialog.setCancelable(true);
                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        lp.copyFrom(dialog.getWindow().getAttributes());
                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                        TextView text = dialog.findViewById(R.id.atext);
                        text.setText("Congrats! You Completed The Level. watch this video to get 500 extra coins");
                        TextView a = dialog.findViewById(R.id.yes);
                        TextView b = dialog.findViewById(R.id.no);

                        countDownTimer = new CountDownTimer(TimeLeftInMillis, 5000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                TimeLeftInMillis=millisUntilFinished;
                                int seconds = (int) (TimeLeftInMillis / 1000) % 60;
                                String timeLeftFormatted = String.format(Locale.getDefault(), "%02d", seconds);
                                TextView a = dialog.findViewById(R.id.yes);
                                a.setText("Ad will start in 5sec");

                                //updateCountDown();
                            }
                            @Override
                            public void onFinish() {
                                mTimerRunning = false;
                                if(stateAlive[0]){showInterstitial();}
                                else{
                                    Toast.makeText(MediumActivity.this, "You Won", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }.start();
                        (dialog.findViewById(R.id.no)).setOnClickListener(v1 -> {
                            stateAlive[0] =false;

                            //finish();
                            startTimer(mTimeLeftInMillis);
                            final Dialog dialog2 = new Dialog(MediumActivity.this,R.style.PauseDialog);
                            dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                            dialog2.setContentView(R.layout.finish);
                            dialog2.setCancelable(true);
                            WindowManager.LayoutParams lp2 = new WindowManager.LayoutParams();
                            lp2.copyFrom(dialog2.getWindow().getAttributes());
                            lp2.width = WindowManager.LayoutParams.MATCH_PARENT;
                            lp2.height = WindowManager.LayoutParams.MATCH_PARENT;
                            TextView lvl = dialog2.findViewById(R.id.flevel);
                            lvl.setText("Easy");
                            (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_off);
                            (dialog2.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_off);
                            (dialog2.findViewById(R.id.fstar3)).setBackgroundResource(R.drawable.star_off);
                            SharedPreferences.Editor clvleditor = getSharedPreferences(MainActivity.LEVEL,MODE_PRIVATE).edit();
                            clvleditor.putInt(MainActivity.CURRENT,clvl);
                            clvleditor.apply();
                            clvleditor.commit();
                            (dialog2.findViewById(R.id.fnext)).setOnClickListener(v2 -> {
                                SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
                                editor.clear();
                                editor.apply();
                                editor.commit();
                                startActivity(new Intent(MediumActivity.this,PlayActivity.class));
                            });
                            (dialog2.findViewById(R.id.fhome)).setOnClickListener(v2 -> {
                                SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
                                editor.clear();
                                editor.apply();
                                editor.commit();
                                startActivity(new Intent(MediumActivity.this,MainActivity.class));
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
                            SharedPreferences.Editor edit = getSharedPreferences(LEVEL,MODE_PRIVATE).edit();
                            int score = getSharedPreferences(LEVEL,MODE_PRIVATE).getInt(SCORE,0);
                            TextView scoretxt = (dialog2.findViewById(R.id.scoretext));
                            if(c<6){
                                (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                                score = score+(c*100);
                                edit.putInt(MainActivity.SCORE,score);
                                scoretxt.setText(String.valueOf(c*100));
                            }else if(c<=14){
                                (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                                (dialog2.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_on);
                                score = score+(c*100);
                                edit.putInt(MainActivity.SCORE,score);
                                scoretxt.setText(String.valueOf(c*100));
                                edit.putInt(MainActivity.CURRENT,1);
                            }else if(c==15){
                                (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                                (dialog2.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_on);
                                (dialog2.findViewById(R.id.fstar3)).setBackgroundResource(R.drawable.star_on);
                                score = score+(c*100);
                                edit.putInt(MainActivity.SCORE,score);
                                scoretxt.setText(String.valueOf(c*100));
                            }
                            edit.apply();
                            edit.commit();
                            dialog2.show();
                            dialog2.getWindow().setAttributes(lp2);
                            showinterstitial();
                            dialog.dismiss();
                        });
                        dialog.show();
                        dialog.getWindow().setAttributes(lp);
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
                            //Toast.makeText(MediumActivity.this, "Game Over", Toast.LENGTH_SHORT).show();
                            final Dialog dialog2 = new Dialog(MediumActivity.this,R.style.PauseDialog);
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

                            SharedPreferences.Editor clvleditor = getSharedPreferences(MainActivity.LEVEL,MODE_PRIVATE).edit();
                            clvleditor.putInt(MainActivity.CURRENT,clvl);
                            clvleditor.apply();
                            clvleditor.commit();
                            (dialog2.findViewById(R.id.fnext)).setOnClickListener(v2 -> {
                                SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
                                editor.clear();
                                editor.apply();
                                editor.commit();
                                startActivity(new Intent(MediumActivity.this, PlayActivity.class));
                            });

                            (dialog2.findViewById(R.id.fhome)).setOnClickListener(v2 -> {
                                SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
                                editor.clear();
                                editor.apply();
                                editor.commit();
                                startActivity(new Intent(MediumActivity.this, MainActivity.class));
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

                            SharedPreferences.Editor edit = getSharedPreferences(LEVEL,MODE_PRIVATE).edit();
                            int score = getSharedPreferences(LEVEL,MODE_PRIVATE).getInt(SCORE,0);

                            TextView scoretxt = (dialog2.findViewById(R.id.scoretext));
                            if(c==1){
                                (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                                score = score+100;
                                edit.putInt(MainActivity.SCORE,score);
                                scoretxt.setText("100");
                            }else if(c==2){
                                (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                                (dialog2.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_on);
                                score = score+200;
                                edit.putInt(MainActivity.SCORE,score);
                                edit.putInt(MainActivity.CURRENT,2);
                                scoretxt.setText("200");
                            }else if(c==3){
                                (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                                (dialog2.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_on);
                                (dialog2.findViewById(R.id.fstar3)).setBackgroundResource(R.drawable.star_on);
                                score = score+300;
                                edit.putInt(MainActivity.SCORE,score);
                                scoretxt.setText("300");
                            }
                            edit.apply();
                            edit.commit();
                            dialog2.show();
                            dialog2.getWindow().setAttributes(lp2);
                            showInterstitial();
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

                    if(currentQuestion<questionItems.size()-1){
                        currentQuestion++;
                        i++;
                        current.setText(i+"/");
                        setQuestionScreen(currentQuestion);
                        clickable(true);
                        reset();
                    }else {

                        final Dialog dialog = new Dialog(MediumActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                        dialog.setContentView(R.layout.alert);
                        dialog.setCancelable(true);
                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        lp.copyFrom(dialog.getWindow().getAttributes());
                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                        TextView text = dialog.findViewById(R.id.atext);
                        text.setText("Congrats! You Completed The Level. watch this video to get 500 extra coins");
                        TextView a = dialog.findViewById(R.id.yes);
                        TextView b = dialog.findViewById(R.id.no);

                        countDownTimer = new CountDownTimer(TimeLeftInMillis, 5000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                TimeLeftInMillis=millisUntilFinished;
                                int seconds = (int) (TimeLeftInMillis / 1000) % 60;
                                String timeLeftFormatted = String.format(Locale.getDefault(), "%02d", seconds);
                                TextView a = dialog.findViewById(R.id.yes);
                                a.setText("Ad will start in 5sec");

                                //updateCountDown();
                            }
                            @Override
                            public void onFinish() {
                                mTimerRunning = false;
                                if(stateAlive[0]){showInterstitial();}
                                else{
                                    Toast.makeText(MediumActivity.this, "You Won", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }.start();
                        (dialog.findViewById(R.id.no)).setOnClickListener(v1 -> {
                            stateAlive[0] =false;

                            //finish();
                            startTimer(mTimeLeftInMillis);
                            final Dialog dialog2 = new Dialog(MediumActivity.this,R.style.PauseDialog);
                            dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                            dialog2.setContentView(R.layout.finish);
                            dialog2.setCancelable(true);
                            WindowManager.LayoutParams lp2 = new WindowManager.LayoutParams();
                            lp2.copyFrom(dialog2.getWindow().getAttributes());
                            lp2.width = WindowManager.LayoutParams.MATCH_PARENT;
                            lp2.height = WindowManager.LayoutParams.MATCH_PARENT;
                            TextView lvl = dialog2.findViewById(R.id.flevel);
                            lvl.setText("Easy");
                            (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_off);
                            (dialog2.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_off);
                            (dialog2.findViewById(R.id.fstar3)).setBackgroundResource(R.drawable.star_off);
                            SharedPreferences.Editor clvleditor = getSharedPreferences(MainActivity.LEVEL,MODE_PRIVATE).edit();
                            clvleditor.putInt(MainActivity.CURRENT,clvl);
                            clvleditor.apply();
                            clvleditor.commit();
                            (dialog2.findViewById(R.id.fnext)).setOnClickListener(v2 -> {
                                SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
                                editor.clear();
                                editor.apply();
                                editor.commit();
                                startActivity(new Intent(MediumActivity.this,PlayActivity.class));
                            });
                            (dialog2.findViewById(R.id.fhome)).setOnClickListener(v2 -> {
                                SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
                                editor.clear();
                                editor.apply();
                                editor.commit();
                                startActivity(new Intent(MediumActivity.this,MainActivity.class));
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
                            SharedPreferences.Editor edit = getSharedPreferences(LEVEL,MODE_PRIVATE).edit();
                            int score = getSharedPreferences(LEVEL,MODE_PRIVATE).getInt(SCORE,0);
                            TextView scoretxt = (dialog2.findViewById(R.id.scoretext));
                            if(c<6){
                                (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                                score = score+(c*100);
                                edit.putInt(MainActivity.SCORE,score);
                                scoretxt.setText(String.valueOf(c*100));
                            }else if(c<=14){
                                (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                                (dialog2.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_on);
                                score = score+(c*100);
                                edit.putInt(MainActivity.SCORE,score);
                                scoretxt.setText(String.valueOf(c*100));
                                edit.putInt(MainActivity.CURRENT,1);
                            }else if(c==15){
                                (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                                (dialog2.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_on);
                                (dialog2.findViewById(R.id.fstar3)).setBackgroundResource(R.drawable.star_on);
                                score = score+(c*100);
                                edit.putInt(MainActivity.SCORE,score);
                                scoretxt.setText(String.valueOf(c*100));
                            }
                            edit.apply();
                            edit.commit();
                            dialog2.show();
                            dialog2.getWindow().setAttributes(lp2);
                            showinterstitial();
                            dialog.dismiss();
                        });
                        dialog.show();
                        dialog.getWindow().setAttributes(lp);
                    }
                }
            },800);

        } else {
            selected.setBackground(getResources().getDrawable(R.drawable.option_wrong));
            Toast.makeText(this, "Double Dip", Toast.LENGTH_SHORT).show();
            clickable(true);
            dip=false;
            op1.setOnClickListener(v -> {checkAnswer(op1);});
            op2.setOnClickListener(v -> {checkAnswer(op2);});
            op3.setOnClickListener(v -> {checkAnswer(op3);});
            op4.setOnClickListener(v -> {checkAnswer(op4);});

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

                        if (currentQuestion < questionItems.size()-1) {
                            currentQuestion++;
                            i++;
                            current.setText(i + "/");
                            setQuestionScreen(currentQuestion);
                            clickable(true);
                            reset();
                        } else {

                            final Dialog dialog = new Dialog(MediumActivity.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                            dialog.setContentView(R.layout.alert);
                            dialog.setCancelable(true);
                            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                            lp.copyFrom(dialog.getWindow().getAttributes());
                            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                            TextView text = dialog.findViewById(R.id.atext);
                            text.setText("Congrats! You Completed The Level. watch this video to get 500 extra coins");
                            TextView a = dialog.findViewById(R.id.yes);
                            TextView b = dialog.findViewById(R.id.no);

                            countDownTimer = new CountDownTimer(TimeLeftInMillis, 5000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    TimeLeftInMillis=millisUntilFinished;
                                    int seconds = (int) (TimeLeftInMillis / 1000) % 60;
                                    String timeLeftFormatted = String.format(Locale.getDefault(), "%02d", seconds);
                                    TextView a = dialog.findViewById(R.id.yes);
                                    a.setText("Ad will start in 5sec");

                                    //updateCountDown();
                                }
                                @Override
                                public void onFinish() {
                                    mTimerRunning = false;
                                    if(stateAlive[0]){showInterstitial();}
                                    else{
                                        Toast.makeText(MediumActivity.this, "You Won", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }.start();
                            (dialog.findViewById(R.id.no)).setOnClickListener(v1 -> {
                                stateAlive[0] =false;

                                //finish();
                                startTimer(mTimeLeftInMillis);
                                final Dialog dialog2 = new Dialog(MediumActivity.this,R.style.PauseDialog);
                                dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                                dialog2.setContentView(R.layout.finish);
                                dialog2.setCancelable(true);
                                WindowManager.LayoutParams lp2 = new WindowManager.LayoutParams();
                                lp2.copyFrom(dialog2.getWindow().getAttributes());
                                lp2.width = WindowManager.LayoutParams.MATCH_PARENT;
                                lp2.height = WindowManager.LayoutParams.MATCH_PARENT;
                                TextView lvl = dialog2.findViewById(R.id.flevel);
                                lvl.setText("Easy");
                                (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_off);
                                (dialog2.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_off);
                                (dialog2.findViewById(R.id.fstar3)).setBackgroundResource(R.drawable.star_off);
                                SharedPreferences.Editor clvleditor = getSharedPreferences(MainActivity.LEVEL,MODE_PRIVATE).edit();
                                clvleditor.putInt(MainActivity.CURRENT,clvl);
                                clvleditor.apply();
                                clvleditor.commit();
                                (dialog2.findViewById(R.id.fnext)).setOnClickListener(v2 -> {
                                    SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
                                    editor.clear();
                                    editor.apply();
                                    editor.commit();
                                    startActivity(new Intent(MediumActivity.this,PlayActivity.class));
                                });
                                (dialog2.findViewById(R.id.fhome)).setOnClickListener(v2 -> {
                                    SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
                                    editor.clear();
                                    editor.apply();
                                    editor.commit();
                                    startActivity(new Intent(MediumActivity.this,MainActivity.class));
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
                                SharedPreferences.Editor edit = getSharedPreferences(LEVEL,MODE_PRIVATE).edit();
                                int score = getSharedPreferences(LEVEL,MODE_PRIVATE).getInt(SCORE,0);
                                TextView scoretxt = (dialog2.findViewById(R.id.scoretext));
                                if(c<6){
                                    (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                                    score = score+(c*100);
                                    edit.putInt(MainActivity.SCORE,score);
                                    scoretxt.setText(String.valueOf(c*100));
                                }else if(c<=14){
                                    (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                                    (dialog2.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_on);
                                    score = score+(c*100);
                                    edit.putInt(MainActivity.SCORE,score);
                                    scoretxt.setText(String.valueOf(c*100));
                                    edit.putInt(MainActivity.CURRENT,1);
                                }else if(c==15){
                                    (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                                    (dialog2.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_on);
                                    (dialog2.findViewById(R.id.fstar3)).setBackgroundResource(R.drawable.star_on);
                                    score = score+(c*100);
                                    edit.putInt(MainActivity.SCORE,score);
                                    scoretxt.setText(String.valueOf(c*100));
                                }
                                edit.apply();
                                edit.commit();
                                dialog2.show();
                                dialog2.getWindow().setAttributes(lp2);
                                showinterstitial();
                                dialog.dismiss();
                            });
                            dialog.show();
                            dialog.getWindow().setAttributes(lp);
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

                    if(currentQuestion<questionItems.size()-1){
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
//                        doubleDip.setClickable(true);
//                        doubleDip.setForeground(null);
//                        fifty.setClickable(true);
//                        fifty.setForeground(null);

                    }else {

                        final Dialog dialog = new Dialog(MediumActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                        dialog.setContentView(R.layout.alert);
                        dialog.setCancelable(true);
                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        lp.copyFrom(dialog.getWindow().getAttributes());
                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                        TextView text = dialog.findViewById(R.id.atext);
                        text.setText("Congrats! You Completed The Level. watch this video to get 500 extra coins");
                        TextView a = dialog.findViewById(R.id.yes);
                        TextView b = dialog.findViewById(R.id.no);

                        countDownTimer = new CountDownTimer(TimeLeftInMillis, 5000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                TimeLeftInMillis=millisUntilFinished;
                                int seconds = (int) (TimeLeftInMillis / 1000) % 60;
                                String timeLeftFormatted = String.format(Locale.getDefault(), "%02d", seconds);
                                TextView a = dialog.findViewById(R.id.yes);
                                a.setText("Ad will start in 5sec");

                                //updateCountDown();
                            }
                            @Override
                            public void onFinish() {
                                mTimerRunning = false;
                                if(stateAlive[0]){showInterstitial();}
                                else{
                                    Toast.makeText(MediumActivity.this, "You Won", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }.start();
                        (dialog.findViewById(R.id.no)).setOnClickListener(v1 -> {
                            stateAlive[0] =false;

                            //finish();
                            startTimer(mTimeLeftInMillis);
                            final Dialog dialog2 = new Dialog(MediumActivity.this,R.style.PauseDialog);
                            dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                            dialog2.setContentView(R.layout.finish);
                            dialog2.setCancelable(true);
                            WindowManager.LayoutParams lp2 = new WindowManager.LayoutParams();
                            lp2.copyFrom(dialog2.getWindow().getAttributes());
                            lp2.width = WindowManager.LayoutParams.MATCH_PARENT;
                            lp2.height = WindowManager.LayoutParams.MATCH_PARENT;
                            TextView lvl = dialog2.findViewById(R.id.flevel);
                            lvl.setText("Easy");
                            (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_off);
                            (dialog2.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_off);
                            (dialog2.findViewById(R.id.fstar3)).setBackgroundResource(R.drawable.star_off);
                            SharedPreferences.Editor clvleditor = getSharedPreferences(MainActivity.LEVEL,MODE_PRIVATE).edit();
                            clvleditor.putInt(MainActivity.CURRENT,clvl);
                            clvleditor.apply();
                            clvleditor.commit();
                            (dialog2.findViewById(R.id.fnext)).setOnClickListener(v2 -> {
                                SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
                                editor.clear();
                                editor.apply();
                                editor.commit();
                                startActivity(new Intent(MediumActivity.this,PlayActivity.class));
                            });
                            (dialog2.findViewById(R.id.fhome)).setOnClickListener(v2 -> {
                                SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
                                editor.clear();
                                editor.apply();
                                editor.commit();
                                startActivity(new Intent(MediumActivity.this,MainActivity.class));
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
                            SharedPreferences.Editor edit = getSharedPreferences(LEVEL,MODE_PRIVATE).edit();
                            int score = getSharedPreferences(LEVEL,MODE_PRIVATE).getInt(SCORE,0);
                            TextView scoretxt = (dialog2.findViewById(R.id.scoretext));
                            if(c<6){
                                (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                                score = score+(c*100);
                                edit.putInt(MainActivity.SCORE,score);
                                scoretxt.setText(String.valueOf(c*100));
                            }else if(c<=14){
                                (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                                (dialog2.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_on);
                                score = score+(c*100);
                                edit.putInt(MainActivity.SCORE,score);
                                scoretxt.setText(String.valueOf(c*100));
                                edit.putInt(MainActivity.CURRENT,1);
                            }else if(c==15){
                                (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                                (dialog2.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_on);
                                (dialog2.findViewById(R.id.fstar3)).setBackgroundResource(R.drawable.star_on);
                                score = score+(c*100);
                                edit.putInt(MainActivity.SCORE,score);
                                scoretxt.setText(String.valueOf(c*100));
                            }
                            edit.apply();
                            edit.commit();
                            dialog2.show();
                            dialog2.getWindow().setAttributes(lp2);
                            showinterstitial();
                            dialog.dismiss();
                        });
                        dialog.show();
                        dialog.getWindow().setAttributes(lp);
                    }
                }
            },800);

        } else {
            selected.setBackground(getResources().getDrawable(R.drawable.option_wrong));
            wrong++;
            final boolean[] stateAlive = {true};
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    reset();
                    pauseTimer();
                    if(used[0]) {
                        used[0]=false;
                        final Dialog dialog = new Dialog(MediumActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                        dialog.setContentView(R.layout.alert);
                        dialog.setCancelable(true);
                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        lp.copyFrom(dialog.getWindow().getAttributes());
                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                        TextView text = dialog.findViewById(R.id.atext);
                        text.setText("Watch This video and get chance");
                        TextView a = dialog.findViewById(R.id.yes);
                        countDownTimer = new CountDownTimer(TimeLeftInMillis, 5000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                TimeLeftInMillis = millisUntilFinished;
                                int seconds = (int) (TimeLeftInMillis / 1000) % 60;
                                String timeLeftFormatted = String.format(Locale.getDefault(), "%02d", seconds);
                                TextView a = dialog.findViewById(R.id.yes);
                                a.setText("Ad will start in 5sec");


                                //updateCountDown();
                            }

                            @Override
                            public void onFinish() {
                                mTimerRunning = false;

                                if (Constants.rewardedAd.isLoaded() && stateAlive[0]) {
                                    Constants.rewardedAd.show(MediumActivity.this, new RewardedAdCallback() {
                                        @Override
                                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                            startTimer(mTimeLeftInMillis);
                                            //Toast.makeText(MediumActivity.this, "done", Toast.LENGTH_SHORT).show();
                                            Constants.loadRewardedAd(MediumActivity.this);
                                            dialog.dismiss();
                                        }
                                    });
                                } else {
//                                    startTimer(mTimeLeftInMillis);
//                                    Toast.makeText(MediumActivity.this, "Please Wait, Ad is loading", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }.start();
                        (dialog.findViewById(R.id.no)).setOnClickListener(v1 -> {
                            stateAlive[0] = false;

                            //finish();
                            //startTimer(mTimeLeftInMillis);
                            final Dialog dialog2 = new Dialog(MediumActivity.this, R.style.PauseDialog);
                            dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                            dialog2.setContentView(R.layout.finish);
                            dialog2.setCancelable(true);
                            WindowManager.LayoutParams lp2 = new WindowManager.LayoutParams();
                            lp2.copyFrom(dialog2.getWindow().getAttributes());
                            lp2.width = WindowManager.LayoutParams.MATCH_PARENT;
                            lp2.height = WindowManager.LayoutParams.MATCH_PARENT;
                            TextView lvl = dialog2.findViewById(R.id.flevel);
                            lvl.setText("Easy");
                            (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_off);
                            (dialog2.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_off);
                            (dialog2.findViewById(R.id.fstar3)).setBackgroundResource(R.drawable.star_off);
                            SharedPreferences.Editor clvleditor = getSharedPreferences(MainActivity.LEVEL, MODE_PRIVATE).edit();
                            clvleditor.putInt(MainActivity.CURRENT, clvl);
                            clvleditor.apply();
                            clvleditor.commit();
                            (dialog2.findViewById(R.id.fnext)).setOnClickListener(v2 -> {
                                SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
                                editor.clear();
                                editor.apply();
                                editor.commit();
                                startActivity(new Intent(MediumActivity.this, PlayActivity.class));
                            });
                            (dialog2.findViewById(R.id.fhome)).setOnClickListener(v2 -> {
                                SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
                                editor.clear();
                                editor.apply();
                                editor.commit();
                                startActivity(new Intent(MediumActivity.this, MainActivity.class));
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
                            SharedPreferences.Editor edit = getSharedPreferences(LEVEL, MODE_PRIVATE).edit();
                            int score = getSharedPreferences(LEVEL, MODE_PRIVATE).getInt(SCORE, 0);
                            TextView scoretxt = (dialog2.findViewById(R.id.scoretext));
                            if (c < 6) {
                                (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                                score = score + (c * 100);
                                edit.putInt(MainActivity.SCORE, score);
                                scoretxt.setText(String.valueOf(c * 100));
                            } else if (c <= 14) {
                                (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                                (dialog2.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_on);
                                score = score + (c * 100);
                                edit.putInt(MainActivity.SCORE, score);
                                scoretxt.setText(String.valueOf(c * 100));
                                edit.putInt(MainActivity.CURRENT, 1);
                            } else if (c == 15) {
                                (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                                (dialog2.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_on);
                                (dialog2.findViewById(R.id.fstar3)).setBackgroundResource(R.drawable.star_on);
                                score = score + (c * 100);
                                edit.putInt(MainActivity.SCORE, score);
                                scoretxt.setText(String.valueOf(c * 100));
                            }
                            edit.apply();
                            edit.commit();
                            dialog2.show();
                            dialog2.getWindow().setAttributes(lp2);
                            showinterstitial();
                            dialog.dismiss();

                        });
                        dialog.show();
                        dialog.getWindow().setAttributes(lp);
                    }else{
                        stateAlive[0] = false;

                        //finish();
                        //startTimer(mTimeLeftInMillis);
                        final Dialog dialog2 = new Dialog(MediumActivity.this, R.style.PauseDialog);
                        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                        dialog2.setContentView(R.layout.finish);
                        dialog2.setCancelable(true);
                        WindowManager.LayoutParams lp2 = new WindowManager.LayoutParams();
                        lp2.copyFrom(dialog2.getWindow().getAttributes());
                        lp2.width = WindowManager.LayoutParams.MATCH_PARENT;
                        lp2.height = WindowManager.LayoutParams.MATCH_PARENT;
                        TextView lvl = dialog2.findViewById(R.id.flevel);
                        lvl.setText("Easy");
                        (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_off);
                        (dialog2.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_off);
                        (dialog2.findViewById(R.id.fstar3)).setBackgroundResource(R.drawable.star_off);
                        SharedPreferences.Editor clvleditor = getSharedPreferences(MainActivity.LEVEL, MODE_PRIVATE).edit();
                        clvleditor.putInt(MainActivity.CURRENT, clvl);
                        clvleditor.apply();
                        clvleditor.commit();
                        (dialog2.findViewById(R.id.fnext)).setOnClickListener(v2 -> {
                            SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
                            editor.clear();
                            editor.apply();
                            editor.commit();
                            startActivity(new Intent(MediumActivity.this, PlayActivity.class));
                        });
                        (dialog2.findViewById(R.id.fhome)).setOnClickListener(v2 -> {
                            SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
                            editor.clear();
                            editor.apply();
                            editor.commit();
                            startActivity(new Intent(MediumActivity.this, MainActivity.class));
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
                        SharedPreferences.Editor edit = getSharedPreferences(LEVEL, MODE_PRIVATE).edit();
                        int score = getSharedPreferences(LEVEL, MODE_PRIVATE).getInt(SCORE, 0);
                        TextView scoretxt = (dialog2.findViewById(R.id.scoretext));
                        if (c < 6) {
                            (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                            score = score + (c * 100);
                            edit.putInt(MainActivity.SCORE, score);
                            scoretxt.setText(String.valueOf(c * 100));
                        } else if (c <= 14) {
                            (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                            (dialog2.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_on);
                            score = score + (c * 100);
                            edit.putInt(MainActivity.SCORE, score);
                            scoretxt.setText(String.valueOf(c * 100));
                            edit.putInt(MainActivity.CURRENT, 1);
                        } else if (c == 15) {
                            (dialog2.findViewById(R.id.fstar1)).setBackgroundResource(R.drawable.star_on);
                            (dialog2.findViewById(R.id.fstar2)).setBackgroundResource(R.drawable.star_on);
                            (dialog2.findViewById(R.id.fstar3)).setBackgroundResource(R.drawable.star_on);
                            score = score + (c * 100);
                            edit.putInt(MainActivity.SCORE, score);
                            scoretxt.setText(String.valueOf(c * 100));
                        }
                        edit.apply();
                        edit.commit();
                        dialog2.show();
                        dialog2.getWindow().setAttributes(lp2);
                        showinterstitial();

                    }
                    /////

                }
            },800);
        }
        SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
        editor.putInt(ANS, correct);
        editor.apply();
        editor.commit();

    }

    //set question
    private void  setQuestionScreen(int n){
        if(n<=15) {
            if (mTimerRunning) {
                pauseTimer();
                resetTimer();
                startTimer(mTimeLeftInMillis);
            } else {
                startTimer(mTimeLeftInMillis);
            }
            mbar.setProgress(n+1);
//            TranslateAnimation anim = new TranslateAnimation(0,0,2,0);
//            anim.setDuration(1000);
//            question.startAnimation(anim);
            fifty2.setVisibility(View.INVISIBLE);
            doubleDip2.setVisibility(View.INVISIBLE);
            fifty.setVisibility(View.VISIBLE);
            doubleDip.setVisibility(View.VISIBLE);
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

        fifty2.setVisibility(View.INVISIBLE);
        doubleDip2.setVisibility(View.INVISIBLE);
        fifty.setVisibility(View.VISIBLE);
        doubleDip.setVisibility(View.VISIBLE);
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
            for(int i = 25; i<40;i++){
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
                pauseTimer();
                SharedPreferences.Editor editor = getSharedPreferences(CORRECT, MODE_PRIVATE).edit();
                //Toast.makeText(MediumActivity.this, "pause", Toast.LENGTH_SHORT).show();
                final Dialog dialog = new Dialog(MediumActivity.this,R.style.PauseDialog);
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
                    startTimer(mTimeLeftInMillis);
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
                    startActivity(new Intent(MediumActivity.this, MainActivity.class));

                });
                if(c<6){
                    (dialog.findViewById(R.id.pstar1)).setBackgroundResource(R.drawable.star_on);
                }else if(c<=13){
                    (dialog.findViewById(R.id.pstar1)).setBackgroundResource(R.drawable.star_on);
                    (dialog.findViewById(R.id.pstar2)).setBackgroundResource(R.drawable.star_on);
                }else if(c==14){
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