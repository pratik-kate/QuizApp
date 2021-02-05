package com.supertridents.ncert.class10.quiz.game.solutionns;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;

public class ShopActivity extends AppCompatActivity {

    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        getSupportActionBar().hide();
        ImageView back  =findViewById(R.id.backShop);

        AdView adView = new AdView(this);

        adView.setAdSize(AdSize.BANNER);

        adView.setAdUnitId("ca-app-pub-5324429581828078/9078647581");

        mAdView = findViewById(R.id.adShop);
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

        back.setOnClickListener(v -> {
            super.onBackPressed();
        });

        CardView watch = findViewById(R.id.watch);
        CardView buy = findViewById(R.id.buy);


        SharedPreferences preferences = getSharedPreferences(MainActivity.LEVEL,MODE_PRIVATE);
        final int[] credits = {preferences.getInt(String.valueOf(MainActivity.coins), 0)};
        watch.setOnClickListener(v -> {

            if(Constants.rewardedAd.isLoaded()){
                Constants.rewardedAd.show(ShopActivity.this, new RewardedAdCallback() {
                    @Override
                    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                        credits[0] = credits[0] + 500;
                        SharedPreferences.Editor editor = getSharedPreferences(MainActivity.LEVEL,MODE_PRIVATE).edit();
                        editor.putInt(String.valueOf(MainActivity.coins),credits[0]);
                        editor.apply();
                        editor.commit();
                        Toast.makeText(ShopActivity.this, "500 Coins Added", Toast.LENGTH_SHORT).show();
                        Constants.loadRewardedAd(ShopActivity.this);
                    }
                });
            }
            else
            {
                Toast.makeText(ShopActivity.this, "Please Wait, Ad is loading", Toast.LENGTH_SHORT).show();
            }
        });

        buy.setOnClickListener(v -> Toast.makeText(ShopActivity.this, "Available Soon", Toast.LENGTH_SHORT).show());
    }
}