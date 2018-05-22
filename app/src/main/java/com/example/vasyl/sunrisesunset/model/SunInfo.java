package com.example.vasyl.sunrisesunset.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;


public class SunInfo implements Parcelable {

    private static final  String KEY_SUNRISE = "sunrise";
    private static final  String KEY_SUNSET = "sunset";
    private static final  String KEY_SOLAR_NOON = "solar_noon";
    private static final  String KEY_DAY_LENGTH = "day_length";

    @SerializedName(KEY_SUNRISE)

    private String sunrise;

    @SerializedName(KEY_SUNSET)

    private String sunset;

    @SerializedName(KEY_SOLAR_NOON)

    private String solarNoon;

    @SerializedName(KEY_DAY_LENGTH)

    private String dayLength;


    protected SunInfo(Parcel in) {
        sunrise = in.readString();
        sunset = in.readString();
        solarNoon = in.readString();
        dayLength = in.readString();
    }

    public static final Creator<SunInfo> CREATOR = new Creator<SunInfo>() {
        @Override
        public SunInfo createFromParcel(Parcel in) {
            return new SunInfo(in);
        }

        @Override
        public SunInfo[] newArray(int size) {
            return new SunInfo[size];
        }
    };

    public String getSunrise() {

        return sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public String getSolarNoon() {
        return solarNoon;
    }

    public String getDayLength() {
        return dayLength;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SunInfo that = (SunInfo) o;
        return Objects.equals(sunrise, that.sunrise) &&
                Objects.equals(sunset, that.sunset) &&
                Objects.equals(solarNoon, that.solarNoon) &&
                Objects.equals(dayLength, that.dayLength);
    }

    @Override
    public int hashCode() {

        return Objects.hash(sunrise, sunset, solarNoon, dayLength);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sunrise);
        dest.writeString(sunset);
        dest.writeString(solarNoon);
        dest.writeString(dayLength);
    }

    @Override
    public String toString() {
        return "SunInfo{" +
                "sunrise='" + sunrise + '\'' +
                ", sunset='" + sunset + '\'' +
                ", solarNoon='" + solarNoon + '\'' +
                ", dayLength='" + dayLength + '\'' +
                '}';
    }
}
