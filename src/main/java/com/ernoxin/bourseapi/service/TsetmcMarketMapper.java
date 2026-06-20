package com.ernoxin.bourseapi.service;

import com.ernoxin.bourseapi.common.util.JalaliDateTimeFormatter;
import com.ernoxin.bourseapi.domain.TsetmcMarketModels;
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

        Integer eventDate = intOrNull(closingPriceInfoNode, "dEven");
        Integer eventTime = intOrNull(closingPriceInfoNode, "hEven");

        return new TsetmcMarketModels.ClosingPriceInfoResult(
                instrumentState,
                doubleOrNull(closingPriceInfoNode, "priceChange"),
                doubleOrNull(closingPriceInfoNode, "priceMin"),
                doubleOrNull(closingPriceInfoNode, "priceMax"),
                doubleOrNull(closingPriceInfoNode, "priceYesterday"),
                doubleOrNull(closingPriceInfoNode, "priceFirst"),
                textOrNull(closingPriceInfoNode, "insCode"),
                eventDate,
                eventTime,
                JalaliDateTimeFormatter.formatGregorianDevenHeven(eventDate, eventTime),
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
        // TSETMC: I = حقیقی (individual), N = حقوقی (non-individual / institutional)
        return new TsetmcMarketModels.ClientTypeResult(
                doubleOrNull(clientTypeNode, "buy_N_Volume"),
                doubleOrNull(clientTypeNode, "buy_I_Volume"),
                doubleOrNull(clientTypeNode, "buy_DDD_Volume"),
                intOrNull(clientTypeNode, "buy_CountN"),
                intOrNull(clientTypeNode, "buy_CountI"),
                intOrNull(clientTypeNode, "buy_CountDDD"),
                doubleOrNull(clientTypeNode, "sell_N_Volume"),
                doubleOrNull(clientTypeNode, "sell_I_Volume"),
                intOrNull(clientTypeNode, "sell_CountN"),
                intOrNull(clientTypeNode, "sell_CountI")
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
                    JalaliDateTimeFormatter.gregorianDevenToJalaliDeven(intOrNull(noticeNode, "publishDateTime_DEven")),
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
                JalaliDateTimeFormatter.formatGregorianDevenHeven(deven, hEven),
                doubleOrNull(etfNode, "pRedTran"),
                doubleOrNull(etfNode, "pSubTran"),
                intOrNull(etfNode, "iClose")
        );
    }

    private Boolean booleanOrNull(JsonNode node, String field) {
        JsonNode fieldNode = node.get(field);
        if (fieldNode == null || fieldNode.isNull()) {
            return null;
        }
        return fieldNode.asBoolean();
    }
}
