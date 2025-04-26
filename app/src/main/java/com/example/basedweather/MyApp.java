package com.example.basedweather;

import android.app.Application;
import com.jakewharton.threetenabp.AndroidThreeTen;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
    }
}