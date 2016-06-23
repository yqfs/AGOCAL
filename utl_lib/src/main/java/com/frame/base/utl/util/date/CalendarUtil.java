package com.frame.base.utl.util.date;

import java.util.Calendar;

public class CalendarUtil {

    public static void setTimeToMidnight(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    public static int millisecondsToDays(long intervalMs) {
        return Math.round((intervalMs / (1000 * 86400)));
    }

    public static int GetNumFromDate(Calendar now, Calendar returnDate) {
        Calendar cNow = (Calendar) now.clone();
        Calendar cReturnDate = (Calendar) returnDate.clone();
        setTimeToMidnight(cNow);
        setTimeToMidnight(cReturnDate);

        long todayMs = cNow.getTimeInMillis();
        long returnMs = cReturnDate.getTimeInMillis();
        long intervalMs = todayMs - returnMs;
        int index = millisecondsToDays(intervalMs);

        return index;
    }

    public static Calendar GetStartDate() {
        int iDay = 0;
        Calendar today = GetTodayDate();
        today.add(Calendar.WEEK_OF_YEAR, -3);

        iDay = today.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;

        if (iDay < 0) {
            iDay = 6;
        }

        today.add(Calendar.DAY_OF_WEEK, -iDay);
        return today;
    }

    public static Calendar GetTodayDate() {
        Calendar cal_Today = Calendar.getInstance();
        cal_Today.set(Calendar.HOUR_OF_DAY, 0);
        cal_Today.set(Calendar.MINUTE, 0);
        cal_Today.set(Calendar.SECOND, 0);
        cal_Today.setFirstDayOfWeek(Calendar.MONDAY);

        return cal_Today;
    }
}
