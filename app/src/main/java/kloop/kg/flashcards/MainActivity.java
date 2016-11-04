package kloop.kg.flashcards;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends Activity {
    DBHelper dbHelper;
    WordFrequency wordFrequency;

    private View.OnClickListener onClickListener;
    private View.OnTouchListener onTouchListener;
    private ArrayList<Word> list = new ArrayList<>();
    private ArrayList<Integer> idList = new ArrayList<>();
    private ArrayList<Integer> idList2 = new ArrayList<>();
    private ArrayList<String> allFrequencyWords;

    private Random random = new Random();
    private int rndWord = -777;
    private int preWord = -777;
    private int ostatok;
    private float x1, x2;
    int qq;

    private Button btnLearned, btnAddNewWords, btnHide, btnAllWordsList, btnMix;
    private TextView txtWord;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setListeners();
        connectListeners();
        dbHelper = new DBHelper(this);
        wordFrequency = new WordFrequency(this);
    }

    private void init() {
        btnLearned = (Button) findViewById(R.id.btnLearned);
        btnAllWordsList = (Button) findViewById(R.id.btnAllWordsList);
        btnHide = (Button) findViewById(R.id.btnHide);
        btnAddNewWords = (Button) findViewById(R.id.btnAddNewWords);
        btnMix = (Button) findViewById(R.id.btnMix);
        txtWord = (TextView) findViewById(R.id.txtWord);
    }

    private void setListeners() {
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ContentValues cv = new ContentValues();
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                switch (view.getId()) {
                    case R.id.btnAddNewWords:
                        Intent intent2 = new Intent(MainActivity.this, Main2Activity.class);
                        startActivity(intent2);
                        break;
                    case R.id.btnLearned:
                        Cursor c = db.query("mytable", null, "learning = 1", null, null, null, null);
                        if (c.moveToFirst()) {
                            if (qq == 1) {
                                cv.put("learning", "0");
                                cv.put("learned", "1");
                                cv.put("notlearned", "0");
                                db.update("mytable", cv, "_id = " + list.get(preWord).getId(), null);
                                qq = 2;
                            } else {
                                cv.put("learning", "0");
                                cv.put("learned", "1");
                                cv.put("notlearned", "0");
                                db.update("mytable", cv, "_id = " + list.get(rndWord).getId(), null);
                                qq = 2;
                            }
                        }
                        c.close();
                        db = dbHelper.getWritableDatabase();
                        c = db.query("mytable", null, "learning = 1", null, null, null, null);
                        list.clear();
                        if (c.moveToFirst()) {
                            do {
                                Word word = new Word();
                                word.setId(c.getString(c.getColumnIndex("_id")));
                                word.setTranslate(c.getString(c.getColumnIndex("translateWord")));
                                word.setWord(c.getString(c.getColumnIndex("word")));
                                list.add(word);
                            } while (c.moveToNext());
                            preWord = rndWord;
                            if (list.size() > 1) {
                                while (preWord == rndWord) rndWord = random.nextInt(list.size());
                                txtWord.setText(list.get(rndWord).getWord());
                            } else if (list.size() <= 1) {
                                rndWord = random.nextInt(list.size());
                                txtWord.setText(list.get(rndWord).getWord());
                            }
                        } else txtWord.setText("Добавьте слова для изучения");
                        c.close();
                        break;
                    case R.id.btnAllWordsList:
                        Intent intent6 = new Intent(MainActivity.this, Main3Activity.class);
                        startActivity(intent6);
                        break;
                    case R.id.btnHide:
                        Cursor cc = db.query("mytable", null, "learning = 1", null, null, null, null);
                        if (cc.moveToFirst()) {
                            if (qq == 1) {
                                cv.put("learning", "0");
                                cv.put("learned", "0");
                                cv.put("notlearned", "1");
                                cv.put("hidden", "1");
                                db.update("mytable", cv, "_id = " + list.get(preWord).getId(), null);
                                qq = 2;
                            } else {
                                cv.put("learning", "0");
                                cv.put("learned", "0");
                                cv.put("notlearned", "1");
                                cv.put("hidden", "1");
                                db.update("mytable", cv, "_id = " + list.get(rndWord).getId(), null);
                                qq = 2;
                            }
                            cc.close();
                        }
                        db = dbHelper.getWritableDatabase();
                        cc = db.query("mytable", null, "learning = 1", null, null, null, null);
                        list.clear();
                        if (cc.moveToFirst()) {
                            do {
                                Word word = new Word();
                                word.setId(cc.getString(cc.getColumnIndex("_id")));
                                word.setTranslate(cc.getString(cc.getColumnIndex("translateWord")));
                                word.setWord(cc.getString(cc.getColumnIndex("word")));
                                list.add(word);
                            } while (cc.moveToNext());
                            preWord = rndWord;
                            if (list.size() > 1) {
                                while (preWord == rndWord) rndWord = random.nextInt(list.size());
                                txtWord.setText(list.get(rndWord).getWord());
                            } else if (list.size() <= 1) {
                                rndWord = random.nextInt(list.size());
                                txtWord.setText(list.get(rndWord).getWord());
                            }
                        } else txtWord.setText("Добавьте слова для изучения");
                        cc.close();
                        break;
                    case R.id.btnMix:
                        mix();
                        onStartApp();
                }
                dbHelper.close();
            }
        };

        onTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // нажатие
                        x1 = event.getX();
                        break;
                    case MotionEvent.ACTION_MOVE: // движение

                        break;
                    case MotionEvent.ACTION_UP: // отпускание
                    case MotionEvent.ACTION_CANCEL:
                        x2 = event.getX();
                        if (x2 > (x1 + 40)) {
                            //Toast.makeText(getApplicationContext(), "srabotalo to left", Toast.LENGTH_LONG).show();
                            if (preWord >= 0) {
                                if (list.size() == 0) break;
                                txtWord.setText(list.get(preWord).getWord());
                                qq = 1;
                            }
                        } else if ((x2 + 40 )< x1) {
                            if (qq == 1) {
                                if (list.size() > 0) {
                                txtWord.setText(list.get(rndWord).getWord());
                                qq = 2;
                                }
                            } else if (list.size() != 0) {
                                preWord = rndWord;
                                if (list.size() > 1) while (rndWord == preWord) rndWord = random.nextInt(list.size());
                                txtWord.setText(list.get(rndWord).getWord());
                                qq = 2;
                            } else txtWord.setText("Добавьте слова для изучения");
                        } else if (Math.abs(x1-x2) < 40) {
                            if (qq == 1) {
                                if (list.size() > 0)
                                    txtWord.setText(list.get(preWord).getTranslate());
                                else txtWord.setText("Добавьте слова для изучения");
                            } else {
                                if (list.size() > 0)
                                    txtWord.setText(list.get(rndWord).getTranslate());
                                else txtWord.setText("Добавьте слова для изучения");
                            }
                        }
                        break;
                }
                return true;
            }
        };
    }

    private void connectListeners() {
        btnLearned.setOnClickListener(onClickListener);
        btnAllWordsList.setOnClickListener(onClickListener);
        btnHide.setOnClickListener(onClickListener);
        btnAddNewWords.setOnClickListener(onClickListener);
        btnMix.setOnClickListener(onClickListener);
        txtWord.setOnTouchListener(onTouchListener);
    }

    private void mix() {
        Cursor c;
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        for (int i = 0; i < list.size(); i++) {
            cv.put("learning", "0");
            db.update("mytable", cv, "_id = " + list.get(i).getId(), null);
        }
        c = db.query("mytable", null, "notlearned = 1", null, null, null, null);
        if (c.moveToFirst()) {
            list.clear();
            do {
                Word word = new Word();
                word.setId(c.getString(c.getColumnIndex("_id")));
                word.setWord(c.getString(c.getColumnIndex("word")));
                list.add(word);
            } while (c.moveToNext());
        }
        c.close();
        allFrequencyWords = new ArrayList<String>();
        SQLiteDatabase wF = wordFrequency.getWritableDatabase();
        c = wF.query("mytable", null, null, null, null, null, null);
        if (c.moveToFirst()) {
            allFrequencyWords.clear();
            do {
                allFrequencyWords.add(c.getString(c.getColumnIndex("word")));
            } while (c.moveToNext());
        }
        c.close();
        //int k = list.size() * 9 / 10;
        int k = 10;
        Toast.makeText(getApplicationContext(), Integer.toString(k), Toast.LENGTH_LONG).show();
        int kk = 0;
        for (int i = 0; i < allFrequencyWords.size(); i++) {
            for (int j = 0; j < list.size(); j++) {
                if (list.get(j).getWord().equals(allFrequencyWords.get(i))) {
                    kk++;
                    cv.put("learning", "1");
                    db.update("mytable", cv, "_id = " + list.get(j).getId(), null);
                    if (kk == k) break;
                }
            }
            if (kk == k) break;
        }
        //----____----____----____----____----____----____----____----____----____----____----____----____----____----____
        //____----____----____----____----____----____----____----____----____----____----____----____----____----____----
        //----____----____----____----____----____----____----____----____----____----____----____----____----____----____
        //____----____----____----____----____----____----____----____----____----____----____----____----____----____----
        //----____----____----____----____----____----____----____----____----____----____----____----____----____----____
        //____----____----____----____----____----____----____----____----____----____----____----____----____----____----
        ostatok = k - kk;
        list.clear();
        c = db.query("mytable", null, "notlearned = 1 AND learning = 0", null, null, null, null);
        if (c.moveToFirst()) {
            idList.clear();
            do {
                idList.add(c.getInt(c.getColumnIndex("_id")));
            } while (c.moveToNext());
            if (ostatok <= idList.size()) {
                idList2 = getRandomNumber(ostatok, idList.size());
                for (int i = 0; i < idList2.size(); i++) {
                    cv.put("learning", "1");
                    db.update("mytable", cv, "_id = " + idList.get(i).toString(), null);
                }
            } else {
                for (int i = 0; i < idList.size(); i++) {
                    cv.put("learning", "1");
                    db.update("mytable", cv, "_id = " + idList.get(i).toString(), null);
                }
            }
        }
        c.close();
        //----____----____----____----____----____----____----____----____----____----____----____----____----____----____
        //____----____----____----____----____----____----____----____----____----____----____----____----____----____----
        //----____----____----____----____----____----____----____----____----____----____----____----____----____----____
        //____----____----____----____----____----____----____----____----____----____----____----____----____----____----
        //----____----____----____----____----____----____----____----____----____----____----____----____----____----____
        //____----____----____----____----____----____----____----____----____----____----____----____----____----____----

        list.clear();
        c = db.query("mytable", null, "learned = 1", null, null, null, null);
        if (c.moveToFirst()) {
            idList.clear();
            do {
                idList.add(c.getInt(c.getColumnIndex("_id")));
            } while (c.moveToNext());
            k = idList.size() / 10;
            idList2 = getRandomNumber(k, idList.size());
            for (int i = 0; i < idList2.size(); i++) {
                cv.put("learning", "1");
                db.update("mytable", cv, "_id = " + idList.get(i).toString(), null);
            }
        }
        c.close();
        db.close();
        wF.close();
        allFrequencyWords.clear();
    }

    private void onStartApp() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("mytable", null, null, null, null, null, null);
        list.clear();
        if (c.getCount() != 0) {
            c.close();
            c = db.query("mytable", null, "learning = 1", null, null, null, null);
            if (c.moveToFirst()) {
                list.clear();
                do {
                    Word word = new Word();
                    word.setId(c.getString(c.getColumnIndex("_id")));
                    word.setTranslate(c.getString(c.getColumnIndex("translateWord")));
                    word.setWord(c.getString(c.getColumnIndex("word")));
                    list.add(word);
                } while (c.moveToNext());
                preWord = rndWord;
                if (list.size() > 1) {
                    while (preWord == rndWord) rndWord = random.nextInt(list.size());
                    txtWord.setText(list.get(rndWord).getWord());
                } else {
                    rndWord = random.nextInt(list.size());
                    txtWord.setText(list.get(rndWord).getWord());
                }
            } else txtWord.setText("Добавьте слова для изучения");
            c.close();
            dbHelper.close();
        } else txtWord.setText("Добавьте слова для изучения");

        String mCSVfile = "5000words.csv";
        AssetManager manager = getAssets();
        InputStream inStream = null;
        try {
            inStream = manager.open(mCSVfile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        SQLiteDatabase frequency = wordFrequency.getWritableDatabase();
        Cursor cc = frequency.query("mytable", null, null, null, null, null, null);
        ContentValues cv = new ContentValues();

        if (cc.getCount() == 0) {
            BufferedReader buffer = new BufferedReader(new InputStreamReader(inStream));
            String line = "";
            try {
                while ((line = buffer.readLine()) != null) {
                    String[] colums = line.split(";", 2);
                    cv.put("word", colums[0]);
                    cv.put("partOfSpeech", colums[1]);
                    frequency.insert("mytable", null, cv);
                }
                Toast.makeText(getApplicationContext(), "Frequency words added", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        cc.close();
    }

    @Override
    protected void onStart() {
        super.onStart();
        onStartApp();
    }

    private ArrayList<Integer> getRandomNumber(int k, int kk) {
        ArrayList<Integer> numbersGenerated = new ArrayList<>();

        for (int i = 0; i < k; i++) {
            Random randNumber = new Random();
            int iNumber = randNumber.nextInt(kk);

            if (!numbersGenerated.contains(iNumber)) {
                numbersGenerated.add(iNumber);
            } else {
                i--;
            }
        }
        return numbersGenerated;
    }
}

