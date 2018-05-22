package com.example.vasyl.sunrisesunset.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class SunInfoResult implements Parcelable {

    private static final  String KEY_RESULTS = "results";

    @SerializedName(KEY_RESULTS)
    private SunInfo mSunInfo;

    protected SunInfoResult(Parcel in) {
        mSunInfo = in.readParcelable(SunInfo.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mSunInfo, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SunInfoResult> CREATOR = new Creator<SunInfoResult>() {
        @Override
        public SunInfoResult createFromParcel(Parcel in) {
            return new SunInfoResult(in);
        }

        @Override
        public SunInfoResult[] newArray(int size) {
            return new SunInfoResult[size];
        }
    };

    public SunInfo getSunriseSunset() {
        return mSunInfo;
    }

    @Override
    public String toString() {
        return "SunInfoResult{" +
                ", mSunInfo=" + mSunInfo +
                '}';
    }
}
