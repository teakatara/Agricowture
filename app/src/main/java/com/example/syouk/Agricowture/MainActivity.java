package com.example.syouk.Agricowture;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback , OnMapLoadedCallback , LocationListener {

    private String url;
    public static GoogleMap mMap;
    public static AlertDialog.Builder ad;
    private boolean reloadFlag = true;
    private int counter = 0;
    private double nowLat;
    private double nowLng;
    private Polyline polyline;
    private boolean polyLineFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ここからセットアップ
        Constant.CONTEXT = this;
        polyLineFlag = false;
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationStart();
        Button reloadButton = findViewById(R.id.reloadButton);
        url = Constant.fUrl + "/get";
        final Handler handler = new Handler();
        ad = new AlertDialog.Builder(this);

        //牛の名前のロード
        Constant.cowNameMap = new HashMap<>();
        Constant.cowNameFilePath = MyApplication.getAppContext().getFilesDir().getPath() + "/cowName.txt";
        File cowNameFile = new File(Constant.cowNameFilePath);
        //ファイルの存在チェック
        if(cowNameFile.exists()) {
            Log.d("cowNameFile","exists");
            try (BufferedReader in = new BufferedReader(new FileReader(cowNameFile))) {
                String line;
                //牛の名前をハッシュマップに登録
                while ((line = in.readLine()) != null) {
                    Log.d("in",String.valueOf(in));
                    String[] readLine = line.split(",", 0);
                    Constant.cowNameMap.put(readLine[0], readLine[1]);
                    Log.d("readLine", readLine[0] + ":" + readLine[1]);
                }
            } catch (IOException e) {
                Log.e("error","IOException");
                e.printStackTrace();
            }
        }
        //セットアップ終了


        //更新ボタンが押された時の処理
        reloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("mapLoadFinishedFlag",""+Constant.mapLoadFinishedFlag);
                if (Constant.mapLoadFinishedFlag){
                    Log.d("reloadFlag",""+reloadFlag);
                    if(reloadFlag) {
                        counter = 0;
                        reloadFlag = false;
                        if(!Constant.jsonFailureFlag) {
                            //マーカーの削除
                            for (int i = 0; i < Constant.MVoA; i++) {
                                Constant.marker[i].remove();
                            }
                            Constant.jsonFailureFlag = true;
                        }

                        if(polyLineFlag){
                            //polyLineの削除
                            polyline.remove();
                            polyLineFlag = false;
                        }

                        //牛の位置情報を取得するためのスレッドをスタート
                        JsonThread jsonThread = new JsonThread();
                        Thread thread = new Thread(jsonThread);
                        Constant.jsonFlag = false;
                        Constant.urlSt = url;
                        thread.start();

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while(true){
                                    Log.d("jsonFailureFlag",""+Constant.jsonFailureFlag);
                                    Log.d("jsonFlag",""+Constant.jsonFlag);
                                    try{
                                        if(Constant.jsonFlag){
                                            //牛の位置情報を取得出来次第処理を開始する

                                            JSONArray jsonArray = new JSONArray(Constant.resultText);
                                            Constant.MVoA = jsonArray.length();
                                            Log.d("JSONArrayLength",""+Constant.MVoA);
                                            Constant.cowID = new String[Constant.MVoA];
                                            Constant.lat = new Double[Constant.MVoA];
                                            Constant.lng = new Double[Constant.MVoA];
                                            Constant.estrus = new int[Constant.MVoA];

                                            //取得した情報を書く配列に格納
                                            for (int i = 0; i < Constant.MVoA; i++) {
                                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                Constant.cowID[i] = jsonObject.getString("CowID");
                                                Constant.lat[i] = jsonObject.getDouble("Lat");
                                                Constant.lng[i] = jsonObject.getDouble("Lng");
                                                Constant.estrus[i] = jsonObject.getInt("Estrus");

                                                Log.d("CowID", Constant.cowID[i]);
                                                Log.d("Lat", "" + Constant.lat[i]);
                                                Log.d("Lng", "" + Constant.lng[i]);
                                                Log.d("estrus","" + Constant.estrus[i]);
                                            }

                                            //牛の名前をハッシュマップから配列に格納
                                            Constant.cowName = new String[Constant.MVoA];
                                            for(int i = 0; i < Constant.MVoA; i++){
                                                Constant.cowName[i] = Constant.cowNameMap.get(Constant.cowID[i]);
                                                if(Constant.cowName[i] == null){
                                                    //牛の名前が存在しない場合、牛のIDを格納
                                                    Constant.cowName[i] = Constant.cowID[i];
                                                }
                                            }

                                            Log.d("CowData","Successful reception");

                                            //取得した情報を元にマーカーを描画
                                            Log.d("MarkerAdd","Start");
                                            Constant.marker = new Marker[Constant.MVoA];
                                            handler.post(new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    BitmapDescriptor abnormalBmp = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
                                                    for(int i = 0;i < Constant.MVoA;i++) {
                                                        Log.d("MarkerAdd", "for = " + i);
                                                        LatLng place = new LatLng(Constant.lat[i], Constant.lng[i]);
                                                        Log.d("LatLng", "for = " + i);
                                                        Log.d("LatLng",place.toString());
                                                        if(Constant.estrus[i] == 2){
                                                            //発情しているときのマーカー
                                                            Constant.marker[i] = mMap.addMarker(new MarkerOptions().position(place).title(Constant.cowName[i]).icon(abnormalBmp));
                                                        } else {
                                                            //通常のマーカー
                                                            Constant.marker[i] = mMap.addMarker(new MarkerOptions().position(place).title(Constant.cowName[i]));
                                                        }
                                                        Log.d("MarkerAdd",""+i);
                                                    }
                                                    Log.d("MarkerAdd","Done");
                                                }
                                            }));
                                            Constant.jsonFailureFlag = false;
                                            break;
                                        } else {
                                            Thread.sleep(1000);
                                            counter++;
                                            if(counter > 1000){
                                                //タイムアウト
                                                Log.d("counter","timeout");
                                                break;
                                            }
                                        }
                                    } catch (InterruptedException e) {
                                        Log.e("error","InterruptedException");
                                        break;
                                    } catch (JSONException e) {
                                        Log.e("error","JSONException");
                                        break;
                                    }
                                }
                                reloadFlag = true;
                                Log.d("reload","end");
                            }
                        }).start();
                    }
                }
            }
        });

        //牛の情報アクティビティに遷移
        Button cowInformationButton;
        cowInformationButton = findViewById(R.id.CowInformationActivityButton);
        cowInformationButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View view) {
                Log.d("CalendarActivityButton","pushed");
                Intent intent = new Intent(getApplication(), CowInformationActivity.class);
                startActivity(intent);
            }
        });

        //動画アクティビティに遷移
        Button videoButton;
        videoButton = findViewById(R.id.VideoActivityButton);
        videoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("VideoActivityButton","pushed");
                Intent intent = new Intent(getApplication(), VideoListActivity.class);
                startActivity(intent);
            }
        });

        //カレンダーアクティビティに遷移
        Button calendarActivityButton;
        calendarActivityButton = findViewById(R.id.CalendarActivityButton);
        calendarActivityButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View view) {
                Log.d("CalendarActivityButton","pushed");
                Intent intent = new Intent(getApplication(), CalendarActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMapLoadedCallback(this);
    }

    @Override
    public void onMapLoaded(){
        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(35.9438234,139.3178846)).zoom(8).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        Constant.mapLoadFinishedFlag = true;

        //マーカーがタップされた時の処理
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.d("droneThreadOK",""+Constant.droneThreadOK);
                String id = marker.getId();
                Log.d("MarkerClickListener","in");
                Log.d("MarkerClickListener",""+id);
                int num = Integer.parseInt(id.substring(1));
                Log.d("num",""+num);
                Constant.cowNum = 0;
                Constant.cowNum = (num % Constant.MVoA) + 1;
                Log.d("CowNumber",""+Constant.cowNum);

                if(Constant.droneThreadOK) {
                    //ダイアログのセットアップ
                    ad.setTitle(Constant.cowName[Constant.cowNum - 1] + "がいる位置にドローンを向かわせますか？");
                    if(polyLineFlag){
                        //マーカーまでに引かれている線を消す
                        polyline.remove();
                        polyLineFlag = false;
                    }
                    // OKボタン
                    ad.setPositiveButton("はい", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d("Dialog", "positive");
                            DroneThread droneThread = new DroneThread();
                            Thread dThread = new Thread(droneThread);
                            dThread.start();
                        }
                    });
                    // NGボタン
                    ad.setNegativeButton("いいえ", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d("Dialog", "negative");
                        }
                    });

                    //ダイアログを下に表示
                    AlertDialog alertDialog = ad.create();
                    WindowManager.LayoutParams lp = Objects.requireNonNull(alertDialog.getWindow()).getAttributes();
                    lp.gravity = Gravity.BOTTOM;
                    alertDialog.getWindow().setAttributes(lp);
                    alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    alertDialog.setCancelable(false);
                    alertDialog.show();

                    //マーカーと現在位置の間に線を引く
                    PolylineOptions polylineOptions = new PolylineOptions();
                    polylineOptions.add(new LatLng(Constant.lat[Constant.cowNum-1],Constant.lng[Constant.cowNum-1]));
                    polylineOptions.add(new LatLng(nowLat,nowLng));
                    polylineOptions.color(R.color.black);
                    polylineOptions.width(25);
                    polylineOptions.geodesic(false);
                    polyline = mMap.addPolyline(polylineOptions);
                    polyLineFlag = true;
                } else {
                    //ドローンへの命令を処理中の場合のダイアログ
                    ad.setTitle("処理中");
                    ad.setMessage("いつまでもこの表示が出る場合は更新ボタンを押してください");
                    ad.setPositiveButton("OK", null);
                    AlertDialog alertDialog = ad.create();
                    WindowManager.LayoutParams lp = Objects.requireNonNull(alertDialog.getWindow()).getAttributes();
                    lp.gravity = Gravity.BOTTOM;
                    alertDialog.getWindow().setAttributes(lp);
                    alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                }
                return false;
            }
        });
        Toast.makeText(this,"地図の描画完了", Toast.LENGTH_LONG).show();
    }

    private void locationStart(){
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.d("locationStart", "location manager Enabled");
        } else {
            // GPSを設定するように促す
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
            Log.d("locationStart", "not gpsEnable, startActivity");
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            Log.d("debug", "checkSelfPermission false");
            return;
        }
        assert locationManager != null;
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000, 50, this);
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[]permissions, @NonNull int[] grantResults) {
        if (requestCode == 1000) {
            // 使用が許可された
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("debug","checkSelfPermission true");
                locationStart();
            } else {
                // それでも拒否された時の対応
                Toast toast = Toast.makeText(this,"位置情報をOnにしないと使用できません", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        //現在の緯度を取得
        nowLat = location.getLatitude();
        Log.d("nowLat",String.valueOf(nowLat));
        //現在の経度を取得
        nowLng = location.getLongitude();
        Log.d("nowLng",String.valueOf(nowLng));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
