package com.ernoxin.bourseapi.common.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

final class JalaliChartDateSupport {

    private JalaliChartDateSupport() {
    }

    static int normalizeCompactDateToGregorianDeven(int compact) {
        int year = compact / 10_000;
        if (year >= 1200 && year <= 1500) {
            return jalaliDevenToGregorianDeven(compact);
        }
        return compact;
    }

    static int jalaliDevenToGregorianDeven(int jalaliDeven) {
        int jy = jalaliDeven / 10_000;
        int jm = (jalaliDeven % 10_000) / 100;
        int jd = jalaliDeven % 100;
        int[] g = JalaliCalendarMath.jalaliToGregorian(jy, jm, jd);
        return g[0] * 10_000 + g[1] * 100 + g[2];
    }

    static LocalDate parseNormalizedChartEventDate(String eventDate) {
        if (eventDate == null || eventDate.isBlank()) {
            return null;
        }

        String normalized = eventDate.trim();
        if (normalized.length() >= 8 && Character.isDigit(normalized.charAt(0))) {
            try {
                int compact = Integer.parseInt(normalized.substring(0, 8));
                compact = normalizeCompactDateToGregorianDeven(compact);
                return LocalDate.parse(String.format("%08d", compact), DateTimeFormatter.BASIC_ISO_DATE);
            } catch (DateTimeParseException | NumberFormatException ignored) {
            }
        }

        try {
            return LocalDate.parse(normalized, DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss"));
        } catch (DateTimeParseException ignored) {
            return null;
        }
    }

    static LocalDate jalaliMonthStart(LocalDate gregorianDate) {
        int[] jalali = JalaliCalendarMath.gregorianToJalali(
                gregorianDate.getYear(),
                gregorianDate.getMonthValue(),
                gregorianDate.getDayOfMonth()
        );
        int[] gregorian = JalaliCalendarMath.jalaliToGregorian(jalali[0], jalali[1], 1);
        return LocalDate.of(gregorian[0], gregorian[1], gregorian[2]);
    }

    static LocalDate jalaliYearStart(LocalDate gregorianDate) {
        int[] jalali = JalaliCalendarMath.gregorianToJalali(
                gregorianDate.getYear(),
                gregorianDate.getMonthValue(),
                gregorianDate.getDayOfMonth()
        );
        int[] gregorian = JalaliCalendarMath.jalaliToGregorian(jalali[0], 1, 1);
        return LocalDate.of(gregorian[0], gregorian[1], gregorian[2]);
    }

    static LocalDate rollingWeekStart(LocalDate date) {
        long epochDay = date.toEpochDay();
        return LocalDate.ofEpochDay((epochDay / 7) * 7);
    }
}
