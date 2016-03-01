package com.tweets.search.application.dao;


import android.content.Context;

import com.tweets.search.application.model.Tweet;
import com.tweets.search.application.model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import twitter4j.Status;

public class TwitterDAO {

    static TwitterDAO instance;
    private Context context;

    public static TwitterDAO getInstance(Context context){
        if(instance == null){
            instance = new TwitterDAO(context);
        }

        return instance;
    }

    private TwitterDAO(Context context){
        this.context = context;
    }

    public List<Tweet> getTweetsList(List<Status> twitterStatusList) throws DAOException{

        List<Tweet> tweets =  new ArrayList<>();

        try {

            for (int index = 0; index < twitterStatusList.size(); index++) {

                Tweet tweet = new Tweet();

                tweet.setMessage(twitterStatusList.get(index).getText());

                SimpleDateFormat sf = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", Locale.ENGLISH);
                sf.setLenient(true);

                try {
                    tweet.setPublishDate(sf.parse(String.valueOf(twitterStatusList.get(index).getCreatedAt())));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                User user = new User();
                user.setProfilePicture(twitterStatusList.get(index).getUser().getBiggerProfileImageURL());

                tweet.setUser(user);

                tweets.add(tweet);
            }
        } catch (Exception e){
            throw new DAOException("Error obteniendo informacion de los tweets." + e.getMessage());
        }

        return tweets;
    }
}
