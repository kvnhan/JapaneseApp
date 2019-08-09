package com.example.newjapaneseapp.KanjiPage;

public class KanjiDetailsPage {
    private String kanji, kanjiMeaning;

    public KanjiDetailsPage(String kanji, String kanjiMeaning) {
        this.kanji = kanji;
        this.kanjiMeaning = kanjiMeaning;
    }

    public String getKanji() {
        return kanji;
    }

    public void setKanji(String kanji) {
        this.kanji = kanji;
    }

    public String getKanjiMeaning() {
        return kanjiMeaning;
    }

    public void setKanjiMeaning(String kanjiMeaning) {
        this.kanjiMeaning = kanjiMeaning;
    }
}
