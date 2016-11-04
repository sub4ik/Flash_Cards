package kloop.kg.flashcards;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

public class Main2Activity extends Activity {
    DBHelper dbHelper;
    private View.OnClickListener onClickListener;
    private Button btnAdd;
    private Button btnLearn;
    private Button btnList1;
    private EditText etWord;
    private EditText etTranslate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.db_add);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        init();
        setListeners();
        connectListeners();

        etWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (etWord.isFocused()) {
                    String text = translate(etWord.getText().toString(), "ru");
                    etTranslate.setText(text);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dbHelper = new DBHelper(this);
        getTextInIntent();
    }

    String translate (String text, String to) {
        try {
            String urlStr = "https://translate.yandex.net/api/v1.5/tr.json/translate?key=trnsl.1.1.20160906T054913Z.1f4da7cc7b0dcbb7.d29686ed5de9b86400a9f918fe4a6edf3bf46277";
            URL urlObj = new URL(urlStr);
            HttpsURLConnection connection = (HttpsURLConnection)urlObj.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.writeBytes("text=" + URLEncoder.encode(text, "UTF-8") + "&lang=" + to);

            InputStream response = connection.getInputStream();
            String json = new java.util.Scanner(response).nextLine();
            int start = json.indexOf("[");
            int end = json.indexOf("]");
            String translated = json.substring(start + 2, end - 1);
            return translated;
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        return null;
    }

    private void getTextInIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra(Intent.EXTRA_TEXT)){
            String text = intent.getStringExtra(Intent.EXTRA_TEXT);
            etWord.setText("");
            etTranslate.setText("");
            etWord.setText(text);
            String text1 = translate(text, "ru");
            etTranslate.setText(text1);

            ContentValues cv = new ContentValues();
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            cv.put("word", text);
            cv.put("translateWord", text1);
            cv.put("learning", "0");
            cv.put("learned", "0");
            cv.put("notlearned", "1");
            cv.put("hidden", "0");
            db.insert("mytable", null, cv);
            Toast.makeText(getApplicationContext(),text1 , Toast.LENGTH_LONG).show();
            dbHelper.close();
            this.finish();
        } else {
            etWord.setText("");
            etTranslate.setText("");
        }
    }

    private void setListeners() {
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues cv = new ContentValues();
                String word = etWord.getText().toString();
                String translate = etTranslate.getText().toString();
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                switch (v.getId()) {
                    case R.id.btnAdd:
                        cv.put("word", word);
                        cv.put("translateWord", translate);
                        cv.put("learning", "0");
                        cv.put("learned", "0");
                        cv.put("notlearned", "1");
                        cv.put("hidden", "0");
                        db.insert("mytable", null, cv);
                        Toast.makeText(getApplicationContext(),"Добавлено!" , Toast.LENGTH_LONG).show();
                        break;
                    case R.id.btnLearn:
                        Intent intent = new Intent(Main2Activity.this, MainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.btnList1:
                        Intent intent1 = new Intent(Main2Activity.this, Main3Activity.class);
                        startActivity(intent1);
                        break;

                }
                dbHelper.close();
            }
        };
    }

    private void connectListeners() {
        btnAdd.setOnClickListener(onClickListener);
        btnLearn.setOnClickListener(onClickListener);
        btnList1.setOnClickListener(onClickListener);
    }

    private void init() {
        btnAdd = (Button)findViewById(R.id.btnAdd);
        btnLearn = (Button)findViewById(R.id.btnLearn);
        btnList1 = (Button)findViewById(R.id.btnList1);
        etWord = (EditText)findViewById(R.id.etWord);
        etTranslate = (EditText)findViewById(R.id.etTranslate);
    }

}

