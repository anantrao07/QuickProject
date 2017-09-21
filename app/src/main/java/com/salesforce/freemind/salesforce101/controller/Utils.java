package com.salesforce.freemind.salesforce101.controller;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.compat.BuildConfig;
import android.util.Log;

/**
 * Created by anant on 2017-09-20.
 */

public class Utils {
    protected final  static String review_results = "results";


    public static void debugLogStatment(String tag , String message){

        if(message!= null){
            if(BuildConfig.DEBUG ){

                Log.d(tag , message);

            }
        }
    }


    public static boolean isNetworkAvailable(Context context){

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return  networkInfo != null && networkInfo.isConnected();


    }


}
