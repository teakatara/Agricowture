package com.example.syouk.Agricowture;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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
    private LineChart mChart;
    private String JSON = "[{\"amount\":\"3.9\",\"date\":\"8月23日\"},{\"amount\":\"2.1\",\"date\":\"8月24日\"},{\"amount\":\"1.9\",\"date\":\"8月25日\"},{\"amount\":\"3.2\",\"date\":\"8月26日\"},{\"amount\":\"5.5\",\"date\":\"8月27日\"},{\"amount\":\"8.2\",\"date\":\"8月28日\"},{\"amount\":\"0.4\",\"date\":\"8月29日\"}]";
    private float[] amount;
    private String[] date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        int a = 0;

        try {
            JSONArray jsonArray = new JSONArray(JSON);
            a = jsonArray.length();
            amount = new float[a];
            date = new String[a];
            for (int i = 0; i < a; i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                amount[i] = Float.valueOf(jsonObject.getString("amount"));
                date[i] = jsonObject.getString("date");
                Log.d("amount","" + amount[i]);
                Log.d("date",date[i]);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LineChart lineChart = findViewById(R.id.chart);
        lineChart.setDragEnabled(false);
        lineChart.setScaleEnabled(false);
        lineChart.getAxisRight().setEnabled(false);


        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setStartAtZero(true);

        ArrayList<LineDataSet> dataSets = new ArrayList<>();

        ArrayList<String> xValues = new ArrayList<>(Arrays.asList(date));

        ArrayList<Entry> value = new ArrayList<>();
        for (int i = 0; i < a; i++){
            value.add(new Entry(amount[i],i));
        }

        LineDataSet valueDataSet = new LineDataSet(value,"牛１");
        valueDataSet.setCircleColor(Color.rgb(60,179,113));
        valueDataSet.setColor(Color.rgb(60,179,113));
        valueDataSet.setValueTextSize(15f);
        valueDataSet.setCircleSize(7f);
        valueDataSet.setLineWidth(3f);
        dataSets.add(valueDataSet);

        lineChart.setData(new LineData(xValues,dataSets));
    }
}
