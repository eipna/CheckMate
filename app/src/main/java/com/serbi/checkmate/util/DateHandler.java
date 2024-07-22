package com.serbi.checkmate.util;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHandler {

    // Returns current time stamp
    public static long getCurrentTimeStamp() {
        return System.currentTimeMillis();
    }

    // Returns a detailed format of the current date (Ex. Monday, 22 July 2024)
    public static String getDetailedDate() {
        Date currentDate = new Date(getCurrentTimeStamp());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("EEEE, dd MMMM yyyy");
        return simpleDateFormat.format(currentDate);
    }

    // Returns a detailed format from time stamp
    public static String getDetailedDate(long timeStamp) {
        Date currentDate = new Date(timeStamp);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("EEEE, dd MMMM yyyy");
        return simpleDateFormat.format(currentDate);
    }
}