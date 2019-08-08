package com.example.newjapaneseapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class MyDb extends SQLiteOpenHelper {
    private static final int DB_VERSION = 4;
    private static final String DB_NAME = "myDb";
    private static final String GRAMMAR_TABLE = "grammarTable";
    private static final String GA_TABLE = "gaTable";
    private static final String KEY_ID = "id";
    private static final String PARTICLE_NAME = "particleName";
    private static final String QUESTION= "question";
    private static final String HAS_SEEN = "hasSeen";
    private static final String NUM_CORRECT = "numCorrect";
    private static final String NUM_INCORRECT = "numIncorrect";

    public final static String DATABASE_PATH = "/data/data/com.pkgname/databases/";
    private static final String CREATE_GA_TABLE = "CREATE TABLE " + GA_TABLE + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + QUESTION +
            " TEXT," + HAS_SEEN + " INTEGER," + NUM_CORRECT + " INTEGER," + NUM_INCORRECT + " INTEGER " + ")";

    public MyDb(Context context){
        super(context,DB_NAME, null, DB_VERSION);
        printTable();
    }

    private boolean checkDatabaseExists() {
        boolean checkDB = false;
        try {
            String PATH = DATABASE_PATH + DB_NAME;
            File dbFile = new File(PATH);
            checkDB = dbFile.exists();

        } catch (SQLiteException e) {

        }
        return checkDB;
    }

    public void createDatabase() throws IOException {
        boolean dbExist = checkDatabaseExists();

        if (dbExist) {
            Log.v("DB Exists", "db exists");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_GA_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        // Drop older table if exist
        db.execSQL("DROP TABLE IF EXISTS " + GA_TABLE);
        // Create tables again
        onCreate(db);
    }

    public void addQuestion(String particle, GrammarQuizPage sImpleQuiz){
        //TODO: Add questions to the correct Table by particles
        SQLiteDatabase db = this.getWritableDatabase();
        if(!checkForQuestion(GA_TABLE, sImpleQuiz.getQuestion())) {
            addQuestionToGa(db, sImpleQuiz);
        }
    }

    private void addQuestionToGa(SQLiteDatabase db, GrammarQuizPage sImpleQuiz){
        ContentValues contentValues = new ContentValues();
        contentValues.put(QUESTION, sImpleQuiz.getQuestion());
        contentValues.put(HAS_SEEN, 0);
        contentValues.put(NUM_CORRECT, 0);
        contentValues.put(NUM_INCORRECT, 0);
        db.insert(GA_TABLE, null, contentValues);
        db.close();
    }

    public void updateCorrectness(String question, boolean isCorrect){
        SQLiteDatabase db = this.getWritableDatabase();
        final String whereClause = QUESTION + "=?";
        ContentValues contentValues = new ContentValues();
        String query = "SELECT " + NUM_CORRECT + ", " + NUM_INCORRECT + " FROM " + GA_TABLE + " WHERE " + QUESTION + "=?";
        Cursor c = db.rawQuery(query, new String[]{question});
        c.moveToFirst();
        if(isCorrect){
            int i = c.getInt(c.getColumnIndex(NUM_CORRECT));
            contentValues.put(NUM_CORRECT, i +1);
        }else{
            int i = c.getInt(c.getColumnIndex(NUM_INCORRECT));
            contentValues.put(NUM_INCORRECT, i +1);
        }
        db.update(GA_TABLE, contentValues, whereClause, new String[]{question});
        db.close();

    }

    public void updatetHasSeen(String question){
        //TODO: get hasSeen from the correct Table by particles
        SQLiteDatabase db = this.getWritableDatabase();
        updateHasSeenFromGa(db, question);

    }

    public void updateHasSeenFromGa(SQLiteDatabase db, String question){
        final String whereClause = QUESTION + "=?";
        String query = "SELECT " + HAS_SEEN + " FROM " + GA_TABLE + " WHERE " + QUESTION + "=?";
        Cursor c = db.rawQuery(query, new String[]{question});
        ContentValues contentValues = new ContentValues();
        c.moveToFirst();
        int i = c.getInt(c.getColumnIndex(HAS_SEEN));
        contentValues.put(HAS_SEEN, i + 1);
        db.update(GA_TABLE, contentValues, whereClause, new String[]{question});
        db.close();
    }

    public boolean checkForQuestion(String table, String question){
        Cursor c = null;
        SQLiteDatabase db = getReadableDatabase();
        try{
            String query = "SELECT * FROM " + table + " WHERE " + QUESTION + "=?";
            c = db.rawQuery(query, new String[]{question});
            if(c.moveToFirst()){
                return true;
            }
            return false;
        }finally {
        }
    }

    public void printTable(){
        Cursor c = null;
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + GA_TABLE;
        c = db.rawQuery(query, null);
        String cString = "";
        if(c.moveToFirst()){
            String[] columnNames = c.getColumnNames();
            for (String name: columnNames){
                cString += String.format("%s ][ ", name);
            }
            cString += "\n";
            do{
                for (String name: columnNames){
                    cString += String.format("%s ][ ",
                            c.getString(c.getColumnIndex(name)));
                }
                cString += "\n";
            }while(c.moveToNext());
        }

        System.out.print(cString);
    }
}
