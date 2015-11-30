package com.leileitest.paperdemo.app;

import android.app.Application;

import io.paperdb.Paper;

/**
 * Created by liulei
 * 20:50
 * 2015/11/28
 */
public class DemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Paper.init(getApplicationContext());
    }

}