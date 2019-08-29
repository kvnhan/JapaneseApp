package com.example.newjapaneseapp.Parser;

import android.content.Context;
import com.example.newjapaneseapp.ActivityMemory;
import com.example.newjapaneseapp.KanjiManagement;
import com.example.newjapaneseapp.KanjiPage.KanjiDetailsPage;
import com.example.newjapaneseapp.KanjiQuiz.Kanji;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class KanjiJsonParser {

    private String json;
    private ArrayList<Kanji> kanjiArrayList  = new ArrayList<>();
    private static final KanjiJsonParser KANJI_JSON_PARSER = new KanjiJsonParser();
    private ActivityMemory activityMemory = ActivityMemory.getInstance();

    public static KanjiJsonParser getInstance(){
        return KANJI_JSON_PARSER;
    }

    private KanjiJsonParser(){
    }

    public void generateList(){
        if(kanjiArrayList.size() == 0){
            KanjiManagement kanjiManagement = KanjiManagement.getInstance();
            loadJSONFromAsset(kanjiManagement.getJsonMap().get("n5_kanji"));
            getQuestions();
        }else{
            return;
        }
    }

    public void loadJSONFromAsset(int data) {
        try {
            Context context = (Context) activityMemory.getCurrentActivity();
            InputStream is = context.getResources().openRawResource(data);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public ArrayList<KanjiDetailsPage> getKanji_and_Meaning(){
        ArrayList<KanjiDetailsPage> kanjiDetailsPages = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("n5_kanji");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String word = obj.getString("kanji");
                word += " - " + obj.getString("kana");
                JSONArray meaningArray = obj.getJSONArray("meaning");
                String meaning = "";
                for (int j = 0; j < meaningArray.length(); j++){
                    meaning += meaningArray.getString(j) + "\n";
                }
                KanjiDetailsPage kanjiDetailsPage = new KanjiDetailsPage(word, meaning);
                kanjiDetailsPages.add(kanjiDetailsPage);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  kanjiDetailsPages;
    }

    public void getQuestions(){
        try {
            kanjiArrayList = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(json);
            //TODO: Change "n5_kanji" to a variable
            JSONArray jsonArray = jsonObject.getJSONArray("n5_kanji");
            Random random = new Random();
            for(int i=0; i < jsonArray.length(); i++){
                JSONObject obj = jsonArray.getJSONObject(i);
                Kanji kanji = null;
                int n = random.nextInt(2);
                String kanjiquestion = obj.getString("kanji");
                String kanaquestion = obj.getString("kana");
                ArrayList<String> pos_enChoices = convertJsonArray(obj.getJSONArray("pChoices"), 0, 3);
                ArrayList<String> pos_kanjiChoices = convertJsonArray(obj.getJSONArray("kanjiChoices"), 0, 3);
                ArrayList<String> temp = new ArrayList<String>();
                if(n == 0){
                    temp = convertJsonArray(obj.getJSONArray("meaning"), 0, obj.getJSONArray("meaning").length());
                    Collections.shuffle(temp);
                    pos_enChoices.add(temp.get(0));
                    Collections.shuffle(pos_enChoices);
                    kanji = new Kanji(kanjiquestion, pos_enChoices, temp);
                }
                else if(n == 1){
                    temp.add(kanjiquestion);
                    pos_kanjiChoices.add(kanjiquestion);
                    Collections.shuffle(pos_kanjiChoices);
                    kanji = new Kanji(kanaquestion, pos_kanjiChoices, temp);
                }
                kanjiArrayList.add(kanji);
            }
        }catch(Exception e){
            e.printStackTrace();

        }
    }

    public ArrayList<Kanji> getNQuestion(int n) {
        Collections.shuffle(kanjiArrayList);
        ArrayList<Kanji> topTenList = new ArrayList<Kanji>(kanjiArrayList.subList(0, n));
        return topTenList;
    }

    public ArrayList<JSONObject> shuffleJSONArray(JSONArray array) throws JSONException {
        ArrayList<JSONObject> arrayList = new ArrayList<>();
        for (int i=0; i< array.length();i++){
            arrayList.add(array.getJSONObject(i));
        }
        return arrayList;
    }

    public ArrayList<String> convertJsonArray(JSONArray jsonArray, int fromIndex, int toIndex){
        ArrayList<String> arrayList = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++){
            try {
                arrayList.add(jsonArray.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        arrayList = new ArrayList<String>(arrayList.subList(fromIndex, toIndex));
        return arrayList;
    }
    public ArrayList<Kanji> getKanjiArrayList() {
        return kanjiArrayList;
    }

    public void setKanjiArrayList(ArrayList<Kanji> kanjiArrayList) {
        this.kanjiArrayList = kanjiArrayList;
    }
}
