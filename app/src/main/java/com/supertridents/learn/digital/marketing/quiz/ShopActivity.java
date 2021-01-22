package com.supertridents.learn.digital.marketing.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

public class ShopActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        getSupportActionBar().hide();
        ImageView back  =findViewById(R.id.backShop);

        back.setOnClickListener(v -> {
            super.onBackPressed();
        });
    }
}