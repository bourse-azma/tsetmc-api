package com.ernoxin.boorsapi.service;

import com.ernoxin.boorsapi.domain.TsetmcMarketModels;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TsetmcMarketService {

    private final TsetmcMarketFetchService fetchService;

    @Cacheable(cacheResolver = "tsetmcCacheResolver", key = "#marketId", sync = true)
    public TsetmcMarketModels.MarketOverviewResult getMarketOverview(int marketId) {
        return fetchService.getMarketOverview(marketId);
    }

    @Cacheable(cacheResolver = "tsetmcCacheResolver", key = "#marketId", sync = true)
    public TsetmcMarketModels.SelectedIndexesResult getSelectedIndexes(int marketId) {
        return fetchService.getSelectedIndexes(marketId);
    }

    @Cacheable(
            cacheResolver = "tsetmcCacheResolver",
            key = "T(com.ernoxin.boorsapi.cache.TsetmcCacheKeys).marketIdAndLimit(#marketId, #limit)",
            sync = true
    )
    public TsetmcMarketModels.InstrumentEffectsResult getInstrumentEffects(int marketId, int limit) {
        return fetchService.getInstrumentEffects(marketId, limit);
    }

    @Cacheable(
            cacheResolver = "tsetmcCacheResolver",
            key = "T(com.ernoxin.boorsapi.cache.TsetmcCacheKeys).marketIdAndLimit(#marketId, #limit)",
            sync = true
    )
    public TsetmcMarketModels.MostVisitedResult getMostVisited(int marketId, int limit) {
        return fetchService.getMostVisited(marketId, limit);
    }

    @Cacheable(
            cacheResolver = "tsetmcCacheResolver",
            key = "T(com.ernoxin.boorsapi.cache.TsetmcCacheKeys).instrumentCode(#instrumentCode)",
            sync = true
    )
    public TsetmcMarketModels.ClosingPriceInfoResult getClosingPriceInfo(String instrumentCode) {
        return fetchService.getClosingPriceInfo(instrumentCode);
    }

    @Cacheable(
            cacheResolver = "tsetmcCacheResolver",
            key = "T(com.ernoxin.boorsapi.cache.TsetmcCacheKeys).instrumentCode(#instrumentCode)",
            sync = true
    )
    public TsetmcMarketModels.BestLimitsResult getBestLimits(String instrumentCode) {
        return fetchService.getBestLimits(instrumentCode);
    }

    @Cacheable(
            cacheResolver = "tsetmcCacheResolver",
            key = "T(com.ernoxin.boorsapi.cache.TsetmcCacheKeys).clientType(#instrumentCode, #clientTypeParam1, #clientTypeParam2)",
            sync = true
    )
    public TsetmcMarketModels.ClientTypeResult getClientType(String instrumentCode, int clientTypeParam1, int clientTypeParam2) {
        return fetchService.getClientType(instrumentCode, clientTypeParam1, clientTypeParam2);
    }

    @Cacheable(
            cacheResolver = "tsetmcCacheResolver",
            key = "T(com.ernoxin.boorsapi.cache.TsetmcCacheKeys).instrumentCodeAndDays(#instrumentCode, #days)",
            sync = true
    )
    public TsetmcMarketModels.ClosingPriceDailyResult getClosingPriceDaily(String instrumentCode, int days) {
        return fetchService.getClosingPriceDaily(instrumentCode, days);
    }

    @Cacheable(
            cacheResolver = "tsetmcCacheResolver",
            key = "T(com.ernoxin.boorsapi.cache.TsetmcCacheKeys).instrumentCodeAndPeriod(#instrumentCode, #period)",
            sync = true
    )
    public TsetmcMarketModels.ClosingPriceChartDataResult getClosingPriceChartData(String instrumentCode, String period) {
        return fetchService.getClosingPriceChartData(instrumentCode, period);
    }

    @Cacheable(
            cacheResolver = "tsetmcCacheResolver",
            key = "T(com.ernoxin.boorsapi.cache.TsetmcCacheKeys).instrumentCode(#instrumentCode)",
            sync = true
    )
    public TsetmcMarketModels.InstrumentInfoResult getInstrumentInfo(String instrumentCode) {
        return fetchService.getInstrumentInfo(instrumentCode);
    }

    @Cacheable(
            cacheResolver = "tsetmcCacheResolver",
            key = "T(com.ernoxin.boorsapi.cache.TsetmcCacheKeys).sectorCode(#sectorCode)",
            sync = true
    )
    public TsetmcMarketModels.RelatedCompanyResult getRelatedCompanies(String sectorCode) {
        return fetchService.getRelatedCompanies(sectorCode);
    }

    @Cacheable(
            cacheResolver = "tsetmcCacheResolver",
            key = "T(com.ernoxin.boorsapi.cache.TsetmcCacheKeys).instrumentCodeAndLimit(#instrumentCode, #limit)",
            sync = true
    )
    public TsetmcMarketModels.CodalNoticesResult getCodalByInstrument(String instrumentCode, int limit) {
        return fetchService.getCodalByInstrument(instrumentCode, limit);
    }

    @Cacheable(cacheResolver = "tsetmcCacheResolver", key = "#limit", sync = true)
    public TsetmcMarketModels.CodalNoticesResult getCodalLatest(int limit) {
        return fetchService.getCodalLatest(limit);
    }

    @Cacheable(
            cacheResolver = "tsetmcCacheResolver",
            key = "T(com.ernoxin.boorsapi.cache.TsetmcCacheKeys).flowAndLimit(#flow, #limit)",
            sync = true
    )
    public TsetmcMarketModels.MarketMessagesResult getMarketMessagesByFlow(int flow, int limit) {
        return fetchService.getMarketMessagesByFlow(flow, limit);
    }

    @Cacheable(cacheResolver = "tsetmcCacheResolver", key = "#limit", sync = true)
    public TsetmcMarketModels.InstrumentStateTopResult getInstrumentStateTop(int limit) {
        return fetchService.getInstrumentStateTop(limit);
    }

    @Cacheable(
            cacheResolver = "tsetmcCacheResolver",
            key = "T(com.ernoxin.boorsapi.cache.TsetmcCacheKeys).instrumentCode(#instrumentCode)",
            sync = true
    )
    public TsetmcMarketModels.TradesResult getTrades(String instrumentCode) {
        return fetchService.getTrades(instrumentCode);
    }

    @Cacheable(
            cacheResolver = "tsetmcCacheResolver",
            key = "T(com.ernoxin.boorsapi.cache.TsetmcCacheKeys).instrumentCode(#instrumentCode)",
            sync = true
    )
    public TsetmcMarketModels.MarketMessagesResult getMarketMessagesByInstrument(String instrumentCode) {
        return fetchService.getMarketMessagesByInstrument(instrumentCode);
    }

    @Cacheable(
            cacheResolver = "tsetmcCacheResolver",
            key = "T(com.ernoxin.boorsapi.cache.TsetmcCacheKeys).instrumentCode(#instrumentCode)",
            sync = true
    )
    public TsetmcMarketModels.EtfInfoResult getEtfInfo(String instrumentCode) {
        return fetchService.getEtfInfo(instrumentCode);
    }

    @Cacheable(
            cacheResolver = "tsetmcCacheResolver",
            key = "T(com.ernoxin.boorsapi.cache.TsetmcCacheKeys).codalStatementContent(#reportType, #reportSubType, #pageId, #instrumentCode)",
            sync = true
    )
    public TsetmcMarketModels.CodalStatementContentResult getCodalStatementContentByInstrument(
            int reportType,
            int reportSubType,
            int pageId,
            String instrumentCode
    ) {
        return fetchService.getCodalStatementContentByInstrument(reportType, reportSubType, pageId, instrumentCode);
    }
}
