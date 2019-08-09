package com.example.newjapaneseapp.Parser;

import android.content.Context;
import com.example.newjapaneseapp.ActivityMemory;
import com.example.newjapaneseapp.KanjiManagement;
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

    public void getQuestions(){
        try {
            kanjiArrayList = new ArrayList<>();
            JSONArray questions = new JSONArray(json);
            ArrayList<JSONObject> newList = shuffleJSONArray(questions);
            Random random = new Random();
            for(int i=0; i < questions.length(); i++){
                JSONObject obj = questions.getJSONObject(i);
                Kanji kanji = null;
                int n = random.nextInt(2);
                String question;
                question = obj.getString("word");
                if(question.equals("null")){
                    question = obj.getString("hiragana");
                }
                if(n == 0){
                    kanji = new Kanji(question, obj.getString("hiragana"), obj.getString("english"), obj.getString("english"), randomizeList(obj.getString("english"), newList, "english"));
                }
                else if(n == 1){
                    kanji = new Kanji(obj.getString("english"), "", question, question, randomizeList(question, newList, "word"));
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

    public String[] randomizeList(String answer, ArrayList<JSONObject> array, String name) throws JSONException {
        Random rand = new Random();
        String answerList[] = new String[4];
        int spaceLeft = 3;
        int array_index = rand.nextInt(4);
        ArrayList<Integer> indexList = new ArrayList<>();
        answerList[array_index] = answer;
        while(spaceLeft > 0){
            array_index = rand.nextInt(4);
            if(answerList[array_index] == null){
                int n = rand.nextInt(array.size());
                if(!array.get(n).equals("null")){
                    if(!indexList.contains(n) & !array.get(n).equals(answer)){
                        indexList.add(n);
                        spaceLeft--;
                        answerList[array_index] = array.get(n).getString(name);
                    }
                }

            }

        }
        return answerList;
    }

    public ArrayList<Kanji> getKanjiArrayList() {
        return kanjiArrayList;
    }

    public void setKanjiArrayList(ArrayList<Kanji> kanjiArrayList) {
        this.kanjiArrayList = kanjiArrayList;
    }
}
