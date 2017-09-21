package com.salesforce.freemind.salesforce101.controller;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.salesforce.freemind.salesforce101.R;
import com.salesforce.freemind.salesforce101.data.TrailerDetail;

import java.util.ArrayList;

/**
 * Created by anant on 2017-09-21.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.MyViewHolder>{

    Context context ;
    ArrayList<TrailerDetail> trailerData;
    LayoutInflater trailerInflator;
    Drawable drawable;


    public TrailersAdapter(Context context, ArrayList<TrailerDetail> trailerData) {
        this.context = context;
        this.trailerData = trailerData;
        this.trailerInflator = LayoutInflater.from(context);
        drawable = context.getResources().getDrawable(R.drawable.icons8_play);

    }

    @Override
    public TrailersAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = trailerInflator.inflate(R.layout.trailers_list_item , parent,false);

        return new MyViewHolder(view);

    }


    @Override
    public void onBindViewHolder(TrailersAdapter.MyViewHolder holder, int position) {

        holder.trailerName.setText(trailerData.get(position).getName());
        holder.playImage.setImageDrawable(drawable);

    }


    @Override
    public int getItemCount() {
        return trailerData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView trailerName ;

        ImageView playImage;
        public MyViewHolder(View itemView) {
            super(itemView);

            trailerName = (TextView)itemView.findViewById(R.id.trailer_name_tv);
            playImage = (ImageView)itemView.findViewById(R.id.play_icon_iv);

        }

        @Override
        public void onClick(View view) {




        }
    }
}
