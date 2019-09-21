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
        String url = Constant.fUrl + "/graphdata/";
        final Handler handler = new Handler();

        //左上の牛の名前をセット
        cowNameText = findViewById(R.id.cowIdText);
        cowNameText.setText(Constant.cowName[position]);
        //右上の発情期画像をセット
        ImageView estrusImage = findViewById(R.id.estrusInformation);
        if (Constant.estrus[position] == 2){
            estrusImage.setImageResource(R.drawable.abnormal);
        } else {
            estrusImage.setImageResource(R.drawable.normal);
        }

        //折れ線グラフのオフセットやスクロール無効化などの設定
        lineChart = findViewById(R.id.chart);
        lineChart.setDragEnabled(false);
        lineChart.setScaleEnabled(false);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.setDescriptionTextSize(0f);
        lineChart.getXAxis().setTextSize(15f);
        lineChart.setExtraTopOffset(lineChart.getExtraTopOffset()+ 2);
        lineChart.setExtraLeftOffset(lineChart.getExtraLeftOffset() + 10);
        lineChart.setExtraRightOffset(lineChart.getExtraRightOffset() + 20);

        //Y軸設定
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
                //Y軸に単位をつけて描画
                for (int i = 0; i < to; i++) {
                    String text = mYAxis.getFormattedLabel(i) + "m";
                    c.drawText(text, fixedPosition, positions[i * 2 + 1] + offset, mAxisLabelPaint);
                }
            }
        });

        //サーバからグラフデータの取得
        Constant.jsonFlag = false;
        counter = 0;
        JsonThread jsonThread = new JsonThread();
        Thread thread = new Thread(jsonThread);
        Constant.urlSt = url + Constant.cowID[position];
        Log.d("finalUrl", url);
        thread.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                //取得したデータを元にグラフを描画
                try {
                    while (true) {
                        Log.d("jsonFlag", String.valueOf(Constant.jsonFlag));
                        if (Constant.jsonFlag) {
                            JSONArray jsonArray = new JSONArray(Constant.resultText);
                            days = jsonArray.length();
                            amount = new int[days];
                            date = new String[days];
                            int count = 0;
                            //取得したデータを逆順に保存
                            for (int i = days-1; i >= 0; i--) {
                                JSONObject jsonObject = jsonArray.getJSONObject(count);
                                amount[i] = Integer.parseInt(jsonObject.getString("moving"));
                                date[i] = jsonObject.getString("time");
                                Log.d("amount", String.valueOf(amount[i]));
                                Log.d("date", date[i]);
                                count++;
                            }

                            dataSets = new ArrayList<>();
                            xValues = new ArrayList<>(Arrays.asList(date));
                            //データをグラフに反映
                            ArrayList<Entry> value = new ArrayList<>();
                            for (int i = 0; i < days; i++) {
                                value.add(new Entry(amount[i], i));
                            }

                            //グラフの色の設定
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
                                    //グラフを描画
                                    lineChart.setData(new LineData(xValues, dataSets));
                                    lineChart.notifyDataSetChanged();
                                    lineChart.invalidate();
                                    Log.d("lineChartData", "finish");
                                }
                            });
                            break;
                        } else {
                            Thread.sleep(1000);
                            counter++;
                            if (counter > 1000) {
                                Log.d("counter", "timeout");
                                //タイムアウト
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

        //名前変更アクティビティに遷移
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

        //戻るボタンを押されたときの処理
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("BackButton","pushed");
                Intent intent1 = new Intent();
                setResult(RESULT_OK,intent1);
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //バックキーが押された時に前のアクティビティに戻る
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
            //名前が変更された時に左上の名前に反映
            cowNameText.setText(Constant.cowName[position]);
        }
    }
}
