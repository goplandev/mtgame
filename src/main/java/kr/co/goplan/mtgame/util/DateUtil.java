package kr.co.goplan.mtgame.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

public class DateUtil {
    private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);

    //등록일자, 수정일자시 UTC로 Date() 생성
    public static LocalDateTime getUTCDate() {
        return DateConvertUtil.convertToUTCDate(new Date());
    }

    /*public static Date getDateByTimeZoneGroup(TimeZoneGroup timeZoneGroup) {
        return getDateByTimeZoneGroupName(timeZoneGroup.getName());
    }*/
    public static Date getDateByTimeZoneGroupName(String zoneId) {
        try {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(zoneId));
            return calendar.getTime();
        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return null;
        }
    }

    //등록일자, 수정일자시 UTC로 Date(long l) 생성
    public static LocalDateTime getUTCDate(long l) {
        return DateConvertUtil.convertToUTCDate(new Date(l));
    }

    //로컬기준의 일자
    public static Date getDate() {
        return new Date();
    }

    //로컬기준의 일자
    public static Date getDate(long l) {
        return new Date(l);
    }

    public static String getStringDateToday() {
        return getStringDate(new Date());
    }

    public static String getStringDate(Date date) {
        return new SimpleDateFormat("yyyyMMdd").format(date);
    }

    public static String getStringDateToday(Locale locale) {
        return new SimpleDateFormat("yyyyMMdd", locale).format(new Date());
    }

    public static Date getAfterTime(int after) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, after);

        return cal.getTime();
    }

    public static Date getAfterMinuteTime(Date date, int min) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, min);

        return cal.getTime();
    }

    public static Date getAfterTime(String hhmm, int after) {

        String hour = hhmm.substring(0, 2);
        String min = hhmm.substring(2, 4);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
        cal.set(Calendar.MINUTE, Integer.parseInt(min));
        cal.add(Calendar.MINUTE, after);
        return cal.getTime();
    }

    public static Date getYesterday() {

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, -1);

        return cal.getTime();
    }

    public static String getYesterdayString() {
        return new SimpleDateFormat("yyyyMMdd").format(getYesterday());
    }

    public static Date getTomorrow() {

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, 1);

        return cal.getTime();
    }

    public static String getTomorrowString() {
        return new SimpleDateFormat("yyyyMMdd").format(getTomorrow());
    }

    public static List<String> getStringDateListBetweenStartDateAndEndDate(String startDate, String endDate) {

        List<String> result = new ArrayList<String>();

        try {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

            if (null == startDate || null == endDate ||Integer.parseInt(startDate) > Integer.parseInt(endDate)) {
                return result;
            } else if (startDate.equals(endDate)) {
                result.add(startDate);
            } else {

                Calendar startCal = Calendar.getInstance();
                startCal.setTime(sdf.parse(startDate));

                boolean isContinue = true;

                while (isContinue) {

                    String startDateTmp = sdf.format(startCal.getTime());

                    result.add(startDateTmp);

                    startCal.add(Calendar.DATE, 1);

                    if (startDateTmp.equals(endDate)) {
                        isContinue = false;
                    }
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        return result;
    }

    public static Date getLastDate() {

        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.YEAR, 9999);
        cal.set(Calendar.MONTH, 11);
        cal.set(Calendar.DATE, 31);

        return cal.getTime();
    }

    public static Integer getSecondsBetweenStartDateToEndDate(Date startDate, Date endDate) {

        long startTime = startDate.getTime();
        long endTime = endDate.getTime();

        long mills = (endTime - startTime) / 1000;

        try {
            return (int) mills;
        } catch (IllegalArgumentException ex) {
            logger.error(ex.getMessage(), ex);
            return 0;
        }
    }

    public static boolean isValidDate(String yyyyMMdd) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            sdf.parse(yyyyMMdd);
        }
        catch (ParseException e) {
            return false;
        }
        return true;
    }

    public static Integer getSecondsBetweenStartDateToEndDate(String date, String startTime, String endTime) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

        try {
            Date startDate = sdf.parse(date + startTime);
            Date endDate = sdf.parse(date + endTime);

            return getSecondsBetweenStartDateToEndDate(startDate, endDate);

        } catch (ParseException ex) {
            logger.error(ex.getMessage(), ex);
        }

        return 0;
    }

    public static String getStringTime(int time) {

        if (time < 0 || time > 240000) {
            return "";
        }

        if (time < 100000) {
            return "0" + time;
        } else {
            return String.valueOf(time);
        }
    }

    public static String getHHmmss(Date date) {
        return new SimpleDateFormat("HHmmss").format(date);
    }

    public static int convertStringSecToIntSec(String hhmm) {

        int time = Integer.parseInt(hhmm);

        int h = time / 100;
        int m = time % 100;

        int hour = h * 3600;
        int min = m * 60;

        return hour + min;
    }

    public static Date getDateBeforeDayOfMonth(int month) {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, month * -1);

        return calendar.getTime();
    }

    public static Date getDateYyyyMmDd(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        try {
            return sdf.parse(date);
        } catch (ParseException ex) {
            logger.error(ex.getMessage(), ex);
            return new Date();
        }
    }
}
