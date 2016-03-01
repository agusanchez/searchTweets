package com.tweets.search.application.adapters;

import android.content.Context;

import com.tweets.search.application.model.Tweet;

import java.util.List;

public class TwitterAdapter extends TwitterRecyclerAdapter {


    public TwitterAdapter(Context context, List<Tweet> timeline, boolean hasMore) {
        super(context);

        getItemsCreator().add(new TwitterCreateHolder(context));
        getItemsCreator().add(new LoaderCreateHolder(context));

        for (Tweet tweet : timeline){
            getItemsBinder().add(new TwitterDelegateAdapter(context, tweet));
        }

        if(hasMore)
            getItemsBinder().add(new LoaderDelegateAdapter());
    }

    public void addItems(List<Tweet> data, boolean hasMore) {

        getItemsBinder().remove(getItemsBinder().size() - 1);

        notifyItemRemoved(getItemsBinder().size());

        int startPosition = getItemsBinder().size();

        for (Tweet tweet : data){
            getItemsBinder().add(new TwitterDelegateAdapter(getContext(), tweet));
        }

        if(hasMore)
            getItemsBinder().add(new LoaderDelegateAdapter());

        notifyItemRangeInserted(startPosition, (getItemsBinder().size() - 1) - startPosition);
    }
}
