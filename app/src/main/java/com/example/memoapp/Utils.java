package com.example.memoapp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// class for custom date and hours related functions
public class Utils {

    // checks if date1 comes before date2
    protected static boolean isExpired(String date1, String date2) {
        if (date1.compareTo(date2) == 0)
            return false;

        String[] d1 = date1.split("/");
        String[] d2 = date2.split("/");

        if (d1[2].compareTo(d2[2]) > 0)
            return true;
        if (d1[2].compareTo(d2[2]) == 0 && d1[1].compareTo(d2[1]) > 0)
            return true;
        if (d1[2].compareTo(d2[2]) == 0 && d1[1].compareTo(d2[1]) == 0 && d1[0].compareTo(d2[0]) > 0)
            return true;
        return false;
    }

    // returns current date in format dd/MM/yyyy
    protected static String currentDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        return formatter.format(now);
    }


}
