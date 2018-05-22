package com.example.vasyl.sunrisesunset;

import android.annotation.SuppressLint;
import android.support.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {

    private DateUtils() { }

    @Nullable
    public static String utcToLocalTime (String time){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            Date date = dateFormat.parse(time);
            dateFormat.setTimeZone(TimeZone.getDefault());
            return dateFormat.format(date);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return null;
    }
}

