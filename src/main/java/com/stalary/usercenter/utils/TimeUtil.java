package com.stalary.usercenter.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * TimeUtil
 *
 * @author lirongqian
 * @since 2018/03/25
 */
public class TimeUtil {

    static SimpleDateFormat defaultFormatter = new SimpleDateFormat("yyyy-MM-dd");

    static SimpleDateFormat generalFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    static SimpleDateFormat monthFormatter = new SimpleDateFormat("yyyyMM");

    static SimpleDateFormat chineseFormatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");

    static SimpleDateFormat chineseFormatterSimple = new SimpleDateFormat("yyyy年MM月dd日");

    static java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static Date stringToTime(String dateStr) {
        if (dateStr == null || dateStr.length() == 0) {
            return null;
        }
        Date date;
        try {
            date = generalFormatter.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return date;
    }

    public static Date stringToDate(String dateStr) {
        if (dateStr == null || dateStr.length() == 0) {
            return null;
        }
        return localDateToDate(LocalDate.parse(dateStr, formatter));
    }

    public static Date localDateToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }


    public static String dateToString(Date date) {

        if (date == null) {
            return "";
        }

        return generalFormatter.format(date);
    }

    public static String dateToChinese(Date date) {

        if (date == null) {
            return "";
        }

        return chineseFormatter.format(date);
    }

    public static String dateToChineseSimple(Date date) {

        if (date == null) {
            return "";
        }

        return chineseFormatterSimple.format(date);
    }

    public static String getThisMonth() {
        Date date = new Date();
        return monthFormatter.format(date);
    }




    public static Date plusDays(Date date, int days) {
        return new Date(date.getTime() + 86400000L * days);
    }

    public static Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }


    public static String getSystemDateStamp() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return sdf.format(date);

    }

    public static String getSystemTimeSimple() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);

    }


    public static Long getTimeDifference(String date1, String date2) throws ParseException {
        long beginTime = defaultFormatter.parse(date1).getTime();
        long endTime = defaultFormatter.parse(date2).getTime();
        return endTime - beginTime;
    }

    public static Long getTimeDifference(String date1) throws ParseException {
        long beginTime = defaultFormatter.parse(date1).getTime();
        long endTime = System.currentTimeMillis();
        return endTime - beginTime;
    }

    public static String getDateFromTimeStampStr(String timeStampStr){
        if (StringUtils.isEmpty(timeStampStr))
            return null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long lt = new Long(timeStampStr);
        Date date = new Date(lt);
        return simpleDateFormat.format(date);
    }

    public static String getDateTimeFromTimeStampStr(String timeStampStr){
        if (StringUtils.isEmpty(timeStampStr))
            return null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(timeStampStr);
        Date date = new Date(lt);
        return simpleDateFormat.format(date);

    }

    public static Date getDateByDateStr(String dataFormate, String dataStr) {

        if (StringUtils.isEmpty(dataFormate)) {
            dataFormate = "yyyy-MM-dd";
        }

        DateFormat sdf = new SimpleDateFormat(dataFormate);
        Date date = null;
        try {
            if (StringUtils.isNumeric(dataStr)) {
                long lt = new Long(dataStr);
                date = new Date(lt);
            } else {
                date = sdf.parse(dataStr);

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;

    }
}