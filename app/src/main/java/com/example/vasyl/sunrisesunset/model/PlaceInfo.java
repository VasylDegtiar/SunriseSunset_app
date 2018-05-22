package com.example.vasyl.sunrisesunset.model;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.location.places.Place;


import java.util.Map;
import java.util.TreeMap;

public class PlaceInfo implements Parcelable {

    private Double latitude;
    private Double longitude;

    public PlaceInfo(Place place) {
        this.latitude = place.getLatLng().latitude;
        this.longitude = place.getLatLng().longitude;
    }

    public PlaceInfo(Location location) {
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }


    protected PlaceInfo(Parcel in) {
        if (in.readByte() == 0) {
            latitude = null;
        } else {
            latitude = in.readDouble();
        }
        if (in.readByte() == 0) {
            longitude = null;
        } else {
            longitude = in.readDouble();
        }
    }

    public static final Creator<PlaceInfo> CREATOR = new Creator<PlaceInfo>() {
        @Override
        public PlaceInfo createFromParcel(Parcel in) {
            return new PlaceInfo(in);
        }

        @Override
        public PlaceInfo[] newArray(int size) {
            return new PlaceInfo[size];
        }
    };

    public Map<String, String> mapRepresentation() {
        return new TreeMap<String, String>(){{
            {
                put("lat", String.valueOf(latitude));
                put("lng", String.valueOf(longitude));
            }
        }};
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlaceInfo placeInfo = (PlaceInfo) o;

        if (latitude != null ? !latitude.equals(placeInfo.latitude) : placeInfo.latitude != null)
            return false;
        return longitude != null ? longitude.equals(placeInfo.longitude) : placeInfo.longitude == null;
    }

    @Override
    public int hashCode() {
        int result = latitude != null ? latitude.hashCode() : 0;
        result = 31 * result + (longitude != null ? longitude.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PlaceInfo{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (latitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(latitude);
        }
        if (longitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(longitude);
        }
    }
}
