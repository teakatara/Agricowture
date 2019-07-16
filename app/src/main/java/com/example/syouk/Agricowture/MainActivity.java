package com.example.syouk.Agricowture;

import android.Manifest;
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

public class MainActivity extends FragmentActivity implements OnMapReadyCallback , OnMapLoadedCallback {

    public static GoogleMap mMap;
    private boolean reloadflag = true;
    private boolean firstbootflag;

    //デバッグ用
    private long Count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Constant.CONTEXT = this;
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Button reloadbutton;
        reloadbutton = findViewById(R.id.ReloadButton);
        firstbootflag = true;

        final Handler handler = new Handler();

        reloadbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //バグ防止
                Constant.dronethreadOK = true;
                Constant.droneOK = false;
                Constant.droneWhileEscape = false;

                Log.d("loadmapfinishedFlag",""+Constant.loadmapfinishedFlag);
                if (Constant.loadmapfinishedFlag){
                    Log.d("reloadflag",""+reloadflag);
                    if(reloadflag) {
                        Log.d("firstbootflag",""+firstbootflag);
                        if(firstbootflag) {
                            firstbootflag = false;
                        } else {
                            if(!Constant.jsonFailureflag) {
                                for (int i = 0; i < Constant.MVoA; i++) {
                                    Constant.marker[i].remove();
                                }
                            } else {
                              Constant.jsonFailureflag = false;
                            }
                        }
                        Count = 0;
                        reloadflag = false;
                        Constant.jsonflag = false;
                        JsonThread jsonThread = new JsonThread();
                        Thread thread = new Thread(jsonThread);
                        thread.start();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                while(true){
                                    Log.d("jsonFailureflag",""+Constant.jsonFailureflag);
                                    Log.d("jsonflag",""+Constant.jsonflag);
                                    try{
                                        if(Constant.jsonflag){
                                            Log.d("MarkerAdd","Start");
                                            Constant.marker = new Marker[Constant.MVoA];
                                            handler.post(new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    for(int i = 0;i < Constant.MVoA;i++) {
                                                        Log.d("MarkerAdd", "for = " + i);
                                                        LatLng place = new LatLng(Constant.lat[i], Constant.lng[i]);
                                                        Log.d("LatLng", "for = " + i);
                                                        Log.d("LatLng",""+place.toString());
                                                        Constant.marker[i] = mMap.addMarker(new MarkerOptions().position(place).title(Constant.cowID[i]));
                                                        Log.d("MarkerAdd",""+i);
                                                    }
                                                    Log.d("MarkerAdd","Done");
                                                }
                                            }));
                                            break;
                                        } else if(Constant.jsonFailureflag){
                                            Log.d("jsonFailureflagin", "true");
                                            break;
                                        } else {
                                            Thread.sleep(5000);
                                        }
                                    } catch (InterruptedException e) {
                                        Log.e("error","InterruptedException");
                                        break;
                                    }
                                }
                                reloadflag = true;
                                Log.d("addMarker","end");
                            }
                        }).start();

                    }
                }
            }
        });

        Button graphactivity_button;
        graphactivity_button = findViewById(R.id.GraphActivity_button);
        graphactivity_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), GraphActivity.class);
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
        Constant.loadmapfinishedFlag = true;
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                    Log.d("droneOK",""+Constant.droneOK);
                    Log.d("dronethreadOK",""+Constant.dronethreadOK);
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
                    if(Constant.dronethreadOK) {
                        bundle.putInt("CowNum",Constant.CowNum);
                        droneDialog.setArguments(bundle);
                        droneDialog.show(getFragmentManager(),"");
                        Log.d("dialog","end");

                        DroneThread droneThread = new DroneThread();
                        Thread dthread = new Thread(droneThread);
                        dthread.start();
                    } else {
                        bundle.putInt("CowNum",0);
                        droneDialog.setArguments(bundle);
                        droneDialog.show(getFragmentManager(),"");
                        Log.d("elsedialog","end");
                    }
                return false;
            }
        });
        //デバッグ用
        Toast.makeText(this,"地図の描画完了", Toast.LENGTH_LONG).show();
    }
}
