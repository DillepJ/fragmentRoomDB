package com.nic.sqlite.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nic.sqlite.R;
import com.nic.sqlite.Utils.Utils;

public class MindReadingGame extends AppCompatActivity implements View.OnClickListener {

    LinearLayout first_stage,second_stage,third_stage,fourth_stage,fifth_stage;
    EditText answer;
    Button find;
    TextView next,previous;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mind_reading_game);

        fifth_stage = findViewById(R.id.fifth_stage);
        fourth_stage = findViewById(R.id.fourth_stage);
        third_stage = findViewById(R.id.third_stage);
        second_stage = findViewById(R.id.second_stage);
        first_stage = findViewById(R.id.first_stage);

        answer = findViewById(R.id.answer);

        find = findViewById(R.id.find);

        next = findViewById(R.id.next);
        previous = findViewById(R.id.previous);

        first_stage.setVisibility(View.VISIBLE);
        second_stage.setVisibility(View.GONE);
        third_stage.setVisibility(View.GONE);
        fourth_stage.setVisibility(View.GONE);
        fifth_stage.setVisibility(View.GONE);
        find.setVisibility(View.GONE);
        answer.setVisibility(View.GONE);

        previous.setVisibility(View.GONE);
        next.setVisibility(View.VISIBLE);

        next.setOnClickListener(this::onClick);
        previous.setOnClickListener(this::onClick);
        find.setOnClickListener(this::onClick);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.next:
                if(first_stage.getVisibility()== View.VISIBLE){
                    first_stage.setVisibility(View.GONE);
                    second_stage.setVisibility(View.VISIBLE);
                    third_stage.setVisibility(View.GONE);
                    fourth_stage.setVisibility(View.GONE);
                    fifth_stage.setVisibility(View.GONE);
                    find.setVisibility(View.GONE);
                    answer.setVisibility(View.GONE);

                    previous.setVisibility(View.VISIBLE);
                    next.setVisibility(View.VISIBLE);
                    break;
                }
                if(second_stage.getVisibility()== View.VISIBLE){
                    first_stage.setVisibility(View.GONE);
                    second_stage.setVisibility(View.GONE);
                    third_stage.setVisibility(View.VISIBLE);
                    fourth_stage.setVisibility(View.GONE);
                    fifth_stage.setVisibility(View.GONE);
                    find.setVisibility(View.GONE);
                    answer.setVisibility(View.GONE);

                    previous.setVisibility(View.VISIBLE);
                    next.setVisibility(View.VISIBLE);
                    break;
                }
                if(third_stage.getVisibility()== View.VISIBLE){
                    first_stage.setVisibility(View.GONE);
                    second_stage.setVisibility(View.GONE);
                    third_stage.setVisibility(View.GONE);
                    fourth_stage.setVisibility(View.VISIBLE);
                    fifth_stage.setVisibility(View.GONE);
                    find.setVisibility(View.GONE);
                    answer.setVisibility(View.GONE);

                    previous.setVisibility(View.VISIBLE);
                    next.setVisibility(View.VISIBLE);
                    break;
                }
                if(fourth_stage.getVisibility()== View.VISIBLE){
                    first_stage.setVisibility(View.GONE);
                    second_stage.setVisibility(View.GONE);
                    third_stage.setVisibility(View.GONE);
                    fourth_stage.setVisibility(View.GONE);
                    fifth_stage.setVisibility(View.VISIBLE);
                    find.setVisibility(View.VISIBLE);
                    answer.setVisibility(View.VISIBLE);

                    previous.setVisibility(View.VISIBLE);
                    next.setVisibility(View.GONE);
                    break;
                }
                break;

            case R.id.previous:
                if(second_stage.getVisibility()== View.VISIBLE){
                    first_stage.setVisibility(View.VISIBLE);
                    second_stage.setVisibility(View.GONE);
                    third_stage.setVisibility(View.GONE);
                    fourth_stage.setVisibility(View.GONE);
                    fifth_stage.setVisibility(View.GONE);
                    find.setVisibility(View.GONE);
                    answer.setVisibility(View.GONE);

                    previous.setVisibility(View.GONE);
                    next.setVisibility(View.VISIBLE);
                    break;
                }
                if(third_stage.getVisibility()== View.VISIBLE){
                    first_stage.setVisibility(View.GONE);
                    second_stage.setVisibility(View.VISIBLE);
                    third_stage.setVisibility(View.GONE);
                    fourth_stage.setVisibility(View.GONE);
                    fifth_stage.setVisibility(View.GONE);
                    find.setVisibility(View.GONE);
                    answer.setVisibility(View.GONE);

                    previous.setVisibility(View.VISIBLE);
                    next.setVisibility(View.VISIBLE);
                    break;
                }
                if(fourth_stage.getVisibility()== View.VISIBLE){
                    first_stage.setVisibility(View.GONE);
                    second_stage.setVisibility(View.GONE);
                    third_stage.setVisibility(View.VISIBLE);
                    fourth_stage.setVisibility(View.GONE);
                    fifth_stage.setVisibility(View.GONE);
                    find.setVisibility(View.GONE);
                    answer.setVisibility(View.GONE);

                    previous.setVisibility(View.VISIBLE);
                    next.setVisibility(View.VISIBLE);
                    break;
                }
                if(fifth_stage.getVisibility()== View.VISIBLE){
                    first_stage.setVisibility(View.GONE);
                    second_stage.setVisibility(View.GONE);
                    third_stage.setVisibility(View.GONE);
                    fourth_stage.setVisibility(View.VISIBLE);
                    fifth_stage.setVisibility(View.GONE);
                    find.setVisibility(View.GONE);
                    answer.setVisibility(View.GONE);

                    previous.setVisibility(View.VISIBLE);
                    next.setVisibility(View.VISIBLE);
                    break;
                }
                break;

            case R.id.find:
                if(answer.getText().toString().equals("")){
                    Utils.showAlert(this,"Please Enter Your Answer");
                }
                else {
                    char ch1 = answer.getText().toString().charAt(0);
                    int your_number = Integer.parseInt(String.valueOf(ch1))-1;

                    Utils.showAlert(this,"Your Answer is >>>> "+ your_number);
                }
                break;
        }
    }
}
