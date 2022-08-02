package kr.co.goplan.mtgame.util;

import org.springframework.format.annotation.DateTimeFormat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static com.fasterxml.jackson.databind.type.LogicalType.DateTime;

public class DateConvertUtil {
    /*
     * UTC 기준 today를 return(yyyymmdd)
     * : DateUtil.getStringDateToday()
     */
    public static String getStringDateTodayUTC() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyymmdd");
        return LocalDateTime.now().format(dateTimeFormatter);
    }

    public static String getStringDateTimeTodayUTC() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return LocalDateTime.now().format(dateTimeFormatter);
    }

    /*
     * Input : Date
     * Date 입력 시 UTC 기준으로 return(yyyymmdd)
     * : DateUtil.getStringDate(Date date)
     */
    public static String getStringDateUTC(Date dt) {
        if(null == dt) {
            return null;
        } else {
            LocalDateTime ldt = LocalDateTime.ofInstant(dt.toInstant(), ZoneId.of("UTC"));
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            return ldt.format(dateTimeFormatter);
        }
    }

    /*
     * Input : timezone
     * 입력된 Timezone기준 today를 return(yyyymmdd)
     * : DateUtil.getStringDateToday(Locale locale)
     */
    public static String getStringDateTodayUTC(String tz) {
        LocalDate ld = LocalDate.now(ZoneId.of(tz));
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return (tz == null ? null : ld.format(dateTimeFormatter));
    }

    /*
     * Input : Date, min
     * 입력된 min을 추가하여 UTC로 변환하여 return
     * DateUtil.getAfterMinuteTime(Date date, int min)
     */
    public static Date getAfterMinuteTimeUTC(Date dt, Long min) {
        if(null == dt) {
            return null;
        } else {
            LocalDateTime ldt = LocalDateTime.ofInstant(dt.toInstant(), ZoneId.of("UTC")).plusMinutes(min);
            return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        }
    }

    /*
     * UTC 기준 yesterday를 return(yyyymmdd)
     * DateUtil.getYesterdayString()
     */
    public static String getYesterdayStringUTC() {
        LocalDate ld = LocalDate.now(ZoneId.of("UTC")).minusDays(1);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return  ld.format(dateTimeFormatter);
    }

    /*
     * UTC 기준 tomorrow를 return(yyyymmdd)
     * DateUtil.getTomorrowString()
     */
    public static String getTomorrowStringUTC() {
        LocalDate ld = LocalDate.now(ZoneId.of("UTC")).plusDays(1);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return  ld.format(dateTimeFormatter);
    }

    public static String getTomorrowDateTimeStringUTC() {
        LocalDateTime ld = LocalDateTime.now(ZoneId.of("UTC")).plusDays(1);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return  ld.format(dateTimeFormatter);
    }

    /*
     * Input : Date
     * 입력된 date를 UTC로 변환하여 시간 return(HHmmss)
     * DateUtil.getHHmmss(Date date)
     */
    public static String getHHmmssUTC(Date dt) {
        if(null == dt) {
            return null;
        } else {
            LocalDateTime ldt = LocalDateTime.ofInstant(dt.toInstant(), ZoneId.of("UTC"));
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HHmmss");
            return ldt.format(dateTimeFormatter);
        }
    }

    /*
     * Input : Date
     * Date 입력 시 UTC를 적용하여 return
     */
    public static LocalDateTime convertToUTCDate(Date dt) {
        if(null == dt) {
            return null;
        } else {
            LocalDateTime ldt = LocalDateTime.ofInstant(dt.toInstant(), ZoneId.of("UTC"));
            return ldt;//Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        }
    }

    /*
     * Input : Date
     * Date 입력 시 local Timezone을 적용하여 return
     */
    public static Date convertToLocalDate(Date dt) {
        if(null == dt) {
            return null;
        } else {
            LocalDateTime ldt = LocalDateTime.ofInstant(dt.toInstant(), ZoneId.systemDefault());
            return Date.from(ldt.atZone(ZoneId.of("UTC")).toInstant());
        }
    }

    /*
     * Input : Date, Timezone
     * Date, Timezone 입력 시 입력된 Timezone을 적용하여 return
     */
    public static Date convertToDateByTimeZone(Date dt, String tz) {
        if(null == dt) {
            return null;
        } else {
            LocalDateTime ldt = LocalDateTime.ofInstant(dt.toInstant(), ZoneId.of(tz));
            return Date.from(ldt.atZone(ZoneId.of("UTC")).toInstant());
        }
    }

    /**
     * 로컬 날짜 형식으로 리턴한다. 로케일이 없으면 시스템 디폴트로 출력함.<br>
     * @param dt 변환할 날짜
     * @param timeZoneId 타임존
     * @param locale 언어코드
     * @return 변환된 시간 문자열
     */
    public static String toLocalDateString(Date dt, Locale locale, String timeZoneId) {
        if(null == dt) {
            return null;
        } else {
            //DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", getUserLocale(location));
            DateFormat df = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.MEDIUM, locale);
            df.setTimeZone(TimeZone.getTimeZone(timeZoneId));
            return df.format(convertToLocalDate(dt));
        }
    }

    /*
     * Input : Date, Timezone
     * 입력된 Local Date와 Timezone기준으로 UTC를 산정함.
     */
    private static LocalDateTime convertToUTCDateByTimezone(Date dt, String tz){
        ZonedDateTime zdt = ZonedDateTime.ofInstant(dt.toInstant(), ZoneId.systemDefault());
        zdt = ZonedDateTime.of(zdt.toLocalDateTime(), ZoneId.of(tz));
        return zdt.toLocalDateTime();//convertToUTCDate(Date.from(zdt.toLocalDateTime().atZone(ZoneId.systemDefault()).toInstant()));
    }

    /*
     * Input : Date, Timezone
     * 입력된 Local Timezone을 적용하여 Date를 리턴함  <-- 작동 안함. 무조건 현재 시간 반환
     */
/*
	public static Date convertLocalDateByTimezone(Date dt, String tz){
		ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of(tz));
		return Date.from(zdt.toLocalDateTime().atZone(ZoneId.systemDefault()).toInstant());
	}
*/

    public static Date convertDateByTimezone(Date registeredDatetime, String zoneId) {
        //LocalDateTime ldt = LocalDateTime.ofInstant(registeredDatetime.toInstant(), ZoneId.of(zoneId));
        //return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());

        LocalDateTime utcLdt = LocalDateTime.ofInstant(registeredDatetime.toInstant(), ZoneId.of("UTC"));
        return Date.from(utcLdt.atZone(ZoneId.of(zoneId)).toInstant());
    }
}
