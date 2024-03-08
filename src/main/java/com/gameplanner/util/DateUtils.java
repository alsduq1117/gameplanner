package com.gameplanner.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    public static String formatUnixTime(long unixTime, String pattern) {
        LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochSecond(unixTime), ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return date.format(formatter);
    }


    // 년도와 월을 유닉스 시간으로 변환하는 메소드
    public static long toUnixTime(int year, int month) {
        return LocalDateTime.of(year, month, 1, 0, 0)
                .toEpochSecond(ZoneOffset.UTC);
    }

    public static LocalDateTime getNextMonth(int year, int month) {
        LocalDateTime dateTime = LocalDateTime.of(year, month, 1, 0, 0);
        return dateTime.plusMonths(1);
    }

}
