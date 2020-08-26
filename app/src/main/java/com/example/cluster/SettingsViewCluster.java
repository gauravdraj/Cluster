package com.example.cluster;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

public class SettingsViewCluster extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_view);
        Intent intent = getIntent();
        String title = intent.getStringExtra("Settings");
        setTitle(title);
    }
}
