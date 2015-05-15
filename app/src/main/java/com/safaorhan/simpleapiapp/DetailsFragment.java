package com.safaorhan.simpleapiapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A fragment to show details and a photo of a venue.
 * <p/>
 * Gets venue data as arguments.
 */
public class DetailsFragment extends Fragment {
    private static final String TAG = "DetailsFragment";

    JSONObject mObject = null;

    TextView textName, textAddress, textRating;
    ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_details, container, false);

        textName = (TextView) view.findViewById(R.id.textName);
        textAddress = (TextView) view.findViewById(R.id.textAddress);
        textRating = (TextView) view.findViewById(R.id.textRating);

        imageView = (ImageView) view.findViewById(R.id.imageView);

        String name = null, address = null, id = null;
        double rating = -1;


        try {
            mObject = new JSONObject(getArguments().getString(Const.ARG_DATA));
            name = mObject.getJSONObject("venue").getString("name");
            address = mObject.getJSONObject("venue").getJSONObject("location").getString("address");
            rating = mObject.getJSONObject("venue").getDouble("rating");
            id = mObject.getJSONObject("venue").getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        textName.setText(name);
        textAddress.setText(address);
        textRating.setText(rating + "");

        fetchPhoto(id);

        return view;
    }

    /**
     * Fetches url of and loads a photo of the venue with the given id.
     * <p/>
     * Uses photos API.
     *
     * @param id ID of venue whose photo to be fetched.
     */
    private void fetchPhoto(String id) {
        String url = String.format(Const.URL_PHOTO, id);

        RequestParams params = new RequestParams();

        params.put(Const.API_CLIENT_ID, Const.CLIENT_ID);
        params.put(Const.API_CLIENT_SECRET, Const.CLIENT_SECRET);
        params.put(Const.API_VERSION, Const.VERSION);
        params.put(Const.API_MODE, Const.MODE);

        AsyncHttpClient photoClient = new AsyncHttpClient();


        photoClient.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                String url = null;

                try {
                    url = extractFirstPhotoUrlFromResponse(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ImageLoader.getInstance().displayImage(url, imageView);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                if (statusCode == 0) {

                    Log.e(TAG, "onFailure, Status Code: 0");
                } else {

                    Log.e(TAG, "onFailure, Status Code: " + statusCode);
                }
            }

        });

    }


    /**
     * Extracts and builds the photo url from the given photos API response.
     *
     * @param response photos API response
     * @return Url string of photo.
     * @throws JSONException
     */
    private String extractFirstPhotoUrlFromResponse(JSONObject response) throws JSONException {
        JSONObject photo = response.getJSONObject("response").getJSONObject("photos").getJSONArray("items").getJSONObject(0);
        return photo.getString("prefix") + "width300" + photo.getString("suffix");
    }

}

