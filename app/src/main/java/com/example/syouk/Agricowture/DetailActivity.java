package com.example.syouk.Agricowture;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DetailActivity extends AppCompatActivity {

    private TextView date_text;
    private EditText editText;
    private Button save_button;
    private Button back_button;
    private String detail_text = "";
    private String date;
    private String textfile_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        final Intent intent = getIntent();
        date = intent.getStringExtra("date");
        Log.d("date", date);

        textfile_path = Constant.detail_file_path + "/" + date + ".txt";
        File detail_file = new File(textfile_path);

        date_text = findViewById(R.id.date_text);
        date_text.setText(date);

        editText = findViewById(R.id.edit_text);

        save_button = findViewById(R.id.save_button);
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detail_text = editText.getText().toString();
                if (detail_text.length() != 0) {
                    Log.d("detail_text", detail_text);
                    SaveFile_func();
                } else {
                    Log.d("detail_text", "no input");
                }
            }
        });

        back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("back_button","pushed");
                Intent intent1 = new Intent();
                intent1.putExtra("detail",detail_text);
                setResult(RESULT_OK,intent1);
                finish();
            }
        });

    }

    @Override
    public boolean onKeyDown(int key_code, KeyEvent event) {
        if (key_code == KeyEvent.KEYCODE_BACK) {
            Log.d("BackKey","pushed");
            Intent intent1 = new Intent();
            intent1.putExtra("detail", detail_text);
            setResult(RESULT_OK, intent1);
            finish();
        }
        return true;
    }

    private void SaveFile_func() {
        try (FileOutputStream fileOutputstream =new FileOutputStream(textfile_path)) {
            fileOutputstream.write(detail_text.getBytes());
            Log.d("file","created");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

