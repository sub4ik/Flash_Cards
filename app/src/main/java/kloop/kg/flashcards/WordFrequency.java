package kloop.kg.flashcards;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WordFrequency extends SQLiteOpenHelper {
    public WordFrequency(Context context) {
        super(context, "frequencyWordsDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table mytable (" + "_id integer primary key autoincrement, " + "word text," + "partOfSpeech text" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
