package kloop.kg.flashcards;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {super(context, "mydb", null, 1);}

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table mytable (" + "_id integer primary key autoincrement, " + "word text, " + "translateWord text," + "learning integer," + "learned integer," + "notlearned integer," + "hidden integer" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

