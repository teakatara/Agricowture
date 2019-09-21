package com.example.syouk.Agricowture;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class CalendarActivity extends AppCompatActivity {

    private TextView dateText;
    private TextView detailText;
    private String date;
    private String detail = "";
    private String fileContent = "";
    private boolean dateClickFlag;
    private HashMap<String,String> detailFilename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        //ここからセットアップ
        Constant.detailFilePath = MyApplication.getAppContext().getFilesDir().getPath() + "/detailText";
        Log.d("file path",Constant.detailFilePath);
        detailFilename = new HashMap<>();
        //詳細ファイルの読み込み
        File dir = new File(Constant.detailFilePath);
        Log.d("File dir","instanced");
        if(!dir.mkdir()) {
            File[] list = dir.listFiles();
            Log.d("File list", "created");
            if(list != null){
                Log.d("list", Arrays.toString(list));
                Log.d("listLength",String.valueOf(list.length));
                for (int i = 0; i < list.length; i++) {
                    Log.d("File read", "for:" + Integer.toString(i));
                    try {
                        FileReader fileReader = new FileReader(list[i]);
                        int data;
                        fileContent = "";
                        while ((data = fileReader.read()) != -1) {
                            fileContent += (char) data;
                        }
                        fileReader.close();
                    } catch (FileNotFoundException e) {
                        Log.e("error","FileNotFoundException");
                        e.printStackTrace();
                    } catch (IOException e) {
                        Log.e("error","IOException");
                        e.printStackTrace();
                    }
                    Log.d("File read", "completed");
                    detailFilename.put(list[i].getName(), fileContent);
                    Log.d(list[i].getName() + "\n", "" + fileContent);
                }
            } else {
                Log.d("fileList","null");
            }
        } else {
            Log.d("mkdir","Done");
        }

        dateClickFlag = false;
        //セットアップ終了

        dateText = findViewById(R.id.dateText);
        detailText = findViewById(R.id.detailText);
        CalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                //カレンダーの日付をタップしたときの処理
                dateClickFlag = true;
                date = i + "年" + (i1 + 1) + "月" + i2 + "日";
                dateText.setText(date);
                Log.d("clickDate",date);
                //詳細テキストのハッシュマップからgetして存在している場合、詳細欄に描画
                if((detail = detailFilename.get(date + ".txt")) != null){
                    detailText.setText(detail);
                } else {
                    detailText.setText("");
                }

            }
        });

        //編集ボタンを押されたときの処理
        Button editButton = findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dateClickFlag){
                    Log.d("date",date);
                    Intent intent = new Intent(CalendarActivity.this,DetailActivity.class);
                    intent.putExtra("date",date);
                    intent.putExtra("detail",detailFilename.get(date + ".txt"));
                    Log.d("putDate",date);
                    int requestCode = 1000;
                    //詳細アクティビティに遷移
                    startActivityForResult(intent,requestCode);
                } else {
                    //一回も日付にタップしてない場合
                    Log.d("dateClickFlag","false");
                    Toast toast = Toast.makeText(CalendarActivity.this,"一回は曜日にタップしてください", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        //マップ(メイン)アクティビティに遷移
        Button mapActivityButton = findViewById(R.id.MainActivityButton);
        mapActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Main(Map)ActivityButton","pushed");
                Intent intent = new Intent(getApplication(),MainActivity.class);
                startActivity(intent);
            }
        });

        //牛の情報アクティビティに遷移
        Button cowInformationActivityButton = findViewById(R.id.CowInformationActivityButton);
        cowInformationActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("CowInformationActivityButton","pushed");
                Intent intent = new Intent(getApplication(),CowInformationActivity.class);
                startActivity(intent);
            }
        });

        //動画アクティビティに遷移
        Button videoActivityButton = findViewById(R.id.VideoActivityButton);
        videoActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("VideoActivityButton","pushed");
                Intent intent = new Intent(getApplication(),VideoListActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);

        boolean flag = intent.getBooleanExtra("changeFlag",false);
        if(requestCode == 1000 && resultCode == RESULT_OK && flag){
            //詳細テキストが変更された時に以下の処理が行われる
            String detailString = intent.getStringExtra("detail");
            //ハッシュマップに詳細テキストをput
            detailFilename.put(date + ".txt", detailString);
            //詳細欄にテキストをセット
            detailText.setText(detailString);
            Log.d("detailFile",date + ".txt");
            Log.d("detailText",detailString);
        }
    }
}
