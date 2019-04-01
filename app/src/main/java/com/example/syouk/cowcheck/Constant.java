package com.example.syouk.cowcheck;

import android.annotation.SuppressLint;
import android.content.Context;

import com.google.android.gms.maps.model.Marker;

class Constant {
    @SuppressLint("StaticFieldLeak")
    static Context CONTEXT;
    static boolean jsonflag = false;
    static boolean jsonFailureflag = false;
    static boolean reloadflag = true;
    static boolean loadmapfinishedFlag = false;
    static int MVoA;
    static String[] cowID;
    static Double[] lat;
    static Double[] lng;
    static Marker[] marker;
}
