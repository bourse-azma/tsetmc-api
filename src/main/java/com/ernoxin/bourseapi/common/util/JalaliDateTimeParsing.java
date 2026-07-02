package com.ernoxin.bourseapi.common.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class JalaliDateTimeParsing {

    private static final ZoneId TEHRAN = ZoneId.of("Asia/Tehran");
    private static final Pattern JALALI_DATE_FIRST = Pattern.compile(
            "^(\\d{4})/(\\d{1,2})/(\\d{1,2})(?:\\s+(\\d{1,2}:\\d{1,2}(?::\\d{1,2})?))?$"
    );
    private static final Pattern JALALI_TIME_FIRST = Pattern.compile(
            "^(\\d{1,2}:\\d{1,2}(?::\\d{1,2})?)\\s+(\\d{4})/(\\d{1,2})/(\\d{1,2})$"
    );

    private JalaliDateTimeParsing() {
    }

    static String normalizeDisplayDateTime(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }

        String normalized = toLatinDigits(raw.trim()).replaceAll("\\s+", " ");
        Matcher dateFirst = JALALI_DATE_FIRST.matcher(normalized);
        if (dateFirst.matches()) {
            return formatJalaliDateTime(
                    Integer.parseInt(dateFirst.group(1)),
                    Integer.parseInt(dateFirst.group(2)),
                    Integer.parseInt(dateFirst.group(3)),
                    dateFirst.group(4)
            );
        }

        Matcher timeFirst = JALALI_TIME_FIRST.matcher(normalized);
        if (timeFirst.matches()) {
            return formatJalaliDateTime(
                    Integer.parseInt(timeFirst.group(4)),
                    Integer.parseInt(timeFirst.group(5)),
                    Integer.parseInt(timeFirst.group(6)),
                    timeFirst.group(1)
            );
        }

        ZonedDateTime zonedDateTime = parseToTehranDateTime(normalized);
        if (zonedDateTime != null) {
            LocalDateTime localDateTime = zonedDateTime.withZoneSameInstant(TEHRAN).toLocalDateTime();
            int[] jalali = JalaliCalendarMath.gregorianToJalali(
                    localDateTime.getYear(),
                    localDateTime.getMonthValue(),
                    localDateTime.getDayOfMonth()
            );
            return formatJalaliDateTime(
                    jalali[0],
                    jalali[1],
                    jalali[2],
                    localDateTime.getHour(),
                    localDateTime.getMinute(),
                    localDateTime.getSecond()
            );
        }

        return normalized;
    }

    static String fromIsoDateAndHeven(String transactionDate, Integer hEven) {
        if (transactionDate == null || transactionDate.isBlank()) {
            return null;
        }

        ZonedDateTime zonedDateTime = parseToTehranDateTime(transactionDate.trim());
        if (zonedDateTime == null) {
            return normalizeDisplayDateTime(transactionDate);
        }

        if (hEven != null && hEven > 0) {
            int hour = hEven / 10_000;
            int minute = (hEven % 10_000) / 100;
            int second = hEven % 100;
            zonedDateTime = zonedDateTime
                    .withZoneSameInstant(TEHRAN)
                    .withHour(hour)
                    .withMinute(minute)
                    .withSecond(second)
                    .withNano(0);
        }

        LocalDateTime localDateTime = zonedDateTime.withZoneSameInstant(TEHRAN).toLocalDateTime();
        int[] jalali = JalaliCalendarMath.gregorianToJalali(
                localDateTime.getYear(),
                localDateTime.getMonthValue(),
                localDateTime.getDayOfMonth()
        );
        return formatJalaliDateTime(
                jalali[0],
                jalali[1],
                jalali[2],
                localDateTime.getHour(),
                localDateTime.getMinute(),
                localDateTime.getSecond()
        );
    }

    static String firstNonBlank(String... values) {
        if (values == null) {
            return null;
        }

        for (String value : values) {
            String normalized = normalizeDisplayDateTime(value);
            if (normalized != null && !normalized.isBlank()) {
                return normalized;
            }
        }

        return null;
    }

    static String formatGregorianDevenHeven(Integer deven, Integer hEven) {
        if (deven == null || hEven == null) {
            return null;
        }

        int year = deven / 10_000;
        int month = (deven % 10_000) / 100;
        int day = deven % 100;
        int[] jalali = JalaliCalendarMath.gregorianToJalali(year, month, day);
        int hour = hEven / 10_000;
        int minute = (hEven % 10_000) / 100;
        int second = hEven % 100;
        return formatJalaliDateTime(jalali[0], jalali[1], jalali[2], hour, minute, second);
    }

    static Integer gregorianDevenToJalaliDeven(Integer deven) {
        if (deven == null) {
            return null;
        }

        int gy = deven / 10_000;
        int gm = (deven % 10_000) / 100;
        int gd = deven % 100;
        int[] j = JalaliCalendarMath.gregorianToJalali(gy, gm, gd);
        return j[0] * 10_000 + j[1] * 100 + j[2];
    }

    private static String formatJalaliDateTime(int year, int month, int day, String timeText) {
        if (timeText == null || timeText.isBlank()) {
            return String.format("%d/%02d/%02d", year, month, day);
        }

        int[] timeParts = parseTimeParts(timeText);
        if (timeParts == null) {
            return String.format("%d/%02d/%02d", year, month, day);
        }

        return formatJalaliDateTime(year, month, day, timeParts[0], timeParts[1], timeParts[2]);
    }

    private static String formatJalaliDateTime(int year, int month, int day, int hour, int minute, int second) {
        return String.format("%d/%02d/%02d %02d:%02d:%02d", year, month, day, hour, minute, second);
    }

    private static int[] parseTimeParts(String timeText) {
        String[] parts = timeText.split(":");
        if (parts.length < 2 || parts.length > 3) {
            return null;
        }

        try {
            int hour = Integer.parseInt(parts[0]);
            int minute = Integer.parseInt(parts[1]);
            int second = parts.length == 3 ? Integer.parseInt(parts[2]) : 0;
            return new int[]{hour, minute, second};
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private static ZonedDateTime parseToTehranDateTime(String raw) {
        try {
            return OffsetDateTime.parse(raw).atZoneSameInstant(TEHRAN);
        } catch (DateTimeParseException ignored) {
        }

        try {
            return ZonedDateTime.parse(raw).withZoneSameInstant(TEHRAN);
        } catch (DateTimeParseException ignored) {
        }

        try {
            return LocalDateTime.parse(raw, DateTimeFormatter.ISO_LOCAL_DATE_TIME).atZone(TEHRAN);
        } catch (DateTimeParseException ignored) {
        }

        try {
            LocalDate localDate = LocalDate.parse(raw.substring(0, Math.min(raw.length(), 10)));
            return localDate.atStartOfDay(TEHRAN);
        } catch (DateTimeParseException | StringIndexOutOfBoundsException ignored) {
        }

        return null;
    }

    private static String toLatinDigits(String value) {
        StringBuilder builder = new StringBuilder(value.length());
        for (char ch : value.toCharArray()) {
            switch (ch) {
                case '۰' -> builder.append('0');
                case '۱' -> builder.append('1');
                case '۲' -> builder.append('2');
                case '۳' -> builder.append('3');
                case '۴' -> builder.append('4');
                case '۵' -> builder.append('5');
                case '۶' -> builder.append('6');
                case '۷' -> builder.append('7');
                case '۸' -> builder.append('8');
                case '۹' -> builder.append('9');
                case '٠' -> builder.append('0');
                case '١' -> builder.append('1');
                case '٢' -> builder.append('2');
                case '٣' -> builder.append('3');
                case '٤' -> builder.append('4');
                case '٥' -> builder.append('5');
                case '٦' -> builder.append('6');
                case '٧' -> builder.append('7');
                case '٨' -> builder.append('8');
                case '٩' -> builder.append('9');
                default -> builder.append(ch);
            }
        }
        return builder.toString();
    }
}
