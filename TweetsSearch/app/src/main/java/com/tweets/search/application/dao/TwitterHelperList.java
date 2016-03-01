package com.tweets.search.application.dao;


import android.content.Context;

import com.tweets.search.application.model.Tweet;
import com.tweets.search.application.utils.TwitterKeys;

import java.util.List;


import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterHelperList {

	private Context context;
	private static TwitterHelperList instance;
	private TwitterFactory tf;
	private Integer sinceId = 0;

	private Query query;
	private QueryResult result;

	static final int DEFAULT_PAGE_INDEX = 0;
	static final int DEFAULT_PAGE_COUNT = 20;

	private int pageIndex = DEFAULT_PAGE_INDEX;
	private static final String HASHTAG = "#";


	public static TwitterHelperList getInstance(Context context){
		if(instance == null)
			instance = new TwitterHelperList(context);
		return instance;
	}
	private TwitterHelperList(Context context) {
		this.context = context;
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		.setOAuthConsumerKey(TwitterKeys.CONSUMER_KEY_LIST)
		.setOAuthConsumerSecret(TwitterKeys.CONSUMER_SECRET_LIST)
		.setOAuthAccessToken(TwitterKeys.ACCESS_TOKEN)
		.setOAuthAccessTokenSecret(TwitterKeys.ACCESS_TOKEN_SECRET);
		tf = new TwitterFactory(cb.build());
	}

	public List<Tweet> searchTwitters(String searchQuery) {

		List<Status> tweets = null;
		Twitter twitter = tf.getInstance();

		try {

			if(pageIndex == 0){
				query = new Query(HASHTAG + searchQuery);
				query.setCount(DEFAULT_PAGE_COUNT);
				query.setSinceId(sinceId);
				result = twitter.search(query);
				tweets = result.getTweets();
				pageIndex++;
			}else{
				if((query = result.nextQuery()) != null){
					result = twitter.search(query);
					tweets = result.getTweets();
				}else{
					tweets = null;
				}
			}
		} catch (TwitterException te) {
			te.getMessage();
			return null;
		}
		try {
			return TwitterDAO.getInstance(context).getTweetsList(tweets);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}


	public void reset(){
		pageIndex = DEFAULT_PAGE_INDEX;
	}

	public int getPageCount(){
		return DEFAULT_PAGE_COUNT;
	}
}
