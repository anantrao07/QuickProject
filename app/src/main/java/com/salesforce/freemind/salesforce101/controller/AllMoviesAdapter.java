package com.salesforce.freemind.salesforce101.controller;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.salesforce.freemind.salesforce101.BuildConfig;
import com.salesforce.freemind.salesforce101.R;
import com.salesforce.freemind.salesforce101.data.AllMoviesData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by anant on 2017-09-20.
 */

public class AllMoviesAdapter extends RecyclerView.Adapter<AllMoviesAdapter.MyViewHolder>   {

    private final static int FADE_DURATION = 500;

    Context context ;
    ArrayList<AllMoviesData> moviesData;
    LayoutInflater moviesInflator;

    public AllMoviesAdapter(Context context, ArrayList<AllMoviesData> moviesData ) {
        this.context = context;
        this.moviesData = moviesData;
        moviesInflator = LayoutInflater.from(context);


    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    /**
     * Called when RecyclerView needs a new {@link //ViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #//onBindViewHolder(ViewHolder, int, List)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #//onBindViewHolder(ViewHolder, int)
     */
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = moviesInflator.inflate(R.layout.movies_list_item , parent,false);

        return new MyViewHolder(view);

    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link //ViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link //ViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p>
     * Override {@link #//onBindViewHolder(ViewHolder, int, List)} instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.movieTitleTv.setText(moviesData.get(position).getMovieTitle());
        Picasso.with(context).load(BuildConfig.IMAGE_BASEURL+moviesData.get(position).getMoviesPoster()).into(holder.moviePosterIv);
        /*Picasso.with(context)
                .load()
                .placeholder(R.drawable.user_placeholder)
                .error(R.drawable.user_placeholder_error)
                .into(holder.moviePosterIv);*/
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {


        return moviesData.size();
    }

    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }


    public   class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView moviePosterIv ;
        TextView movieTitleTv;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            moviePosterIv = (ImageView)itemView.findViewById(R.id.movie_poster);
            movieTitleTv = (TextView)itemView.findViewById(R.id.movie_title);

        }

        @Override
        public void onClick(View view) {


            Intent getIdIntent = new Intent("movie_id_info");

            getIdIntent.putExtra("position" , String.valueOf(getLayoutPosition()));

            LocalBroadcastManager.getInstance(context).sendBroadcast(getIdIntent);


        }


    }
}
