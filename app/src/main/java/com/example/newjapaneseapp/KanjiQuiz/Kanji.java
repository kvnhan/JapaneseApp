package com.example.newjapaneseapp.KanjiQuiz;

import java.util.ArrayList;

public class Kanji {

    private String question;
    private ArrayList<String> choices = new ArrayList<String>();
    private ArrayList<String> correctAnswer = new ArrayList<String>();

    public Kanji(String question, ArrayList<String> choices, ArrayList<String> correctAnswer) {
        this.question = question;
        this.choices = choices;
        this.correctAnswer = correctAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<String> getChoices() {
        return choices;
    }

    public void setChoices(ArrayList<String> choices) {
        this.choices = choices;
    }

    public ArrayList<String> getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(ArrayList<String> correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}
