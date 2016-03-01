package com.tweets.search.application.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tweets.search.application.R;

public class LoaderCreateHolder implements TwitterRecyclerAdapter.CreateViewHolderAdapter {

    private Context context;

    public LoaderCreateHolder(Context context){
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHolder(ViewGroup parent) {
        return new LoaderHolder(LayoutInflater.from(context).
                inflate(R.layout.adapter_loader, parent, false));
    }

    public class LoaderHolder extends RecyclerView.ViewHolder{

        public LoaderHolder(View itemView) {
            super(itemView);
        }
    }
}
