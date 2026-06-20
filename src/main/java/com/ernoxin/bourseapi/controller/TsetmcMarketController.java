package com.ernoxin.bourseapi.controller;

import com.ernoxin.bourseapi.common.api.ApiResponse;
import com.ernoxin.bourseapi.domain.TsetmcMarketModels;
import com.ernoxin.bourseapi.service.TsetmcMarketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TsetmcMarketController {

    private final TsetmcMarketService service;

    @GetMapping("/overview/{marketId}")
    public ApiResponse<TsetmcMarketModels.MarketOverviewResult> getMarketOverview(
            @PathVariable int marketId) {
        return ApiResponse.success(service.getMarketOverview(marketId));
    }

    @GetMapping("/indexes/selected/{marketId}")
    public ApiResponse<TsetmcMarketModels.SelectedIndexesResult> getSelectedIndexes(
            @PathVariable int marketId) {
        return ApiResponse.success(service.getSelectedIndexes(marketId));
    }

    @GetMapping("/indexes/instrument-effects/{marketId}/{limit}")
    public ApiResponse<TsetmcMarketModels.InstrumentEffectsResult> getInstrumentEffects(
            @PathVariable int marketId,
            @PathVariable int limit) {
        return ApiResponse.success(service.getInstrumentEffects(marketId, limit));
    }

    @GetMapping("/instruments/most-visited/{marketId}/{limit}")
    public ApiResponse<TsetmcMarketModels.MostVisitedResult> getMostVisited(
            @PathVariable int marketId,
            @PathVariable int limit) {
        return ApiResponse.success(service.getMostVisited(marketId, limit));
    }

    @GetMapping("/instruments/{instrumentCode}/closing-price")
    public ApiResponse<TsetmcMarketModels.ClosingPriceInfoResult> getClosingPriceInfo(
            @PathVariable String instrumentCode) {
        return ApiResponse.success(service.getClosingPriceInfo(instrumentCode));
    }

    @GetMapping("/instruments/{instrumentCode}/best-limits")
    public ApiResponse<TsetmcMarketModels.BestLimitsResult> getBestLimits(
            @PathVariable String instrumentCode) {
        return ApiResponse.success(service.getBestLimits(instrumentCode));
    }

    @GetMapping("/instruments/{instrumentCode}/client-type/{clientTypeParam1}/{clientTypeParam2}")
    public ApiResponse<TsetmcMarketModels.ClientTypeResult> getClientType(
            @PathVariable String instrumentCode,
            @PathVariable int clientTypeParam1,
            @PathVariable int clientTypeParam2) {
        return ApiResponse.success(service.getClientType(instrumentCode, clientTypeParam1, clientTypeParam2));
    }

    @GetMapping("/instruments/{instrumentCode}/closing-prices/daily/{days}")
    public ApiResponse<TsetmcMarketModels.ClosingPriceDailyResult> getClosingPriceDaily(
            @PathVariable String instrumentCode,
            @PathVariable int days) {
        return ApiResponse.success(service.getClosingPriceDaily(instrumentCode, days));
    }

    @GetMapping("/instruments/{instrumentCode}/closing-prices/chart/{period}")
    public ApiResponse<TsetmcMarketModels.ClosingPriceChartDataResult> getClosingPriceChartData(
            @PathVariable String instrumentCode,
            @PathVariable String period) {
        return ApiResponse.success(service.getClosingPriceChartData(instrumentCode, period));
    }

    @GetMapping("/instruments/{instrumentCode}/info")
    public ApiResponse<TsetmcMarketModels.InstrumentInfoResult> getInstrumentInfo(
            @PathVariable String instrumentCode) {
        return ApiResponse.success(service.getInstrumentInfo(instrumentCode));
    }

    @GetMapping("/sectors/{sectorCode}/related-companies")
    public ApiResponse<TsetmcMarketModels.RelatedCompanyResult> getRelatedCompanies(
            @PathVariable String sectorCode) {
        return ApiResponse.success(service.getRelatedCompanies(sectorCode));
    }

    @GetMapping({"/codal/instruments/{instrumentCode}/{limit}", "/codal/instruments/{instrumentCode}/notices/{limit}"})
    public ApiResponse<TsetmcMarketModels.CodalNoticesResult> getCodalByInstrument(
            @PathVariable String instrumentCode,
            @PathVariable int limit) {
        return ApiResponse.success(service.getCodalByInstrument(instrumentCode, limit));
    }

    @GetMapping("/codal/latest-notices/{limit}")
    public ApiResponse<TsetmcMarketModels.CodalNoticesResult> getCodalLatest(
            @PathVariable int limit) {
        return ApiResponse.success(service.getCodalLatest(limit));
    }

    @GetMapping({"/messages/{flow}/{limit}", "/messages/flow/{flow}/{limit}"})
    public ApiResponse<TsetmcMarketModels.MarketMessagesResult> getMarketMessagesByFlow(
            @PathVariable int flow,
            @PathVariable int limit) {
        return ApiResponse.success(service.getMarketMessagesByFlow(flow, limit));
    }

    @GetMapping("/instruments/{instrumentCode}/messages")
    public ApiResponse<TsetmcMarketModels.MarketMessagesResult> getMarketMessagesByInstrument(
            @PathVariable String instrumentCode) {
        return ApiResponse.success(service.getMarketMessagesByInstrument(instrumentCode));
    }

    @GetMapping("/instrument-states/top-list/{limit}")
    public ApiResponse<TsetmcMarketModels.InstrumentStateTopResult> getInstrumentStateTop(
            @PathVariable int limit) {
        return ApiResponse.success(service.getInstrumentStateTop(limit));
    }

    @GetMapping("/instruments/{instrumentCode}/trades")
    public ApiResponse<TsetmcMarketModels.TradesResult> getTrades(
            @PathVariable String instrumentCode) {
        return ApiResponse.success(service.getTrades(instrumentCode));
    }

    @GetMapping("/instruments/{instrumentCode}/etf-info")
    public ApiResponse<TsetmcMarketModels.EtfInfoResult> getEtfInfo(
            @PathVariable String instrumentCode) {
        return ApiResponse.success(service.getEtfInfo(instrumentCode));
    }

    @GetMapping("/codal/instruments/{instrumentCode}/statement-content/{reportType}/{reportSubType}/{pageId}")
    public ApiResponse<TsetmcMarketModels.CodalStatementContentResult> getCodalStatementContentByInstrument(
            @PathVariable String instrumentCode,
            @PathVariable int reportType,
            @PathVariable int reportSubType,
            @PathVariable int pageId) {
        return ApiResponse.success(service.getCodalStatementContentByInstrument(reportType, reportSubType, pageId, instrumentCode));
    }
}
