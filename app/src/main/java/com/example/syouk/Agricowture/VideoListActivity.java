package com.example.syouk.Agricowture;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class VideoListActivity extends AppCompatActivity {

    protected ListView listView;
//    private String url = "https://github.com/teakatara/test/archive/master.zip";
    private String url;

    private String[] fileNames;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);

        listView = findViewById(R.id.videoList);

        Constant.sdCardPath = String.valueOf(MyApplication.getAppContext().getExternalFilesDir(null));
        Log.d("videoPath",Constant.sdCardPath);
        url = Constant.fUrl + "/getmovie.zip";
        url = "https://github.com/teakatara/test/archive/master.zip";
        handler = new Handler();
        ReloadFunc();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("onItemClickFileName",fileNames[i]);
                Intent intent = new Intent(getApplication(),VideoActivity.class);
                String videoFilePath = Constant.sdCardPath + "/videos/" + fileNames[i];
                intent.putExtra("videoFilePath",videoFilePath);
                Log.d("videoFilePath",videoFilePath);
                Log.d("VideoActivity","start");
                startActivity(intent);
            }
        });

        Button reloadButton = findViewById(R.id.reloadButton);
        reloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("reloadButton","pushed");
                Constant.fileDecompressionFlag = false;
                long id = DownloadFunc();
                MyThread myThread = new MyThread();
                Thread thread = new Thread(myThread);
                thread.start();
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

    class MyThread extends Thread{
        public void run(){
            int counter = 0;
            while (true){
                if (Constant.fileDecompressionFlag){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            ReloadFunc();
                        }
                    });
                    break;
                } else {
                    try {
                        counter++;
                        Log.d("counter",String.valueOf(counter));
                        if (counter > 1000) {
                            break;
                        }
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Log.e("error", "InterruptedException");
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    protected long DownloadFunc(){
        Log.d("downloadFunc","start");
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        Log.d("request","create");
        Log.d("sdPath", Arrays.toString(this.getExternalFilesDirs(null)));
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setDestinationInExternalFilesDir(getApplicationContext(), Environment.DIRECTORY_DOWNLOADS,"a.zip");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle("牛の動画");
        request.setDescription("タップするとダウンロードを中止できます");
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        assert downloadManager != null;
        long id = downloadManager.enqueue(request);
        Log.d("download","start");
        return id;
    }

    protected void ReloadFunc(){
        File dir = new File(Constant.sdCardPath + "/videos");
        Log.d("loadFile","start");
        if(!dir.mkdir() && dir.exists()) {
            File[] list = dir.listFiles();
            if (list != null) {
                ArrayList<String> listItems = new ArrayList<>();
                Log.d("list", Arrays.toString(list));
                int length = list.length;
                Log.d("listLength", String.valueOf(length));
                fileNames = new String[length];
                for (int i = 0; i < length; i++) {
                    Log.d("video", "for" + (i + 1));
                    fileNames[i] = String.valueOf(list[i].getName());
                    listItems.add(fileNames[i]);
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(MyApplication.getAppContext(), android.R.layout.simple_expandable_list_item_1, listItems);
                listView.setAdapter(arrayAdapter);
                Log.d("adapterSet", "Done");
            } else {
                Log.d("fileList", "null");
            }
        } else {
            Log.d("mkdir","Done");
        }
    }
}
