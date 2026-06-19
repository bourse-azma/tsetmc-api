package com.ernoxin.boorsapi.cache;

public final class TsetmcCacheKeys {

    private TsetmcCacheKeys() {
    }

    public static String instrumentCode(String instrumentCode) {
        return instrumentCode.trim();
    }

    public static String sectorCode(String sectorCode) {
        return sectorCode.trim();
    }

    public static String marketIdAndLimit(int marketId, int limit) {
        return marketId + ":" + limit;
    }

    public static String instrumentCodeAndLimit(String instrumentCode, int limit) {
        return instrumentCode(instrumentCode) + ":" + limit;
    }

    public static String clientType(String instrumentCode, int clientTypeParam1, int clientTypeParam2) {
        return instrumentCode(instrumentCode) + ":" + clientTypeParam1 + ":" + clientTypeParam2;
    }

    public static String instrumentCodeAndDays(String instrumentCode, int days) {
        return instrumentCode(instrumentCode) + ":" + days;
    }

    public static String instrumentCodeAndPeriod(String instrumentCode, String period) {
        return instrumentCode(instrumentCode) + ":" + period.trim();
    }

    public static String flowAndLimit(int flow, int limit) {
        return flow + ":" + limit;
    }

    public static String codalStatementContent(int reportType, int reportSubType, int pageId, String instrumentCode) {
        return instrumentCode(instrumentCode) + ":" + reportType + ":" + reportSubType + ":" + pageId;
    }
}
