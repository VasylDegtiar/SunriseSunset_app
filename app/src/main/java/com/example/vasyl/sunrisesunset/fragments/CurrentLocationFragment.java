package com.example.vasyl.sunrisesunset.fragments;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;

import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.vasyl.sunrisesunset.Constants;
import com.example.vasyl.sunrisesunset.DateUtils;
import com.example.vasyl.sunrisesunset.R;
import com.example.vasyl.sunrisesunset.model.PlaceInfo;
import com.example.vasyl.sunrisesunset.model.SunInfoResult;
import com.example.vasyl.sunrisesunset.services.RequestService;



public class CurrentLocationFragment extends Fragment {

    private static final int REQUEST_PERMISSIONS_LOCATION = 99;
    private static final String TAG = "22";
    private TextView mSunrise;
    private TextView mSunset;
    private TextView mSolarNoon;
    private TextView mDayLength;
    private LocalBroadcastManager mBroadcastManager;
    private CurrentLocationReceiver mReceiver = new CurrentLocationReceiver();
    private LocationManager mLocationManager;



    public static CurrentLocationFragment newInstance() {
        return new CurrentLocationFragment();

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view
                = inflater.inflate(R.layout.current_location_fragment, container, false);

        mSunrise = view.findViewById(R.id.sun_sunrise_your_place);
        mSunrise.setText(getString(R.string.sun_sunrise, ""));
        mSunset = view.findViewById(R.id.sun_sunset_your_place);
        mSunset.setText(getString(R.string.sun_sunset, ""));
        mSolarNoon = view.findViewById(R.id.sun_solar_noon_your_place);
        mSolarNoon.setText(getString(R.string.sun_solar_noon, ""));
        mDayLength = view.findViewById(R.id.sun_day_length_your_place);
        mDayLength.setText(getString(R.string.sun_day_length, ""));

        view.findViewById(R.id.show_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation();

            }
        });
        // Set for a first time
        getCurrentLocation();
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void getCurrentLocation() {

        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getActivity()
                    .getSystemService(Context.LOCATION_SERVICE);
        }

        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_LOCATION);
        }else{
            boolean isNetworkAvailable
                    = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if(isNetworkAvailable) {
                Criteria criteria = new Criteria();
                    criteria.setAccuracy(Criteria.ACCURACY_FINE);
                    mLocationManager.requestSingleUpdate(criteria, new android.location.LocationListener() {
                                @Override
                                public void onLocationChanged(Location location) {
                                    PlaceInfo info = new PlaceInfo(location);
                                    RequestService.getCurrentPlaceSunInfo(getContext(), info);
                                }

                                @Override
                                public void onStatusChanged(String provider, int status, Bundle extras) {

                                }

                                @Override
                                public void onProviderEnabled(String provider) {

                                }

                                @Override
                                public void onProviderDisabled(String provider) {

                                }
                            }, null);


                } else {
                    showFailedGetLocationToast();
            }
        }

    }


    private void showFailedGetLocationToast() {
        Toast.makeText(getActivity(),
                getString(R.string.failed_to_get_current_location),
                Toast.LENGTH_LONG)
                .show();
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

    class CurrentLocationReceiver extends BroadcastReceiver {

            private final String TAG = "CurrentLocationReceiver";

            /* package */final IntentFilter mIntentFilter = new IntentFilter();

            CurrentLocationReceiver() {
                mIntentFilter.addAction(RequestService.ACTION_GET_CURRENT_PLACE_SUN_INFO);

            }

            @Override
            public void onReceive(Context context, Intent intent) {
                Exception exception
                        = (Exception) intent.getSerializableExtra(Constants.EXTRA_EXCEPTION);

                if (exception != null) {
                    Toast.makeText(getActivity(), exception.getLocalizedMessage(), Toast.LENGTH_LONG)
                            .show();
                }

                if (intent.getAction() != null) {
                    switch (intent.getAction()) {
                        case RequestService.ACTION_GET_CURRENT_PLACE_SUN_INFO:
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


