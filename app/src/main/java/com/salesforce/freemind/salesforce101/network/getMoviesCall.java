package com.salesforce.freemind.salesforce101.network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.salesforce.freemind.salesforce101.controller.AppContext;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by anant on 2017-09-20.
 */


public class getMoviesCall extends AsyncTask<String , Void, String> {


    private final String  TAG = getMoviesCall.class.getSimpleName();

    Context context;

    String mAllMoviesObject;


    public getMoviesCall() {
    }

    OkHttpClient moviesClient = AppContext.mGlobalOkHttpClient;

    public getMoviesCall(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... strings) {


        Request moviesRequest =  new Request.Builder()
                .url(strings[0]).build();//Utils.addHeader().url(mGetImagesUrl).build();

        try {
            Response response = moviesClient.newCall(moviesRequest).execute();


            if(response.isSuccessful()){
               mAllMoviesObject = response.body().string();
            }

           Log.d(TAG , mAllMoviesObject );

            return mAllMoviesObject;


        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


}
