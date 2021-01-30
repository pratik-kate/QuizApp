package com.supertridents.learn.digital.marketing.quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;

public class PlayActivity extends AppCompatActivity {

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

        adView.setAdUnitId("ca-app-pub-5324429581828078/6070672454");

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
        if(completed==4){(findViewById(R.id.timerModeimg)).setVisibility(View.VISIBLE);}
        else{(findViewById(R.id.lock)).setVisibility(View.VISIBLE);}
        timeMode.setOnClickListener(v -> {
            if(completed==4) {
                startActivity(new Intent(this, TimeModeActivity.class));
            }
            else {
                AlertDialog alertbox = new AlertDialog.Builder(this)
                        .setMessage("Watch Ad to play this mode")
                        .setPositiveButton("Yes", (arg0, arg1) -> {
                            if(Constants.rewardedAd.isLoaded()){
                                Constants.rewardedAd.show(PlayActivity.this, new RewardedAdCallback() {
                                    @Override
                                    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                        startActivity(new Intent(PlayActivity.this, TimeModeActivity.class));
                                        Constants.loadRewardedAd(PlayActivity.this);
                                    }
                                });
                            }
                            else
                            {
                                Toast.makeText(PlayActivity.this, "Please Wait, Ad is loading", Toast.LENGTH_SHORT).show();
                            }
                            finish();
                        })
                        .setNegativeButton("No", (arg0, arg1) -> arg0.dismiss())
                        .show();

            }
        });

        back.setOnClickListener(v -> {
            super.onBackPressed();
        });

        easy.setOnClickListener(v -> {
            startActivity(new Intent(this,GameActivity.class));
        });
        normal.setOnClickListener(v -> {
            startActivity(new Intent(this,MediumActivity.class));
        });
        hard.setOnClickListener(v -> {
            startActivity(new Intent(this,HardActivity.class));
        });

    }
}