package com.mu.util;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
/**
 *
 * @author Peng Mu
 */
public class RandomUtil
{
    static public File pickFile(File folder)
    {
        double r = Math.random();
        File[] fileList = folder.listFiles();
        int i = (int)(r*fileList.length);
        return fileList[i];
    }

    static public String[] pickDate(String min, String max, int daysInBetween)
    {
        String[] re = new String[2];
        Date maxDay = DateUtil.toDate(max);
        Date minDay = DateUtil.toDate(min);
        long mm = (maxDay.getTime() - minDay.getTime())/TimeConstants.MILLIS_IN_A_DAY - daysInBetween;
        double r = Math.random();
        long startDay = (long)(mm*r);
        re[0] = DateUtil.toDateStr(new Date(minDay.getTime() + startDay*TimeConstants.MILLIS_IN_A_DAY));
        re[1] = DateUtil.toDateStr(new Date(minDay.getTime() + (startDay+daysInBetween)*TimeConstants.MILLIS_IN_A_DAY));
        return re;        
        
    }

    static public String pickDate(String min, String max)
    {
        String re;
        Date maxDay = DateUtil.toDate(max);
        Date minDay = DateUtil.toDate(min);
        long mm = (maxDay.getTime() - minDay.getTime())/TimeConstants.MILLIS_IN_A_DAY;
        double r = Math.random();
        long startDay = (long)(mm*r);
        re = DateUtil.toDateStr(new Date(minDay.getTime() + startDay*TimeConstants.MILLIS_IN_A_DAY));
        return re;

    }
    static public Date pickDate(Date min, Date max)
    {
        Date re;
        long mm = (max.getTime() - min.getTime())/TimeConstants.MILLIS_IN_A_DAY;
        double r = Math.random();
        long startDay = (long)(mm*r);
        re = new Date(min.getTime() + startDay*TimeConstants.MILLIS_IN_A_DAY);
        return re;

    }
    static public int pickInt(int min, int max)
    {
        int mm = max - min;
        double r = Math.random();
        return min + (int)(mm*r);
    }
    static public float pickFloat(float min, float max)
    {
        float mm = max - min;
        double r = Math.random();
        return min + (float)(mm*r);
    }
}
