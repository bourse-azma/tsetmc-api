package com.ernoxin.bourseapi.service;

import com.ernoxin.bourseapi.domain.TsetmcMarketModels;
import com.ernoxin.bourseapi.domain.TsetmcMarketReportModels;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TsetmcMarketMapper {

    private final TsetmcMarketOverviewMapper overviewMapper;
    private final TsetmcInstrumentDataMapper instrumentMapper;
    private final TsetmcChartDataMapper chartMapper;
    private final TsetmcCodalDataMapper codalMapper;

    public TsetmcMarketModels.MarketOverviewResult toMarketOverview(JsonNode root) {
        return overviewMapper.toMarketOverview(root);
    }

    public TsetmcMarketModels.SelectedIndexesResult toSelectedIndexes(JsonNode root) {
        return overviewMapper.toSelectedIndexes(root);
    }

    public TsetmcMarketModels.InstrumentEffectsResult toInstrumentEffects(JsonNode root) {
        return overviewMapper.toInstrumentEffects(root);
    }

    public TsetmcMarketModels.MostVisitedResult toMostVisited(JsonNode root) {
        return overviewMapper.toMostVisited(root);
    }

    public TsetmcMarketModels.ClosingPriceInfoResult toClosingPriceInfo(JsonNode root) {
        return instrumentMapper.toClosingPriceInfo(root);
    }

    public TsetmcMarketModels.BestLimitsResult toBestLimits(JsonNode root) {
        return instrumentMapper.toBestLimits(root);
    }

    public TsetmcMarketModels.ClientTypeResult toClientType(JsonNode root) {
        return instrumentMapper.toClientType(root);
    }

    public TsetmcMarketModels.ClosingPriceDailyResult toClosingPriceDaily(JsonNode root) {
        return chartMapper.toClosingPriceDaily(root);
    }

    public TsetmcMarketModels.ClosingPriceChartDataResult toClosingPriceChartData(JsonNode root) {
        return chartMapper.toClosingPriceChartData(root);
    }

    public TsetmcMarketModels.ClosingPriceChartDataResult dailyListToChartData(JsonNode root) {
        return chartMapper.dailyListToChartData(root);
    }

    public TsetmcMarketModels.InstrumentInfoResult toInstrumentInfo(JsonNode root) {
        return instrumentMapper.toInstrumentInfo(root);
    }

    public TsetmcMarketModels.RelatedCompanyResult toRelatedCompanies(JsonNode root) {
        return instrumentMapper.toRelatedCompanies(root);
    }

    public TsetmcMarketReportModels.CodalNoticesResult toCodalNotices(JsonNode root) {
        return codalMapper.toCodalNotices(root);
    }

    public TsetmcMarketReportModels.MarketMessagesResult toMarketMessages(JsonNode root) {
        return overviewMapper.toMarketMessages(root);
    }

    public TsetmcMarketReportModels.InstrumentStateTopResult toInstrumentStateTop(JsonNode root) {
        return overviewMapper.toInstrumentStateTop(root);
    }

    public TsetmcMarketReportModels.TradesResult toTrades(JsonNode root) {
        return instrumentMapper.toTrades(root);
    }

    public TsetmcMarketReportModels.CodalStatementContentResult toCodalStatementContent(JsonNode root) {
        return codalMapper.toCodalStatementContent(root);
    }

    public TsetmcMarketReportModels.EtfInfoResult toEtfInfo(JsonNode root) {
        return instrumentMapper.toEtfInfo(root);
    }
}
