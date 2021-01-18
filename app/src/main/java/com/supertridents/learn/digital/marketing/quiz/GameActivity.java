package com.supertridents.learn.digital.marketing.quiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.supertridents.learn.digital.marketing.quiz.questions.QuestionFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import pl.droidsonroids.gif.GifImageView;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    TextView question,op1,op2,op3,op4;
    List<QuestionItem> questionItems;
    int currentQuestion = 0;
    int correct = 0,wrong = 0,i = 1;
    GifImageView gif;
    TextView current,total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getSupportActionBar().hide();
//        QuestionFragment fragment = new QuestionFragment();
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.screen,fragment);
//        transaction.commit();
        question = findViewById(R.id.question);
        op1 = findViewById(R.id.option_1);
        op2 = findViewById(R.id.option_2);
        op3 = findViewById(R.id.option_3);
        op4 = findViewById(R.id.option_4);

        loadQuestions();
        Collections.shuffle(questionItems);
        setQuestionScreen(currentQuestion);
        total = findViewById(R.id.total);
        current =findViewById(R.id.current);
        total.setText("3");
        current.setText(i+"/");
//        op1.setOnClickListener(v->{
//            if(questionItems.get(currentQuestion).getOp1().equals(questionItems.get(currentQuestion).getCorrect())){
//                op1.setBackground(getResources().getDrawable(R.drawable.option_right));
//                clickable(false);
//                correct++;
//                delay();
//
//            }else{
//                op1.setBackground(getResources().getDrawable(R.drawable.option_wrong));
//                wrong++;
//                delay();
//            }
//
//            //loading next questions
//            if(currentQuestion< questionItems.size()-1){
//                currentQuestion++;
//                setQuestionScreen(currentQuestion);
//                clickable(true);
//                reset();
//            }else {
//                Toast.makeText(this, "Game Over", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        op2.setOnClickListener(v->{
//            if(questionItems.get(currentQuestion).getOp2().equals(questionItems.get(currentQuestion).getCorrect())){
//                op2.setBackground(getResources().getDrawable(R.drawable.option_right));
//                clickable(false);
//                correct++;
//                delay();
//            }else{
//                op2.setBackground(getResources().getDrawable(R.drawable.option_wrong));
//                wrong++;
//                delay();
//            }
//
//            //loading next questions
//            if(currentQuestion< questionItems.size()-1){
//                currentQuestion++;
//                setQuestionScreen(currentQuestion);
//                clickable(true);
//                reset();
//            }else {
//                Toast.makeText(this, "Game Over", Toast.LENGTH_SHORT).show();
//
//            }
//        });
//
//        op3.setOnClickListener(v->{
//            if(questionItems.get(currentQuestion).getOp3().equals(questionItems.get(currentQuestion).getCorrect())){
//                op3.setBackground(getResources().getDrawable(R.drawable.option_right));
//                clickable(false);
//                correct++;
//              delay();
//            }else{
//                op3.setBackground(getResources().getDrawable(R.drawable.option_wrong));
//                wrong++;
//                delay();
//            }
//
//            //loading next questions
//            if(currentQuestion< questionItems.size()-1){
//                currentQuestion++;
//                setQuestionScreen(currentQuestion);
//                clickable(true);
//                reset();
//            }else {
//                Toast.makeText(this, "Game Over", Toast.LENGTH_SHORT).show();
//
//            }
//        });
//
//        op4.setOnClickListener(v->{
//            if(questionItems.get(currentQuestion).getOp4().equals(questionItems.get(currentQuestion).getCorrect())){
//                op4.setBackground(getResources().getDrawable(R.drawable.option_right));
//                clickable(false);
//                correct++;
//                delay();
//            }else{
//                op4.setBackground(getResources().getDrawable(R.drawable.option_wrong));
//                wrong++;
//                delay();
//            }
//
//            //loading next questions
//            if(currentQuestion< questionItems.size()-1){
//                currentQuestion++;
//                setQuestionScreen(currentQuestion);
//                clickable(true);
//                reset();
//            }else {
//                Toast.makeText(this, "Game Over", Toast.LENGTH_SHORT).show();
//
//            }
//        });
        op1.setOnClickListener(this);
        op2.setOnClickListener(this);
        op3.setOnClickListener(this);
        op4.setOnClickListener(this);
        Intent intent = getIntent();
        int level = intent.getIntExtra("level",0);
        TextView lvl = findViewById(R.id.lvltxt);
        lvl.setText("Level "+String.valueOf(level));

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private boolean checkAnswer(TextView selected) {
        String selectedAnswer = selected.getText().toString();
        if(selectedAnswer.equals(questionItems.get(currentQuestion).getCorrect())){
            selected.setBackground(getResources().getDrawable(R.drawable.option_right));
            //gif.setVisibility(View.VISIBLE);
            clickable(false);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                   // gif.setVisibility(View.INVISIBLE);
                    reset();
                    clickable(true);

                    if(currentQuestion<2){
                        currentQuestion++;
                        i++;
                        current.setText(i+"/");
                         setQuestionScreen(currentQuestion);
                         clickable(true);
                          reset();
                     }else {
                        Toast.makeText(GameActivity.this, "Game Over", Toast.LENGTH_SHORT).show();
                     }
                    }
            },800);
            return true;
        } else {
            selected.setBackground(getResources().getDrawable(R.drawable.option_wrong));

            final Dialog dialog = new Dialog(GameActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
            dialog.setContentView(R.layout.wrong);
            dialog.setCancelable(true);

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            (dialog.findViewById(R.id.lifeline)).setOnClickListener(v ->{
                reset();
                clickable(true);
                        dialog.dismiss();
            });

            (dialog.findViewById(R.id.closewrong)).setOnClickListener(v -> {
                reset();
                if(currentQuestion<2){
                    currentQuestion++;
                    i++;
                    current.setText(i+"/");
                    setQuestionScreen(currentQuestion);
                    clickable(true);
                    reset();
                }else {
                    Toast.makeText(GameActivity.this, "Game Over", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            });

            dialog.show();
            dialog.getWindow().setAttributes(lp);
            return false;
        }

    }

    //set question
    private void  setQuestionScreen(int n){
        if(n<3) {
            question.setText(questionItems.get(n).getQuestion());
            op1.setText(questionItems.get(n).getOp1());
            op2.setText(questionItems.get(n).getOp2());
            op3.setText(questionItems.get(n).getOp3());
            op4.setText(questionItems.get(n).getOp4());
        }
        else{
            Toast.makeText(this, "Game Over", Toast.LENGTH_SHORT).show();
        }
    }

    //load questions
    private  void loadQuestions(){
        questionItems = new ArrayList<>();
        String jsonstr = loadJSON("quizz.json");
        try{
            JSONObject jsonobject = new JSONObject(jsonstr);
            JSONArray questions = jsonobject.getJSONArray("questions");
            for(int i = 0; i<questions.length();i++){
                JSONObject question = questions.getJSONObject(i);
                String questionString = question.getString("q");
                String op1String = question.getString("op1");
                String op2String = question.getString("op2");
                String op3String = question.getString("op3");
                String op4String = question.getString("op4");
                String answerString = question.getString("ans");
                questionItems.add(new QuestionItem(
                   questionString,
                   op1String,
                   op2String,
                   op3String,
                   op4String,
                   answerString
                ));

            }
        }catch (JSONException e){
            e.printStackTrace();
        }

    }

    //load json file
    private String loadJSON(String file){
        String json="";
        try {
            InputStream is = getAssets().open(file);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json=new String(buffer,"UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    private void clickable(Boolean a) {
        op1.setClickable(a);
        op2.setClickable(a);
        op3.setClickable(a);
        op4.setClickable(a);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void reset() {
        op4.setBackground(getResources().getDrawable(R.drawable.option_background));
        op3.setBackground(getResources().getDrawable(R.drawable.option_background));
        op2.setBackground(getResources().getDrawable(R.drawable.option_background));
        op1.setBackground(getResources().getDrawable(R.drawable.option_background));
    }

    @Override
    public void onClick(View v) {
        boolean ans;
        switch (v.getId()){
            case R.id.option_1:
                ans=checkAnswer(op1);
                break;
            case R.id.option_2:
                ans=checkAnswer(op2);
                break;
            case R.id.option_3:
                ans=checkAnswer(op3);
                break;
            case R.id.option_4:
                ans=checkAnswer(op4);
                break;

            default:
                ans=false;
        }
    }
}