package com.example.newjapaneseapp.GrammarQuiz;

import java.util.ArrayList;

public class GrammarQuizPage {

    private String question;
    private boolean questionSeen;
    private String correctAnswer;
    private String soundText;
    private String array_answers[] = new String[4];
    private ArrayList<String> singleStringArrayList = new ArrayList<String>();
    private int view;

    public GrammarQuizPage(String question, boolean questionSeen, String correctAnswer, String soundText, String[] array_answers, ArrayList<String> singleStringArrayList, int view) {
        this.question = question;
        this.questionSeen = questionSeen;
        this.correctAnswer = correctAnswer;
        this.soundText = soundText;
        this.array_answers = array_answers;
        this.view = view;
        this.singleStringArrayList = singleStringArrayList;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public boolean isQuestionSeen() {
        return questionSeen;
    }

    public void setQuestionSeen(boolean questionSeen) {
        this.questionSeen = questionSeen;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getSoundText() {
        return soundText;
    }

    public void setSoundText(String soundText) {
        this.soundText = soundText;
    }

    public String[] getArray_answers() {
        return array_answers;
    }

    public void setArray_answers(String[] array_answers) {
        this.array_answers = array_answers;
    }

    public ArrayList<String> getSingleStringArrayList() {
        return singleStringArrayList;
    }

    public void setSingleStringArrayList(ArrayList<String> singleStringArrayList) {
        this.singleStringArrayList = singleStringArrayList;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }
}
