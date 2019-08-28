package com.example.syouk.Agricowture;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class VideoListActivity extends AppCompatActivity {

    private ArrayList<String> listItems;
    private ArrayAdapter<String> arrayAdapter;
    private ListView listView;
    private File[] list;

    private String str = "akwfb";
    private String testPath;
    private int counter = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);

        listView = findViewById(R.id.videoList);
        listItems = new ArrayList<>();

        Constant.videoPath = MyApplication.getAppContext().getFilesDir().getPath() + "/videos";
        Log.d("videoPath",Constant.videoPath);
        ReloadFanc();


        Button reloadButton = findViewById(R.id.reloadButton);
        reloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("reloadButton","pushed");


                ReloadFanc();
            }
        });


        Button mapActivityButton = findViewById(R.id.MainActivityButton);
        mapActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Main(Map)ActivityButton","pushed");
                Intent intent = new Intent(getApplication(),MainActivity.class);
                startActivity(intent);
            }
        });

        Button cowInformationActivityButton = findViewById(R.id.CowInformationActivityButton);
        cowInformationActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("CowInformationActivityButton","pushed");
                Intent intent = new Intent(getApplication(),CowInformationActivity.class);
                startActivity(intent);
            }
        });

        Button calendarActivityButton = findViewById(R.id.CalendarActivityButton);
        calendarActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("CalendarActivityButton","pushed");
                Intent intent = new Intent(getApplication(),CalendarActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void ReloadFanc(){
        File dir = new File(Constant.videoPath);
        if(dir.mkdir()){
            Log.d("mkdir","Done");
            Log.d("loadFile","start");
            list = dir.listFiles();
            if(list != null) {
                Log.d("list", Arrays.toString(list));
                Log.d("listLength",String.valueOf(list.length));
                for (int i = 0; i < list.length; i++) {
                    Log.d("video", "for" + (i + 1));
                    listItems.add(String.valueOf(list[i].getName()));
                }
                arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, listItems);
                listView.setAdapter(arrayAdapter);
                Log.d("adapterSet", "Done");
            } else {
                Log.d("fileList","null");
            }
        }
    }
}
