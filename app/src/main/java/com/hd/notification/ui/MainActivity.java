package com.hd.notification.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.hd.notification.R;


/**
 * @Description: 
 * @Author: liaoyuhuan
 * @CreateDate: 2021/9/18
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_recorder_and_translating).setOnClickListener(v -> {
            Intent intent = new Intent(this, RecorderAndTranslatingActivity.class);
            MainActivity.this.startActivity(intent);
        });
    }
}