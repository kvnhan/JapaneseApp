package com.example.newjapaneseapp.GrammarQuiz;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;
import com.example.newjapaneseapp. ParticleMemory;
import com.example.newjapaneseapp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;

public class ParticlesAdapter extends RecyclerView.Adapter<ParticlesAdapter.ParticleHolder> implements Runnable{

    private ArrayList<String> wordTileArrayList = new ArrayList<>();
    private CopyOnWriteArrayList<String> threadSafeList;
    private ParticleMemory currentParticleSelected = ParticleMemory.getInstance();
    private long mLastClickTime = System.currentTimeMillis();
    private static final long CLICK_TIME_INTERVAL = 300;
    private Thread thread;
    private final String threadName = "ParticleAdapter";
    private static ParticlesAdapter particlesAdapter = new ParticlesAdapter();

    public static ParticlesAdapter getInstance(){
        return particlesAdapter;
    }

    private ParticlesAdapter() {
    }

    public void setupList(ArrayList<String> wordTileArrayList){
        threadSafeList = new CopyOnWriteArrayList<>();
        this.wordTileArrayList = new ArrayList<>();
        threadSafeList.addAll(wordTileArrayList);
        thread = new Thread(this, threadName);
        thread.start();
    }
    @Override
    public ParticleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View item = layoutInflater.inflate(R.layout.word_tile, parent, false);
        ParticleHolder ph = new ParticleHolder(item);
        return ph;
    }

    @Override
    public void onBindViewHolder(final ParticleHolder holder, final int position) {
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
                quizSystem.setSelectedPosition(position);
                quizSystem.setTile(word);
                quizSystem.setParticletileClicked(true);
                quizSystem.setKanjitileClicked(false);
                QuizAnswerAdapter quizAnswerAdapter = QuizAnswerAdapter.getInstance();
                if(quizSystem.isSentenceClicked()){
                    if(quizSystem.getSentence().equals(word)){
                        quizAnswerAdapter.notifyAChange();
                    }else {
                        quizSystem.setParticletileClicked(false);
                        quizSystem.setSentenceClicked(false);
                        quizAnswerAdapter.resetText();
                    }
                    notifyChange();
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
            for(String wordTile: threadSafeList){
                if(currentParticleSelected.getJsonMap().containsKey(wordTile)){
                    this.wordTileArrayList.add(wordTile);
                }
            }
            Collections.shuffle(this.wordTileArrayList);
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Thread: " + threadName + " is done");
    }

    public static class ParticleHolder extends RecyclerView.ViewHolder{

        public Button wordTile;

        public ParticleHolder(View itemView) {
            super(itemView);
            wordTile = itemView.findViewById(R.id.wordTile);
        }
    }

    public void notifyChange(){
        QuizSystem quizSystem = QuizSystem.getInstance();
        wordTileArrayList.remove(quizSystem.getSelectedPosition());
        quizSystem.reset();
        notifyDataSetChanged();
    }

    public void notifyInccorrectChange(String word){
        QuizSystem quizSystem = QuizSystem.getInstance();
        wordTileArrayList.remove(word);
        quizSystem.reset();
        notifyDataSetChanged();
    }
}
