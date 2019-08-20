package com.example.syouk.Agricowture;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class GraphActivity extends AppCompatActivity {
    private String JSON = "[{\"amount\":\"3.9\",\"date\":\"8月23日\"},{\"amount\":\"2.1\",\"date\":\"8月24日\"},{\"amount\":\"1.9\",\"date\":\"8月25日\"},{\"amount\":\"3.2\",\"date\":\"8月26日\"},{\"amount\":\"5.5\",\"date\":\"8月27日\"},{\"amount\":\"8.2\",\"date\":\"8月28日\"},{\"amount\":\"0.4\",\"date\":\"8月29日\"}]";
    private ArrayList<String> xValues;
    private ArrayList<LineDataSet> dataSets;

    //ここのURLを変える
    final String finalUrl = "https://cowcheck.herokuapp.com/graphdata/";

    private LineChart lineChart;
    private float[] amount;
    private String[] date;
    private int days;
    private int position;
    private int counter;
    private TextView cow_id_text;
    private ImageView estrus_image;
    private Button video_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        Intent intent = getIntent();
        position = intent.getIntExtra("position",-1);

        final Handler handler = new Handler();

        cow_id_text = findViewById(R.id.cowIdText);
        cow_id_text.setText(Constant.cowID[position]);

        estrus_image = findViewById(R.id.estrusInformation);
        if (Constant.estrus[position]){
            estrus_image.setImageResource(R.drawable.abnormal);
        } else {
            estrus_image.setImageResource(R.drawable.nomal);
        }

        lineChart = findViewById(R.id.chart);
        lineChart.setDragEnabled(false);
        lineChart.setScaleEnabled(false);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.setDescriptionTextSize(0f);
        lineChart.getXAxis().setTextSize(15f);
        lineChart.setExtraLeftOffset(lineChart.getExtraLeftOffset() + 10);
        lineChart.setExtraRightOffset(lineChart.getExtraRightOffset() + 20);


        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setTextSize(15f);
        yAxis.setStartAtZero(true);


        Constant.jsonFlag = false;
        counter = 0;
        JsonThread jsonThread = new JsonThread();
        Thread thread = new Thread(jsonThread);
        Constant.urlSt = finalUrl + String.valueOf(position);
        Log.d("finalUrl",finalUrl);
//        thread.start();
        //デバッグ用
        Constant.jsonFlag = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Log.d("jsonFlag", String.valueOf(Constant.jsonFlag));
                    try {
                        if (Constant.jsonFlag) {
                            //テスト用
                            JSONArray jsonArray = new JSONArray(JSON);
                            //本番用
//                            JSONArray jsonArray = new JSONArray(Constant.resultText);
                            days = jsonArray.length();
                            amount = new float[days];
                            date = new String[days];
                            for (int i = 0; i < days; i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                amount[i] = Float.valueOf(jsonObject.getString("amount"));
                                date[i] = jsonObject.getString("date");
                                Log.d("amount", String.valueOf(amount[i]));
                                Log.d("date", date[i]);
                            }

                            dataSets = new ArrayList<>();
                            xValues = new ArrayList<>(Arrays.asList(date));

                            ArrayList<Entry> value = new ArrayList<>();
                            for (int i = 0; i < days; i++){
                                value.add(new Entry(amount[i],i));
                            }

                            LineDataSet valueDataSet = new LineDataSet(value,Constant.cowID[position]);
                            valueDataSet.setCircleColor(Color.rgb(60,179,113));
                            valueDataSet.setColor(Color.rgb(60,179,113));
                            valueDataSet.setValueTextSize(15f);
                            valueDataSet.setCircleSize(7f);
                            valueDataSet.setLineWidth(3f);
                            dataSets.add(valueDataSet);

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d("lineChartData","set");
                                    lineChart.setData(new LineData(xValues,dataSets));
                                    lineChart.notifyDataSetChanged();
                                    lineChart.invalidate();
                                    Log.d("lineChartData","finish");
                                }
                            });
                            break;
                        } else {
                            Thread.sleep(5000);
                            counter++;
                            if (counter > 100) {
                                Log.d("counter", "timeout");
                                break;
                            }
                        }
                    } catch (InterruptedException e) {
                        Log.e("error","InterruptedException");
                        e.printStackTrace();
                    } catch (JSONException e) {
                        Log.e("error","JSONException");
                        e.printStackTrace();
                    }
                }
            }
        }).start();




//        ArrayList<LineDataSet> dataSets = new ArrayList<>();

//        ArrayList<String> xValues = new ArrayList<>(Arrays.asList(date));



//        video_button = findViewById(R.id.videoButton);
//        video_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//            }
//        });
    }
}
