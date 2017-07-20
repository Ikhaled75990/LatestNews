package com.example.android.latestnews;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Ikki on 18/07/2017.
 */

public class LatestNewsAdapter extends ArrayAdapter<LatestNews> {
    public static final String LOG_TAG = LatestNewsAdapter.class.getName();

    //Constructor of the Adapter.
    public LatestNewsAdapter(Activity context, ArrayList<LatestNews> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listView = convertView;
        if (listView == null) {
            listView = LayoutInflater.from(getContext()).inflate(R.layout.list, parent, false);
        }

        if (position < getCount()) {
            LatestNews currentItem = getItem(position);

            TextView dateTextView = (TextView) listView.findViewById(R.id.published_date);
            dateTextView.setText(formatDate(currentItem.getmDate()));

            TextView titleTextView = (TextView) listView.findViewById(R.id.title_text_view);
            titleTextView.setText(currentItem.getmTitle());

            TextView sectionTextView = (TextView) listView.findViewById(R.id.section_text_view);
            sectionTextView.setText(currentItem.getmSectionName());

        }

        return listView;
    }

    public String formatDate(String date) {
        String newFormatData = "";
        if (date.length() >= 10) {
            CharSequence splittedDate = date.subSequence(0, 10);
            try {
                Date formatDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(splittedDate.toString());
                newFormatData = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(formatDate);
            } catch (ParseException e) {
                Log.e(LOG_TAG, e.getMessage());
            }
        } else {
            newFormatData = date;
        }

        return newFormatData;
    }
}
