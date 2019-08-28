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

    private EditText editText;
    private String detailText = "";
    private String textFilePath;

    protected Boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        final Intent intent = getIntent();
        String date = intent.getStringExtra("date");
        Log.d("date", date);
        String temText = intent.getStringExtra("detail");

        flag = false;

        editText = findViewById(R.id.editText);
        if(temText != null){
            editText.setText(temText);
        }

        textFilePath = Constant.detailFilePath + "/" + date + ".txt";

        TextView dateText = findViewById(R.id.dateText);
        dateText.setText(date);


        Button saveButton = findViewById(R.id.saveButton);
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

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("backButton","pushed");
                Intent intent1 = new Intent();
                intent1.putExtra("detail",detailText);
                intent1.putExtra("flag",flag);
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
            intent1.putExtra("flag",flag);
            setResult(RESULT_OK, intent1);
            finish();
        }
        return true;
    }

    private void SaveFileFunc() {
        try (FileOutputStream fileOutputstream =new FileOutputStream(textFilePath)) {
            fileOutputstream.write(detailText.getBytes());
            flag = true;
            Log.d("file","created");
        } catch (IOException e) {
            Log.e("error","IOException");
            e.printStackTrace();
        }
    }
}

