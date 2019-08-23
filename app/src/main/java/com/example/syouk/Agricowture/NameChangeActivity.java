package com.example.syouk.Agricowture;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class NameChangeActivity extends AppCompatActivity {

    private String afterNameText;
    private EditText afterName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_change);
        final Intent intent = getIntent();
        final int position = intent.getIntExtra("Position",-1);
        TextView beforeName = findViewById(R.id.beforeName);
        beforeName.setText(Constant.cowName[position]);
        afterNameText = "";

        afterName = findViewById(R.id.afterName);
        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                afterNameText = afterName.getText().toString();
                Log.d("afterNameText",afterNameText);
                if(!afterNameText.equals("")) {
                    Constant.cowNameMap.put(Constant.cowID[position], afterNameText);
                    Constant.cowName[position] = afterNameText;
                } else {
                    Constant.cowNameMap.remove(Constant.cowID[position]);
                    Constant.cowName[position] = Constant.cowID[position];
                }

                StringBuilder str = new StringBuilder();
                String tem;
                for(int i = 0; i < Constant.MVoA; i++){
                    tem = Constant.cowNameMap.get(Constant.cowID[i]);
                    if(tem != null){
                        str.append(Constant.cowID[i]).append(",").append(tem).append("\n");
                    }
                }
                Log.d("str",str.toString());

                try(FileOutputStream fileOutputStream = openFileOutput("cowName.txt", Context.MODE_PRIVATE)){
                    fileOutputStream.write(str.toString().getBytes());
                } catch (FileNotFoundException e) {
                    Log.e("error","FileNotFoundException");
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.e("error","IOException");
                    e.printStackTrace();
                }
            }
        });

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent();
                setResult(RESULT_OK, intent1);
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.d("BackKey","pushed");
            Intent intent1 = new Intent();
            setResult(RESULT_OK, intent1);
            finish();
        }
        return true;
    }
}
