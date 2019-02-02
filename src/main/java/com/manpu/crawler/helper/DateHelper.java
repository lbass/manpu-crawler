package com.manpu.crawler.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateHelper {

    private final static Logger logger = LoggerFactory.getLogger(DateHelper.class);

    public static LocalDateTime getDateStringToDateTime(String date, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDate resultDate = null;
        try {
            resultDate = LocalDate.parse(date, formatter);
        } catch (Exception e) {
            logger.warn("datetime parse error: {}, {}", date, format);
            resultDate = LocalDate.now();
        }
        return resultDate.atStartOfDay();
    }

    public static LocalDateTime getStringToDateTime(String datetime, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDateTime resultDateTime = null;
        try {
            resultDateTime = LocalDateTime.parse(datetime, formatter);
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("datetime parse error: {}, {}", datetime, format);
        }
        return resultDateTime;
    }

    public static String getLongToDateString(long milliSecond, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDate date =
                Instant.ofEpochMilli(milliSecond).atZone(ZoneId.systemDefault()).toLocalDate();
        return date.format(formatter);
    }

    public static long getDateStringToLong(String dateString, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDate date = LocalDate.parse(dateString, formatter);
        return date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static long getTodayMillisecond() {
        return LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static void main(String[] args) {
        System.out.println(DateHelper.getTodayMillisecond());
    }
}
