package com.example.syouk.Agricowture;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_actibity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Intent intent = getIntent();
        String videoFilePath = intent.getStringExtra("videoFilePath");

        VideoView videoView = findViewById(R.id.videoView);
        videoView.setVideoPath(videoFilePath);
        videoView.start();
        videoView.setMediaController(new MediaController(this));
    }
}
