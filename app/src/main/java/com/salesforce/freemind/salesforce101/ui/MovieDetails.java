package com.salesforce.freemind.salesforce101.ui;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.silvestrpredko.dotprogressbar.DotProgressBar;
import com.salesforce.freemind.salesforce101.BuildConfig;
import com.salesforce.freemind.salesforce101.R;
import com.salesforce.freemind.salesforce101.controller.AppContext;
import com.salesforce.freemind.salesforce101.controller.TrailersAdapter;
import com.salesforce.freemind.salesforce101.controller.Utils;
import com.salesforce.freemind.salesforce101.data.AllMoviesProvider;
import com.salesforce.freemind.salesforce101.data.ResponseHandler;
import com.salesforce.freemind.salesforce101.data.ReviewsColumns;
import com.salesforce.freemind.salesforce101.data.ReviewsData;
import com.salesforce.freemind.salesforce101.data.TrailerDetail;
import com.salesforce.freemind.salesforce101.data.TrailersColumns;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MovieDetails extends AppCompatActivity  {

    String mMovieId;
    String mUrlReviews;
    String mUrlTrailers;


    ArrayList<ReviewsData> reviewsDataList;
    ArrayList<TrailerDetail> trailerDetailsList = new ArrayList<>();
    DotProgressBar detailsProgressBar;
    ScrollView movieDetailsScrollContainer;
    ImageView moviePosterIv;
    TextView movieTitleTv;
    RecyclerView allTrailerRecyclerView;

    TrailersAdapter trailerAdapter;


/*
* due to a glitch the recycler view for the trailer list is not showing up . I would love to debug and check on where i am missing the small glitch.*/

    ArrayList<ResponseHandler> DetailsResponseHandlersList;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        context = this;
        Intent getMovieId = getIntent();

        mMovieId = getMovieId.getStringExtra("movie_id");
        detailsProgressBar = (DotProgressBar) findViewById(R.id.dot_progress_bar_movie_detail);
        movieDetailsScrollContainer = (ScrollView) findViewById(R.id.movie_detail_scroll_container);
        moviePosterIv = (ImageView)findViewById(R.id.movieposter_iv) ;
        movieTitleTv = (TextView)findViewById(R.id.movie_title_tv);
        allTrailerRecyclerView = (RecyclerView)findViewById(R.id.trailers_list_recyclerview);
        String  title= getMovieId.getStringExtra("movie_title");
        String poster = getMovieId.getStringExtra("movie_poster");


        movieTitleTv.setText(title);
        Picasso.with(context).load(BuildConfig.IMAGE_BASEURL+poster).into(moviePosterIv);

        Log.d("moviedetail", mMovieId);


        mUrlTrailers = BuildConfig.BASE_URL + "/movie/" + mMovieId + "/videos?api_key=" + getResources().getString(R.string.api_key) + "&language=en-US";

        Log.d("trailers", mUrlTrailers);

        mUrlReviews = BuildConfig.BASE_URL + "/movie/" + mMovieId + "/reviews?api_key=" + getResources().getString(R.string.api_key) + "&language=en-US&page=1";

        Log.d("reviews", mUrlReviews);
    }

    /**
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are <em>not</em> resumed.  This means
     * that in some cases the previous state may still be saved, not allowing
     * fragment transactions that modify the state.  To correctly interact
     * with fragments in their proper state, you should instead override
     * {@link #onResumeFragments()}.
     */
    @Override
    protected void onResume() {
        super.onResume();


        if (Utils.isNetworkAvailable(context)) {
            new getMovieDetails().execute(mUrlReviews, mUrlTrailers);

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

    public class getMovieDetails extends AsyncTask<String, Void, ArrayList<ResponseHandler>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            detailsProgressBar.setVisibility(View.VISIBLE);
            movieDetailsScrollContainer.setVisibility(View.GONE);


        }

        @Override
        protected ArrayList<ResponseHandler> doInBackground(String... strings) {

            DetailsResponseHandlersList = new ArrayList<>();
            OkHttpClient detailsokHttpClient = AppContext.getClient();
            synchronized (detailsokHttpClient) {


                for (int i = 0; i < 2; i++) {
                    Request moviesRequest = new Request.Builder()
                            .url(strings[i]).build();//Utils.addHeader().url(mGetImagesUrl).build();


                    ResponseHandler handler = new ResponseHandler();


                    try {
                        Response response = detailsokHttpClient.newCall(moviesRequest).execute();

                        handler.setResponseData(response.code(), response.body().string());
                        Log.d("response", handler.getResponseBody());

                        DetailsResponseHandlersList.add(handler);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                return DetailsResponseHandlersList;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<ResponseHandler> responseHandler) {
            super.onPostExecute(responseHandler);
            reviewsDataList = new ArrayList<>();
            //trailerDetailsList = new ArrayList<>();
            detailsProgressBar.setVisibility(View.GONE);
            movieDetailsScrollContainer.setVisibility(View.VISIBLE);




            if (responseHandler != null) {

                int reviewsStatusCode , trailerStatusCode;


                try {
                    parseReviews(responseHandler.get(0).getResponseBody());
                    writeReviewDetailsToDB(reviewsDataList, context);
                    reviewsStatusCode = responseHandler.get(0).getResponseCode()/100;
                    if(reviewsStatusCode==2){

                        getReviewDetails(mMovieId);
                      //  trailerAdapter = new TrailersAdapter(context , trailerDetailsList);
                      //  allTrailerRecyclerView.setAdapter(trailerAdapter);


                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                try {
                    parseTrailers(responseHandler.get(1).getResponseBody());
                    writeTrailerDetailsToDB(trailerDetailsList, context);

                    trailerStatusCode = (responseHandler.get(1).getResponseCode())/100;

                    if(trailerStatusCode ==2 ){

                        getTrailerDetails(mMovieId);


                        trailerAdapter = new TrailersAdapter(context , trailerDetailsList);
                        allTrailerRecyclerView.setAdapter(trailerAdapter);



                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }
    }

    private static void writeReviewDetailsToDB(ArrayList<ReviewsData> allReviews, Context context) {
        ContentValues movieInfo = new ContentValues();


        for (int i = 0; i < allReviews.size(); i++) {

            Cursor c = context.getContentResolver().query(AllMoviesProvider.MovieReviews.CONTENT_URI, null,
                    ReviewsColumns.COLUMN_RMOVIE_ID + "=?",
                    new String[]{allReviews.get(i).getReview_id()},
                    null);
            if (c.getCount() == 0) {
                movieInfo.put(ReviewsColumns.COLUMN_AUTHOR, allReviews.get(i).getReview_author());
                movieInfo.put(ReviewsColumns.COLUMN_CONTENT, allReviews.get(i).getReview_content());
                movieInfo.put(ReviewsColumns.COLUMN_RMOVIE_ID, allReviews.get(i).getReview_id());
                movieInfo.put(ReviewsColumns.COLUMN_MOVIE_ID_REVIEW, allReviews.get(i).getMovie_id());
                // movieInfo.put(ReviewsColumns.MOVIESAVED, 1);
                context.getContentResolver().insert(AllMoviesProvider.MovieReviews.CONTENT_URI, movieInfo);


            }
            c.close();
        }

    }


    private static void writeTrailerDetailsToDB(ArrayList<TrailerDetail> allTrailers, Context context) {
        ContentValues movieInfo = new ContentValues();


        for (int i = 0; i < allTrailers.size(); i++) {

            Cursor c = context.getContentResolver().query(AllMoviesProvider.MovieTrailers.CONTENT_URI, null,
                    TrailersColumns.COLUMN_TMOVIE_ID + "=?",
                    new String[]{allTrailers.get(i).getTrailer_id()},
                    null);
            if (c.getCount() == 0) {
                movieInfo.put(TrailersColumns.COLUMN_TMOVIE_ID, allTrailers.get(i).getTrailer_id());
                movieInfo.put(TrailersColumns.COLUMN_MOVIE_TRAILER_KEY, allTrailers.get(i).getKey());
                movieInfo.put(TrailersColumns.COLUMN_MOVIE_TRAILER_NAME, allTrailers.get(i).getName());
                movieInfo.put(TrailersColumns.COLUMN_MOVIE_ID_trailer, allTrailers.get(i).getMovie_id());
                // movieInfo.put(ReviewsColumns.MOVIESAVED, 1);
                context.getContentResolver().insert(AllMoviesProvider.MovieTrailers.CONTENT_URI, movieInfo);


            }
            c.close();
        }

    }

    public void parseReviews(String reviewsPayload) throws IOException, JSONException {

        JSONObject reviewsObject = new JSONObject(reviewsPayload);

        JSONArray reviewArray = reviewsObject.getJSONArray("results");

        for (int i = 0; i < reviewArray.length(); i++) {

            JSONObject reviewsTmp = reviewArray.getJSONObject(i);

            ReviewsData rd = new ReviewsData();
            rd.setReview_id(reviewsTmp.getString("id"));
            rd.setReview_author(reviewsTmp.getString("author"));
            rd.setReview_content(reviewsTmp.getString("content"));
            rd.setMovie_id(reviewsObject.getString("id"));

            reviewsDataList.add(rd);

        }
    }

    public void parseTrailers(String trailerPayload) throws IOException, JSONException {

        JSONObject trailerObject = new JSONObject(trailerPayload);

        JSONArray trailerArray = trailerObject.getJSONArray("results");

        for (int i = 0; i < trailerArray.length(); i++) {

            JSONObject trailerTmp = trailerArray.getJSONObject(i);

            TrailerDetail td = new TrailerDetail();
            td.setTrailer_id(trailerTmp.getString("id"));
            td.setName(trailerTmp.getString("name"));
            td.setKey(trailerTmp.getString("key"));
            td.setMovie_id(trailerObject.getString("id"));
            trailerDetailsList.add(td);

        }


    }

    private void getTrailerDetails(String movieId) {


        /*Cursor c = context.getContentResolver().query(AllMoviesProvider.MovieTrailers.CONTENT_URI, null,
                TrailersColumns.COLUMN_MOVIE_ID_trailer,
                null,
                null);*/
        Cursor c = context.getContentResolver().query(AllMoviesProvider.MovieTrailers.CONTENT_URI, null,
                TrailersColumns.COLUMN_MOVIE_ID_trailer + "=?",
                new String[]{movieId},
                null);

        if (c.getCount() > 0) {
            Log.d("cursor count", String.valueOf(c.getCount()));
           // trailerDetailsList = new ArrayList<>();

            TrailerDetail tdl;
            for (int i = 0; i < c.getCount(); i++) {
                tdl = new TrailerDetail();
                c.moveToPosition(i);
                tdl.setKey(c.getString(c.getColumnIndex(TrailersColumns.COLUMN_MOVIE_TRAILER_KEY)));
                tdl.setName(c.getString(c.getColumnIndex(TrailersColumns.COLUMN_MOVIE_TRAILER_NAME)));
                tdl.setTrailer_id(c.getString(c.getColumnIndex(TrailersColumns.COLUMN_TMOVIE_ID)));

                trailerDetailsList.add(tdl);


            }

            c.close();
            trailerAdapter = new TrailersAdapter(context, trailerDetailsList);
            allTrailerRecyclerView.setAdapter(trailerAdapter);
           /* if (allMoviesAdapter == null) {
                allMoviesAdapter = new AllMoviesAdapter(context, allMoviesList);
                allMoviesRecyclerView.setAdapter(allMoviesAdapter);
            }*/


        }

    }

    private void getReviewDetails(String movieId) {


      /*  Cursor c = context.getContentResolver().query(AllMoviesProvider.MovieReviews.CONTENT_URI, null,
                ReviewsColumns.COLUMN_MOVIE_ID_REVIEW + "=?",
                null,
                null);*/

        Cursor c = context.getContentResolver().query(AllMoviesProvider.MovieReviews.CONTENT_URI, null,
                ReviewsColumns.COLUMN_MOVIE_ID_REVIEW + "=?",
                new String[]{movieId},
                null);

        if (c.getCount() > 0) {
            Log.d("cursor count", String.valueOf(c.getCount()));
            trailerDetailsList = new ArrayList<>();
            ReviewsData rda;

            for (int i = 0; i < c.getCount(); i++) {
                 rda = new ReviewsData();
                c.moveToPosition(i);
                rda.setReview_id(c.getString(c.getColumnIndex(ReviewsColumns.COLUMN_RMOVIE_ID)));
                rda.setReview_author(c.getString(c.getColumnIndex(ReviewsColumns.COLUMN_AUTHOR)));
                rda.setReview_content(c.getString(c.getColumnIndex(ReviewsColumns.COLUMN_CONTENT)));

                reviewsDataList.add(rda);
            }

            c.close();
           /* if (allMoviesAdapter == null) {
                allMoviesAdapter = new AllMoviesAdapter(context, allMoviesList);
                allMoviesRecyclerView.setAdapter(allMoviesAdapter);
            }*/

        }
    }
}
