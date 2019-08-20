package com.example.syouk.Agricowture;

import android.content.Context;
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
import java.util.HashMap;

public class CalendarActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private Context context;
    private TextView dateText;
    private TextView detailText;
    private Button editButton;
    private Button mapActivityButton;
    private String date;
    private String detail = "";
    private String fileContent = "";
    private boolean dateClickFlag;
    private HashMap<String,String> detailFilename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);


        //ここから初期設定(化)
        Constant.detailFilePath = MyApplication.getAppContext().getFilesDir().getPath() + "/detailText";
        Log.d("file path",Constant.detailFilePath);
        detailFilename = new HashMap<>();
        //詳細ファイルの読み込み
        File dir = new File(Constant.detailFilePath);
        Log.d("File dir","instanced");
        if(!dir.mkdir()) {
            File[] list = dir.listFiles();
            Log.d("File list", "created");
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
            Log.d("mkdir","Done");
        }

        dateClickFlag = false;
        //ここまで


        dateText = findViewById(R.id.dateText);
        detailText = findViewById(R.id.detailText);
        calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {

                dateClickFlag = true;
                date = i + "年" + (i1 + 1) + "月" + i2 + "日";
                dateText.setText(date);
                Log.d("clickDate",date);
                if((detail = detailFilename.get(date + ".txt")) != null){
                    detailText.setText(detail);
                } else {
                    detailText.setText("");
                }

            }
        });


        editButton = findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dateClickFlag){
                    Log.d("date",date);
                    Intent intent = new Intent(CalendarActivity.this,DetailActivity.class);
                    intent.putExtra("date",date);
                    int requestCode = 1000;
                    startActivityForResult(intent,requestCode);


                } else {
                    Log.d("dateClickFlag","false");
                    Toast toast = Toast.makeText(CalendarActivity.this,"一回は曜日にタップしてください", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        mapActivityButton = findViewById(R.id.MainActivityButton);
        mapActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(),MainActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);

        if(requestCode == 1000 && resultCode == RESULT_OK && intent != null){
            String detailText = intent.getStringExtra("detail");
            detailFilename.put(date + ".txt", detailText);
        }
    }
}
