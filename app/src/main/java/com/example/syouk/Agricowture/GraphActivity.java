package com.example.syouk.Agricowture;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.renderer.YAxisRenderer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class GraphActivity extends AppCompatActivity {
//    private String JSON = "[{\"moving\":\"1974\",\"time\":\"23日12時\"},{\"moving\":\"1531\",\"time\":\"23日18時\"},{\"moving\":\"621\",\"time\":\"24日0時\"},{\"moving\":\"153\",\"time\":\"24日6時\"},{\"moving\":\"2048\",\"time\":\"24日12時\"},{\"moving\":\"2733\",\"time\":\"24日18時\"}]";
    private ArrayList<String> xValues;
    private ArrayList<LineDataSet> dataSets;

    private LineChart lineChart;
    private TextView cowNameText;
    private int[] amount;
    private String[] date;
    private int days;
    private int position;
    private int counter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        final Intent intent = getIntent();
        position = intent.getIntExtra("position",-1);
        //ここのURLを変える
        String url = Constant.fUrl + "/graphdata/";
        final Handler handler = new Handler();

        cowNameText = findViewById(R.id.cowIdText);
        cowNameText.setText(Constant.cowName[position]);

        ImageView estrusImage = findViewById(R.id.estrusInformation);
        if (Constant.estrus[position]){
            estrusImage.setImageResource(R.drawable.abnormal);
        } else {
            estrusImage.setImageResource(R.drawable.nomal);
        }

        lineChart = findViewById(R.id.chart);
        lineChart.setDragEnabled(false);
        lineChart.setScaleEnabled(false);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.setDescriptionTextSize(0f);
        lineChart.getXAxis().setTextSize(15f);
        lineChart.setExtraTopOffset(lineChart.getExtraTopOffset()+ 2);
        lineChart.setExtraLeftOffset(lineChart.getExtraLeftOffset() + 10);
        lineChart.setExtraRightOffset(lineChart.getExtraRightOffset() + 20);



        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setTextSize(15f);
        yAxis.setStartAtZero(true);

        lineChart.setRendererLeftYAxis(new YAxisRenderer(lineChart.getViewPortHandler(),
                yAxis, lineChart.getTransformer(yAxis.getAxisDependency())){
            @Override
            protected void drawYLabels(Canvas c, float fixedPosition,
                                       float[] positions, float offset){
                final int to = mYAxis.isDrawTopYLabelEntryEnabled()
                        ? mYAxis.mEntryCount
                        : (mYAxis.mEntryCount - 1);

                // draw
                for (int i = 0; i < to; i++) {
                    String text = mYAxis.getFormattedLabel(i) + "m";

                    c.drawText(text, fixedPosition, positions[i * 2 + 1] + offset, mAxisLabelPaint);
                }
            }
        });
        Constant.jsonFlag = false;
        counter = 0;
        JsonThread jsonThread = new JsonThread();
        Thread thread = new Thread(jsonThread);
        Constant.urlSt = url + Constant.cowID[position];
        Log.d("finalUrl", url);
        thread.start();
        //デバッグ用
//        Constant.jsonFlag = true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Log.d("jsonFlag", String.valueOf(Constant.jsonFlag));

                        if (Constant.jsonFlag) {
                            //テスト用
//                                JSONArray jsonArray = new JSONArray(JSON);
                            //本番用
                            JSONArray jsonArray = new JSONArray(Constant.resultText);
                            days = jsonArray.length();
                            amount = new int[days];
                            date = new String[days];
                            for (int i = 0; i < days; i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                amount[i] = Integer.parseInt(jsonObject.getString("moving"));
                                date[i] = jsonObject.getString("time");
                                Log.d("amount", String.valueOf(amount[i]));
                                Log.d("date", date[i]);
                            }

                            dataSets = new ArrayList<>();
                            xValues = new ArrayList<>(Arrays.asList(date));

                            ArrayList<Entry> value = new ArrayList<>();
                            for (int i = 0; i < days; i++) {
                                value.add(new Entry(amount[i], i));
                            }

                            LineDataSet valueDataSet = new LineDataSet(value, Constant.cowID[position]);
                            valueDataSet.setCircleColor(Color.rgb(60, 179, 113));
                            valueDataSet.setColor(Color.rgb(60, 179, 113));
                            valueDataSet.setValueTextSize(15f);
                            valueDataSet.setCircleSize(7f);
                            valueDataSet.setLineWidth(3f);
                            dataSets.add(valueDataSet);

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d("lineChartData", "set");
                                    lineChart.setData(new LineData(xValues, dataSets));
                                    lineChart.notifyDataSetChanged();
                                    lineChart.invalidate();
                                    Log.d("lineChartData", "finish");
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
                    }
                } catch (InterruptedException e) {
                    Log.e("error","InterruptedException");
                    e.printStackTrace();
                } catch (JSONException e) {
                    Log.e("error","JSONException");
                    e.printStackTrace();
                }
            }
        }).start();

        Button nameChangeButton = findViewById(R.id.nameButton);
        nameChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplication(), NameChangeActivity.class);
                int requestCode = 1000;
                intent1.putExtra("Position",position);
                startActivityForResult(intent1,requestCode);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.d("BackKey","pushed");
            Intent intent1 = new Intent();
            setResult(RESULT_OK, intent1);
            finish();
        }
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);

        if(requestCode == 1000 && resultCode == RESULT_OK && intent != null){
            cowNameText.setText(Constant.cowName[position]);
        }
    }
}
