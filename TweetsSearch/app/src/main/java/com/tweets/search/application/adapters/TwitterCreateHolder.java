package com.tweets.search.application.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tweets.search.application.R;


public class TwitterCreateHolder implements TwitterRecyclerAdapter.CreateViewHolderAdapter {

    private Context context;

    public TwitterCreateHolder(Context context){
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHolder(ViewGroup parent) {
        return new SimpleSocialHolder(LayoutInflater.from(context).
                inflate(R.layout.adapter_twitter, parent, false));
    }

    public class SimpleSocialHolder extends RecyclerView.ViewHolder{

        TextView message;

        ImageView profilePicture;

        TextView publishDate;

        public SimpleSocialHolder(View itemView) {
            super(itemView);

            message = (TextView) itemView.findViewById(R.id.user_message);

            profilePicture = (ImageView) itemView.findViewById(R.id.user_picture);

            publishDate = (TextView) itemView.findViewById(R.id.publish_date);

        }
    }
}
