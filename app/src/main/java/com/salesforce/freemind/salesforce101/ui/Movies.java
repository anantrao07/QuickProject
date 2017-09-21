package com.salesforce.freemind.salesforce101.ui;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.silvestrpredko.dotprogressbar.DotProgressBar;
import com.salesforce.freemind.salesforce101.R;
import com.salesforce.freemind.salesforce101.controller.AllMoviesAdapter;
import com.salesforce.freemind.salesforce101.controller.AppContext;
import com.salesforce.freemind.salesforce101.controller.Utils;
import com.salesforce.freemind.salesforce101.data.AllMoviesColumns;
import com.salesforce.freemind.salesforce101.data.AllMoviesData;
import com.salesforce.freemind.salesforce101.data.AllMoviesProvider;
import com.salesforce.freemind.salesforce101.data.ResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Movies extends AppCompatActivity {

/*The app uses MovieDB API to fetch the  popular movies and the second screen corresponds to its details like reviews ,trailers
The API key required is set up in string xml file

  *
  * */


    RecyclerView allMoviesRecyclerView;
    RecyclerView.LayoutManager recyclerLayoutManager;
    AllMoviesAdapter allMoviesAdapter;
    Context context;
    String allMoviesUrl;
    ArrayList<AllMoviesData> allMoviesList;
    DotProgressBar moviesProgressBar;

    private static final String ID = "id";
    private static final String MOVIETITLE = "title";
    private static final String MOVIEPATH = "poster_path";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        context = this;

        allMoviesRecyclerView = (RecyclerView) findViewById(R.id.all_movies_recycler_view);
        recyclerLayoutManager = new GridLayoutManager(this, 2);
        moviesProgressBar = (DotProgressBar) findViewById(R.id.movies_dot_progress_bar);
        allMoviesRecyclerView.setLayoutManager(recyclerLayoutManager);


        allMoviesUrl = "https://api.themoviedb.org/3/discover/movie?api_key=" + getResources().getString(R.string.api_key) + "&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1";

      /* getMoviesBtn = (Button)findViewById(R.id.get_movies_btn);
        getMoviesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new getMoviesCall().execute("https://api.themoviedb.org/3/movie/550?api_key="+getResources().getString(R.string.api_key));
            }
        });
*/

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("movie_id_info"));


    }


    @Override
    protected void onResume() {
        super.onResume();

        if (allMoviesList == null) {
            allMoviesList = new ArrayList<>();
        }
        if (Utils.isNetworkAvailable(context)) {
            new getMoviesCall().execute(allMoviesUrl);
        } else if (!Utils.isNetworkAvailable(context)) {
            Toast.makeText(context, getResources().getString(R.string.offline_message) + " checking offline data", Toast.LENGTH_SHORT).show();
            getofflineMovies();


        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
    private void getofflineMovies() {


        Cursor c = context.getContentResolver().query(AllMoviesProvider.AllMovies.CONTENT_URI, null,
                AllMoviesColumns.MOVIEID,
                null,
                null);

        if (c.getCount() > 0) {
            Log.d("cursor count", String.valueOf(c.getCount()));
            allMoviesList = new ArrayList<>();


            for (int i = 0; i < c.getCount(); i++) {
                AllMoviesData amd = new AllMoviesData();
                c.moveToPosition(i);
                amd.setMovieId(c.getString(c.getColumnIndex(AllMoviesColumns.MOVIEID)));
                amd.setMoviesPoster(c.getString(c.getColumnIndex(AllMoviesColumns.MOVIEPOSTERPATH)));
                amd.setMovieTitle(c.getString(c.getColumnIndex(AllMoviesColumns.MOVIETITLE)));

                allMoviesList.add(amd);


            }

            c.close();
            if (allMoviesAdapter == null) {
                allMoviesAdapter = new AllMoviesAdapter(context, allMoviesList);
                allMoviesRecyclerView.setAdapter(allMoviesAdapter);
            }

        }
        // return allMoviesList;

    }

    public class getMoviesCall extends AsyncTask<String, Void, ResponseHandler> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            moviesProgressBar.setVisibility(View.VISIBLE);
            allMoviesRecyclerView.setVisibility(View.GONE);
        }

        ResponseHandler allMoviesResponseHandler = new ResponseHandler();
        private final String TAG = getMoviesCall.class.getSimpleName();


        OkHttpClient moviesClient = AppContext.mGlobalOkHttpClient;


        @Override
        protected ResponseHandler doInBackground(String... strings) {


            Request moviesRequest = new Request.Builder()
                    .url(strings[0]).build();//Utils.addHeader().url(mGetImagesUrl).build();

            try {
                Response response = moviesClient.newCall(moviesRequest).execute();

                allMoviesResponseHandler.setResponseData(response.code(), response.body().string());
            } catch (IOException ioException) {

                ioException.printStackTrace();
            }

            Utils.debugLogStatment(TAG, allMoviesResponseHandler.getResponseBody());


            return allMoviesResponseHandler;
        }

        @Override
        protected void onPostExecute(ResponseHandler responseHandler) {
            super.onPostExecute(responseHandler);

            if (responseHandler != null) {
                moviesProgressBar.setVisibility(View.GONE);
                allMoviesRecyclerView.setVisibility(View.VISIBLE);
                int statusCode = responseHandler.getResponseCode() / 100;

                if (statusCode == 2) {

                    Log.d("response ", responseHandler.getResponseBody());

                    JSONObject allMoviesJsonObj;
                    AllMoviesData allMoviesData;

                    try {
                        allMoviesJsonObj = new JSONObject(responseHandler.getResponseBody());

                        JSONArray results = allMoviesJsonObj.getJSONArray("results");

                        allMoviesList = new ArrayList<>();

                        for (int i = 0; i < results.length(); i++) {
                            allMoviesData = new AllMoviesData();
                            JSONObject resultObject = results.getJSONObject(i);
                            allMoviesData.setMovieTitle(resultObject.getString("title"));
                            Utils.debugLogStatment("response ", resultObject.getString("title"));
                            allMoviesData.setMovieId(resultObject.getString("id"));
                            Utils.debugLogStatment("response ", resultObject.getString("id"));
                            allMoviesData.setMoviesPoster(resultObject.getString("poster_path"));
                            Utils.debugLogStatment("response ", allMoviesData.getMoviesPoster());
                            allMoviesList.add(allMoviesData);

                        }

                        if (allMoviesAdapter == null) {
                            allMoviesAdapter = new AllMoviesAdapter(context, allMoviesList);
                        }
                        allMoviesRecyclerView.setAdapter(allMoviesAdapter);

                        writeTripDetailsToDB(allMoviesList, context);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                if (statusCode != 2) {

                    Toast.makeText(context, getResources().getString(R.string.offline_message), Toast.LENGTH_SHORT).show();
                    /*if (allMoviesAdapter == null) {
                        allMoviesAdapter = new AllMoviesAdapter(context, allMoviesList);
                        allMoviesRecyclerView.setAdapter(allMoviesAdapter);
                    }*/

                }

            }
        }

        private void writeTripDetailsToDB(ArrayList<AllMoviesData> allMovies, Context context) {
            ContentValues movieInfo = new ContentValues();


            for (int i = 0; i < allMovies.size(); i++) {

                Cursor c = context.getContentResolver().query(AllMoviesProvider.AllMovies.CONTENT_URI, null,
                        AllMoviesColumns.MOVIEID + "=?",
                        new String[]{allMovies.get(i).getMovieId()},
                        null);
                if (c.getCount() == 0) {
                    movieInfo.put(AllMoviesColumns.MOVIEID, allMovies.get(i).getMovieId());
                    movieInfo.put(AllMoviesColumns.MOVIETITLE, allMovies.get(i).getMovieTitle());
                    movieInfo.put(AllMoviesColumns.MOVIEPOSTERPATH, allMovies.get(i).getMoviesPoster());
                    movieInfo.put(AllMoviesColumns.MOVIESAVED, 1);
                    context.getContentResolver().insert(AllMoviesProvider.AllMovies.CONTENT_URI, movieInfo);
                }
                c.close();
            }
        }
    }
        public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Get extra data included in the Intent
                String ItemName = intent.getStringExtra("position");

                String movie_id = allMoviesList.get(Integer.valueOf(ItemName)).getMovieId();

                if (movie_id != null) {
                    Intent movieDetails = new Intent(context, MovieDetails.class);
                    movieDetails.putExtra("movie_id", movie_id);
                    movieDetails.putExtra("movie_title", allMoviesList.get(Integer.valueOf(ItemName)).getMovieTitle());
                    movieDetails.putExtra("movie_poster" , allMoviesList.get(Integer.valueOf(ItemName)).getMoviesPoster());


                    startActivity(movieDetails);
                } else if (movie_id == null) {

                    Toast.makeText(Movies.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }
