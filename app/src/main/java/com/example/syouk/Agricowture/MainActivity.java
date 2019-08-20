package com.example.syouk.Agricowture;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback , OnMapLoadedCallback {

    final String finalUrl = "https://cowcheck.herokuapp.com/get";

    public static GoogleMap mMap;

    private boolean reloadFlag = true;
    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Constant.CONTEXT = this;
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Button reloadButton;
        reloadButton = findViewById(R.id.reloadButton);

        final Handler handler = new Handler();

        reloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //バグ防止
                Constant.droneThreadOK = true;
                Constant.droneOK = false;
                Constant.droneWhileEscape = false;
                counter = 0;

                Log.d("mapLoadFinishedFlag",""+Constant.mapLoadFinishedFlag);
                if (Constant.mapLoadFinishedFlag){
                    Log.d("reloadFlag",""+reloadFlag);
                    if(reloadFlag) {
                        if(!Constant.jsonFailureFlag) {
                            for (int i = 0; i < Constant.MVoA; i++) {
                                Constant.marker[i].remove();
                            }
                            Constant.jsonFailureFlag = true;
                        }
                        reloadFlag = false;
                        Constant.jsonFlag = false;
                        JsonThread jsonThread = new JsonThread();
                        Thread thread = new Thread(jsonThread);
                        Constant.urlSt = finalUrl;
                        thread.start();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                while(true){
                                    Log.d("jsonFailureFlag",""+Constant.jsonFailureFlag);
                                    Log.d("jsonFlag",""+Constant.jsonFlag);
                                    try{
                                        if(Constant.jsonFlag){

                                            JSONArray jsonArray = new JSONArray(Constant.resultText);
                                            Constant.MVoA = jsonArray.length();
                                            Log.d("JSONArrayLength",""+Constant.MVoA);
                                            Constant.cowID = new String[Constant.MVoA];
                                            Constant.lat = new Double[Constant.MVoA];
                                            Constant.lng = new Double[Constant.MVoA];
                                            Constant.estrus = new boolean[Constant.MVoA];
                                            for (int i = 0; i < Constant.MVoA; i++) {
                                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                Constant.cowID[i] = jsonObject.getString("CowID");
                                                Constant.lat[i] = jsonObject.getDouble("Lat");
                                                Constant.lng[i] = jsonObject.getDouble("Lng");
//                                                Constant.estrus[i] = jsonObject.getBoolean("estrus");
                                                //デバッグ用
                                                Constant.estrus[i] = (i % 2) == 0;

                                                Log.d("CowID", Constant.cowID[i]);
                                                Log.d("Lat", "" + Constant.lat[i]);
                                                Log.d("Lng", "" + Constant.lng[i]);
                                                Log.d("estrus","" + Constant.estrus[i]);
                                            }

                                            Log.d("CowData","Successful reception");


                                            Log.d("MarkerAdd","Start");
                                            Constant.marker = new Marker[Constant.MVoA];
                                            handler.post(new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    for(int i = 0;i < Constant.MVoA;i++) {
                                                        Log.d("MarkerAdd", "for = " + i);
                                                        LatLng place = new LatLng(Constant.lat[i], Constant.lng[i]);
                                                        Log.d("LatLng", "for = " + i);
                                                        Log.d("LatLng",place.toString());
                                                        Constant.marker[i] = mMap.addMarker(new MarkerOptions().position(place).title(Constant.cowID[i]));
                                                        Log.d("MarkerAdd",""+i);
                                                    }
                                                    Log.d("MarkerAdd","Done");
                                                }
                                            }));
                                            Constant.jsonFailureFlag = false;
                                            break;
                                        } else {
                                            Thread.sleep(5000);
                                            counter++;
                                            if(counter > 100){
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
                                Log.d("addMarker","end");
                            }
                        }).start();

                    }
                }
            }
        });

        //横をスライドすると出てくるメニューのグラフボタンのイベント
        Button calendarActivityButton;
        calendarActivityButton = findViewById(R.id.CalendarActivityButton);
        calendarActivityButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View view) {
                Log.d("CalendarActivityButton","push");
                Intent intent = new Intent(getApplication(), CalendarActivity.class);
                startActivity(intent);
            }
        });

        Button cow_information_button;
        cow_information_button = findViewById(R.id.CowInformationButton);
        cow_information_button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View view) {
                Log.d("CalendarActivityButton","push");
                Intent intent = new Intent(getApplication(), CowInformationActivity.class);
                startActivity(intent);
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
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
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                    Log.d("droneOK",""+Constant.droneOK);
                    Log.d("droneThreadOK",""+Constant.droneThreadOK);
                    String id = marker.getId();
                    Log.d("MarkerClickListener","in");
                    Log.d("MarkerClickListener",""+id);
                    int num = Integer.parseInt(id.substring(1));
                    Log.d("num",""+num);
                    Constant.CowNum = 0;
                    Constant.CowNum = (num % Constant.MVoA) + 1;
                    Log.d("CowNumber",""+Constant.CowNum);
                    DroneDialog droneDialog = new DroneDialog();
                    Bundle bundle = new Bundle();
                    if(Constant.droneThreadOK) {
                        bundle.putInt("CowNum",Constant.CowNum);
                        droneDialog.setArguments(bundle);
                        droneDialog.show(getFragmentManager(),"");
                        Log.d("Dialog","end");

                        DroneThread droneThread = new DroneThread();
                        Thread dThread = new Thread(droneThread);
                        dThread.start();
                    } else {
                        bundle.putInt("CowNum",0);
                        droneDialog.setArguments(bundle);
                        droneDialog.show(getFragmentManager(),"");
                        Log.d("elseDialog","end");
                    }
                return false;
            }
        });
        //デバッグ用
        Toast.makeText(this,"地図の描画完了", Toast.LENGTH_LONG).show();
    }
}
