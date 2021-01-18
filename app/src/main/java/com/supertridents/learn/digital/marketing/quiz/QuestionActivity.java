package com.supertridents.learn.digital.marketing.quiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;

import java.util.ArrayList;

public class QuestionActivity extends AppCompatActivity {

    RecyclerView levels;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        getSupportActionBar().hide();

        levels = findViewById(R.id.list);
        ArrayList<RecyclerModel> items = new ArrayList<>();

        items.add(new RecyclerModel(1,R.drawable.ic_coins,R.drawable.ic_coins,R.drawable.ic_coins,R.drawable.ic_lock));
        items.add(new RecyclerModel(2,R.drawable.ic_coins,R.drawable.ic_coins,R.drawable.ic_coins,R.drawable.ic_lock));
        items.add(new RecyclerModel(3,R.drawable.ic_coins,R.drawable.ic_coins,R.drawable.ic_coins,R.drawable.ic_lock));
        items.add(new RecyclerModel(4,R.drawable.ic_coins,R.drawable.ic_coins,R.drawable.ic_coins,R.drawable.ic_lock));
        items.add(new RecyclerModel(5,R.drawable.ic_coins,R.drawable.ic_coins,R.drawable.ic_coins,R.drawable.ic_lock));
        items.add(new RecyclerModel(6,R.drawable.ic_coins,R.drawable.ic_coins,R.drawable.ic_coins,R.drawable.ic_lock));
        items.add(new RecyclerModel(7,R.drawable.ic_coins,R.drawable.ic_coins,R.drawable.ic_coins,R.drawable.ic_lock));
        items.add(new RecyclerModel(8,R.drawable.ic_coins,R.drawable.ic_coins,R.drawable.ic_coins,R.drawable.ic_lock));
        items.add(new RecyclerModel(9,R.drawable.ic_coins,R.drawable.ic_coins,R.drawable.ic_coins,R.drawable.ic_lock));
        items.add(new RecyclerModel(10,R.drawable.ic_coins,R.drawable.ic_coins,R.drawable.ic_coins,R.drawable.ic_lock));
        items.add(new RecyclerModel(11,R.drawable.ic_coins,R.drawable.ic_coins,R.drawable.ic_coins,R.drawable.ic_lock));
        items.add(new RecyclerModel(12,R.drawable.ic_coins,R.drawable.ic_coins,R.drawable.ic_coins,R.drawable.ic_lock));
        items.add(new RecyclerModel(13,R.drawable.ic_coins,R.drawable.ic_coins,R.drawable.ic_coins,R.drawable.ic_lock));
        items.add(new RecyclerModel(14,R.drawable.ic_coins,R.drawable.ic_coins,R.drawable.ic_coins,R.drawable.ic_lock));
        items.add(new RecyclerModel(15,R.drawable.ic_coins,R.drawable.ic_coins,R.drawable.ic_coins,R.drawable.ic_lock));
        items.add(new RecyclerModel(16,R.drawable.ic_coins,R.drawable.ic_coins,R.drawable.ic_coins,R.drawable.ic_lock));
        items.add(new RecyclerModel(17,R.drawable.ic_coins,R.drawable.ic_coins,R.drawable.ic_coins,R.drawable.ic_lock));
        items.add(new RecyclerModel(18,R.drawable.ic_coins,R.drawable.ic_coins,R.drawable.ic_coins,R.drawable.ic_lock));
        items.add(new RecyclerModel(19,R.drawable.ic_coins,R.drawable.ic_coins,R.drawable.ic_coins,R.drawable.ic_lock));
        items.add(new RecyclerModel(20,R.drawable.ic_coins,R.drawable.ic_coins,R.drawable.ic_coins,R.drawable.ic_lock));

        RecyclerAdapter adapter = new RecyclerAdapter(items,QuestionActivity.this);
        levels.setAdapter(adapter);

        GridLayoutManager layout = new GridLayoutManager(this,3);
        levels.setLayoutManager(layout);

        ImageView back = findViewById(R.id.back2);
        back.setOnClickListener(v -> {
            super.onBackPressed();
        });
    }
}