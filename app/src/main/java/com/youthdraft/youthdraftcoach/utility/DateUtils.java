package com.youthdraft.youthdraftcoach.utility;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jjupin on 11/21/16.
 *
 * Sample data for the players JSON data structure....
 *
 *

 { "players":[{ "first_name":"John",
                "last_name":"Doe",
                "assess_date":"11-21-2016T18:25:00.000Z",
                "league":"TVLL",
                "league_age":"10",
                "bib":"23"},
            { "first_name":"Mike",
                "last_name":"Trout",
                "assess_date":"11-21-2016T18:25:00.000Z",
                "league":"TVLL",
                "league_age":"10",
                "bib":"23"},
            { "first_name":"Bryce",
                "last_name":"Harper",
                "assess_date":"11-21-2016T18:25:00.000Z",
                "league":"TVLL",
                "league_age":"10",
                "bib":"23"}
            ]
 }

 *
 *
 */

public class DateUtils {

    private static final SimpleDateFormat MONTH_FORMATTER = new SimpleDateFormat("MMM");
    private static final SimpleDateFormat DAY_FORMATTER = new SimpleDateFormat("d");
    private static final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat("h:mm a");

    public static String getMonthForDate(Date d) {
        return MONTH_FORMATTER.format(d);
    }

    public static String getDayForDate(Date d) {
        return DAY_FORMATTER.format(d);
    }

    public static String getTimeForDate(Date d) {
        return TIME_FORMATTER.format(d);
    }
}
