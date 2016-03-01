package com.tweets.search.application.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tweets.search.application.R;

import java.util.List;

public abstract class RecyclerViewFragment<T> extends Fragment implements LoaderManager.LoaderCallbacks<List<T>>{

    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView recyclerView;

    private ProgressBar progress;
    private TextView noResults;

    private List<T> twitterList;

    private boolean isLoadingMore = false;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_twitter, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

            initControls();

            setListeners();
    }

    private void initControls(){

        noResults = (TextView) getView().findViewById(R.id.no_results);

        progress = (ProgressBar) getView().findViewById(R.id.progress);

        swipeRefresh = (SwipeRefreshLayout) getView().findViewById(R.id.swipe_refresh);

        recyclerView = (RecyclerView) getView().findViewById(R.id.timeline_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
    }

    private void setListeners(){

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadData();
            }
        });

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();

                if(twitterList != null && lastVisibleItemPosition == twitterList.size() && !isLoadingMore){
                    isLoadingMore = true;
                    loadMore();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(!ImageLoader.getInstance().isInited())
                    return;

                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        ImageLoader.getInstance().resume();
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        ImageLoader.getInstance().pause();
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        ImageLoader.getInstance().pause();
                        break;
                }
            }
        });
    }

    public abstract void reloadData();
    public abstract void loadMore();

    public RecyclerView getRecyclerView(){
        return recyclerView;
    }

    public List<T> getTimeline(){
        return twitterList;
    }

    public void hideProgress(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            swipeRefresh.setAlpha(0f);
        }
        swipeRefresh.setVisibility(View.VISIBLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            swipeRefresh.animate()
                    .alpha(1f)
                    .setDuration(500)
                    .setListener(null);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            progress.animate()
                    .alpha(0f)
                    .setDuration(500)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            progress.setVisibility(View.GONE);
                        }
                    });
        } else {
            progress.setVisibility(View.GONE);
        }
    }

    public void showProgress(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            progress.animate()
                    .alpha(1f)
                    .setDuration(500)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            progress.setVisibility(View.VISIBLE);
                        }
                    });
        } else {
            progress.setVisibility(View.VISIBLE);
        }
    }

    public void hideNoResults(){
            noResults.setVisibility(View.GONE);
    }

    @Override
    public void onLoadFinished(Loader<List<T>> loader, List<T> data) {
        if(data != null && data.size() != 0) {
            hideNoResults();
            loadData(data);
        } else {
            hideProgress();
        }
    }

    public void loadData(List<T> data){

        if(isLoadingMore){
            twitterList.addAll(data);

            isLoadingMore = false;

            addData(data);

            return;
        }

        twitterList = data;

        setData(twitterList);

        if(swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);

            animateChangData();

            return;
        }

        hideProgress();
    }

    public void animateChangData(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            recyclerView.setAlpha(0f);
        }

        if(twitterList != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
                recyclerView.animate().alpha(1f).setDuration(500);
            }
        }
    }

    public abstract void setData(List<T> data);
    public abstract void addData(List<T> data);

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(ImageLoader.getInstance().isInited())
            ImageLoader.getInstance().clearMemoryCache();
    }
}
