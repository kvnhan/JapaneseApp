package com.example.newjapaneseapp;

public class WritingQuiz {
    private String word;
    private boolean hasSeen;
    private int numTries;

    public WritingQuiz(String word, boolean hasSeen, int numTries) {
        this.word = word;
        this.hasSeen = hasSeen;
        this.numTries = numTries;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public boolean isHasSeen() {
        return hasSeen;
    }

    public void setHasSeen(boolean hasSeen) {
        this.hasSeen = hasSeen;
    }

    public int getNumTries() {
        return numTries;
    }

    public void setNumTries(int numTries) {
        this.numTries = numTries;
    }
}
