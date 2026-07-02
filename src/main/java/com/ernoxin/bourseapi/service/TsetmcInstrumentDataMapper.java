package com.ernoxin.bourseapi.service;

import com.ernoxin.bourseapi.common.util.JalaliDateTimeFormatter;
import com.ernoxin.bourseapi.domain.TsetmcMarketModels;
import com.ernoxin.bourseapi.domain.TsetmcMarketReportModels;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.ernoxin.bourseapi.service.TsetmcJsonNodeSupport.*;

@Component
class TsetmcInstrumentDataMapper {

    TsetmcMarketModels.ClosingPriceInfoResult toClosingPriceInfo(JsonNode root) {
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

    TsetmcMarketModels.BestLimitsResult toBestLimits(JsonNode root) {
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

    TsetmcMarketModels.ClientTypeResult toClientType(JsonNode root) {
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

    TsetmcMarketModels.InstrumentInfoResult toInstrumentInfo(JsonNode root) {
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

    TsetmcMarketModels.RelatedCompanyResult toRelatedCompanies(JsonNode root) {
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

    TsetmcMarketReportModels.TradesResult toTrades(JsonNode root) {
        List<TsetmcMarketReportModels.TradeItem> trades = new ArrayList<>();
        for (JsonNode tradeNode : root.path("trade")) {
            trades.add(new TsetmcMarketReportModels.TradeItem(
                    intOrNull(tradeNode, "nTran"),
                    intOrNull(tradeNode, "hEven"),
                    doubleOrNull(tradeNode, "qTitTran"),
                    doubleOrNull(tradeNode, "pTran"),
                    intOrNull(tradeNode, "canceled")
            ));
        }
        return new TsetmcMarketReportModels.TradesResult(trades);
    }

    TsetmcMarketReportModels.EtfInfoResult toEtfInfo(JsonNode root) {
        JsonNode etfNode = root.path("etf");
        Integer deven = intOrNull(etfNode, "deven");
        Integer hEven = intOrNull(etfNode, "hEven");
        return new TsetmcMarketReportModels.EtfInfoResult(
                textOrNull(etfNode, "insCode"),
                JalaliDateTimeFormatter.formatGregorianDevenHeven(deven, hEven),
                doubleOrNull(etfNode, "pRedTran"),
                doubleOrNull(etfNode, "pSubTran"),
                intOrNull(etfNode, "iClose")
        );
    }
}
