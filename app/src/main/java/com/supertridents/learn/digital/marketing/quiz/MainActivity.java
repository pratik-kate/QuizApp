package com.supertridents.learn.digital.marketing.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static String LEVEL="LEVEL";
    public static String CURRENT="CURRENT";
    public static int currency;
    public static int coins;
    TextView setting,exit,game;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        setting = findViewById(R.id.options);
        exit = findViewById(R.id.exit);
        game = findViewById(R.id.startgame);

        game.setOnClickListener(v -> {
            Intent game = new Intent(MainActivity.this,QuestionActivity.class);
            startActivity(game);
        });
        setting.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        exit.setOnClickListener( v ->{
            super.onBackPressed();
        });

        SharedPreferences preferences = getSharedPreferences(LEVEL,MODE_PRIVATE);
        int coin = preferences.getInt(String.valueOf(coins),1);

        TextView coins = findViewById(R.id.coins);
        coins.setText(String.valueOf(coin));
    }

}