package com.example.syouk.Agricowture;

import android.annotation.SuppressLint;
import android.content.Context;

import com.google.android.gms.maps.model.Marker;

class Constant {
    @SuppressLint("StaticFieldLeak")
    static Context CONTEXT;
    final static int CONNECTION_TIME_OUT = 30 * 1000;
    final static int READ_TIME_OUT = 30 * 1000;
    static boolean jsonFlag = false;
    static boolean jsonFailureFlag = true;
    static boolean mapLoadFinishedFlag = false;
    static boolean droneOK = false;
    static boolean droneWhileEscape = false;
    static boolean droneThreadOK = true;
    static int MVoA = 0;
    static int CowNum;
    static String[] cowID;
    static Double[] lat;
    static Double[] lng;
    static boolean[] estrus;
    static String[] cowName;
    static Marker[] marker;
    static String detailFilePath;
    static String resultText;
    static String urlSt;
}
