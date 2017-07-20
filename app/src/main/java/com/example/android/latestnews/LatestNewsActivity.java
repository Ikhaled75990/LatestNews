package com.example.android.latestnews;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class LatestNewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<LatestNews>> {

    private static final String LOG_TAG = LatestNewsActivity.class.getName();

    private static final int LATEST_NEWS_LOADER_ID = 1;

    private static final String REQUEST_URL = "https://content.guardianapis.com/search?page-size=20&show-tags=contributor&show-fields=all&api-key=67ea10eb-bfc6-4d99-97b9-ce277a0a5e95";

    private ListView listView;
    private LatestNewsAdapter mAdapter;
    private LoaderManager loaderManager;
    private TextView emptyStateTextView;
    private EditText searchField = null;
    private Button searchButton = null;
    private ProgressBar mProgBar;
    private String query = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.latest_news_activity);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        listView = (ListView) findViewById(R.id.list);

        emptyStateTextView = (TextView) findViewById(R.id.empty_view);

        mProgBar = (ProgressBar) findViewById(R.id.loading_indicator);
        mProgBar.setVisibility(View.GONE);

        loaderManager = getLoaderManager();
        loaderManager.initLoader(1, null, this);

        mAdapter = new LatestNewsAdapter(this, new ArrayList<LatestNews>());
        listView.setAdapter(mAdapter);


        searchField = (EditText) findViewById(R.id.search_bar);

        searchButton = (Button) findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgBar.setVisibility(View.VISIBLE);
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    LoaderManager loaderManager = getLoaderManager();
                    loaderManager.initLoader(LATEST_NEWS_LOADER_ID, null, LatestNewsActivity.this);
                    mAdapter.clear();
                    String userQuery = searchField.getText().toString();
                    query = REQUEST_URL + userQuery;
                    getLoaderManager().restartLoader(LATEST_NEWS_LOADER_ID, null, LatestNewsActivity.this);
                } else {
                    mProgBar.setVisibility(View.GONE);
                    mAdapter.clear();
                    emptyStateTextView.setText(R.string.no_internet);
                }

            }
        });

        listView.setEmptyView(emptyStateTextView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                LatestNews news = mAdapter.getItem(position);

                Uri latestNewsUri = Uri.parse(news.getmUrl());

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, latestNewsUri);

                startActivity(websiteIntent);
            }
        });


    }

    @Override
    public Loader<List<LatestNews>> onCreateLoader(int i, Bundle bundle) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String maxResults = sharedPreferences.getString(getString(R.string.settings_max_results_key), getString(R.string.settings_max_results_default));
        String orderBy = sharedPreferences.getString(getString(R.string.settings_order_by_key), getString(R.string.settings_order_by_default));
        String section = sharedPreferences.getString(getString(R.string.settings_section_key), getString(R.string.settings_section_default));

        Uri baseUri = Uri.parse((REQUEST_URL));
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("q", query);
        uriBuilder.appendQueryParameter("page-size", maxResults);
        uriBuilder.appendQueryParameter("order-by", orderBy);
        uriBuilder.appendQueryParameter("section", section);

        return new LatestNewsLoader(this, REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<LatestNews>> loader, List<LatestNews> data) {
        mProgBar.setVisibility(View.GONE);
        emptyStateTextView.setText(R.string.no_internet);
        mAdapter.clear();
        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<LatestNews>> loader) {
        mAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
