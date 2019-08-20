package com.example.syouk.Agricowture;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileOutputStream;
import java.io.IOException;

public class DetailActivity extends AppCompatActivity {

    private TextView dateText;
    private EditText editText;
    private Button saveButton;
    private Button backButton;
    private String detailText = "";
    private String date;
    private String textFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        final Intent intent = getIntent();
        date = intent.getStringExtra("date");
        Log.d("date", date);

        textFilePath = Constant.detailFilePath + "/" + date + ".txt";

        dateText = findViewById(R.id.dateText);
        dateText.setText(date);

        editText = findViewById(R.id.editText);

        saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detailText = editText.getText().toString();
                if (detailText.length() != 0) {
                    Log.d("detailText", detailText);
                    SaveFileFunc();
                } else {
                    Log.d("detailText", "no input");
                }
            }
        });

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("backButton","pushed");
                Intent intent1 = new Intent();
                intent1.putExtra("detail",detailText);
                setResult(RESULT_OK,intent1);
                finish();
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.d("BackKey","pushed");
            Intent intent1 = new Intent();
            intent1.putExtra("detail", detailText);
            setResult(RESULT_OK, intent1);
            finish();
        }
        return true;
    }

    private void SaveFileFunc() {
        try (FileOutputStream fileOutputstream =new FileOutputStream(textFilePath)) {
            fileOutputstream.write(detailText.getBytes());
            Log.d("file","created");
        } catch (IOException e) {
            Log.e("error","IOException");
            e.printStackTrace();
        }
    }
}

