package com.example.vasyl.sunrisesunset.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vasyl.sunrisesunset.Constants;
import com.example.vasyl.sunrisesunset.DateUtils;
import com.example.vasyl.sunrisesunset.R;
import com.example.vasyl.sunrisesunset.model.PlaceInfo;
import com.example.vasyl.sunrisesunset.model.SunInfoResult;
import com.example.vasyl.sunrisesunset.services.RequestService;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;


public class SearchFragment extends android.support.v4.app.Fragment {

    private TextView mSunrise;
    private TextView mSunset;
    private TextView mSolarNoon;
    private TextView mDayLength;

    private LocalBroadcastManager mBroadcastManager;
    private SearchReceiver mReceiver = new SearchReceiver();

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        mBroadcastManager.registerReceiver(mReceiver, mReceiver.mIntentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBroadcastManager.unregisterReceiver(mReceiver);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ftagment_sunrise_sunset,container,false);

        mSunrise = view.findViewById(R.id.sun_sunrise);
        mSunrise.setText(getString(R.string.sun_sunrise,""));
        mSunset = view.findViewById(R.id.sun_sunset);
        mSunset.setText(getString(R.string.sun_sunset,""));
        mSolarNoon = view.findViewById(R.id.sun_solar_noon);
        mSolarNoon.setText(getString(R.string.sun_solar_noon,""));
        mDayLength = view.findViewById(R.id.sun_day_length);
        mDayLength.setText(getString(R.string.sun_day_length,""));

        setupCitySearchFragment();

        return view;
    }

    private void setupCitySearchFragment() {
        SupportPlaceAutocompleteFragment autocompleteFragment = (SupportPlaceAutocompleteFragment)
                getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragments);

        EditText editText = autocompleteFragment.getView().
                findViewById(R.id.place_autocomplete_search_input);

        editText.setHint(R.string.search);
        editText.setHintTextColor(Color.BLACK);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {

            @Override
            public void onPlaceSelected(Place place) {

                PlaceInfo info = new PlaceInfo(place);
                RequestService.getPlaceSunInfo(getContext(), info);
            }

            @Override
            public void onError(Status status) {
                Toast.makeText(getActivity(), status.getStatusMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void populateInfo(SunInfoResult result) {

        mSunrise.setText(getString(R.string.sun_sunrise,
                DateUtils.utcToLocalTime(result.getSunriseSunset().getSunrise())));
        mSunset.setText(getString(R.string.sun_sunset,
                DateUtils.utcToLocalTime(result.getSunriseSunset().getSunset())));
        mSolarNoon.setText(getString(R.string.sun_solar_noon,
                DateUtils.utcToLocalTime(result.getSunriseSunset().getSolarNoon())));
        mDayLength.setText(getString(R.string.sun_day_length,
                result.getSunriseSunset().getDayLength()));
    }

    class SearchReceiver extends BroadcastReceiver {

        private final String TAG = "SearchReceiver";

        final IntentFilter mIntentFilter = new IntentFilter();

        SearchReceiver() {
            mIntentFilter.addAction(RequestService.ACTION_GET_PLACE_SUN_INFO);

        }

        @Override
        public void onReceive(Context context, Intent intent) {
            Exception exception
                    = (Exception) intent.getSerializableExtra(Constants.EXTRA_EXCEPTION);

            if (exception != null) {
                Toast.makeText(getActivity(),exception.getLocalizedMessage(), Toast.LENGTH_LONG)
                        .show();
            }

            if (intent.getAction() != null) {
                switch (intent.getAction()) {
                    case RequestService.ACTION_GET_PLACE_SUN_INFO:
                        Log.d(TAG, intent.getAction());
                        SunInfoResult result
                                = intent.getParcelableExtra(RequestService.EXTRA_SUN_INFO_RESULT);
                        populateInfo(result);
                        break;
                }
            }
        }
    }
}
