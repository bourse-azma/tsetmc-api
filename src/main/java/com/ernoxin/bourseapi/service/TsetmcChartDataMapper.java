package com.ernoxin.bourseapi.service;

import com.ernoxin.bourseapi.common.util.JalaliDateTimeFormatter;
import com.ernoxin.bourseapi.domain.TsetmcMarketModels;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.ernoxin.bourseapi.service.TsetmcJsonNodeSupport.*;

@Component
class TsetmcChartDataMapper {

    private static final DateTimeFormatter CHART_EVENT_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss")
            .withZone(ZoneOffset.UTC);

    TsetmcMarketModels.ClosingPriceDailyResult toClosingPriceDaily(JsonNode root) {
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

    TsetmcMarketModels.ClosingPriceChartDataResult toClosingPriceChartData(JsonNode root) {
        List<TsetmcMarketModels.ClosingPriceChartDataItem> chartData = new ArrayList<>();
        for (JsonNode chartNode : root.path("closingPriceChartData")) {
            chartData.add(new TsetmcMarketModels.ClosingPriceChartDataItem(
                    chartEventDateOrNull(chartNode, "dEven"),
                    firstDoubleOrNull(chartNode, "pDrCotVal", "pClosing"),
                    doubleOrNull(chartNode, "qTotTran5J"),
                    doubleOrNull(chartNode, "priceFirst"),
                    doubleOrNull(chartNode, "priceMin"),
                    doubleOrNull(chartNode, "priceMax"),
                    null,
                    null
            ));
        }
        return new TsetmcMarketModels.ClosingPriceChartDataResult(chartData);
    }

    TsetmcMarketModels.ClosingPriceChartDataResult dailyListToChartData(JsonNode root) {
        List<TsetmcMarketModels.ClosingPriceChartDataItem> chartData = new ArrayList<>();
        for (JsonNode dailyNode : root.path("closingPriceDaily")) {
            Integer deven = intOrNull(dailyNode, "dEven");
            if (deven == null) {
                continue;
            }
            int gregorianDeven = JalaliDateTimeFormatter.normalizeCompactDateToGregorianDeven(deven);
            chartData.add(new TsetmcMarketModels.ClosingPriceChartDataItem(
                    String.format("%08d 00:00:00", gregorianDeven),
                    firstDoubleOrNull(dailyNode, "pDrCotVal", "pClosing"),
                    doubleOrNull(dailyNode, "qTotTran5J"),
                    doubleOrNull(dailyNode, "priceFirst"),
                    doubleOrNull(dailyNode, "priceMin"),
                    doubleOrNull(dailyNode, "priceMax"),
                    null,
                    null
            ));
        }
        return new TsetmcMarketModels.ClosingPriceChartDataResult(chartData);
    }

    private String chartEventDateOrNull(JsonNode node, String field) {
        JsonNode fieldNode = node.get(field);
        if (fieldNode == null || fieldNode.isNull()) {
            return null;
        }

        long eventDate = fieldNode.asLong();
        if (eventDate >= 10_000_000 && eventDate <= 99_999_999) {
            int gregorianDeven = JalaliDateTimeFormatter.normalizeCompactDateToGregorianDeven((int) eventDate);
            return String.format("%08d 00:00:00", gregorianDeven);
        }

        return CHART_EVENT_DATE_FORMATTER.format(Instant.ofEpochSecond(eventDate));
    }
}
