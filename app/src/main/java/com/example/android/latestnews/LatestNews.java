package com.example.android.latestnews;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LatestNews {

    private String mTitle;
    private String mAuthor;
    private String mBody;
    private String mDate;
    private String mSectionName;
    private String mUrl;


    /**
     * @param title       Title of the news
     * @param author      name of the writer/author
     * @param body        short description of the news
     * @param date        published date
     * @param sectionName Name of the section
     * @param url         url of the webpage
     */
    public LatestNews(String title, String author, String body, String date, String sectionName, String url) {
        mTitle = title;
        mAuthor = author;
        mBody = body;
        mDate = date;
        mSectionName = sectionName;
        mUrl = url;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public String getmBody() {
        return mBody;
    }

    public String getmDate() {
        return mDate;
    }

    public String getmSectionName() {
        return mSectionName;
    }

    public String getmUrl() {
        return mUrl;
    }


}
