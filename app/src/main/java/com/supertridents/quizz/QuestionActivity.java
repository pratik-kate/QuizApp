package com.supertridents.quizz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

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

        items.add(new RecyclerModel(1,R.drawable.ic_coins,R.drawable.ic_coins,R.drawable.ic_coins));
        items.add(new RecyclerModel(2,R.drawable.ic_coins,R.drawable.ic_coins,R.drawable.ic_coins));
        items.add(new RecyclerModel(3,R.drawable.ic_coins,R.drawable.ic_coins,R.drawable.ic_coins));
        items.add(new RecyclerModel(4,R.drawable.ic_coins,R.drawable.ic_coins,R.drawable.ic_coins));
        items.add(new RecyclerModel(5,R.drawable.ic_coins,R.drawable.ic_coins,R.drawable.ic_coins));
        items.add(new RecyclerModel(6,R.drawable.ic_coins,R.drawable.ic_coins,R.drawable.ic_coins));
        items.add(new RecyclerModel(7,R.drawable.ic_coins,R.drawable.ic_coins,R.drawable.ic_coins));

        RecyclerAdapter adapter = new RecyclerAdapter(items,QuestionActivity.this);
        levels.setAdapter(adapter);

        GridLayoutManager layout = new GridLayoutManager(this,3);
        levels.setLayoutManager(layout);
    }
}