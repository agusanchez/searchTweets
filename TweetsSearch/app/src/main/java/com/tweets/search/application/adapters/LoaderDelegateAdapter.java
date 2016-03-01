package com.tweets.search.application.adapters;

import android.support.v7.widget.RecyclerView;

public class LoaderDelegateAdapter implements TwitterRecyclerAdapter.DelegateAdapter {

    @Override
    public void onBindHolder(RecyclerView.ViewHolder holder, int position) {
    }

    @Override
    public int getItemViewType() {
        return 1;
    }
}
