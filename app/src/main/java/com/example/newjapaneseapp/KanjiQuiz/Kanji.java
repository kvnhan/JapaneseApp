package com.example.newjapaneseapp.KanjiQuiz;

public class Kanji {

    private String word, hiragana, english, correctAnswer;
    private String[] choices = new String[4];

    public Kanji(String word, String hiragana, String english, String correctAnswer, String[] choices) {
        this.word = word;
        this.hiragana = hiragana;
        this.english = english;
        this.correctAnswer = correctAnswer;
        this.choices = choices;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getHiragana() {
        return hiragana;
    }

    public void setHiragana(String hiragana) {
        this.hiragana = hiragana;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String[] getChoices() {
        return choices;
    }

    public void setChoices(String[] choices) {
        this.choices = choices;
    }
}
