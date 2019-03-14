package com.example.syouk.cowcheck;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

public class App extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        startService(new Intent(this,MyService.class));
        Log.d("startservice","Success");
    }
}
