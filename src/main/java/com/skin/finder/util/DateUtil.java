/*
 * $RCSfile: DateUtil.java,v $
 * $Revision: 1.1 $
 * $Date: 2010-04-28 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <p>Title: DateUtil</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class DateUtil {
    /**
     * @param pattern
     * @param target
     * @return Date
     */
    public static Date getToday(String pattern, String target) {
        return getDate(new Date(), pattern, target);
    }

    /**
     * @param pattern
     * @param target
     * @return Date
     */
    public static Date getTomrrow(String pattern, String target) {
        Date date = new Date(System.currentTimeMillis() + 24L * 60L * 60L * 1000L);
        return getDate(date, pattern, target);
    }

    /**
     * @param date
     * @param pattern
     * @param target
     * @return Date
     */
    public static Date getDate(Date date, String pattern, String target) {
        String dateTime = DateUtil.format(date, pattern);
        return DateUtil.parse(dateTime, target);
    }

    /**
     * @param date
     * @return Date
     */
    public static Date parse(String date) {
        return parse(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * @param date
     * @param pattern
     * @return Date
     */
    public static Date parse(String date, String pattern) {
        if(date == null) {
            return null;
        }

        DateFormat dateFormat = new SimpleDateFormat(pattern);

        try {
            return dateFormat.parse(date);
        }
        catch(ParseException e) {
        }
        return null;
    }

    /**
     * @param timeMillis
     * @param pattern
     * @return String
     */
    public static String format(long timeMillis, String pattern) {
        if(timeMillis > 0L) {
            DateFormat dateFormat = new SimpleDateFormat(pattern);
            return dateFormat.format(new Date(timeMillis));
        }
        return "";
    }

    /**
     * @param date
     * @param pattern
     * @return String
     */
    public static String format(Date date, String pattern) {
        if(date == null) {
            return "";
        }

        DateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

    /**
     * @param date
     * @return int
     */
    public static int year(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * @param date
     * @return int
     */
    public static int month(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH);
    }

    /**
     * @param date
     * @return int
     */
    public static int day(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * @param date
     * @param value
     * @return Date
     */
    public static Date addDay(Date date, int value) {
        return add(date, Calendar.DAY_OF_MONTH, value);
    }

    /**
     * @param date
     * @param field
     * @param value
     * @return Date
     */
    public static Date add(Date date, int field, int value) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int current = calendar.get(field);
        calendar.set(field, current + value);
        return calendar.getTime();
    }
}
