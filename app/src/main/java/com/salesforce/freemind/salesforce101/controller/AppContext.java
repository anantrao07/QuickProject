package com.salesforce.freemind.salesforce101.controller;

import android.app.Application;

import okhttp3.OkHttpClient;

/**
 * Created by anant on 2017-09-20.
 */



public class AppContext extends Application {
    public  static OkHttpClient mGlobalOkHttpClient;


    @Override
    public void onCreate() {
        super.onCreate();

        mGlobalOkHttpClient = new OkHttpClient.Builder().build();


    }

    public static OkHttpClient getClient(){
        return  mGlobalOkHttpClient;
    }
}

