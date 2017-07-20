package com.example.android.latestnews;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;


/**
 * Created by Ikki on 19/07/2017.
 */

public class LatestNewsLoader extends AsyncTaskLoader<List<LatestNews>>{
    /* Query URL
     */
    private String mUrl;

    public LatestNewsLoader(Context context, String url){
    super(context);
    mUrl = url;
    }

    @Override
    protected void onStartLoading(){
        forceLoad();
    }

    @Override
    public List<LatestNews> loadInBackground(){
        if (mUrl == null){
            return null;
        }
        List<LatestNews> news = QueryUtils.fetchData(mUrl);
        return news;
    }
}

