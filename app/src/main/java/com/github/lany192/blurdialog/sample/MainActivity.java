package com.github.lany192.blurdialog.sample;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(view -> {
            SampleDialogFragment dialogFragment = SampleDialogFragment.newInstance();
            dialogFragment.show(getSupportFragmentManager(), "SampleDialogFragment");
        });
        findViewById(R.id.button2).setOnClickListener(view -> {
            SampleBottomDialogFragment dialogFragment = SampleBottomDialogFragment.newInstance();
            dialogFragment.show(getSupportFragmentManager(), "SampleDialogFragment");
        });
    }
}
