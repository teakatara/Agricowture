package com.example.syouk.cowcheck;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MyThread implements Runnable {

    @Override
    public void run() {
        final int CONNECTION_TIME_OUT = 30 * 1000;
        final int READ_TIME_OUT = 30 * 1000;
        HttpURLConnection con;
        URL url;
        String urlSt = "https://cowcheck.herokuapp.com/get";
        StringBuilder result = new StringBuilder();


        try {
            url = new URL(urlSt);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(CONNECTION_TIME_OUT);
            con.setReadTimeout(READ_TIME_OUT);
            con.setInstanceFollowRedirects(false);
            con.setDoInput(true);
            Log.d("URLConnection", "Connecting");
            con.connect();
            int statusCode = con.getResponseCode();
            Log.d("statusCode", "" + statusCode);
            if (statusCode == HttpURLConnection.HTTP_OK) {

                final InputStream in = con.getInputStream();
                Log.d("in", "" + in);
                final InputStreamReader inReader = new InputStreamReader(in, StandardCharsets.UTF_8);
                Log.d("inReader", "" + inReader);
                final BufferedReader bufferedReader = new BufferedReader(inReader);
                Log.d("bufferedReader", "" + bufferedReader);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    Log.d("while", "true");
                    Log.d("line", "" + line);
                    result.append(line);
                    Log.d("result", "" + result);
                }
                bufferedReader.close();
                inReader.close();
                in.close();

                Log.d("json", "" + result);
            }
        } catch (FileNotFoundException e) {
            Log.e("error", "FileNotFoundException");
        } catch (MalformedURLException e) {
            Log.e("error", "MalformedURLException");
        } catch (IOException e) {
            Log.e("error", "IOException");
        }
        try {
            JSONArray jsonArray = new JSONArray(result.toString());
            Constant.MVoA = jsonArray.length();
            Log.d("JSONArrayLength",""+Constant.MVoA);
            Constant.cowID = new String[Constant.MVoA];
            Constant.lat = new Double[Constant.MVoA];
            Constant.lng = new Double[Constant.MVoA];
            for (int i = 0; i < Constant.MVoA; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Constant.cowID[i] = jsonObject.getString("CowID");
                Constant.lat[i] = jsonObject.getDouble("Lat");
                Constant.lng[i] = jsonObject.getDouble("Lng");
            }
            //デバッグ用
            for (int i = 0; i < Constant.MVoA; i++) {
                Log.d("CowID", "" + Constant.cowID[i]);
                Log.d("Lat", "" + Constant.lat[i]);
                Log.d("Lng", "" + Constant.lng[i]);
            }
            Constant.jsonflag = true;
            Log.d("CowData","Successful reception");
        } catch (JSONException e) {
            Log.e("error", "JSONArrayException");
        }
        if(!Constant.jsonflag){
            Log.d("!jsonflag","in");
            Constant.jsonFailureflag = true;
        }
        else {
            Log.d("!jsonflag","out");
        }
    }
}
