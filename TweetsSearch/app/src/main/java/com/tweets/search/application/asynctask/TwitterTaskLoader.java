package com.tweets.search.application.asynctask;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import com.tweets.search.application.dao.TwitterHelperList;
import com.tweets.search.application.model.Tweet;

import java.util.List;

public class TwitterTaskLoader extends AsyncTaskLoader<List<Tweet>> {

    private TwitterHelperList twitterHelper;

    private List<Tweet> data;
    private Context context;
    private String query;

    public TwitterTaskLoader(Context context, TwitterHelperList twitterHelper, String query) {
        super(context);
        this.context = context;
        this.twitterHelper = twitterHelper;
        this.query = query;
    }

    @Override
    public List<Tweet> loadInBackground() {

        if(query == null)
            return null;

        return twitterHelper.searchTwitters(query);
    }

    @Override
    public void deliverResult(List<Tweet> data) {
        // TODO Auto-generated method stub
        this.data = data;

        if(isStarted())
            super.deliverResult(data);
    }

    @Override
    protected void onStartLoading() {
        // TODO Auto-generated method stub
        if(data != null)
            deliverResult(data);
        else{
            forceLoad();
        }
    }
}
