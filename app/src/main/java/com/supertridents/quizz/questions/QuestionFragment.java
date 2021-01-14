package com.supertridents.quizz.questions;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.supertridents.quizz.R;

public class QuestionFragment extends Fragment {
    TextView question,op1,op2,op3,op4;
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

    question.setText("Q.What is Capital of Maharashtra?");
    op1.setText("Nagpur");
    op2.setText("Nashik");
    op3.setText("Mumbai");
    op4.setText("Pune");

      op1.setOnClickListener(v -> checkAnswer(op1));
      op2.setOnClickListener(v -> checkAnswer(op2));
      op3.setOnClickListener(v -> checkAnswer(op3));
      op4.setOnClickListener(v -> checkAnswer(op4));
        return view;
    }


    private void checkAnswer(TextView selected) {
        String selectedAnswer = selected.getText().toString();
        if(selectedAnswer.equals("Mumbai")) {
            selected.setBackground(getResources().getDrawable(R.drawable.option_right));
        } else {
            selected.setBackground(getResources().getDrawable(R.drawable.option_wrong));
        }
    }
}