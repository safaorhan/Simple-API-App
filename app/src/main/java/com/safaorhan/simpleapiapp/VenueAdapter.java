package com.safaorhan.simpleapiapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Adapter of the list view.
 * <p/>
 * Stores data as JSONArray, parses the data and inflates list items when {@link #getView(int, View, ViewGroup)} is called.
 */
public class VenueAdapter extends BaseAdapter {

    JSONArray venues = null;

    Context mContext;

    public VenueAdapter(Context context, String data) {
        mContext = context;
        try {
            this.venues = new JSONArray(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        return venues.length();
    }


    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Note: No need to use view holder pattern here, since the data is minimal.

        TextView textName, textRating;

        JSONObject item;
        String name = "";
        double rating = -1;

        try {
            item = venues.getJSONObject(position).getJSONObject("venue");
            name = item.getString("name");
            rating = item.getDouble("rating");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_item, parent, false);
        }

        textName = (TextView) convertView.findViewById(R.id.textName);
        textRating = (TextView) convertView.findViewById(R.id.textRating);

        textName.setText(name);
        textRating.setText(rating + "");


        return convertView;
    }
}
