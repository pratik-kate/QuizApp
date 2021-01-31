package com.supertridents.ncert.class10.quiz.game.solutionns;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getSupportActionBar().hide();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                boolean previouslyStarted = prefs.getBoolean(getString(R.string.pref_previously_started), false);
                if(!previouslyStarted) {
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.putBoolean(getString(R.string.pref_previously_started), Boolean.TRUE);
                    edit.commit();
                    edit.apply();

                    SharedPreferences.Editor editor = getSharedPreferences(MainActivity.LEVEL, MODE_PRIVATE).edit();
                    editor.putInt(MainActivity.CURRENT, 0);
                    editor.putInt(MainActivity.SCORE,0);
                    editor.putInt(String.valueOf(MainActivity.coins), 500);
                    editor.apply();
                    editor.commit();

//                    SharedPreferences.Editor coinseditor = getSharedPreferences(String.valueOf(MainActivity.currency), MODE_PRIVATE).edit();
//                    coinseditor.putInt(String.valueOf(MainActivity.coins), 100);
//                    coinseditor.apply();
//                    coinseditor.commit();


                    //Toast.makeText(SplashActivity.this, "First Start", Toast.LENGTH_SHORT).show();
                }
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
                finish();
            }
        },3000);

        Constants.loadRewardedAd(this);
        Constants.loadAd(this);
    }
}