package com.tweets.search.application.application;

import android.app.Application;
import android.graphics.Bitmap;


import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;



public class TweetsSearchApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .resetViewBeforeLoading(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new FadeInBitmapDisplayer(700))
                .build();


        ImageLoaderConfiguration config = new
                ImageLoaderConfiguration.Builder(this)
                .threadPoolSize(3)
                .memoryCacheSizePercentage(8)
                .threadPriority(Thread.MIN_PRIORITY + 3)
                .memoryCache(new WeakMemoryCache())
                .imageDownloader(new BaseImageDownloader(this, 10 * 1000, 20 * 1000))
                .defaultDisplayImageOptions(options)
                .writeDebugLogs()
                .build();

        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config);
    }
}
