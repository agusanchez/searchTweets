package com.tweets.search.application.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.util.ArrayList;
import java.util.List;

public class TwitterRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface CreateViewHolderAdapter{
        public RecyclerView.ViewHolder onCreateHolder(ViewGroup parent);
    }

    public interface DelegateAdapter{
        public void onBindHolder(RecyclerView.ViewHolder holder, int position);
        public int getItemViewType();
    }

    private Context context;

    private List<CreateViewHolderAdapter> itemsCreator;
    private List<DelegateAdapter> itemsBinder;

    public TwitterRecyclerAdapter(Context context){
        this.context = context;
        this.itemsCreator = new ArrayList<>();
        this.itemsBinder = new ArrayList<>();

        initImageLoader();
    }

    private void initImageLoader(){

        if(ImageLoader.getInstance().isInited())
            return;

        ImageLoaderConfiguration config = new
                ImageLoaderConfiguration.Builder(context)
                .threadPoolSize(3)
                .memoryCacheSizePercentage(5)
                .threadPriority(Thread.MIN_PRIORITY + 3)
                .memoryCache(new WeakMemoryCache())
                .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 5 * 1000))
                .writeDebugLogs()
                .build();

        ImageLoader.getInstance().init(config);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return itemsCreator.get(viewType).onCreateHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        this.itemsBinder.get(position).onBindHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return itemsBinder.size();
    }

    @Override
    public int getItemViewType(int position) {
        return itemsBinder.get(position).getItemViewType();
    }

    public List<DelegateAdapter> getItemsBinder() {
        return itemsBinder;
    }

    public void setItemsBinder(List<DelegateAdapter> itemsBinder) {
        this.itemsBinder = itemsBinder;
    }

    public List<CreateViewHolderAdapter> getItemsCreator() {
        return itemsCreator;
    }

    public void setItemsCreator(List<CreateViewHolderAdapter> itemsCreator) {
        this.itemsCreator = itemsCreator;
    }

    public Context getContext(){
        return context;
    }
}
