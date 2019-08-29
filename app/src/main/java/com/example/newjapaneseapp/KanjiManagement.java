package com.example.newjapaneseapp;

import java.util.HashMap;

public class KanjiManagement {
    private static final KanjiManagement ourInstance = new KanjiManagement();
    private String kanji_level;
    private HashMap<String, Integer> jsonMap = new HashMap<>();

    public static KanjiManagement getInstance() {
        return ourInstance;
    }

    private KanjiManagement() {
        jsonMap.put("n5_kanji", R.raw.n5_vocab);
    }

    public String getKanji_level() {
        return kanji_level;
    }

    public void setKanji_level(String kanji_level) {
        this.kanji_level = kanji_level;
    }

    public HashMap<String, Integer> getJsonMap() {
        return jsonMap;
    }

    public void setJsonMap(HashMap<String, Integer> jsonMap) {
        this.jsonMap = jsonMap;
    }
}
