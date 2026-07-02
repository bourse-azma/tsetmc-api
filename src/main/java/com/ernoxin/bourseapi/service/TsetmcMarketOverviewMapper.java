package com.ernoxin.bourseapi.service;

import com.ernoxin.bourseapi.domain.TsetmcMarketModels;
import com.ernoxin.bourseapi.domain.TsetmcMarketReportModels;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.ernoxin.bourseapi.service.TsetmcJsonNodeSupport.*;

@Component
class TsetmcMarketOverviewMapper {

    TsetmcMarketModels.MarketOverviewResult toMarketOverview(JsonNode root) {
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

    TsetmcMarketModels.SelectedIndexesResult toSelectedIndexes(JsonNode root) {
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

    TsetmcMarketModels.InstrumentEffectsResult toInstrumentEffects(JsonNode root) {
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

    TsetmcMarketModels.MostVisitedResult toMostVisited(JsonNode root) {
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

    TsetmcMarketReportModels.MarketMessagesResult toMarketMessages(JsonNode root) {
        List<TsetmcMarketReportModels.MarketMessageItem> messages = new ArrayList<>();
        for (JsonNode messageNode : root.path("msg")) {
            messages.add(new TsetmcMarketReportModels.MarketMessageItem(
                    longOrNull(messageNode, "tseMsgIdn"),
                    intOrNull(messageNode, "dEven"),
                    intOrNull(messageNode, "hEven"),
                    textOrNull(messageNode, "tseTitle"),
                    textOrNull(messageNode, "tseDesc"),
                    intOrNull(messageNode, "flow")
            ));
        }
        return new TsetmcMarketReportModels.MarketMessagesResult(messages);
    }

    TsetmcMarketReportModels.InstrumentStateTopResult toInstrumentStateTop(JsonNode root) {
        List<TsetmcMarketReportModels.InstrumentStateItem> instrumentStates = new ArrayList<>();
        for (JsonNode stateNode : root.path("instrumentState")) {
            instrumentStates.add(new TsetmcMarketReportModels.InstrumentStateItem(
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
        return new TsetmcMarketReportModels.InstrumentStateTopResult(instrumentStates);
    }
}
