package com.example.syouk.Agricowture;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class CowInformationActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cowinformation);

        listView = findViewById(R.id.listView);
        reloadFunc();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //リストのアイテムがタップされた時の処理
                Log.d( "position",String.valueOf(position));
                Intent intent = new Intent(getApplication(),GraphActivity.class);
                intent.putExtra("position",position);
                int requestCode = 100;
                startActivityForResult(intent,requestCode);
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

        //カレンダーアクティビティに遷移
        Button calendarActivityButton = findViewById(R.id.CalendarActivityButton);
        calendarActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("CalenderActivityButton","pushed");
                Intent intent = new Intent(getApplication(),CalendarActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);

        if(requestCode == 100 && resultCode == RESULT_OK && intent != null){
            reloadFunc();
        }
    }

    private void reloadFunc(){
        //牛の情報をリストアップして描画
        ArrayList<List_item> listItems = new ArrayList<>();
        for (int i = 0; i < Constant.MVoA; i++){
            Log.d("cowInformation", "for" + (i + 1));
            Bitmap bmp;
            if(Constant.estrus[i] == 2){
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.abnormal);
            } else {
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.normal);
            }
            List_item item = new List_item(bmp, Constant.cowName[i]);
            listItems.add(item);
        }
        ListAdapter adapter = new ListAdapter(this, R.layout.list_item, listItems);
        listView.setAdapter(adapter);
        Log.d("adapterSet","end");
    }
}
