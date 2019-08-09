package com.example.newjapaneseapp.KanjiPage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.newjapaneseapp.ActivityMemory;
import com.example.newjapaneseapp.R;
import java.util.ArrayList;

public class KanjiPageAdapter extends RecyclerView.Adapter<KanjiPageAdapter.KanjiPageHolder>{

    private ArrayList<KanjiDetailsPage> kanjiDetailsPages = new ArrayList<>();

    public KanjiPageAdapter(ArrayList<KanjiDetailsPage> kanjiDetailsPages){
        this.kanjiDetailsPages = kanjiDetailsPages;
    }

    @Override
    public KanjiPageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.kanji_page_layout, parent, false);
        KanjiPageHolder vh = new KanjiPageHolder(listItem);
        return vh;
    }

    @Override
    public void onBindViewHolder(final KanjiPageHolder holder, int position) {
        final KanjiDetailsPage kanjiDetailsData = kanjiDetailsPages.get(position);
        holder.kanji.setText(kanjiDetailsData.getKanji());
        holder.meaning.setText(kanjiDetailsData.getKanjiMeaning());
        holder.kanji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.toggleContents(view);
            }
        });
    }

    @Override
    public int getItemCount() {
        return kanjiDetailsPages.size();
    }


    public static class KanjiPageHolder extends RecyclerView.ViewHolder {
        public TextView kanji, meaning;
        public ActivityMemory activityMemory = ActivityMemory.getInstance();
        public Context context;

        public KanjiPageHolder(View itemView) {
            super(itemView);
            this.kanji = itemView.findViewById(R.id.kanjiWord);
            this.meaning = itemView.findViewById(R.id.kanjiMeaning);
            this.context = (Context) activityMemory.getCurrentActivity();
            this.meaning.setVisibility(View.GONE);
        }

        public void toggleContents(View v){
            if(meaning.isShown()){
                slide_up(context, meaning);
                meaning.setVisibility(v.GONE);
            }else{
                meaning.setVisibility(v.VISIBLE);
                slide_down(context, meaning);
            }
        }

        public void slide_down(Context ctx, View v) {
            Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_down);
            if (a != null) {
                a.reset();
                if (v != null) {
                    v.clearAnimation();
                    v.startAnimation(a);
                }
            }
        }

        public void slide_up(Context ctx, View v) {
            Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_up);
            if (a != null) {
                a.reset();
                if (v != null) {
                    v.clearAnimation();
                    v.startAnimation(a);
                }
            }
        }
    }
}
