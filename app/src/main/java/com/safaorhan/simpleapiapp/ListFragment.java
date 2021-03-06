package com.safaorhan.simpleapiapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;


/**
 * A fragment to show a list of venues.
 * <p/>
 * Gets venue data as arguments.
 */
public class ListFragment extends Fragment {

    private static final String TAG = "ListFragment";

    String mData = null;

    ListView mListView;

    VenueAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list, container, false);

        mListView = (ListView) view.findViewById(R.id.listView);

        if (savedInstanceState == null)
            mData = getArguments().getString(Const.ARG_DATA);
        else
            mData = savedInstanceState.getString(Const.ARG_DATA);

        mAdapter = new VenueAdapter(getActivity(), mData);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    JSONArray dataArr = new JSONArray(mData);
                    String venueData = dataArr.getJSONObject(position).toString();
                    ((MainActivity) getActivity()).showDetailScreen(venueData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        mListView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Const.ARG_DATA, mData);
    }
}
