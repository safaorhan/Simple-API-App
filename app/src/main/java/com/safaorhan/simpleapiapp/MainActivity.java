package com.safaorhan.simpleapiapp;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * This one and only activity of this application has a fragment container in it.
 * <p/>
 * With the help of the fragment replace functions it provides, fragments can replace themselves with others.
 */
public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MainActivity";
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Create global configuration of universal image loader.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);

        setContentView(R.layout.activity_main);

        // If we're being restored from a previous state,
        // then we don't need to do anything and should return.
        if (savedInstanceState != null) {
            return;
        }

        showBlankScreen(getString(R.string.loading), false);
        fetchVenues();

    }


    /**
     * Executes the query to the 4SQ venues/explore API.
     * After successful completion, pass data to {@link ListFragment}.
     * If there occurs an error, switch to {@link BlankFragment}.
     */
    public void fetchVenues() {
        RequestParams params = new RequestParams();

        params.put(Const.API_NEAR, Const.NEAR);
        params.put(Const.API_CLIENT_ID, Const.CLIENT_ID);
        params.put(Const.API_CLIENT_SECRET, Const.CLIENT_SECRET);
        params.put(Const.API_VERSION, Const.VERSION);
        params.put(Const.API_MODE, Const.MODE);
        params.put(Const.API_SECTION, Const.SECTION);

        AsyncHttpClient exploreClient = new AsyncHttpClient();

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

    /**
     * Replace the current fragment with {@link ListFragment}
     *
     * @param data Json String data to populate list view.
     */
    private void showListScreen(String data) {
        ListFragment listFragment = new ListFragment();

        Bundle args = new Bundle();
        args.putString(Const.ARG_DATA, data);

        listFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, listFragment).commit();

        getSupportFragmentManager().executePendingTransactions();
    }

    /**
     * Replace the current fragment with {@link BlankFragment}
     *
     * @param message        Message to be shown to user in the fragment.
     * @param hasRetryButton Boolean flag to indicate that the fragment to be created has retry button.
     */
    public void showBlankScreen(String message, boolean hasRetryButton) {
        BlankFragment blankFragment = new BlankFragment();

        Bundle args = new Bundle();
        args.putString(Const.ARG_MESSAGE, message);
        args.putBoolean(Const.ARG_HAS_RETRY_BUTTON, hasRetryButton);

        blankFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, blankFragment).commit();

        getSupportFragmentManager().executePendingTransactions();
    }

    /**
     * Replace the current fragment with {@link DetailsFragment} adding current fragment to back stack.
     *
     * @param data Json String data to inflate the fragment with.
     */
    public void showDetailScreen(String data) {
        DetailsFragment detailsFragment = new DetailsFragment();

        Bundle args = new Bundle();
        args.putString(Const.ARG_DATA, data);

        detailsFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, detailsFragment);
        transaction.addToBackStack(null);

        transaction.commit();

        getSupportFragmentManager().executePendingTransactions();
    }

    /**
     * Little helper function that gets the whole response of venues/explore api, and extracts the part needed.
     *
     * @param response JSONObject response which is returned by venues/explore API.
     * @return the JSONArray String data to populate views and fragments.
     * @throws JSONException
     */
    private String extractDataFromJSON(JSONObject response) throws JSONException {
        return response.getJSONObject("response").getJSONArray("groups").getJSONObject(0).getJSONArray("items").toString();
    }


}
