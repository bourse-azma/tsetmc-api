package com.ernoxin.bourseapi.service;

import com.ernoxin.bourseapi.client.TsetmcMarketClient;
import com.ernoxin.bourseapi.domain.TsetmcMarketModels;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TsetmcMarketFetchService {

    private final TsetmcMarketClient client;
    private final TsetmcMarketMapper mapper;

    public TsetmcMarketModels.MarketOverviewResult getMarketOverview(int marketId) {
        return mapper.toMarketOverview(client.getMarketOverview(marketId));
    }

    public TsetmcMarketModels.SelectedIndexesResult getSelectedIndexes(int marketId) {
        return mapper.toSelectedIndexes(client.getSelectedIndexes(marketId));
    }

    public TsetmcMarketModels.InstrumentEffectsResult getInstrumentEffects(int marketId, int limit) {
        return mapper.toInstrumentEffects(client.getInstrumentEffects(marketId, limit));
    }

    public TsetmcMarketModels.MostVisitedResult getMostVisited(int marketId, int limit) {
        return mapper.toMostVisited(client.getMostVisited(marketId, limit));
    }

    public TsetmcMarketModels.ClosingPriceInfoResult getClosingPriceInfo(String instrumentCode) {
        return mapper.toClosingPriceInfo(client.getClosingPriceInfo(instrumentCode));
    }

    public TsetmcMarketModels.BestLimitsResult getBestLimits(String instrumentCode) {
        return mapper.toBestLimits(client.getBestLimits(instrumentCode));
    }

    public TsetmcMarketModels.ClientTypeResult getClientType(String instrumentCode, int clientTypeParam1, int clientTypeParam2) {
        return mapper.toClientType(client.getClientType(instrumentCode, clientTypeParam1, clientTypeParam2));
    }

    public TsetmcMarketModels.ClosingPriceDailyResult getClosingPriceDaily(String instrumentCode, int days) {
        return mapper.toClosingPriceDaily(client.getClosingPriceDaily(instrumentCode, days));
    }

    public TsetmcMarketModels.ClosingPriceChartDataResult getClosingPriceChartData(String instrumentCode, String period) {
        return mapper.toClosingPriceChartData(client.getClosingPriceChartData(instrumentCode, period));
    }

    public TsetmcMarketModels.InstrumentInfoResult getInstrumentInfo(String instrumentCode) {
        return mapper.toInstrumentInfo(client.getInstrumentInfo(instrumentCode));
    }

    public TsetmcMarketModels.RelatedCompanyResult getRelatedCompanies(String sectorCode) {
        return mapper.toRelatedCompanies(client.getRelatedCompanies(sectorCode));
    }

    public TsetmcMarketModels.CodalNoticesResult getCodalByInstrument(String instrumentCode, int limit) {
        return mapper.toCodalNotices(client.getCodalByInstrument(limit, instrumentCode));
    }

    public TsetmcMarketModels.CodalNoticesResult getCodalLatest(int limit) {
        return mapper.toCodalNotices(client.getCodalLatest(limit));
    }

    public TsetmcMarketModels.MarketMessagesResult getMarketMessagesByFlow(int flow, int limit) {
        return mapper.toMarketMessages(client.getMarketMessagesByFlow(flow, limit));
    }

    public TsetmcMarketModels.InstrumentStateTopResult getInstrumentStateTop(int limit) {
        return mapper.toInstrumentStateTop(client.getInstrumentStateTop(limit));
    }

    public TsetmcMarketModels.TradesResult getTrades(String instrumentCode) {
        return mapper.toTrades(client.getTrades(instrumentCode));
    }

    public TsetmcMarketModels.MarketMessagesResult getMarketMessagesByInstrument(String instrumentCode) {
        return mapper.toMarketMessages(client.getMarketMessagesByInstrument(instrumentCode));
    }

    public TsetmcMarketModels.EtfInfoResult getEtfInfo(String instrumentCode) {
        return mapper.toEtfInfo(client.getEtfByInsCode(instrumentCode));
    }

    public TsetmcMarketModels.CodalStatementContentResult getCodalStatementContentByInstrument(
            int reportType,
            int reportSubType,
            int pageId,
            String instrumentCode
    ) {
        return mapper.toCodalStatementContent(
                client.getCodalStatementContentByInstrument(reportType, reportSubType, pageId, instrumentCode)
        );
    }
}
