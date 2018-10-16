package com.github.lany192.blurdialog.sample;

import android.app.Application;

import com.lany.box.Box;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Box.of().init(this, BuildConfig.DEBUG);
    }
}
