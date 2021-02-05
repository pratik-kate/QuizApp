package com.supertridents.ncert.class10.quiz.game.solutionns.maths;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.supertridents.ncert.class10.quiz.game.solutionns.Constants;
import com.supertridents.ncert.class10.quiz.game.solutionns.MainActivity;
import com.supertridents.ncert.class10.quiz.game.solutionns.R;
import com.supertridents.ncert.class10.quiz.game.solutionns.TimeModeActivity;

public class MathsPlayActivity extends AppCompatActivity {

    CardView easy,normal,hard,timeMode;
    ImageView back;
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        getSupportActionBar().hide();
        easy = findViewById(R.id.easy);
        normal = findViewById(R.id.normal);
        hard = findViewById(R.id.hard);
        back = findViewById(R.id.back2);
        timeMode = findViewById(R.id.timeMode);

        AdView adView = new AdView(this);

        adView.setAdSize(AdSize.BANNER);

        adView.setAdUnitId("ca-app-pub-5324429581828078/4782155832");

        mAdView = findViewById(R.id.adPlay);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                super.onAdLoaded();
                //Toast.makeText(MainActivity.this, "ad Loded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
                super.onAdFailedToLoad(adError);
                mAdView.loadAd(adRequest);
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
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
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });

        SharedPreferences pref = getSharedPreferences(MainActivity.LEVEL, MODE_PRIVATE);

        int completed = pref.getInt(MainActivity.CURRENT, 0);
        if(completed>=4){(findViewById(R.id.timerModeimg)).setVisibility(View.VISIBLE);}
        else{(findViewById(R.id.lock)).setVisibility(View.VISIBLE);}

        if(completed>=1){(findViewById(R.id.easyimg)).setVisibility(View.VISIBLE);}
        else{(findViewById(R.id.elock)).setVisibility(View.VISIBLE);}

        if(completed>=2){(findViewById(R.id.himg)).setVisibility(View.VISIBLE);}
        else{(findViewById(R.id.hlock)).setVisibility(View.VISIBLE);}

        timeMode.setOnClickListener(v -> {
            if(completed==4) {
                startActivity(new Intent(this, TimeModeActivity.class));
            }
            else {
                final Dialog dialog2 = new Dialog(MathsPlayActivity.this);
                dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                dialog2.setContentView(R.layout.alert);
                dialog2.setCancelable(true);
                WindowManager.LayoutParams lp2 = new WindowManager.LayoutParams();
                lp2.copyFrom(dialog2.getWindow().getAttributes());
                lp2.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp2.height = WindowManager.LayoutParams.WRAP_CONTENT;
                TextView text = dialog2.findViewById(R.id.atext);
                text.setText("Mode-Locked! Complete Other Modes to unlock it Or Want to Watch Ad And Play Now?");
                (dialog2.findViewById(R.id.yes)).setOnClickListener(v1 -> {

                    if(Constants.rewardedAd.isLoaded()){
                        Constants.rewardedAd.show(MathsPlayActivity.this, new RewardedAdCallback() {
                            @Override
                            public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                startActivity(new Intent(MathsPlayActivity.this, MathsTimeModeActivity.class));
                                Toast.makeText(MathsPlayActivity.this, "Congrats! Time Trial Successfully Unlocked", Toast.LENGTH_SHORT).show();
                                Constants.loadRewardedAd(MathsPlayActivity.this);
                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(MathsPlayActivity.this, "Please Wait, Ad is loading", Toast.LENGTH_SHORT).show();
                    }

                    dialog2.dismiss();
                });
                (dialog2.findViewById(R.id.no)).setOnClickListener(v1 -> {

                    dialog2.dismiss();

                });
                dialog2.show();
                dialog2.getWindow().setAttributes(lp2);

            }
        });

        back.setOnClickListener(v -> {
            startActivity(new Intent(this,MainActivity.class));
        });

        easy.setOnClickListener(v -> {
            startActivity(new Intent(this,MathsGameActivity.class));
        });
        normal.setOnClickListener(v -> {
            if(completed>=1) {
                startActivity(new Intent(this, MathsMediumActivity.class));
            }
            else{
                Toast.makeText(this, "Complete Easy Mode To Unlock This Mode", Toast.LENGTH_SHORT).show();
            }
        });
        hard.setOnClickListener(v -> {
            if(completed>2) {
                startActivity(new Intent(this, MathsHardActivity.class));
            }
            else{
                Toast.makeText(this, "Complete Normal Mode To Unlock This Mode", Toast.LENGTH_SHORT).show();
            }
        });

    }
}