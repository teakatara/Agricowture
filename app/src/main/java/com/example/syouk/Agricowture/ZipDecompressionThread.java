package com.example.syouk.Agricowture;

import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipDecompressionThread implements Runnable {

    @Override
    public void run() {
        String fileName = Constant.sdCardPath + "/Download/a.zip";

        ZipInputStream in;
        BufferedOutputStream out;
        ZipEntry zipEntry;
        int len;

        try {
            in = new ZipInputStream(new FileInputStream(fileName));

            // ZIPファイルに含まれるエントリに対して順にアクセス
            while ((zipEntry = in.getNextEntry()) != null) {
                File newFile = new File(zipEntry.getName());

                // 出力用ファイルストリームの生成
                out = new BufferedOutputStream(new FileOutputStream(Constant.sdCardPath + "/videos/"+  newFile.getName()));

                // エントリの内容を出力
                byte[] buffer = new byte[1024];
                while ((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }

                in.closeEntry();
                out.close();
            }
            Log.d("fileDecompression","complete");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //解凍し終わったファイルを削除
            File deleteDir = new File(Constant.sdCardPath + "/Download");
            File[] deleteFiles = deleteDir.listFiles();
            for (File deleteFile : deleteFiles) {
                if (deleteFile.delete()) {
                    Log.d("fileDelete", "complete");
                } else {
                    Log.d("fileDelete", "failed");
                }
            }
            //ファイルの解凍が終わったことを知らせるフラグ
            Constant.fileDecompressionFlag = true;
        }
    }
}
