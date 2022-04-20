package com.socialx.myapplication.utility;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class AppUtils {
    public static String FilterTime(String apiDate){
        String val="0";
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            Date d1 = dateFormat.parse(apiDate);
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateStr = formatter.format(d1);

            Date d2 = Calendar.getInstance().getTime();

            long diff = d2.getTime() - d1.getTime();

            long days = TimeUnit.MILLISECONDS.toDays(diff);
            long remainingHoursInMillis = diff - TimeUnit.DAYS.toMillis(days);
            long hours = TimeUnit.MILLISECONDS.toHours(remainingHoursInMillis);
            long remainingMinutesInMillis = remainingHoursInMillis - TimeUnit.HOURS.toMillis(hours);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(remainingMinutesInMillis);
            long remainingSecondsInMillis = remainingMinutesInMillis - TimeUnit.MINUTES.toMillis(minutes);
            long seconds = TimeUnit.MILLISECONDS.toSeconds(remainingSecondsInMillis);

            if (days > 0) {
                val= days+" days ago";
            } else if (hours > 0) {
                val= hours+" hours ago";
            } else if (minutes > 0) {
                val= minutes+" minutes ago";
            } else {
                val= seconds+" seconds";
            }

        } catch (Exception e) {
            Log.e("Date Time", e.getMessage());
        }
        return val;
    }
}
