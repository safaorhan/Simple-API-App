package com.safaorhan.simpleapiapp;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MainActivity";
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // If we're being restored from a previous state,
        // then we don't need to do anything and should return or else
        // we could end up with overlapping fragments.
        if (savedInstanceState != null) {
            return;
        }

        showBlankScreen(getString(R.string.loading), false);
        fetchVenues();

    }




    public void fetchVenues()
    {
        RequestParams params = new RequestParams();

        params.put(Const.API_NEAR, Const.NEAR);
        params.put(Const.API_CLIENT_ID, Const.CLIENT_ID);
        params.put(Const.API_CLIENT_SECRET, Const.CLIENT_SECRET);
        params.put(Const.API_VERSION, Const.VERSION);
        params.put(Const.API_MODE, Const.MODE);
        params.put(Const.API_SECTION, Const.SECTION);

        AsyncHttpClient exploreClient = new AsyncHttpClient();
        Log.e(TAG, Const.URL_EXPLORE);

        exploreClient.get(Const.URL_EXPLORE, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                String data = null;

                try {
                    data = extractDataFromJSON(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                showListScreen(data);
                Log.e(TAG, "onSuccess");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                if (statusCode == 0) {
                    showBlankScreen(getString(R.string.please_check_connection), true);
                    Log.e(TAG, "onFailure, Status Code: 0");
                } else {
                    showBlankScreen(getString(R.string.an_error_occured), true);
                    Log.e(TAG, "onFailure, Status Code: " + statusCode);
                }
            }

        });

    }

    private void showListScreen(String data)
    {
        ListFragment listFragment = new ListFragment();

        Bundle args = new Bundle();
        args.putString(Const.ARG_DATA, data.toString());

        listFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, listFragment).commit();
    }

    public void showBlankScreen(String message, boolean hasRetryButton)
    {
        BlankFragment blankFragment = new BlankFragment();

        Bundle args = new Bundle();
        args.putString(Const.ARG_MESSAGE, message);
        args.putBoolean(Const.ARG_HAS_RETRY_BUTTON, hasRetryButton);

        blankFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, blankFragment).commit();
    }

    public void showDetailScreen(String data)
    {
        DetailsFragment detailsFragment = new DetailsFragment();

        Bundle args = new Bundle();
        args.putString(Const.ARG_DATA, data.toString());

        detailsFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, detailsFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    private String extractDataFromJSON(JSONObject response) throws JSONException {
        return response.getJSONObject("response").getJSONArray("groups").getJSONObject(0).getJSONArray("items").toString();
    }


}
