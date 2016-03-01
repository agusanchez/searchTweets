package com.tweets.search.application.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.tweets.search.application.R;
import com.tweets.search.application.adapters.TwitterAdapter;
import com.tweets.search.application.asynctask.TwitterTaskLoader;
import com.tweets.search.application.dao.TwitterHelperList;
import com.tweets.search.application.model.Tweet;

import java.util.List;

public class TwitterFragment extends RecyclerViewFragment<Tweet> implements SearchView.OnQueryTextListener {

    private TwitterHelperList twitterHelper;
    private String userQuery;
    private static final String SEARCH_QUERY = "query";

    public static TwitterFragment newInstance() {
        Bundle args = new Bundle();

        TwitterFragment fragment = new TwitterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        twitterHelper = TwitterHelperList.getInstance(getContext());

        getLoaderManager().initLoader(1, getArguments(), this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        getActivity().getMenuInflater().inflate(R.menu.menu_home, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public void reloadData() {
        twitterHelper.reset();
        getLoaderManager().restartLoader(1, getArguments(), this);
    }

    @Override
    public void loadMore() {
        if(hasMore())
            getLoaderManager().restartLoader(1, getArguments(), this);
    }

    @Override
    public void setData(List data) {

        TwitterAdapter adapter = new TwitterAdapter(getActivity(), data, hasMore());

        getRecyclerView().setAdapter(adapter);
    }

    @Override
    public void addData(List<Tweet> data) {

        ((TwitterAdapter)getRecyclerView().getAdapter()).addItems(data, hasMore());
    }

    @Override
    public Loader<List<Tweet>> onCreateLoader(int id, Bundle args) {
         return new TwitterTaskLoader(getActivity(), twitterHelper, args.getString(SEARCH_QUERY));
    }

    @Override
    public void onLoaderReset(Loader<List<Tweet>> loader) {

    }

    private boolean hasMore(){
        return getTimeline().size() % twitterHelper.getPageCount() == 0;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        userQuery = !TextUtils.isEmpty(query) ? query : null;

        if(userQuery != null) {
            getArguments().putString(SEARCH_QUERY, userQuery);
            reloadData();
            showProgress();
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return true;
    }
}
