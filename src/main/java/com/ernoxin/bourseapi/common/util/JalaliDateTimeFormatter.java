package com.ernoxin.bourseapi.common.util;

import java.time.LocalDate;

public final class JalaliDateTimeFormatter {

    private JalaliDateTimeFormatter() {
    }

    public static String formatGregorianDevenHeven(Integer deven, Integer hEven) {
        return JalaliDateTimeParsing.formatGregorianDevenHeven(deven, hEven);
    }

    public static Integer gregorianDevenToJalaliDeven(Integer deven) {
        return JalaliDateTimeParsing.gregorianDevenToJalaliDeven(deven);
    }

    /**
     * TSETMC {@code GetChartData} may return {@code dEven} as Jalali yyyyMMdd (year 1200–1500).
     * Normalize every compact date to Gregorian {@code dEven} before chart aggregation.
     */
    public static int normalizeCompactDateToGregorianDeven(int compact) {
        return JalaliChartDateSupport.normalizeCompactDateToGregorianDeven(compact);
    }

    public static int jalaliDevenToGregorianDeven(int jalaliDeven) {
        return JalaliChartDateSupport.jalaliDevenToGregorianDeven(jalaliDeven);
    }

    public static LocalDate parseNormalizedChartEventDate(String eventDate) {
        return JalaliChartDateSupport.parseNormalizedChartEventDate(eventDate);
    }

    public static LocalDate jalaliMonthStart(LocalDate gregorianDate) {
        return JalaliChartDateSupport.jalaliMonthStart(gregorianDate);
    }

    public static LocalDate jalaliYearStart(LocalDate gregorianDate) {
        return JalaliChartDateSupport.jalaliYearStart(gregorianDate);
    }

    public static LocalDate rollingWeekStart(LocalDate date) {
        return JalaliChartDateSupport.rollingWeekStart(date);
    }

    public static String normalizeDisplayDateTime(String raw) {
        return JalaliDateTimeParsing.normalizeDisplayDateTime(raw);
    }

    public static String fromIsoDateAndHeven(String transactionDate, Integer hEven) {
        return JalaliDateTimeParsing.fromIsoDateAndHeven(transactionDate, hEven);
    }

    public static String firstNonBlank(String... values) {
        return JalaliDateTimeParsing.firstNonBlank(values);
    }
}
