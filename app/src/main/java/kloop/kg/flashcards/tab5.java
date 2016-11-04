package kloop.kg.flashcards;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class tab5 extends Fragment {

    DBHelper dbHelper;
    private ArrayList<Word> list = new ArrayList<>();
    private ArrayList<String> wordList = new ArrayList<>();
    private View.OnClickListener onClickListener;
    private Button btnDeleteFromList;
    private Button btnBackBack;
    private ListView lvMain;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.lists,container,false);

        btnDeleteFromList = (Button) view.findViewById(R.id.btnDeleteFromList);
        btnBackBack = (Button) view.findViewById(R.id.btnBackBack);
        lvMain = (ListView) view.findViewById(R.id.lvMainMain);
        lvMain.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        btnDeleteFromList.setText("Добавить в изучаемые");

        setListeners();
        connectListeners();
        dbHelper = new DBHelper(getActivity());
        startActivity();

        return view;
    }

    private void setListeners() {
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                SparseBooleanArray sbArray = lvMain.getCheckedItemPositions();
                ContentValues cv = new ContentValues();

                switch (v.getId()) {
                    case R.id.btnDeleteFromList:
                        for (int i = 0; i < sbArray.size(); i++) {
                            int key = sbArray.keyAt(i);
                            if (sbArray.get(key)){
                                cv.put("hidden", "0");
                                cv.put("learning", "1");
                                cv.put("notlearned", "1");
                                db.update("mytable", cv, "_id = " + list.get(key).getId(), null);
                            }
                        }
                        wordList.clear();
                        startActivity();
                        break;
                    case R.id.btnBackBack:
                        getActivity().finish();
                        break;
                }
                dbHelper.close();
            }
        };
    }

    private void connectListeners() {
        btnDeleteFromList.setOnClickListener(onClickListener);
        btnBackBack.setOnClickListener(onClickListener);
    }

    private void startActivity() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("mytable", null, "hidden = 1", null, null, null, "word ASC");
        if (c.moveToFirst()) {
            list.clear();
            do {
                Word word = new Word();
                word.setId(c.getString(c.getColumnIndex("_id")));
                word.setTranslate(c.getString(c.getColumnIndex("translateWord")));
                word.setWord(c.getString(c.getColumnIndex("word")));
                list.add(word);
                wordList.add(word.getWord() + "\n" + word.getTranslate());
            } while (c.moveToNext());
        }
        c.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_multiple_choice, wordList);
        lvMain.setAdapter(adapter);
        dbHelper.close();
    }
}
