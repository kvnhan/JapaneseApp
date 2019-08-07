package com.example.newjapaneseapp;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonParser {
    private String json;
    private JSONObject jsonObject;
    private static final JsonParser jsonParser = new JsonParser();
    private ArrayList<GrammarQuizPage> grammarQuizPageArrayList;
    public ParticleMemory particleMemory = ParticleMemory.getInstance();
    public ActivityMemory activityMemory = ActivityMemory.getInstance();


    public static JsonParser getInstance(){
        return jsonParser;
    }

    private JsonParser(){}

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

    public JSONObject parseJSON(String particle) throws JSONException {
        jsonObject = new JSONObject(json);
        JSONObject particleObject = jsonObject.getJSONObject(particle);
        return particleObject;
    }

    public String getParticleDescription(JSONObject particleObject) throws JSONException {
        String description = particleObject.getString("description");
        return description;
    }

    public void getParticleQuestion(String particle) throws JSONException {
        grammarQuizPageArrayList = new ArrayList<GrammarQuizPage>();
        JSONObject particleObject = parseJSON(particle);
        JSONArray questions = particleObject.getJSONArray("questions");
        JSONArray pos_ans = particleObject.getJSONArray("possible_answers");
        ArrayList<String> array = new ArrayList<String>();
        array = convertJASONArray(pos_ans);
        ArrayList<Integer> viewList = new ArrayList<>();
        viewList.add(0);
        viewList.add(1);
        //MyDb myDb = new MyDb(context);
        for(int i=0; i < questions.length(); i++){
            JSONObject obj = questions.getJSONObject(i);
            String[] answerList = randomizeList(obj.getString("correct"), array);
            Collections.shuffle(viewList);
            ArrayList<String> charList = breakStringApart(obj.getString("q"));
            GrammarQuizPage grammarQuizPage = new GrammarQuizPage(obj.getString("q"), false,  obj.getString("correct"),
                    obj.getString("sound"), answerList, charList, viewList.get(0));
            grammarQuizPageArrayList.add(grammarQuizPage);
            //myDb.addQuestion("", sImpleQuiz);
        }
    }

    public ArrayList<String> convertJASONArray(JSONArray answerArray) throws JSONException{
        ArrayList<String> array = new ArrayList<String>();
        for (int i=0; i< answerArray.length();i++){
            array.add(answerArray.getString(i));
        }
        return array;
    }

    public ArrayList<GrammarQuizPage> getTenQuestion() {
        Collections.shuffle(grammarQuizPageArrayList);
        ArrayList<GrammarQuizPage> topTenList = new ArrayList<GrammarQuizPage>(grammarQuizPageArrayList.subList(0, 10));
        return topTenList;
    }

    public String[] randomizeList(String answer, ArrayList<String> array){
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
                if(!indexList.contains(n) && !array.get(n).equals(answer)){
                    indexList.add(n);
                    spaceLeft--;
                    answerList[array_index] = array.get(n);
                }
            }

        }
        return answerList;
    }

    public ArrayList<String> breakStringApart(String question){
        char[] stringArray = question.toCharArray();
        ArrayList<String> newList = new ArrayList<>();
        for (int i = 0; i < stringArray.length; i++){
            String word = Character.toString(stringArray[i]);
            newList.add(word);
        }

        return  newList;
    }

}
