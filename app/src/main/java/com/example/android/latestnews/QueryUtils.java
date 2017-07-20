package com.example.android.latestnews;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.author;
import static android.R.id.input;

/**
 * Created by Ikki on 19/07/2017.
 */

public class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getName();

    private QueryUtils()

    {

    }

    public static List<LatestNews> fetchData(String requestUrl) {

        //Create a URL object.
        URL url = createUrl(requestUrl);
        //Perform HTTP request tp URL and receive JSON response.
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
        List<LatestNews> news = extractData(jsonResponse);
        return news;
    }

    /**
     * Returns new URL object from the String URL
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        //If the URL is null, returns early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(1000 /*milliseconds*/);
            urlConnection.setConnectTimeout(15000 /*millisecond*/);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }


    /**
     * Convert the InputStream into a string which will contain the JSON response from
     * the server
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a @link LatestNews objects that has been built up from the JSON response parsing.
     */
    public static List<LatestNews> extractData(String jsonResponse) {
        ArrayList<LatestNews> latestNews = new ArrayList<>();
        //Try to parse the data
        try {
            //This creates the root JSONObject by calling jsonResponse.
            JSONObject baseJsonResponse = new JSONObject(jsonResponse);
            JSONObject response = baseJsonResponse.getJSONObject("response");
            //This gets the objects contained in the latest news array.
            if (response.has("results")) {
                JSONArray newsArray = response.getJSONArray("results");
                for (int i = 0; i < newsArray.length(); i++) {
                    JSONObject singleNews = newsArray.getJSONObject(i);
                    String title = "";
                    if (singleNews.has("webTitle")) {
                        title = singleNews.getString("webTitle");
                    }
                    String sectionName = "";
                    if (singleNews.has("sectionName")) {
                        singleNews.get("sectionName");
                    }
                    String publishedDate = "";
                    if (singleNews.has("webPublicationDate")) {
                        publishedDate = singleNews.getString("webPublicationDate");
                    }

                    String webUrl = "";
                    if (singleNews.has("webUrl")) {
                        webUrl = singleNews.getString("webUrl");
                    }


                    LatestNews news = new LatestNews(title, sectionName, publishedDate, webUrl);
                    latestNews.add(news);
                }
            } else {
                Log.v(LOG_TAG, "No results found");
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
        return latestNews;
    }
}

