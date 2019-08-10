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
    private TextView date_text;
    private TextView detail_text;
    private Button edit_button;
    private String date;
    private String test_detail = "";
    private String file_content = null;
    private boolean date_click_flag;
    private Context context;
    private HashMap<String,String> detail_filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        Constant.detail_file_path = "/data/data/"+ getPackageName() + "/files/detail_text";
        Log.d("file path",Constant.detail_file_path);
        detail_filename = new HashMap<>();
        //詳細ファイルの読み込み
        File dir = new File(Constant.detail_file_path);
        Log.d("File dir","instanced");
        if(dir.exists()) {
            File[] list = dir.listFiles();
            Log.d("File list", "created");
            for (int i = 0; i < list.length; i++) {
                Log.d("File read", "for:" + Integer.toString(i));
                try {
                    FileReader fileReader = new FileReader(list[i]);
                    int data;
                    while ((data = fileReader.read()) != -1) {
                        file_content += (char) data;
                    }
                    fileReader.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d("File read", "completed");
                detail_filename.put(list[i].getName(), file_content);
                Log.d(list[i].getName() + "\n", "" + file_content);
            }
        } else {
            Log.d("mkdir","start");
            dir.mkdir();
            Log.d("mkdir","success");
        }

        date_click_flag = false;

        date_text = findViewById(R.id.date_text);
        detail_text = findViewById(R.id.detail_text);

        calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {

                date_click_flag = true;
                date = i + "年" + (i1 + 1) + "月" + i2 + "日";
                date_text.setText(date);
                Log.d("clickdate",date);

            }
        });

        edit_button = findViewById(R.id.edit_button);
        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(date_click_flag){
                    Log.d("date",date);
                    Intent intent = new Intent(CalendarActivity.this,DetailActivity.class);
                    intent.putExtra("date",date);
                    startActivity(intent);


                } else {
                    Log.d("date_click_flag","false");
                    Toast toast = Toast.makeText(CalendarActivity.this,"一回は曜日にタップしてください", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        Button mapactivity_button;
        mapactivity_button = findViewById(R.id.MainActivity_button);
        mapactivity_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(),MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
