package com.example.go4lunch.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DatesHours {

    public static void getDateToday() {
        Calendar cal = Calendar.getInstance();
        Date currentDate = cal.getTime();
        DateFormat date = new SimpleDateFormat("dd-mm-yyy z");
        String dayDate = date.format(currentDate);
        Log.d("TestDate", dayDate);

    }

    public static int getCurrentTime() {
        Calendar calendar= Calendar.getInstance();
        Date currentLocalTime=calendar.getTime();
        DateFormat date= new SimpleDateFormat("HHmm");
        String localTime = date.format(currentLocalTime);
        Log.d("TestHour", localTime);
        return Integer.parseInt(localTime);
    }

    public static String convertStringHours(String hour) {
        String hour1 = hour.substring(0,2);
        String hour2 = hour.substring(2,4);
        return hour1 + ":" + hour2;
    }

    public static String convertDateHours(Date date) {
        DateFormat dfTime = new SimpleDateFormat("HH:mm");
        return dfTime.format(date);
    }
}
