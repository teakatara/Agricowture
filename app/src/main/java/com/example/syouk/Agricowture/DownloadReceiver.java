package com.example.syouk.Agricowture;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class DownloadReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)){
            Log.d("download","complete");
            Toast toast = Toast.makeText(MyApplication.getAppContext(),"downloadComplete",Toast.LENGTH_SHORT);
            toast.show();
            Constant.downloadFlag = true;
            ZipDecompressionThread zipDecompressionThread = new ZipDecompressionThread();
            Thread thread = new Thread(zipDecompressionThread);
            thread.start();
            Log.d("zipDecompression","start");
        }
    }
}
