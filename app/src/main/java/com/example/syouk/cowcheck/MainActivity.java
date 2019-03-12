package com.example.syouk.cowcheck;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
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
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback , OnMapLoadedCallback {

    private GoogleMap mMap;
    public Button reloadbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        reloadbutton = findViewById(R.id.ReloadButton);

        reloadbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                while (true){
                    Log.d("LoadMapFinishedFlag",""+Constant.loadmapfinishedFlag);
                    if(Constant.loadmapfinishedFlag){
                        LatLng toretorevillage = new LatLng(33.6806225,135.3751043);
                        mMap.addMarker(new MarkerOptions().position(toretorevillage).title("Marker is とれとれビレッジ") );
                        break;
                    }
                }
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
        Toast.makeText(this,"地図の描画完了", Toast.LENGTH_LONG).show();
        Constant.loadmapfinishedFlag = true;

        LatLng tokyo = new LatLng(35.689487, 139.691706);
        mMap.addMarker(new MarkerOptions().position(tokyo).title("Marker in Tokyo"));

        LatLng tokyodisneyland = new LatLng(35.632896,139.880394);
        mMap.addMarker(new MarkerOptions().position(tokyodisneyland).title("Marker in TokyoDisneyLand"));

    }
}
