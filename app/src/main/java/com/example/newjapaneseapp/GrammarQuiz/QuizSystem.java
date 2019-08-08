package com.example.newjapaneseapp.GrammarQuiz;

public class QuizSystem {

    private static final QuizSystem quizSystem = new QuizSystem();
    private boolean particletileClicked = false;
    private boolean kanjitileClicked = false;
    private boolean sentenceClicked = false;
    private String tile = "";
    private String sentence = "";
    private int selectedPosition;

    public static QuizSystem getInstance(){
        return quizSystem;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public boolean isParticletileClicked() {
        return particletileClicked;
    }

    public void setParticletileClicked(boolean particletileClicked) {
        this.particletileClicked = particletileClicked;
    }

    public boolean isKanjitileClicked() {
        return kanjitileClicked;
    }

    public void setKanjitileClicked(boolean kanjitileClicked) {
        this.kanjitileClicked = kanjitileClicked;
    }

    public boolean isSentenceClicked() {
        return sentenceClicked;
    }

    public void setSentenceClicked(boolean sentenceClicked) {
        this.sentenceClicked = sentenceClicked;
    }

    public void reset(){
        sentenceClicked = false;
        particletileClicked = false;
        kanjitileClicked = false;
        tile = "";
        sentence = "";
    }

    public String getTile() {
        return tile;
    }

    public void setTile(String tile) {
        this.tile = tile;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }
}
