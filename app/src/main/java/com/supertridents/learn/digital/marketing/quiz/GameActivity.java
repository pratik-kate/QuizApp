package com.supertridents.learn.digital.marketing.quiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.supertridents.learn.digital.marketing.quiz.questions.QuestionFragment;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getSupportActionBar().hide();
        QuestionFragment fragment = new QuestionFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.screen,fragment);
        transaction.commit();

        Intent intent = getIntent();

        TextView lvl = findViewById(R.id.lvltxt);
        lvl.setText("Level "+String.valueOf(intent.getIntExtra("level",0)));
    }
}