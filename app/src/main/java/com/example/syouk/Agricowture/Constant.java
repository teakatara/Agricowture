package com.example.syouk.Agricowture;

import android.annotation.SuppressLint;
import android.content.Context;

import com.google.android.gms.maps.model.Marker;

class Constant {
    @SuppressLint("StaticFieldLeak")
    static Context CONTEXT;
    final static int CONNECTION_TIME_OUT = 30 * 1000;
    final static int READ_TIME_OUT = 30 * 1000;
    static boolean jsonflag = false;
    static boolean jsonFailureflag = false;
    static boolean reloadflag = true;
    static boolean loadmapfinishedFlag = false;
    static boolean droneOK = false;
    static boolean droneWhileEscape = false;
    static boolean dronethreadOK = true;
    static int MVoA;
    static int CowNum;
    static String[] cowID;
    static Double[] lat;
    static Double[] lng;
    static Marker[] marker;
    static String detail_file_path;
}
