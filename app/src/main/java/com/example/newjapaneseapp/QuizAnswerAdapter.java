package com.example.newjapaneseapp;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import com.example.newjapaneseapp.ActivityMemory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuizAnswerAdapter extends RecyclerView.Adapter<QuizAnswerAdapter.QuizAnswerHolder> implements Runnable{

    private ArrayList<String> wordTileArray;
    private ArrayList<WritingQuiz> wordMap = new ArrayList<WritingQuiz>();
    ActivityMemory savedView = ActivityMemory.getInstance();
    private Thread thread;
    private final String threadName = "SentenceAdapter";

    private static QuizAnswerAdapter quizAnswerAdapter = new QuizAnswerAdapter();
    private int currentPosition;
    private RecyclerView recyclerView;
    private int numOfCharLeft;

    public static QuizAnswerAdapter getInstance(){
        return quizAnswerAdapter;
    }

    private QuizAnswerAdapter() {

    }

    public void setArrayList(ArrayList<String> wordTileArray, RecyclerView view){
        this.recyclerView = view;
        currentPosition = 0;
        wordMap = new ArrayList<WritingQuiz>();
        this.wordTileArray = wordTileArray;
        numOfCharLeft = 0;
        thread = new Thread(this, threadName);
        thread.start();
    }
    @Override
    public QuizAnswerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View item = layoutInflater.inflate(R.layout.sentence_place_layout, parent, false);
        QuizAnswerHolder aqh = new QuizAnswerHolder(item);
        return aqh;
    }

    @Override
    public void onBindViewHolder(final QuizAnswerHolder holder, final int position) {
        final String wTile = wordMap.get(position).getWord();
        WritingQuiz writingQuiz = wordMap.get(position);
        Context context = (Context) savedView.getCurrentActivity();

        final View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuizSystem quizSystem = QuizSystem.getInstance();
                String word = wordTileArray.get(holder.getAdapterPosition());
                currentPosition = holder.getAdapterPosition();
                quizSystem.setSentence(word);
                quizSystem.setSentenceClicked(true);
                int n = wordMap.get(currentPosition).getNumTries();
                wordMap.get(currentPosition).setNumTries(n + 1);
                if(quizSystem.isKanjitileClicked()){
                    KanjiAdapter kanjiAdapter = KanjiAdapter.getInstance();
                    if(quizSystem.getTile().equals(word)){
                        kanjiAdapter.notifyChange();
                        notifyAChange();
                        quizSystem.reset();
                        return;
                    }
                    kanjiAdapter.notifyInccorrectChange(word);
                    quizSystem.setKanjitileClicked(false);
                    quizSystem.setSentenceClicked(false);
                    resetText();

                }else if (quizSystem.isParticletileClicked()){
                    ParticlesAdapter particlesAdapter = ParticlesAdapter.getInstance();
                    if(quizSystem.getTile().equals(word)){
                        particlesAdapter.notifyChange();
                        notifyAChange();
                        quizSystem.reset();
                        return;
                    }
                    particlesAdapter.notifyInccorrectChange(word);
                    quizSystem.setParticletileClicked(false);
                    quizSystem.setSentenceClicked(false);
                    resetText();
                }else{
                    view.setBackgroundResource(R.drawable.hightlight_word);
                }
            }
        };

        if(!writingQuiz.isHasSeen()) {
            holder.answerHolder.setText("    ");
        }else{
            if(wordMap.get(currentPosition).getNumTries() > 1){
                holder.answerHolder.setBackgroundResource(R.drawable.writing_quiz_wrong_answer);
                holder.answerHolder.setEnabled(false);
                holder.answerHolder.setTextColor(Color.BLACK);
            }else if (wordMap.get(currentPosition).getNumTries() == 1){
                holder.answerHolder.setBackgroundResource(R.drawable.writing_quiz_right_answer);
                holder.answerHolder.setEnabled(false);
                holder.answerHolder.setTextColor(Color.BLACK);
            }
            holder.answerHolder.setText(wTile);
        }

        holder.answerHolder.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return wordTileArray.size();
    }

    @Override
    public void run() {
        System.out.println("Thread: " + threadName + " is running");
        try {
            String regex = "[\u3040-\u30ff\u3400-\u4dbf\u4e00-\u9fff\uf900-\ufaff\uff66-\uff9f]";
            Pattern regexPattern = Pattern.compile(regex);
            for(int i = 0; i < this.wordTileArray.size(); i++){
                Matcher regexMatcher = regexPattern.matcher(this.wordTileArray.get(i));
                WritingQuiz writingQuiz;
                if(regexMatcher.find()) {
                    writingQuiz = new WritingQuiz(this.wordTileArray.get(i), false, 0);
                    numOfCharLeft++;
                }else{
                    writingQuiz = new WritingQuiz(this.wordTileArray.get(i), true, -1);
                }
                wordMap.add(writingQuiz);
            }
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Thread: " + threadName + " is done");
    }

    public static class QuizAnswerHolder extends RecyclerView.ViewHolder{

        public EditText answerHolder;
        public QuizAnswerHolder(View view){
            super(view);
            answerHolder = view.findViewById(R.id.answerHolder);
        }
    }

    public void notifyAChange(){
        wordMap.get(currentPosition).setHasSeen(true);
        numOfCharLeft--;
        notifyItemChanged(currentPosition);
    }

    public void resetText(){
        wordMap.get(currentPosition).setHasSeen(true);
        wordMap.get(currentPosition).setNumTries(2);
        numOfCharLeft--;
        notifyItemChanged(currentPosition);
    }
    public int getNumOfCharLeft() {
        return numOfCharLeft;
    }

    public void setNumOfCharLeft(int numOfCharLeft) {
        this.numOfCharLeft = numOfCharLeft;
    }

}
