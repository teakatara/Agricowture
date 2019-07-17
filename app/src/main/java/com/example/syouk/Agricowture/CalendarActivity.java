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

public class CalendarActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private TextView date_text;
    private TextView detail_text;
    private Button edit_button;
    private String date;
    private String test_detail = "";
    private boolean date_click_flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        date_click_flag = false;

        date_text = findViewById(R.id.date_text);
        detail_text = findViewById(R.id.detail_text);

        calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {

                date_click_flag = true;
                date = i + "/" + (i1 + 1) + "/" + i2;
                date_text.setText(date);
                Log.d("clickdate",""+date);

                //デバッグ用
                test_detail += date;
                detail_text.setText(test_detail);

            }
        });

        edit_button = findViewById(R.id.edit_button);
        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(date_click_flag){
                    Log.d("date",""+date);
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
