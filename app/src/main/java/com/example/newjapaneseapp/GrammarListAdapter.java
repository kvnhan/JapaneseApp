package com.example.newjapaneseapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import java.util.ArrayList;

public class GrammarListAdapter extends RecyclerView.Adapter<GrammarListAdapter.MyViewHolder>{

    private ArrayList<GrammarDetails> grammarDetailsArrayList;
    public ActivityMemory activityMemory = ActivityMemory.getInstance();


    public GrammarListAdapter(ArrayList<GrammarDetails> grammarDetailsArrayList) {
        this.grammarDetailsArrayList = grammarDetailsArrayList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView grammar, grammarDes;
        public Button quizButton;
        public ParticleMemory particleMemory = ParticleMemory.getInstance();
        public ActivityMemory activityMemory = ActivityMemory.getInstance();
        public Context context;


        public MyViewHolder(View v) {
            super(v);
            this.grammar = v.findViewById(R.id.n5);
            this.grammarDes = v.findViewById(R.id.grammarDes);
            this.quizButton = v.findViewById(R.id.quizButton);
            this.context = (Context) activityMemory.getCurrentActivity();
            grammarDes.setVisibility(v.GONE);
            quizButton.setVisibility(v.GONE);
        }

        public void toggleContents(View v, TextView text){
            particleMemory.setParticle(text.getText().toString());
            if(grammarDes.isShown()){
                slide_up(context, grammarDes);
                grammarDes.setVisibility(v.GONE);
                quizButton.setVisibility(v.GONE);
            }else{
                grammarDes.setVisibility(v.VISIBLE);
                quizButton.setVisibility(v.VISIBLE);
                slide_down(context, grammarDes);
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

    @Override
    public GrammarListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.grammar_des_layout, parent, false);
        MyViewHolder vh = new MyViewHolder(listItem);
        return vh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final GrammarDetails grammarDetailsData = grammarDetailsArrayList.get(position);
        holder.grammar.setText(grammarDetailsData.getGrammar());
        holder.grammarDes.setText(grammarDetailsData.getGrammarDes());
        holder.grammar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.toggleContents(view, holder.grammar);
            }
        });
        holder.quizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityMemory activityMemory = ActivityMemory.getInstance();
                ParticleMemory particleMemory = ParticleMemory.getInstance();
                JsonParser jsonParser = JsonParser.getInstance();
                try {
                    jsonParser.loadJSONFromAsset(particleMemory.getJsonMap().get(particleMemory.getParticle()));
                    jsonParser.getParticleQuestion(particleMemory.getParticle());
                    Intent intent = new Intent((Context)activityMemory.getCurrentActivity(), QuizActivity.class);
                    ((Context) activityMemory.getCurrentActivity()).startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }

    @Override
    public int getItemCount() {
        return grammarDetailsArrayList.size();
    }
}
