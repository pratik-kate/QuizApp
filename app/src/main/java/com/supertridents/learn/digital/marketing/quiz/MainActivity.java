package com.supertridents.learn.digital.marketing.quiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class MainActivity extends AppCompatActivity {

    public static String LEVEL="LEVEL";
    public static String CURRENT="CURRENT";
    public static int currency;
    public static int coins;
    public static String SCORE ="SCORE";
    TextView setting,exit;
    CardView profile,settings,shop,game;
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView adView = new AdView(this);

        adView.setAdSize(AdSize.BANNER);

        adView.setAdUnitId("ca-app-pub-5324429581828078/6070672454");

        mAdView = findViewById(R.id.adHome);
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

        //setting = findViewById(R.id.options);
        //exit = findViewById(R.id.exit);
        game = findViewById(R.id.startgame);
        profile = findViewById(R.id.cardProfile);
        settings = findViewById(R.id.cardSettings);
        shop = findViewById(R.id.cardShop);

        game.setOnClickListener(v -> {
            Intent game = new Intent(MainActivity.this,PlayActivity.class);
            startActivity(game);
        });
//        setting.setOnClickListener(v -> {
//            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
//            startActivity(intent);
//        });

//        exit.setOnClickListener( v ->{
//            exitByBackKey();
//        });

        settings.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this,SettingsActivity.class));
        });
        shop.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this,ShopActivity.class));
        });

        SharedPreferences preferences = getSharedPreferences(LEVEL,MODE_PRIVATE);
        int coin = preferences.getInt(String.valueOf(coins),1);
        int score = preferences.getInt(SCORE,1);

        TextView coins = findViewById(R.id.coins);
        coins.setText(String.valueOf(coin));
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void exitByBackKey() {

        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("Do you want to quit the game?")
                .setPositiveButton("Yes", (arg0, arg1) -> {
                    Intent a = new Intent(Intent.ACTION_MAIN);
                    a.addCategory(Intent.CATEGORY_HOME);
                    a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(a);
                    finish();
                })
                .setNegativeButton("No", (arg0, arg1) -> arg0.dismiss())
                .show();
    }
}