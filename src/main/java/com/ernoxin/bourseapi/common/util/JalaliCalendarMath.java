package com.ernoxin.bourseapi.common.util;

final class JalaliCalendarMath {

    private static final int[] JALALI_BREAKS = {
            -61, 9, 38, 199, 426, 686, 756, 818, 1111, 1181, 1210,
            1635, 2060, 2097, 2192, 2262, 2324, 2394, 2456, 3178
    };

    private JalaliCalendarMath() {
    }

    static int[] gregorianToJalali(int gy, int gm, int gd) {
        int jdn = g2d(gy, gm, gd);
        return d2j(jdn);
    }

    static int[] jalaliToGregorian(int jy, int jm, int jd) {
        int jdn = j2d(jy, jm, jd);
        return d2g(jdn);
    }

    private static int j2d(int jy, int jm, int jd) {
        int[] r = jalCal(jy);
        int gy = jy + 621;
        return g2d(gy, 3, r[1]) + (jm - 1) * 31 - div(jm, 7) * (jm - 7) + jd - 1;
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
