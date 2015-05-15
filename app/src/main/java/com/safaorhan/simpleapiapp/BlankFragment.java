package com.safaorhan.simpleapiapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A fragment to show in container in case of errors, connection problems, etc.
 *
 * Gets message and hasRetryButton boolean as arguments to inflate itself as needed.
 */
public class BlankFragment extends Fragment {

    TextView textMessage;
    Button buttonRetry;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank, container, false);

        String message = getArguments().getString(Const.ARG_MESSAGE);
        boolean hasRetryButton = getArguments().getBoolean(Const.ARG_HAS_RETRY_BUTTON);

        textMessage = (TextView) view.findViewById(R.id.textMessage);
        buttonRetry = (Button) view.findViewById(R.id.buttonRetry);

        textMessage.setText(message);

        if (hasRetryButton) {
            buttonRetry.setVisibility(View.VISIBLE);

            buttonRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) getActivity()).fetchVenues();
                }
            });
        } else {
            buttonRetry.setVisibility(View.GONE);
        }


        return view;
    }

}

