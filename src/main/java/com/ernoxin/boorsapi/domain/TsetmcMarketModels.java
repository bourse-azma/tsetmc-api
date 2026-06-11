package com.ernoxin.boorsapi.domain;

import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public final class TsetmcMarketModels {

    public record MarketOverviewResult(
            MarketOverview marketOverview) {
    }

    public record MarketOverview(
            Integer lastDataDate,
            Integer lastDataTime,
            Double totalIndexValue,
            Double totalIndexChange,
            Double equalWeightedIndexValue,
            Double equalWeightedIndexChange,
            Integer marketActivityDate,
            Integer marketActivityTime,
            Long totalTradeCount,
            Double totalTradeValue,
            Double totalTradeVolume,
            String marketStateCode,
            Double marketValue,
            Double marketValueBase,
            String marketStateTitle) {
    }

    public record SelectedIndexesResult(
            List<SelectedIndex> selectedIndexes) {
    }

    public record SelectedIndex(
            String indexCode,
            Integer eventDate,
            Integer eventTime,
            Double currentValue,
            Double dayHighValue,
            Double baseValue,
            Double changePercent,
            Double changeValue,
            String indexName,
            Boolean lastSnapshot,
            Integer auxiliaryFlag1,
            Integer auxiliaryFlag2,
            Integer auxiliaryFlag3,
            Integer auxiliaryFlag4) {
    }

    public record InstrumentEffectsResult(
            List<InstrumentEffect> instrumentEffects) {
    }

    public record InstrumentEffect(
            String instrumentCode,
            String symbol,
            String fullName,
            Double closingPrice,
            Double indexEffectValue) {
    }

    public record MostVisitedResult(
            List<MostVisitedInstrument> mostVisitedInstruments) {
    }

    public record MostVisitedInstrument(
            String instrumentCode,
            String symbol,
            String fullName,
            Integer eventDate,
            Integer eventTime,
            Double closingPrice,
            Double previousClosingPrice,
            Double priceChange,
            Double dayMinPrice,
            Double dayMaxPrice,
            Double firstTradePrice,
            Double tradeCount,
            Double tradeVolume,
            Double tradeValue,
            Double lastTradePrice) {
    }

    public record ClosingPriceInfoResult(
            InstrumentTradingState instrumentState,
            Double priceChange,
            Double dayMinPrice,
            Double dayMaxPrice,
            Double previousClosingPrice,
            Double firstTradePrice,
            String instrumentCode,
            Integer eventDate,
            Integer eventTime,
            Double closingPrice,
            Double lastTradePrice,
            Double tradeCount,
            Double tradeVolume,
            Double tradeValue) {
    }

    public record InstrumentTradingState(
            String stateCode,
            String stateTitle,
            Integer underSupervision) {
    }

    public record BestLimitsResult(
            List<OrderBookLevel> orderBookLevels) {
    }

    public record OrderBookLevel(
            Integer levelNumber,
            Double bidVolume,
            Integer bidOrderCount,
            Double bidPrice,
            Double askPrice,
            Integer askOrderCount,
            Double askVolume) {
    }

    public record ClientTypeResult(
            Double institutionalBuyVolume,
            Double individualBuyVolume,
            Double uncategorizedBuyVolume,
            Integer institutionalBuyCount,
            Integer individualBuyCount,
            Integer uncategorizedBuyCount,
            Double institutionalSellVolume,
            Double individualSellVolume,
            Integer institutionalSellCount,
            Integer individualSellCount) {
    }

    public record ClosingPriceDailyResult(
            List<ClosingPriceDailyItem> dailyPrices) {
    }

    public record ClosingPriceDailyItem(
            String instrumentCode,
            Integer eventDate,
            Double closingPrice,
            Double lastTradePrice,
            Double dayMinPrice,
            Double dayMaxPrice,
            Double firstTradePrice,
            Double previousClosingPrice,
            Double priceChange,
            Double tradeCount,
            Double tradeVolume,
            Double tradeValue) {
    }

    public record ClosingPriceChartDataResult(
            List<ClosingPriceChartDataItem> chartData) {
    }

    public record ClosingPriceChartDataItem(
            String eventDate,
            Double lastTradePrice,
            Double tradeVolume,
            Double firstTradePrice,
            Double dayMinPrice,
            Double dayMaxPrice) {
    }

    public record InstrumentInfoResult(
            String instrumentCode,
            String symbol,
            String fullName,
            String isin,
            SectorInfo sector,
            StaticPriceThreshold staticPriceThreshold,
            EpsInfo eps,
            Double baseVolume,
            Double totalShares,
            Integer marketFlowCode,
            String marketFlowTitle,
            String boardTitle,
            Integer instrumentId) {
    }

    public record EpsInfo(
            Double epsValue,
            Double estimatedEps,
            Double sectorPe,
            Double psr) {
    }

    public record SectorInfo(
            String sectorCode,
            String sectorName) {
    }

    public record StaticPriceThreshold(
            Double minAllowedPrice,
            Double maxAllowedPrice) {
    }

    public record RelatedCompanyResult(
            List<RelatedCompanyItem> relatedCompanies,
            List<RelatedCompanyHistoryItem> relatedCompanyThirtyDayHistory) {
    }

    public record RelatedCompanyItem(
            String instrumentCode,
            String symbol,
            String fullName,
            Double closingPrice,
            Double lastTradePrice,
            Double dayMinPrice,
            Double dayMaxPrice,
            Double priceChange,
            Double tradeCount,
            Double tradeVolume,
            Double tradeValue) {
    }

    public record RelatedCompanyHistoryItem(
            String instrumentCode,
            Integer eventDate,
            Double closingPrice,
            Double lastTradePrice,
            Double tradeCount,
            Double tradeVolume,
            Double tradeValue) {
    }

    public record CodalNoticesResult(
            List<CodalNotice> notices) {
    }

    public record CodalNotice(
            Long noticeId,
            String symbol,
            String companyName,
            String title,
            String publishedAtGregorian,
            Integer publishedDate,
            Integer htmlReportAvailabilityCode,
            Integer excelReportAvailabilityCode,
            Integer pdfReportAvailabilityCode,
            Integer xmlReportAvailabilityCode,
            String trackingNumber) {
    }

    public record MarketMessagesResult(
            List<MarketMessageItem> messages) {
    }

    public record MarketMessageItem(
            Long messageId,
            Integer eventDate,
            Integer eventTime,
            String title,
            String description,
            Integer flowCode) {
    }

    public record InstrumentStateTopResult(
            List<InstrumentStateItem> instrumentStates) {
    }

    public record InstrumentStateItem(
            Long stateId,
            Integer eventDate,
            Integer eventTime,
            String instrumentCode,
            String symbol,
            String fullName,
            String stateCode,
            Integer underSupervision,
            String stateTitle) {
    }

    public record TradesResult(
            List<TradeItem> trades) {
    }

    public record TradeItem(
            Integer tradeNumber,
            Integer eventTime,
            Double tradeVolume,
            Double tradePrice,
            Integer canceledFlag) {
    }

    public record CodalStatementContentResult(
            List<CodalStatementContentItem> statementContents) {
    }

    public record CodalStatementContentItem(
            String title,
            String sentAtGregorian,
            String publishedAtGregorian,
            Integer publishedDate,
            Integer reportSubType,
            Integer pageId,
            String contentXml) {
    }

    public record EtfInfoResult(
            String insCode,
            String navAnnouncementAt,
            Double cancelNav,
            Double issueNav,
            Integer iClose) {
    }
}
