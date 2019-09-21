package com.example.syouk.Agricowture;

import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class JsonThread implements Runnable {

    @Override
    public void run() {

        HttpURLConnection con;
        URL url;
        StringBuilder result = new StringBuilder();

        try {
            url = new URL(Constant.urlSt);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(Constant.CONNECTION_TIME_OUT);
            con.setReadTimeout(Constant.READ_TIME_OUT);
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
                    Log.d("line", line);
                    result.append(line);
                    Log.d("result", "" + result);
                }
                bufferedReader.close();
                inReader.close();
                in.close();

                //取得したJSONデータをStringで保存
                Constant.resultText = result.toString();
                Log.d("json", Constant.resultText);
                //JSONデータの取得が完了したことを知らせるフラグ
                Constant.jsonFlag = true;
            }
        } catch (FileNotFoundException e) {
            Log.e("error", "FileNotFoundException");
            e.printStackTrace();
        } catch (MalformedURLException e) {
            Log.e("error", "MalformedURLException");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("error", "IOException");
            e.printStackTrace();
        }
    }
}
