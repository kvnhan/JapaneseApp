package com.example.newjapaneseapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KanjiAdapter extends RecyclerView.Adapter<KanjiAdapter.KanjiHolder> implements Runnable{

    private ArrayList<String> wordTileArrayList = new ArrayList<>();
    CopyOnWriteArrayList<String> threadSafeList;
    private  ParticleMemory currentParticleSelected =  ParticleMemory.getInstance();
    private long mLastClickTime = System.currentTimeMillis();
    private static final long CLICK_TIME_INTERVAL = 300;
    private Thread thread;
    private final String threadName = "KanjiAdapter";

    private static KanjiAdapter kanjiAdapter = new KanjiAdapter();

    public static KanjiAdapter getInstance(){
        return kanjiAdapter;
    }

    private KanjiAdapter() {
    }

    public void setupList(ArrayList<String> wordTileArrayList){
        threadSafeList = new CopyOnWriteArrayList<>();
        this.wordTileArrayList = new ArrayList<>();
        threadSafeList.addAll(wordTileArrayList);
        thread = new Thread(this, threadName);
        thread.start();
    }

    @Override
    public KanjiHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View item = layoutInflater.inflate(R.layout.word_tile, parent, false);
        KanjiHolder kh = new KanjiHolder(item);
        return kh;
    }

    @Override
    public void onBindViewHolder(final KanjiHolder holder, final int position) {
        final String wTile = wordTileArrayList.get(position);
        holder.wordTile.setText(wTile);
        holder.wordTile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long now = System.currentTimeMillis();
                if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                    return;
                }
                mLastClickTime = now;
                String word = wordTileArrayList.get(holder.getAdapterPosition());
                QuizSystem quizSystem = QuizSystem.getInstance();
                quizSystem.setTile(word);
                quizSystem.setSelectedPosition(holder.getAdapterPosition());
                quizSystem.setKanjitileClicked(true);
                quizSystem.setParticletileClicked(false);
                if(quizSystem.isSentenceClicked()){
                    if(quizSystem.getSentence().equals(word)) {
                        notifyChange();
                        QuizAnswerAdapter quizAnswerAdapter = QuizAnswerAdapter.getInstance();
                        quizAnswerAdapter.notifyAChange();
                    }else{
                        quizSystem.setKanjitileClicked(false);
                        quizSystem.setSentenceClicked(false);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return wordTileArrayList.size();
    }

    @Override
    public void run() {
        System.out.println("Thread: " + threadName + " is running");
        try {
            String regex = "[\u3040-\u30ff\u3400-\u4dbf\u4e00-\u9fff\uf900-\ufaff\uff66-\uff9f]";
            Pattern regexPattern = Pattern.compile(regex);
            for(String wordTile: threadSafeList){
                Matcher regexMatcher = regexPattern.matcher(wordTile);
                if(regexMatcher.find()) {
                    if(!currentParticleSelected.getJsonMap().containsKey(wordTile)){
                        this.wordTileArrayList.add(wordTile);
                    }
                }
            }
            Collections.shuffle(this.wordTileArrayList);
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Thread: " + threadName + " is done");
    }

    public static class KanjiHolder extends RecyclerView.ViewHolder{

        public Button wordTile;

        public KanjiHolder(View view){
            super(view);
            wordTile = view.findViewById(R.id.wordTile);
        }
    }

    public void notifyChange(){
        QuizSystem quizSystem = QuizSystem.getInstance();
        wordTileArrayList.remove(quizSystem.getSelectedPosition());
        quizSystem.reset();
        notifyDataSetChanged();
    }
}
