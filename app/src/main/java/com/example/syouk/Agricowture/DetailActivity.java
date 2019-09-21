package com.example.syouk.Agricowture;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileOutputStream;
import java.io.IOException;

public class DetailActivity extends AppCompatActivity {

    private EditText editText;
    private String detailText = "";
    private String textFilePath;
    protected Boolean changeFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        final Intent intent = getIntent();
        String date = intent.getStringExtra("date");
        Log.d("date", date);
        String temText = intent.getStringExtra("detail");

        //テキストが変更されたか検知するフラグ
        changeFlag = false;

        editText = findViewById(R.id.editText);
        //詳細テキストがnullじゃなければエディットテキストにセット
        if(temText != null){
            editText.setText(temText);
        }

        //保存するテキストファイルのパス
        textFilePath = Constant.detailFilePath + "/" + date + ".txt";

        //上の日付のセット
        TextView dateText = findViewById(R.id.dateText);
        dateText.setText(date);

        //保存ボタンを押されたときの処理
        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detailText = editText.getText().toString();
                Log.d("detailText", detailText);
                SaveFileFunc();
            }
        });

        //戻るボタンを押されたときの処理
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("backButton","pushed");
                Intent intent1 = new Intent();
                intent1.putExtra("detail",detailText);
                intent1.putExtra("changeFlag",changeFlag);
                setResult(RESULT_OK,intent1);
                finish();
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //バックキーが押された時に前のアクティビティに戻る
            Log.d("BackKey","pushed");
            Intent intent1 = new Intent();
            intent1.putExtra("detail", detailText);
            intent1.putExtra("changeFlag",changeFlag);
            setResult(RESULT_OK, intent1);
            finish();
        }
        return true;
    }

    //ファイルの保存をする関数
    private void SaveFileFunc() {
        try (FileOutputStream fileOutputstream =new FileOutputStream(textFilePath)) {
            fileOutputstream.write(detailText.getBytes());
            changeFlag = true;
            Log.d("file","created");
        } catch (IOException e) {
            Log.e("error","IOException");
            e.printStackTrace();
        }
    }
}

