package com.supertridents.learn.digital.marketing.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.widget.Toast;

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

                    SharedPreferences.Editor editor = getSharedPreferences(String.valueOf(MainActivity.level), MODE_PRIVATE).edit();
                    editor.putInt(String.valueOf(MainActivity.current), 1);
                    editor.apply();
                    editor.commit();
                    Toast.makeText(SplashActivity.this, "First Start", Toast.LENGTH_SHORT).show();
                }
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
                finish();
            }
        },3000);
    }
}