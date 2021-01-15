package com.supertridents.quizz.questions;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.supertridents.quizz.GameActivity;
import com.supertridents.quizz.MainActivity;
import com.supertridents.quizz.R;

import java.util.ArrayList;

public class QuestionFragment extends Fragment implements View.OnClickListener {
    TextView question,op1,op2,op3,op4;
    ImageView next;
    ArrayList<String> set1,sop1,sop2,sop3,sop4;
    int i=1;
    public QuestionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_question, container, false);

    question = view.findViewById(R.id.question);
    op1 = view.findViewById(R.id.option_1);
    op2 = view.findViewById(R.id.option_2);
    op3 = view.findViewById(R.id.option_3);
    op4 = view.findViewById(R.id.option_4);

    set1 = new ArrayList<>();
    sop1 = new ArrayList<>();
    sop2 = new ArrayList<>();
    sop3 = new ArrayList<>();
    sop4 = new ArrayList<>();
    //Questions
    set1.add("What is the capital of Maharashtra?");
    set1.add("What is the capital of India?");
    set1.add("Who is prime minister of India?");

    //option1
        sop1.add("Nagpur");
        sop1.add("Mumbai");
        sop1.add("Arvind Kejrival");
    //option2
        sop2.add("Nashik");
        sop2.add("Delhi");
        sop2.add("Rahul Gandhi");
    //option3
        sop3.add("Mumbai");
        sop3.add("Hydrabad");
        sop3.add("Vikas Bajpai");
    //option4
        sop4.add("Pune");
        sop4.add("Kashmir");
        sop4.add("Narendra Modi");

    next = view.findViewById(R.id.next);
    question.setText(set1.get(0));
    op1.setText(sop1.get(0));
    op2.setText(sop2.get(0));
    op3.setText(sop3.get(0));
    op4.setText(sop4.get(0));
//
//      op1.setOnClickListener(v -> checkAnswer(op1));
//      op2.setOnClickListener(v -> checkAnswer(op2));
//      op3.setOnClickListener(v -> checkAnswer(op3));
//      op4.setOnClickListener(v -> checkAnswer(op4));

//      next.setOnClickListener(v ->{
//          reset();
//
//          clickable(true);
//          question.setText("Q.Who is the Prime Minister of India");
//          op1.setText("Arvind Kejrival");
//          op2.setText("Rahul Gandhi");
//          op3.setText("Narendra Modi");
//          op4.setText("Vikas Bajpai");
//      });

        op1.setOnClickListener(this::onClick);
        op2.setOnClickListener(this::onClick);
        op3.setOnClickListener(this::onClick);
        op4.setOnClickListener(this::onClick);
        return view;
    }

    private void clickable(Boolean a) {
        op1.setClickable(a);
        op2.setClickable(a);
        op3.setClickable(a);
        op4.setClickable(a);
    }

    private void reset() {
        op4.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        op3.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        op2.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        op1.setBackground(getResources().getDrawable(R.drawable.option_unselected));
    }


    private boolean checkAnswer(TextView selected) {
        String selectedAnswer = selected.getText().toString();
        if(selectedAnswer.equals("Mumbai")||selectedAnswer.equals("Delhi")||selectedAnswer.equals("Narendra Modi")) {
            selected.setBackground(getResources().getDrawable(R.drawable.option_right));
            clickable(false);

            //dialog
            final Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
            dialog.setContentView(R.layout.right);
            dialog.setCancelable(true);

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;

            (dialog.findViewById(R.id.closeright)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reset();
                    clickable(true);
                    if(i<set1.size()){
                        question.setText(set1.get(i));
                        op1.setText(sop1.get(i));
                        op2.setText(sop2.get(i));
                        op3.setText(sop3.get(i));
                        op4.setText(sop4.get(i));
                        i++;
                    }
                    else{
                        Toast.makeText(getContext(), "Finish", Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                }
            });

            dialog.show();
            dialog.getWindow().setAttributes(lp);
            return true;
        } else {
            selected.setBackground(getResources().getDrawable(R.drawable.option_wrong));
            final Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
            dialog.setContentView(R.layout.right);
            dialog.setCancelable(true);

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;

            (dialog.findViewById(R.id.closeright)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reset();
//                    clickable(true);
                    dialog.dismiss();
                }
            });

            dialog.show();
            dialog.getWindow().setAttributes(lp);
            return false;
        }

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
//        if(ans){
//            reset();
//            clickable(true);
//            question.setText("Q.Who is the Prime Minister of India");
//            op1.setText("Arvind Kejrival");
//            op2.setText("Rahul Gandhi");
//            op3.setText("Narendra Modi");
//            op4.setText("Vikas Bajpai");
//        }
    }
}