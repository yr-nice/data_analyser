/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mu.util;

import java.util.Formatter;

/**
 *
 * @author bernardng
 */
public class TimeConstants {
    public static final int DAYS_IN_A_MONTH = 30;
    public static final int DAYS_IN_A_YEAR = 365;

    public static final long MILLIS_IN_A_MINUTE = 60 * 1000;
    public static final long MILLIS_IN_AN_HOUR = 60 * MILLIS_IN_A_MINUTE;
    public static final long MILLIS_IN_A_DAY = 24 * MILLIS_IN_AN_HOUR;
    public static final long MILLIS_IN_A_WEEK = 7 * MILLIS_IN_A_DAY;
    public static final long MILLIS_IN_A_MONTH = DAYS_IN_A_MONTH * MILLIS_IN_A_DAY;
    public static final long MILLIS_IN_A_YEAR = DAYS_IN_A_YEAR * MILLIS_IN_A_DAY;

    public static final int SECONDS_IN_AN_HOUR = 60 * 60;
    public static final int SECONDS_IN_A_DAY = 24 * SECONDS_IN_AN_HOUR;
    public static final int SECONDS_IN_A_WEEK = 7 * SECONDS_IN_A_DAY;
    public static final int SECONDS_IN_A_MONTH = DAYS_IN_A_MONTH * SECONDS_IN_A_DAY;
    public static final int SECONDS_IN_A_YEAR = DAYS_IN_A_YEAR * SECONDS_IN_A_DAY;

    public static final int MINUTES_IN_A_DAY = 24 * 60;
    public static final int MINUTES_IN_A_WEEK = 7 * MINUTES_IN_A_DAY;
    public static final int MINUTES_IN_A_MONTH = DAYS_IN_A_MONTH * MINUTES_IN_A_DAY;
    public static final int MINUTES_IN_A_YEAR = DAYS_IN_A_YEAR * MINUTES_IN_A_DAY;

    public static final long SG_GMT_MILLIS_OFFSET = 8 * MILLIS_IN_AN_HOUR;
    public static final long SG_GMT_SECONDS_OFFSET = 8 * SECONDS_IN_AN_HOUR;

    public static String millisToString(long millis) {
        if (millis < 0) {
            return "-" + millisToString(-millis);
        }
        
        Formatter f = new Formatter();
        if (millis > MILLIS_IN_A_YEAR) {
            return f.format("%.10f years",  ((double) millis / (double) MILLIS_IN_A_YEAR)).toString();
        } else if (millis > MILLIS_IN_A_MONTH) {
            return f.format("%.9f months", ((double) millis / (double) MILLIS_IN_A_MONTH)).toString();
        } else if (millis > MILLIS_IN_A_WEEK) {
            return f.format("%.8f weeks", ((double) millis / (double) MILLIS_IN_A_WEEK)).toString();
        } else if (millis > MILLIS_IN_A_DAY) {
            return f.format("%.7f days", ((double) millis / (double) MILLIS_IN_A_DAY)).toString();
        } else if (millis > MILLIS_IN_AN_HOUR) {
            return f.format("%.6f hours", ((double) millis / (double) MILLIS_IN_AN_HOUR)).toString();
        } else if (millis > MILLIS_IN_A_MINUTE) {
            return f.format("%.5f minutes", ((double) millis / (double) MILLIS_IN_A_MINUTE)).toString();
        } else {
            return f.format("%.3f seconds", ((double) millis / (double) 1000)).toString();
        }
    }
}