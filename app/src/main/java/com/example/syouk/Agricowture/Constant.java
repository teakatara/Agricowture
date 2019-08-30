package com.example.syouk.Agricowture;

import android.annotation.SuppressLint;
import android.content.Context;

import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;

class Constant {
    @SuppressLint("StaticFieldLeak")
    static Context CONTEXT;
    final static int CONNECTION_TIME_OUT = 30 * 1000;
    final static int READ_TIME_OUT = 30 * 1000;
    static boolean jsonFlag = false;
    static boolean jsonFailureFlag = true;
    static boolean mapLoadFinishedFlag = false;
    static boolean droneThreadOK = true;
    static boolean downloadFlag = false;
    static boolean fileDecompressionFlag = false;
    static int MVoA = 0;
    static int cowNum;
    static String[] cowID;
    static Double[] lat;
    static Double[] lng;
    static boolean[] estrus;
    static String[] cowName;
    static HashMap<String,String> cowNameMap;
    static Marker[] marker;
    static String detailFilePath;
    static String cowNameFilePath;
    static String sdCardPath;
    static String resultText;
    static String urlSt;
    final static String fUrl = "https://4cce9ab4.jp.ngrok.io";
}
