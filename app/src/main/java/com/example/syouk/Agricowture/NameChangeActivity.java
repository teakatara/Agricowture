package com.example.syouk.Agricowture;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class NameChangeActivity extends AppCompatActivity {

    private String afterNameText;
    private EditText afterName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_change);

        //グラフアクティビティからのデータを受け取る
        final Intent intent = getIntent();
        final int position = intent.getIntExtra("Position",-1);

        //変更前の名前を描画
        TextView beforeName = findViewById(R.id.beforeName);
        beforeName.setText(Constant.cowName[position]);

        afterNameText = "";
        afterName = findViewById(R.id.afterName);

        //保存ボタンが押された時の処理
        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //入力された内容を取得
                afterNameText = afterName.getText().toString();
                Log.d("afterNameText",afterNameText);
                if(!afterNameText.equals("")) {
                    //ハッシュマップに登録
                    Constant.cowNameMap.put(Constant.cowID[position], afterNameText);
                    Constant.cowName[position] = afterNameText;
                } else {
                    //空白だった場合牛のIDにする
                    Constant.cowNameMap.remove(Constant.cowID[position]);
                    Constant.cowName[position] = Constant.cowID[position];
                }

                //テキストファイルに牛の名前を全て保存
                StringBuilder str = new StringBuilder();
                String tem;
                for(int i = 0; i < Constant.MVoA; i++){
                    tem = Constant.cowNameMap.get(Constant.cowID[i]);
                    if(tem != null){
                        str.append(Constant.cowID[i]).append(",").append(tem).append("\n");
                    }
                }
                Log.d("str",str.toString());
                try(FileOutputStream fileOutputStream = openFileOutput("cowName.txt", Context.MODE_PRIVATE)){
                    fileOutputStream.write(str.toString().getBytes());
                } catch (FileNotFoundException e) {
                    Log.e("error","FileNotFoundException");
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.e("error","IOException");
                    e.printStackTrace();
                }
            }
        });

        //戻るボタンを押されたときの処理
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent();
                setResult(RESULT_OK, intent1);
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
            setResult(RESULT_OK, intent1);
            finish();
        }
        return true;
    }
}
