package com.adifaisalr.newsapitest.application;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by Adi Faisal Rahman on 7/17/2017.
 */

public class NewsApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(this);
    }
}
