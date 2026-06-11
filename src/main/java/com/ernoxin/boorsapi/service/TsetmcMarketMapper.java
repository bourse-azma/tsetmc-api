package com.ernoxin.boorsapi.service;

import com.ernoxin.boorsapi.domain.TsetmcMarketModels;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class TsetmcMarketMapper {
    private static final DateTimeFormatter CHART_EVENT_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss")
            .withZone(ZoneOffset.UTC);

    public TsetmcMarketModels.MarketOverviewResult toMarketOverview(JsonNode root) {
        JsonNode marketOverview = root.path("marketOverview");
        return new TsetmcMarketModels.MarketOverviewResult(
                new TsetmcMarketModels.MarketOverview(
                        intOrNull(marketOverview, "lastDataDEven"),
                        intOrNull(marketOverview, "lastDataHEven"),
                        doubleOrNull(marketOverview, "indexLastValue"),
                        doubleOrNull(marketOverview, "indexChange"),
                        doubleOrNull(marketOverview, "indexEqualWeightedLastValue"),
                        doubleOrNull(marketOverview, "indexEqualWeightedChange"),
                        intOrNull(marketOverview, "marketActivityDEven"),
                        intOrNull(marketOverview, "marketActivityHEven"),
                        longOrNull(marketOverview, "marketActivityZTotTran"),
                        doubleOrNull(marketOverview, "marketActivityQTotCap"),
                        doubleOrNull(marketOverview, "marketActivityQTotTran"),
                        textOrNull(marketOverview, "marketState"),
                        doubleOrNull(marketOverview, "marketValue"),
                        doubleOrNull(marketOverview, "marketValueBase"),
                        textOrNull(marketOverview, "marketStateTitle")
                )
        );
    }

    public TsetmcMarketModels.SelectedIndexesResult toSelectedIndexes(JsonNode root) {
        List<TsetmcMarketModels.SelectedIndex> selectedIndexes = new ArrayList<>();
        for (JsonNode indexNode : root.path("indexB1")) {
            selectedIndexes.add(new TsetmcMarketModels.SelectedIndex(
                    textOrNull(indexNode, "insCode"),
                    intOrNull(indexNode, "dEven"),
                    intOrNull(indexNode, "hEven"),
                    doubleOrNull(indexNode, "xDrNivJIdx004"),
                    doubleOrNull(indexNode, "xPhNivJIdx004"),
                    doubleOrNull(indexNode, "xPbNivJIdx004"),
                    doubleOrNull(indexNode, "xVarIdxJRfV"),
                    doubleOrNull(indexNode, "indexChange"),
                    textOrNull(indexNode, "lVal30"),
                    booleanOrNull(indexNode, "last"),
                    intOrNull(indexNode, "c1"),
                    intOrNull(indexNode, "c2"),
                    intOrNull(indexNode, "c3"),
                    intOrNull(indexNode, "c4")
            ));
        }
        return new TsetmcMarketModels.SelectedIndexesResult(selectedIndexes);
    }

    public TsetmcMarketModels.InstrumentEffectsResult toInstrumentEffects(JsonNode root) {
        List<TsetmcMarketModels.InstrumentEffect> instrumentEffects = new ArrayList<>();
        for (JsonNode effectNode : root.path("instEffect")) {
            JsonNode instrumentNode = effectNode.path("instrument");
            instrumentEffects.add(new TsetmcMarketModels.InstrumentEffect(
                    textOrNull(effectNode, "insCode"),
                    textOrNull(instrumentNode, "lVal18AFC"),
                    textOrNull(instrumentNode, "lVal30"),
                    doubleOrNull(effectNode, "pClosing"),
                    doubleOrNull(effectNode, "instEffectValue")
            ));
        }
        return new TsetmcMarketModels.InstrumentEffectsResult(instrumentEffects);
    }

    public TsetmcMarketModels.MostVisitedResult toMostVisited(JsonNode root) {
        List<TsetmcMarketModels.MostVisitedInstrument> mostVisitedInstruments = new ArrayList<>();
        for (JsonNode itemNode : root.path("tradeTop")) {
            JsonNode instrumentNode = itemNode.path("instrument");
            mostVisitedInstruments.add(new TsetmcMarketModels.MostVisitedInstrument(
                    textOrNull(itemNode, "insCode"),
                    textOrNull(instrumentNode, "lVal18AFC"),
                    textOrNull(instrumentNode, "lVal30"),
                    intOrNull(itemNode, "dEven"),
                    intOrNull(itemNode, "hEven"),
                    doubleOrNull(itemNode, "pClosing"),
                    doubleOrNull(itemNode, "priceYesterday"),
                    doubleOrNull(itemNode, "priceChange"),
                    doubleOrNull(itemNode, "priceMin"),
                    doubleOrNull(itemNode, "priceMax"),
                    doubleOrNull(itemNode, "priceFirst"),
                    doubleOrNull(itemNode, "zTotTran"),
                    doubleOrNull(itemNode, "qTotTran5J"),
                    doubleOrNull(itemNode, "qTotCap"),
                    doubleOrNull(itemNode, "pDrCotVal")
            ));
        }
        return new TsetmcMarketModels.MostVisitedResult(mostVisitedInstruments);
    }

    public TsetmcMarketModels.ClosingPriceInfoResult toClosingPriceInfo(JsonNode root) {
        JsonNode closingPriceInfoNode = root.path("closingPriceInfo");
        JsonNode instrumentStateNode = closingPriceInfoNode.path("instrumentState");

        TsetmcMarketModels.InstrumentTradingState instrumentState = new TsetmcMarketModels.InstrumentTradingState(
                textOrNull(instrumentStateNode, "cEtaval"),
                textOrNull(instrumentStateNode, "cEtavalTitle"),
                intOrNull(instrumentStateNode, "underSupervision")
        );

        return new TsetmcMarketModels.ClosingPriceInfoResult(
                instrumentState,
                doubleOrNull(closingPriceInfoNode, "priceChange"),
                doubleOrNull(closingPriceInfoNode, "priceMin"),
                doubleOrNull(closingPriceInfoNode, "priceMax"),
                doubleOrNull(closingPriceInfoNode, "priceYesterday"),
                doubleOrNull(closingPriceInfoNode, "priceFirst"),
                textOrNull(closingPriceInfoNode, "insCode"),
                intOrNull(closingPriceInfoNode, "dEven"),
                intOrNull(closingPriceInfoNode, "hEven"),
                doubleOrNull(closingPriceInfoNode, "pClosing"),
                doubleOrNull(closingPriceInfoNode, "pDrCotVal"),
                doubleOrNull(closingPriceInfoNode, "zTotTran"),
                doubleOrNull(closingPriceInfoNode, "qTotTran5J"),
                doubleOrNull(closingPriceInfoNode, "qTotCap")
        );
    }

    public TsetmcMarketModels.BestLimitsResult toBestLimits(JsonNode root) {
        List<TsetmcMarketModels.OrderBookLevel> orderBookLevels = new ArrayList<>();
        for (JsonNode levelNode : root.path("bestLimits")) {
            orderBookLevels.add(new TsetmcMarketModels.OrderBookLevel(
                    intOrNull(levelNode, "number"),
                    doubleOrNull(levelNode, "qTitMeDem"),
                    intOrNull(levelNode, "zOrdMeDem"),
                    doubleOrNull(levelNode, "pMeDem"),
                    doubleOrNull(levelNode, "pMeOf"),
                    intOrNull(levelNode, "zOrdMeOf"),
                    doubleOrNull(levelNode, "qTitMeOf")
            ));
        }
        return new TsetmcMarketModels.BestLimitsResult(orderBookLevels);
    }

    public TsetmcMarketModels.ClientTypeResult toClientType(JsonNode root) {
        JsonNode clientTypeNode = root.path("clientType");
        return new TsetmcMarketModels.ClientTypeResult(
                doubleOrNull(clientTypeNode, "buy_I_Volume"),
                doubleOrNull(clientTypeNode, "buy_N_Volume"),
                doubleOrNull(clientTypeNode, "buy_DDD_Volume"),
                intOrNull(clientTypeNode, "buy_CountI"),
                intOrNull(clientTypeNode, "buy_CountN"),
                intOrNull(clientTypeNode, "buy_CountDDD"),
                doubleOrNull(clientTypeNode, "sell_I_Volume"),
                doubleOrNull(clientTypeNode, "sell_N_Volume"),
                intOrNull(clientTypeNode, "sell_CountI"),
                intOrNull(clientTypeNode, "sell_CountN")
        );
    }

    public TsetmcMarketModels.ClosingPriceDailyResult toClosingPriceDaily(JsonNode root) {
        List<TsetmcMarketModels.ClosingPriceDailyItem> dailyPrices = new ArrayList<>();
        for (JsonNode dailyNode : root.path("closingPriceDaily")) {
            dailyPrices.add(new TsetmcMarketModels.ClosingPriceDailyItem(
                    textOrNull(dailyNode, "insCode"),
                    intOrNull(dailyNode, "dEven"),
                    doubleOrNull(dailyNode, "pClosing"),
                    doubleOrNull(dailyNode, "pDrCotVal"),
                    doubleOrNull(dailyNode, "priceMin"),
                    doubleOrNull(dailyNode, "priceMax"),
                    doubleOrNull(dailyNode, "priceFirst"),
                    doubleOrNull(dailyNode, "priceYesterday"),
                    doubleOrNull(dailyNode, "priceChange"),
                    doubleOrNull(dailyNode, "zTotTran"),
                    doubleOrNull(dailyNode, "qTotTran5J"),
                    doubleOrNull(dailyNode, "qTotCap")
            ));
        }
        return new TsetmcMarketModels.ClosingPriceDailyResult(dailyPrices);
    }

    public TsetmcMarketModels.ClosingPriceChartDataResult toClosingPriceChartData(JsonNode root) {
        List<TsetmcMarketModels.ClosingPriceChartDataItem> chartData = new ArrayList<>();
        for (JsonNode chartNode : root.path("closingPriceChartData")) {
            chartData.add(new TsetmcMarketModels.ClosingPriceChartDataItem(
                    chartEventDateOrNull(chartNode, "dEven"),
                    doubleOrNull(chartNode, "pDrCotVal"),
                    doubleOrNull(chartNode, "qTotTran5J"),
                    doubleOrNull(chartNode, "priceFirst"),
                    doubleOrNull(chartNode, "priceMin"),
                    doubleOrNull(chartNode, "priceMax")
            ));
        }
        return new TsetmcMarketModels.ClosingPriceChartDataResult(chartData);
    }

    public TsetmcMarketModels.InstrumentInfoResult toInstrumentInfo(JsonNode root) {
        JsonNode instrumentInfoNode = root.path("instrumentInfo");
        JsonNode sectorNode = instrumentInfoNode.path("sector");
        JsonNode staticThresholdNode = instrumentInfoNode.path("staticThreshold");
        JsonNode epsNode = instrumentInfoNode.path("eps");

        TsetmcMarketModels.SectorInfo sector = null;
        String sectorCode = textOrNull(sectorNode, "cSecVal");
        String sectorName = textOrNull(sectorNode, "lSecVal");
        if (sectorCode != null || sectorName != null) {
            sector = new TsetmcMarketModels.SectorInfo(sectorCode, sectorName);
        }

        TsetmcMarketModels.StaticPriceThreshold staticPriceThreshold = null;
        Double minAllowedPrice = doubleOrNull(staticThresholdNode, "psGelStaMin");
        Double maxAllowedPrice = doubleOrNull(staticThresholdNode, "psGelStaMax");
        if (minAllowedPrice != null || maxAllowedPrice != null) {
            staticPriceThreshold = new TsetmcMarketModels.StaticPriceThreshold(minAllowedPrice, maxAllowedPrice);
        }

        TsetmcMarketModels.EpsInfo eps = null;
        if (!epsNode.isMissingNode() && !epsNode.isNull()) {
            Double epsValue = doubleOrNull(epsNode, "epsValue");
            Double estimatedEps = doubleOrNull(epsNode, "estimatedEPS");
            Double sectorPe = doubleOrNull(epsNode, "sectorPE");
            Double psr = doubleOrNull(epsNode, "psr");
            if (epsValue != null || estimatedEps != null || sectorPe != null || psr != null) {
                eps = new TsetmcMarketModels.EpsInfo(epsValue, estimatedEps, sectorPe, psr);
            }
        }

        return new TsetmcMarketModels.InstrumentInfoResult(
                textOrNull(instrumentInfoNode, "insCode"),
                textOrNull(instrumentInfoNode, "lVal18AFC"),
                textOrNull(instrumentInfoNode, "lVal30"),
                textOrNull(instrumentInfoNode, "cIsin"),
                sector,
                staticPriceThreshold,
                eps,
                doubleOrNull(instrumentInfoNode, "baseVol"),
                doubleOrNull(instrumentInfoNode, "zTitad"),
                intOrNull(instrumentInfoNode, "flow"),
                textOrNull(instrumentInfoNode, "flowTitle"),
                textOrNull(instrumentInfoNode, "cgrValCotTitle"),
                intOrNull(instrumentInfoNode, "instrumentID")
        );
    }

    public TsetmcMarketModels.RelatedCompanyResult toRelatedCompanies(JsonNode root) {
        List<TsetmcMarketModels.RelatedCompanyItem> relatedCompanies = new ArrayList<>();
        for (JsonNode companyNode : root.path("relatedCompany")) {
            JsonNode instrumentNode = companyNode.path("instrument");
            relatedCompanies.add(new TsetmcMarketModels.RelatedCompanyItem(
                    textOrNull(instrumentNode, "insCode"),
                    textOrNull(instrumentNode, "lVal18AFC"),
                    textOrNull(instrumentNode, "lVal30"),
                    doubleOrNull(companyNode, "pClosing"),
                    doubleOrNull(companyNode, "pDrCotVal"),
                    doubleOrNull(companyNode, "priceMin"),
                    doubleOrNull(companyNode, "priceMax"),
                    doubleOrNull(companyNode, "priceChange"),
                    doubleOrNull(companyNode, "zTotTran"),
                    doubleOrNull(companyNode, "qTotTran5J"),
                    doubleOrNull(companyNode, "qTotCap")
            ));
        }

        List<TsetmcMarketModels.RelatedCompanyHistoryItem> relatedCompanyThirtyDayHistory = new ArrayList<>();
        for (JsonNode historyNode : root.path("relatedCompanyThirtyDayHistory")) {
            relatedCompanyThirtyDayHistory.add(new TsetmcMarketModels.RelatedCompanyHistoryItem(
                    textOrNull(historyNode, "insCode"),
                    intOrNull(historyNode, "dEven"),
                    doubleOrNull(historyNode, "pClosing"),
                    doubleOrNull(historyNode, "pDrCotVal"),
                    doubleOrNull(historyNode, "zTotTran"),
                    doubleOrNull(historyNode, "qTotTran5J"),
                    doubleOrNull(historyNode, "qTotCap")
            ));
        }

        return new TsetmcMarketModels.RelatedCompanyResult(relatedCompanies, relatedCompanyThirtyDayHistory);
    }

    public TsetmcMarketModels.CodalNoticesResult toCodalNotices(JsonNode root) {
        List<TsetmcMarketModels.CodalNotice> notices = new ArrayList<>();
        for (JsonNode noticeNode : root.path("preparedData")) {
            notices.add(new TsetmcMarketModels.CodalNotice(
                    longOrNull(noticeNode, "id"),
                    textOrNull(noticeNode, "symbol"),
                    textOrNull(noticeNode, "name"),
                    textOrNull(noticeNode, "title"),
                    textOrNull(noticeNode, "publishDateTime_Gregorian"),
                    gregorianDevenToJalaliDeven(intOrNull(noticeNode, "publishDateTime_DEven")),
                    intOrNull(noticeNode, "hasHtmlReport"),
                    intOrNull(noticeNode, "hasExcelReport"),
                    intOrNull(noticeNode, "hasPDFReport"),
                    intOrNull(noticeNode, "hasXMLReport"),
                    textOrNull(noticeNode, "tracingNo")
            ));
        }
        return new TsetmcMarketModels.CodalNoticesResult(notices);
    }

    public TsetmcMarketModels.MarketMessagesResult toMarketMessages(JsonNode root) {
        List<TsetmcMarketModels.MarketMessageItem> messages = new ArrayList<>();
        for (JsonNode messageNode : root.path("msg")) {
            messages.add(new TsetmcMarketModels.MarketMessageItem(
                    longOrNull(messageNode, "tseMsgIdn"),
                    intOrNull(messageNode, "dEven"),
                    intOrNull(messageNode, "hEven"),
                    textOrNull(messageNode, "tseTitle"),
                    textOrNull(messageNode, "tseDesc"),
                    intOrNull(messageNode, "flow")
            ));
        }
        return new TsetmcMarketModels.MarketMessagesResult(messages);
    }

    public TsetmcMarketModels.InstrumentStateTopResult toInstrumentStateTop(JsonNode root) {
        List<TsetmcMarketModels.InstrumentStateItem> instrumentStates = new ArrayList<>();
        for (JsonNode stateNode : root.path("instrumentState")) {
            instrumentStates.add(new TsetmcMarketModels.InstrumentStateItem(
                    longOrNull(stateNode, "idn"),
                    intOrNull(stateNode, "dEven"),
                    intOrNull(stateNode, "hEven"),
                    textOrNull(stateNode, "insCode"),
                    textOrNull(stateNode, "lVal18AFC"),
                    textOrNull(stateNode, "lVal30"),
                    textOrNull(stateNode, "cEtaval"),
                    intOrNull(stateNode, "underSupervision"),
                    textOrNull(stateNode, "cEtavalTitle")
            ));
        }
        return new TsetmcMarketModels.InstrumentStateTopResult(instrumentStates);
    }

    public TsetmcMarketModels.TradesResult toTrades(JsonNode root) {
        List<TsetmcMarketModels.TradeItem> trades = new ArrayList<>();
        for (JsonNode tradeNode : root.path("trade")) {
            trades.add(new TsetmcMarketModels.TradeItem(
                    intOrNull(tradeNode, "nTran"),
                    intOrNull(tradeNode, "hEven"),
                    doubleOrNull(tradeNode, "qTitTran"),
                    doubleOrNull(tradeNode, "pTran"),
                    intOrNull(tradeNode, "canceled")
            ));
        }
        return new TsetmcMarketModels.TradesResult(trades);
    }

    public TsetmcMarketModels.CodalStatementContentResult toCodalStatementContent(JsonNode root) {
        List<TsetmcMarketModels.CodalStatementContentItem> statementContents = new ArrayList<>();
        for (JsonNode statementNode : root.path("statemetnContent")) {
            statementContents.add(new TsetmcMarketModels.CodalStatementContentItem(
                    textOrNull(statementNode, "title"),
                    textOrNull(statementNode, "sentDateTime_Gregorian"),
                    textOrNull(statementNode, "publishDateTime_Gregorian"),
                    intOrNull(statementNode, "publishDateTime_DEven"),
                    intOrNull(statementNode, "reportSubType"),
                    intOrNull(statementNode, "pageID"),
                    rawTextOrNull(statementNode, "content")
            ));
        }
        return new TsetmcMarketModels.CodalStatementContentResult(statementContents);
    }

    private String textOrNull(JsonNode node, String field) {
        JsonNode fieldNode = node.get(field);
        if (fieldNode == null || fieldNode.isNull()) {
            return null;
        }
        return fieldNode.asText().trim();
    }

    private String rawTextOrNull(JsonNode node, String field) {
        JsonNode fieldNode = node.get(field);
        if (fieldNode == null || fieldNode.isNull()) {
            return null;
        }
        return fieldNode.asText();
    }

    private Integer intOrNull(JsonNode node, String field) {
        JsonNode fieldNode = node.get(field);
        if (fieldNode == null || fieldNode.isNull()) {
            return null;
        }
        return fieldNode.asInt();
    }

    private Long longOrNull(JsonNode node, String field) {
        JsonNode fieldNode = node.get(field);
        if (fieldNode == null || fieldNode.isNull()) {
            return null;
        }
        return fieldNode.asLong();
    }

    private Double doubleOrNull(JsonNode node, String field) {
        JsonNode fieldNode = node.get(field);
        if (fieldNode == null || fieldNode.isNull()) {
            return null;
        }
        return fieldNode.asDouble();
    }

    private String chartEventDateOrNull(JsonNode node, String field) {
        JsonNode fieldNode = node.get(field);
        if (fieldNode == null || fieldNode.isNull()) {
            return null;
        }

        long eventDate = fieldNode.asLong();
        if (eventDate >= 10_000_000 && eventDate <= 99_999_999) {
            int year = (int) (eventDate / 10_000);
            int month = (int) ((eventDate % 10_000) / 100);
            int day = (int) (eventDate % 100);
            return LocalDate.of(year, month, day).atStartOfDay().format(CHART_EVENT_DATE_FORMATTER);
        }

        return CHART_EVENT_DATE_FORMATTER.format(Instant.ofEpochSecond(eventDate));
    }

    public TsetmcMarketModels.EtfInfoResult toEtfInfo(JsonNode root) {
        JsonNode etfNode = root.path("etf");
        Integer deven = intOrNull(etfNode, "deven");
        Integer hEven = intOrNull(etfNode, "hEven");
        return new TsetmcMarketModels.EtfInfoResult(
                textOrNull(etfNode, "insCode"),
                formatNavAnnouncementAt(deven, hEven),
                doubleOrNull(etfNode, "pRedTran"),
                doubleOrNull(etfNode, "pSubTran"),
                intOrNull(etfNode, "iClose")
        );
    }

    private Integer gregorianDevenToJalaliDeven(Integer deven) {
        if (deven == null) return null;
        int gy = deven / 10000;
        int gm = (deven % 10000) / 100;
        int gd = deven % 100;
        int[] j = gregorianToJalali(gy, gm, gd);
        return j[0] * 10000 + j[1] * 100 + j[2];
    }

    private String formatNavAnnouncementAt(Integer deven, Integer hEven) {
        if (deven == null || hEven == null) return null;
        int year  = deven / 10000;
        int month = (deven % 10000) / 100;
        int day   = deven % 100;
        int[] jalali = gregorianToJalali(year, month, day);
        int h = hEven / 10000;
        int m = (hEven % 10000) / 100;
        int s = hEven % 100;
        return String.format("%d/%02d/%02d  %02d:%02d:%02d", jalali[0], jalali[1], jalali[2], h, m, s);
    }

    // -----------------------------------------------------------------------
    // Jalali (Persian) calendar conversion — ported from jalaali-js library.
    // -----------------------------------------------------------------------

    private static final int[] JALALI_BREAKS = {
        -61, 9, 38, 199, 426, 686, 756, 818, 1111, 1181, 1210,
        1635, 2060, 2097, 2192, 2262, 2324, 2394, 2456, 3178
    };

    private int[] gregorianToJalali(int gy, int gm, int gd) {
        int jdn = g2d(gy, gm, gd);
        return d2j(jdn);
    }

    /** Gregorian date → Julian Day Number. */
    private int g2d(int gy, int gm, int gd) {
        int d = div((gy + div(gm - 8, 6) + 100100) * 1461, 4)
                + div(153 * mod(gm + 9, 12) + 2, 5)
                + gd - 34840408;
        d = d - div(div(gy + 100100 + div(gm - 8, 6), 100) * 3, 4) + 752;
        return d;
    }

    /** Julian Day Number → Jalali date { year, month, day }. */
    private int[] d2j(int jdn) {
        int gy = d2g(jdn)[0];
        int jy = gy - 621;
        int[] r = jalCal(jy);
        int jdn1f = g2d(gy, 3, r[1]); // r[1] = march
        int k = jdn - jdn1f;
        int jm, jd;
        if (k >= 0) {
            if (k <= 185) {
                jm = 1 + div(k, 31);
                jd = mod(k, 31) + 1;
                return new int[]{jy, jm, jd};
            }
            k -= 186;
        } else {
            jy -= 1;
            k += 179;
            if (r[0] == 1) k += 1; // r[0] = leap
        }
        jm = 7 + div(k, 30);
        jd = mod(k, 30) + 1;
        return new int[]{jy, jm, jd};
    }

    /** Julian Day Number → Gregorian date, returns int[]{year, month, day}. */
    private int[] d2g(int jdn) {
        int j = 4 * jdn + 139361631;
        j = j + div(div(4 * jdn + 183187720, 146097) * 3, 4) * 4 - 3908;
        int i = div(mod(j, 1461), 4) * 5 + 308;
        int gd = div(mod(i, 153), 5) + 1;
        int gm = mod(div(i, 153), 12) + 1;
        int gy = div(j, 1461) - 100100 + div(8 - gm, 6);
        return new int[]{gy, gm, gd};
    }

    /**
     * Returns int[]{leap, march} for the Jalali year jy.
     * leap = years-since-last-leap (0 means this IS a leap year).
     * march = Gregorian day in March that is the 1st of Farvardin.
     */
    private int[] jalCal(int jy) {
        int bl = JALALI_BREAKS.length;
        int gy = jy + 621;
        int leapJ = -14;
        int jp = JALALI_BREAKS[0];
        int jm = 0, jump = 0, leap, leapG, march, n;

        if (jy < jp || jy >= JALALI_BREAKS[bl - 1])
            throw new IllegalArgumentException("Invalid Jalaali year: " + jy);

        for (int i = 1; i < bl; i++) {
            jm = JALALI_BREAKS[i];
            jump = jm - jp;
            if (jy < jm) break;
            leapJ = leapJ + div(jump, 33) * 8 + div(mod(jump, 33), 4);
            jp = jm;
        }
        n = jy - jp;
        leapJ = leapJ + div(n, 33) * 8 + div(mod(n, 33) + 3, 4);
        if (mod(jump, 33) == 4 && jump - n == 4) leapJ += 1;

        leapG = div(gy, 4) - div((div(gy, 100) + 1) * 3, 4) - 150;
        march = 20 + leapJ - leapG;

        if (jump - n < 6) n = n - jump + div(jump + 4, 33) * 33;
        leap = mod(mod(n + 1, 33) - 1, 4);
        if (leap == -1) leap = 4;

        return new int[]{leap, march};
    }

    /** Truncation division (towards zero), matching JavaScript's ~~ operator. */
    private int div(int a, int b) { return a / b; }
    /** Truncation remainder, matching JavaScript's mod = a - ~~(a/b)*b. */
    private int mod(int a, int b) { return a - (a / b) * b; }

    private Boolean booleanOrNull(JsonNode node, String field) {
        JsonNode fieldNode = node.get(field);
        if (fieldNode == null || fieldNode.isNull()) {
            return null;
        }
        return fieldNode.asBoolean();
    }
}
