package com.example.newjapaneseapp;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;

public class QuizActivity extends AppCompatActivity implements MyActivity{

    private TextView question, answer1, answer2, answer3, answer4, num_question, soundText, soundButton;
    private Button next_button;
    private ViewFlipper quiz_view_flipper;

    private TextView englishQuestion;
    private Button nextButton;
    private RecyclerView particlesRecycler, wordsRecycler, sentenceRecycler;

    private ActivityMemory activityMemory = ActivityMemory.getInstance();
    private ParticleMemory particleMemory = ParticleMemory.getInstance();
    private JsonParser jsonParser = JsonParser.getInstance();

    private int NUM_QUESTION;
    private static int current_num = 0;
    private static boolean correctAnswerSelected = false;
    private ArrayList<GrammarQuizPage> questions = new ArrayList<GrammarQuizPage>();

    private int NUM_COL = 8;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        initializeTextView0();
        initializeTextView1();
        loadQuestion();
        setUpQuestion();
    }

    public void loadQuestion(){
        try {
            jsonParser.getParticleQuestion(particleMemory.getParticle());
            questions = jsonParser.getTenQuestion();
            NUM_QUESTION = questions.size();
            Collections.synchronizedList(questions);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void setUpQuestion(){
        current_num++;
        correctAnswerSelected = false;
        num_question.setText(current_num + "/" + NUM_QUESTION);
        if(questions.get(0).getView() == 0){
            quiz_view_flipper.setDisplayedChild(questions.get(0).getView());
            resetTextBorder();
            setTextClickable(true);
            question.setText(questions.get(0).getQuestion());
            soundText.setText(questions.get(0).getSoundText());
            answer1.setText(questions.get(0).getArray_answers()[0]);
            answer2.setText(questions.get(0).getArray_answers()[1]);
            answer3.setText(questions.get(0).getArray_answers()[2]);
            answer4.setText(questions.get(0).getArray_answers()[3]);
            setNext_button();
        }else{
            QuizSystem quizSystem = QuizSystem.getInstance();
            quizSystem.reset();
            quiz_view_flipper.setDisplayedChild(questions.get(0).getView());
            ArrayList<String> list = questions.get(0).getSingleStringArrayList();
            englishQuestion.setText(questions.get(0).getCorrectAnswer());
            initializeParticleRecycler(list);
            initializeSentenceRecycler(list);
            initializeWordsRecycler(list);
            setNext_button1();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void setNext_button(){
        next_button.setOnClickListener(new View.OnClickListener() {
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void setNext_button1(){
        final QuizAnswerAdapter quizAnswerAdapter = QuizAnswerAdapter.getInstance();
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quizAnswerAdapter.getNumOfCharLeft() != 0) {
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

    public void initializeTextView1() {
        englishQuestion = findViewById(R.id.englishQuestion);
        nextButton = findViewById(R.id.nButton);
        particlesRecycler = findViewById(R.id.particlesRecycler);
        wordsRecycler = findViewById(R.id.wordsRecycler);
        sentenceRecycler =  findViewById(R.id.sentenceRecycler);
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

    public void initializeParticleRecycler(ArrayList<String> wordTileArrayList){
        ParticlesAdapter particlesAdapter = ParticlesAdapter.getInstance();
        particlesAdapter.setupList(wordTileArrayList);
        particlesRecycler.setHasFixedSize(true);
        particlesRecycler.setAdapter(particlesAdapter);
        particlesRecycler.setLayoutManager(new GridLayoutManager(this, NUM_COL));
    }

    public void initializeWordsRecycler(ArrayList<String> wordTileArrayList){
        KanjiAdapter kanjiAdapter =KanjiAdapter.getInstance();
        kanjiAdapter.setupList(wordTileArrayList);
        wordsRecycler.setHasFixedSize(true);
        wordsRecycler.setAdapter(kanjiAdapter);
        wordsRecycler.setLayoutManager(new GridLayoutManager(this, NUM_COL));
    }

    public void initializeSentenceRecycler(ArrayList<String> wordTileArrayList){
        QuizAnswerAdapter qAdapter = QuizAnswerAdapter.getInstance();
        qAdapter.setArrayList(wordTileArrayList, sentenceRecycler);
        sentenceRecycler.setHasFixedSize(true);
        sentenceRecycler.setAdapter(qAdapter);
        sentenceRecycler.setLayoutManager(new GridLayoutManager(this, NUM_COL + 2));
    }

}
