package com.example.newjapaneseapp.KanjiQuiz;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.newjapaneseapp.MainActivity;
import com.example.newjapaneseapp.MyActivity;
import com.example.newjapaneseapp.Parser.KanjiJsonParser;
import com.example.newjapaneseapp.R;

import java.util.ArrayList;

public class KanjiQuizActivity extends AppCompatActivity implements MyActivity {

    private TextView question, answer1, answer2, answer3, answer4, num_question, soundText, soundButton, cancelMessage, continueCurrent;
    private Button next_button;
    private ViewFlipper quiz_view_flipper;

    private int NUM_QUESTION = 10;
    private static int current_num = 0;
    private static boolean correctAnswerSelected = false;
    private ArrayList<Kanji> questions = new ArrayList<Kanji>();

    private static final long CLICK_TIME_INTERVAL = 300;
    private long mLastClickTime = System.currentTimeMillis();

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initializeTextView0();
        KanjiJsonParser kanjiJsonParser = KanjiJsonParser.getInstance();
        questions = kanjiJsonParser.getNQuestion(NUM_QUESTION);

        setUpQuestion();
        setNext_button();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                final Intent intent = new Intent(KanjiQuizActivity.this, MainActivity.class);
                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.warning_popup_layout);

                cancelMessage = dialog.findViewById(R.id.cancelMessage);
                continueCurrent = dialog.findViewById(R.id.continueCurrent);
                TextView xIcon = dialog.findViewById(R.id.xIcon);
                TextView continueIcon = dialog.findViewById(R.id.continueIcon);

                View.OnClickListener yesListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        long now = System.currentTimeMillis();
                        if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                            return;
                        }
                        mLastClickTime = now;
                        startActivity(intent);
                        finish();
                    }
                };

                View.OnClickListener noListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        long now = System.currentTimeMillis();
                        if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                            return;
                        }
                        mLastClickTime = now;
                        dialog.cancel();
                    }
                };
                cancelMessage.setOnClickListener(noListener);
                xIcon.setOnClickListener(noListener);
                continueCurrent.setOnClickListener(yesListener);
                continueIcon.setOnClickListener(yesListener);

                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void initializeTextView0() {
        question = findViewById(R.id.quiz_question);
        next_button = findViewById(R.id.nButton);
        soundText = findViewById(R.id.soundText);
        answer1 = findViewById(R.id.answer1);
        answer2 = findViewById(R.id.answer2);
        answer3 = findViewById(R.id.answer3);
        answer4 = findViewById(R.id.answer4);
        num_question = findViewById(R.id.numQuestion);
        quiz_view_flipper = findViewById(R.id.quiz_view_flipper);

        View.OnClickListener textListener = new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                TextView tv = (TextView) view;
                if(tv.getText().equals(questions.get(0).getCorrectAnswer())) {
                    tv.setBackground(view.getResources().getDrawable(R.drawable.border_green));
                    setTextClickable(false);
                    correctAnswerSelected = true;
                    //myDb.updateCorrectness(questions.get(0).getQuestion(), true);
                }else{
                    //myDb.updateCorrectness(questions.get(0).getQuestion(), true);
                    tv.setBackground(view.getResources().getDrawable(R.drawable.border_red));
                }
            }
        };

        answer1.setOnClickListener(textListener);
        answer2.setOnClickListener(textListener);
        answer3.setOnClickListener(textListener);
        answer4.setOnClickListener(textListener);
    }

    public void setTextClickable(boolean b){
        answer1.setClickable(b);
        answer2.setClickable(b);
        answer3.setClickable(b);
        answer4.setClickable(b);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void resetTextBorder(){
        answer1.setBackground(answer1.getResources().getDrawable(R.drawable.border));
        answer2.setBackground(answer2.getResources().getDrawable(R.drawable.border));
        answer3.setBackground(answer3.getResources().getDrawable(R.drawable.border));
        answer4.setBackground(answer4.getResources().getDrawable(R.drawable.border));
    }

    public void setNext_button(){
        next_button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                if (!correctAnswerSelected) {
                    Toast.makeText(getApplicationContext(),"Please Complete Your Answer", Toast.LENGTH_LONG).show();
                } else {
                    //myDb.updatetHasSeen(questions.get(0).getQuestion());
                    questions.remove(0);
                    setUpQuestion();
                    if (questions.size() == 1) {
                        next_button.setText("Finish");
                        finishButton();
                    }
                }
            }
        });
    }

    public void finishButton(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void setUpQuestion(){
        current_num++;
        correctAnswerSelected = false;
        quiz_view_flipper.setDisplayedChild(2);
        num_question.setText(current_num + "/" + NUM_QUESTION);
        resetTextBorder();
        setTextClickable(true);
        question.setText(questions.get(0).getWord());
        soundText.setText(questions.get(0).getHiragana());
        answer1.setText(questions.get(0).getChoices()[0]);
        answer2.setText(questions.get(0).getChoices()[1]);
        answer3.setText(questions.get(0).getChoices()[2]);
        answer4.setText(questions.get(0).getChoices()[3]);

    }
}
