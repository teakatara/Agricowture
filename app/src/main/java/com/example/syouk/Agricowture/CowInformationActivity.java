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
    private Bitmap bmp;
    private Button mapActivityButton;
    private Button calendarActivityButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cowinformation);

        ListView listView = findViewById(R.id.listView);

        ArrayList<List_item> listItems = new ArrayList<>();
        for(int i = 0; i < Constant.MVoA; i++) {
            Log.d("cowInformation", "for" + (i + 1));
            if (Constant.estrus[i]) {
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.abnormal);
            } else {
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.nomal);
            }
            List_item item = new List_item(bmp, Constant.cowID[i]);
            listItems.add(item);
        }
        ListAdapter adapter = new ListAdapter(this, R.layout.list_item, listItems);
        Log.d("adapterSet", "start");
        listView.setAdapter(adapter);
        Log.d("adapterSet","end");



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Log.d( "position",String.valueOf(position));
                Intent intent = new Intent(getApplication(),GraphActivity.class);
                intent.putExtra("position",position);
                startActivity(intent);
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

        calendarActivityButton = findViewById(R.id.CalendarActivityButton);
        calendarActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(),CalendarActivity.class);
                startActivity(intent);
            }
        });
    }
}
