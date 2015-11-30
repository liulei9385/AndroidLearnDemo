package com.leileitest.paperdemo.app;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

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
        initFrescoImageLoader();
    }

    private void initFrescoImageLoader() {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        ImagePipelineConfig config = OkHttpImagePipelineConfigFactory
                .newBuilder(this, okHttpClient)
                        // other setters
                        // setNetworkFetchProducer is already called for you
                .build();
        Fresco.initialize(this, config);
    }

}