package com.ernoxin.boorsapi.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class JalaliDateTimeFormatter {

    private static final ZoneId TEHRAN = ZoneId.of("Asia/Tehran");
    private static final Pattern JALALI_DATE_FIRST = Pattern.compile(
            "^(\\d{4})/(\\d{1,2})/(\\d{1,2})(?:\\s+(\\d{1,2}:\\d{1,2}(?::\\d{1,2})?))?$"
    );
    private static final Pattern JALALI_TIME_FIRST = Pattern.compile(
            "^(\\d{1,2}:\\d{1,2}(?::\\d{1,2})?)\\s+(\\d{4})/(\\d{1,2})/(\\d{1,2})$"
    );

    private static final int[] JALALI_BREAKS = {
            -61, 9, 38, 199, 426, 686, 756, 818, 1111, 1181, 1210,
            1635, 2060, 2097, 2192, 2262, 2324, 2394, 2456, 3178
    };

    private JalaliDateTimeFormatter() {
    }

    public static String formatGregorianDevenHeven(Integer deven, Integer hEven) {
        if (deven == null || hEven == null) {
            return null;
        }

        int year = deven / 10_000;
        int month = (deven % 10_000) / 100;
        int day = deven % 100;
        int[] jalali = gregorianToJalali(year, month, day);
        int hour = hEven / 10_000;
        int minute = (hEven % 10_000) / 100;
        int second = hEven % 100;
        return formatJalaliDateTime(jalali[0], jalali[1], jalali[2], hour, minute, second);
    }

    public static Integer gregorianDevenToJalaliDeven(Integer deven) {
        if (deven == null) {
            return null;
        }

        int gy = deven / 10_000;
        int gm = (deven % 10_000) / 100;
        int gd = deven % 100;
        int[] j = gregorianToJalali(gy, gm, gd);
        return j[0] * 10_000 + j[1] * 100 + j[2];
    }

    public static String normalizeDisplayDateTime(String raw) {
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
            int[] jalali = gregorianToJalali(
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

    public static String fromIsoDateAndHeven(String transactionDate, Integer hEven) {
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
        int[] jalali = gregorianToJalali(
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

    public static String firstNonBlank(String... values) {
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

    private static int[] gregorianToJalali(int gy, int gm, int gd) {
        int jdn = g2d(gy, gm, gd);
        return d2j(jdn);
    }

    private static int g2d(int gy, int gm, int gd) {
        int d = div((gy + div(gm - 8, 6) + 100100) * 1461, 4)
                + div(153 * mod(gm + 9, 12) + 2, 5)
                + gd - 34840408;
        d = d - div(div(gy + 100100 + div(gm - 8, 6), 100) * 3, 4) + 752;
        return d;
    }

    private static int[] d2j(int jdn) {
        int gy = d2g(jdn)[0];
        int jy = gy - 621;
        int[] r = jalCal(jy);
        int jdn1f = g2d(gy, 3, r[1]);
        int k = jdn - jdn1f;
        if (k >= 0) {
            if (k <= 185) {
                int jm = 1 + div(k, 31);
                int jd = mod(k, 31) + 1;
                return new int[]{jy, jm, jd};
            }
            k -= 186;
        } else {
            jy -= 1;
            k += 179;
            if (r[0] == 1) {
                k += 1;
            }
        }

        int jm = 7 + div(k, 30);
        int jd = mod(k, 30) + 1;
        return new int[]{jy, jm, jd};
    }

    private static int[] d2g(int jdn) {
        int j = 4 * jdn + 139361631;
        j = j + div(div(4 * jdn + 183187720, 146097) * 3, 4) * 4 - 3908;
        int i = div(mod(j, 1461), 4) * 5 + 308;
        int gd = div(mod(i, 153), 5) + 1;
        int gm = mod(div(i, 153), 12) + 1;
        int gy = div(j, 1461) - 100100 + div(8 - gm, 6);
        return new int[]{gy, gm, gd};
    }

    private static int[] jalCal(int jy) {
        int bl = JALALI_BREAKS.length;
        int gy = jy + 621;
        int leapJ = -14;
        int jp = JALALI_BREAKS[0];
        int jm = 0;
        int jump = 0;
        int n;

        if (jy < jp || jy >= JALALI_BREAKS[bl - 1]) {
            throw new IllegalArgumentException("Invalid Jalaali year: " + jy);
        }

        for (int i = 1; i < bl; i++) {
            jm = JALALI_BREAKS[i];
            jump = jm - jp;
            if (jy < jm) {
                break;
            }
            leapJ = leapJ + div(jump, 33) * 8 + div(mod(jump, 33), 4);
            jp = jm;
        }

        n = jy - jp;
        leapJ = leapJ + div(n, 33) * 8 + div(mod(n, 33) + 3, 4);
        if (mod(jump, 33) == 4 && jump - n == 4) {
            leapJ += 1;
        }

        int leapG = div(gy, 4) - div((div(gy, 100) + 1) * 3, 4) - 150;
        int march = 20 + leapJ - leapG;

        if (jump - n < 6) {
            n = n - jump + div(jump + 4, 33) * 33;
        }

        int leap = mod(mod(n + 1, 33) - 1, 4);
        if (leap == -1) {
            leap = 4;
        }

        return new int[]{leap, march};
    }

    private static int div(int a, int b) {
        return a / b;
    }

    private static int mod(int a, int b) {
        return a - (a / b) * b;
    }
}
