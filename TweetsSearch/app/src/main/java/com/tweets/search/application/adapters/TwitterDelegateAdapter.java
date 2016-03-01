package com.tweets.search.application.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.tweets.search.application.R;
import com.tweets.search.application.utils.Utils;
import com.tweets.search.application.model.Tweet;

import java.util.ArrayList;
import java.util.List;

public class TwitterDelegateAdapter implements TwitterRecyclerAdapter.DelegateAdapter {

    private Context context;

    private Tweet data;

    private DisplayImageOptions pictureProfileoptions;

    private List<String> patterns;

    public TwitterDelegateAdapter(Context context, Tweet data){
        this.context = context;
        this.data = data;

        pictureProfileoptions = new DisplayImageOptions.Builder().
                cacheOnDisk(true).
                displayer(new FadeInBitmapDisplayer(500)).
                imageScaleType(ImageScaleType.EXACTLY).
                resetViewBeforeLoading(true).
                bitmapConfig(Bitmap.Config.RGB_565).
                build();

        patterns = new ArrayList<String>();
        patterns.add("#\\w+");
        patterns.add("@\\w+");
        patterns.add("\\@[a-zA-Z0-9\\-.]+(?:(?:/[a-zA-Z0-9\\-._?,'+\\&%$=~*!():@\\\\]*)+)?");
    }

    @Override
    public void onBindHolder(RecyclerView.ViewHolder holder, int position) {

        ((TwitterCreateHolder.SimpleSocialHolder)holder).message.setText(data.getMessage());
        ((TwitterCreateHolder.SimpleSocialHolder)holder).message.setLinkTextColor(
                context.getResources().getColor(R.color.text_color_link));

        Utils.changeTextinView(((TwitterCreateHolder.SimpleSocialHolder) holder).message,
                Utils.findListOfCoincidences(data.getMessage(), patterns),
                context.getResources().getColor(R.color.text_color_link));

        if(data.getPublishDate() != null)
            ((TwitterCreateHolder.SimpleSocialHolder)holder).publishDate.setText(
                    context.getResources().getString(R.string.time_ago) + " " + Utils.getDateDifference(data.getPublishDate()));

        ImageLoader.getInstance().displayImage(data.getUser().getProfilePicture(),
                ((TwitterCreateHolder.SimpleSocialHolder) holder).profilePicture, pictureProfileoptions);
    }

    @Override
    public int getItemViewType() {
        return 0;
    }
}
