package com.ernoxin.bourseapi.service;

import com.ernoxin.bourseapi.common.util.JalaliDateTimeFormatter;
import com.ernoxin.bourseapi.domain.TsetmcMarketModels;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ClosingPriceChartAggregationService {

    private static final String UPSTREAM_DAILY_PERIOD = "D";

    public TsetmcMarketModels.ClosingPriceChartDataResult aggregateForPeriod(
            TsetmcMarketModels.ClosingPriceChartDataResult dailyResult,
            String requestedPeriod
    ) {
        ChartPeriod period = parsePeriod(requestedPeriod);

        List<TsetmcMarketModels.ClosingPriceChartDataItem> dailyItems = deduplicateByDate(
                dailyResult.chartData() == null ? List.of() : dailyResult.chartData()
        );

        List<TsetmcMarketModels.ClosingPriceChartDataItem> aggregated = switch (period) {
            case DAILY -> dailyItems;
            case WEEKLY -> aggregateWeekly(dailyItems);
            case MONTHLY -> aggregateToBuckets(dailyItems, BucketMode.MONTHLY);
            case YEARLY -> aggregateToBuckets(dailyItems, BucketMode.YEARLY);
        };

        return new TsetmcMarketModels.ClosingPriceChartDataResult(aggregated);
    }

    public String upstreamDailyPeriod() {
        return UPSTREAM_DAILY_PERIOD;
    }

    /**
     * Unions chart and daily-list sources by trading day. Later items win on duplicate dates
     * (daily list is appended last and usually has the fuller history).
     */
    public TsetmcMarketModels.ClosingPriceChartDataResult mergeDailySources(
            TsetmcMarketModels.ClosingPriceChartDataResult chartDaily,
            TsetmcMarketModels.ClosingPriceChartDataResult listDaily
    ) {
        List<TsetmcMarketModels.ClosingPriceChartDataItem> combined = new ArrayList<>();
        if (chartDaily != null && chartDaily.chartData() != null) {
            combined.addAll(chartDaily.chartData());
        }
        if (listDaily != null && listDaily.chartData() != null) {
            combined.addAll(listDaily.chartData());
        }
        return new TsetmcMarketModels.ClosingPriceChartDataResult(deduplicateByDate(combined));
    }

    private ChartPeriod parsePeriod(String period) {
        if (period == null || period.isBlank()) {
            throw new IllegalArgumentException("Chart period is required");
        }

        return switch (period.trim().toUpperCase(Locale.ROOT)) {
            case "D", "1D" -> ChartPeriod.DAILY;
            case "W", "1W" -> ChartPeriod.WEEKLY;
            case "M", "1M" -> ChartPeriod.MONTHLY;
            case "12M" -> ChartPeriod.YEARLY;
            default -> throw new IllegalArgumentException("Unsupported chart period: " + period);
        };
    }

    private List<TsetmcMarketModels.ClosingPriceChartDataItem> deduplicateByDate(
            List<TsetmcMarketModels.ClosingPriceChartDataItem> items
    ) {
        Map<LocalDate, TsetmcMarketModels.ClosingPriceChartDataItem> byDate = new LinkedHashMap<>();
        List<SortableItem> sortable = new ArrayList<>();

        for (TsetmcMarketModels.ClosingPriceChartDataItem item : items) {
            LocalDate date = parseChartDate(item.eventDate());
            if (date == null) {
                continue;
            }
            sortable.add(new SortableItem(date, item));
        }

        sortable.sort(Comparator.comparing(sortableItem -> sortableItem.date));
        for (SortableItem sortableItem : sortable) {
            byDate.put(sortableItem.date, sortableItem.item);
        }

        return new ArrayList<>(byDate.values());
    }

    /**
     * Groups daily bars into consecutive 7-calendar-day windows anchored at the latest bar.
     * Bar time is the last trading day in the window; periodStartDate is the calendar start.
     */
    private List<TsetmcMarketModels.ClosingPriceChartDataItem> aggregateWeekly(
            List<TsetmcMarketModels.ClosingPriceChartDataItem> items
    ) {
        List<SortableItem> sorted = new ArrayList<>();
        for (TsetmcMarketModels.ClosingPriceChartDataItem item : items) {
            LocalDate date = parseChartDate(item.eventDate());
            if (date != null) {
                sorted.add(new SortableItem(date, item));
            }
        }
        sorted.sort(Comparator.comparing(sortableItem -> sortableItem.date));
        if (sorted.isEmpty()) {
            return List.of();
        }

        LocalDate anchor = sorted.get(sorted.size() - 1).date;
        Map<LocalDate, WeeklyBucket> buckets = new TreeMap<>();

        for (SortableItem item : sorted) {
            long daysFromAnchor = java.time.temporal.ChronoUnit.DAYS.between(item.date, anchor);
            if (daysFromAnchor < 0) {
                continue;
            }

            int bucketIndex = (int) (daysFromAnchor / 7);
            LocalDate periodEnd = anchor.minusDays(bucketIndex * 7L);
            LocalDate periodStart = periodEnd.minusDays(6);
            buckets.computeIfAbsent(periodStart, ignored -> new WeeklyBucket(periodStart, periodEnd))
                    .items.add(item);
        }

        List<TsetmcMarketModels.ClosingPriceChartDataItem> aggregated = new ArrayList<>();
        int bucketCount = buckets.size();
        int bucketIndex = 0;
        for (WeeklyBucket bucket : buckets.values()) {
            bucketIndex += 1;
            boolean currentPeriod = bucketIndex == bucketCount;
            List<SortableItem> bucketItems = bucket.items.stream()
                    .sorted(Comparator.comparing(sortableItem -> sortableItem.date))
                    .toList();
            LocalDate tradingEnd = bucketItems.get(bucketItems.size() - 1).date;
            aggregated.add(aggregateWeeklyBucket(tradingEnd, bucket.periodStart, bucketItems, currentPeriod));
        }

        return aggregated;
    }

    private List<TsetmcMarketModels.ClosingPriceChartDataItem> aggregateToBuckets(
            List<TsetmcMarketModels.ClosingPriceChartDataItem> items,
            BucketMode mode
    ) {
        Map<LocalDate, List<TsetmcMarketModels.ClosingPriceChartDataItem>> buckets = new LinkedHashMap<>();

        for (TsetmcMarketModels.ClosingPriceChartDataItem item : items) {
            LocalDate date = parseChartDate(item.eventDate());
            if (date == null) {
                continue;
            }

            LocalDate bucketStart = bucketStart(date, mode);
            buckets.computeIfAbsent(bucketStart, ignored -> new ArrayList<>()).add(item);
        }

        List<TsetmcMarketModels.ClosingPriceChartDataItem> aggregated = new ArrayList<>();
        buckets.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> aggregated.add(aggregateBucket(entry.getKey(), entry.getKey(), toSortable(entry.getValue()))));

        return aggregated;
    }

    private List<SortableItem> toSortable(List<TsetmcMarketModels.ClosingPriceChartDataItem> items) {
        List<SortableItem> sortable = new ArrayList<>();
        for (TsetmcMarketModels.ClosingPriceChartDataItem item : items) {
            LocalDate date = parseChartDate(item.eventDate());
            if (date != null) {
                sortable.add(new SortableItem(date, item));
            }
        }
        return sortable;
    }

    private TsetmcMarketModels.ClosingPriceChartDataItem aggregateBucket(
            LocalDate periodEnd,
            LocalDate periodStart,
            List<SortableItem> bucketItems
    ) {
        return buildAggregatedItem(periodEnd, periodStart, bucketItems, false, false);
    }

    private TsetmcMarketModels.ClosingPriceChartDataItem aggregateWeeklyBucket(
            LocalDate periodEnd,
            LocalDate periodStart,
            List<SortableItem> bucketItems,
            boolean currentPeriod
    ) {
        return buildAggregatedItem(periodEnd, periodStart, bucketItems, true, currentPeriod);
    }

    private TsetmcMarketModels.ClosingPriceChartDataItem buildAggregatedItem(
            LocalDate periodEnd,
            LocalDate periodStart,
            List<SortableItem> bucketItems,
            boolean alwaysIncludePeriodStart,
            boolean currentPeriod
    ) {
        List<SortableItem> sorted = bucketItems.stream()
                .sorted(Comparator.comparing(sortableItem -> sortableItem.date))
                .toList();

        TsetmcMarketModels.ClosingPriceChartDataItem first = sorted.get(0).item;
        TsetmcMarketModels.ClosingPriceChartDataItem last = sorted.get(sorted.size() - 1).item;

        double open = coalesce(first.firstTradePrice(), first.lastTradePrice());
        double close = coalesce(last.lastTradePrice(), last.firstTradePrice());
        double high = sorted.stream()
                .mapToDouble(item -> coalesce(item.item.dayMaxPrice(), item.item.lastTradePrice(), item.item.firstTradePrice()))
                .filter(value -> value > 0)
                .max()
                .orElse(Math.max(open, close));
        double low = sorted.stream()
                .mapToDouble(item -> coalesce(item.item.dayMinPrice(), item.item.lastTradePrice(), item.item.firstTradePrice()))
                .filter(value -> value > 0)
                .min()
                .orElse(Math.min(open, close));
        double volume = sorted.stream()
                .mapToDouble(item -> item.item.tradeVolume() == null ? 0.0 : item.item.tradeVolume())
                .sum();

        String periodStartDate = alwaysIncludePeriodStart || !periodStart.equals(periodEnd)
                ? formatChartDate(periodStart)
                : null;

        return new TsetmcMarketModels.ClosingPriceChartDataItem(
                formatChartDate(periodEnd),
                close,
                volume,
                open,
                low,
                high,
                periodStartDate,
                currentPeriod
        );
    }

    private LocalDate bucketStart(LocalDate date, BucketMode mode) {
        return switch (mode) {
            case MONTHLY -> JalaliDateTimeFormatter.jalaliMonthStart(date);
            case YEARLY -> JalaliDateTimeFormatter.jalaliYearStart(date);
        };
    }

    private LocalDate parseChartDate(String eventDate) {
        return JalaliDateTimeFormatter.parseNormalizedChartEventDate(eventDate);
    }

    private String formatChartDate(LocalDate date) {
        return date.format(DateTimeFormatter.BASIC_ISO_DATE) + " 00:00:00";
    }

    private double coalesce(Double... values) {
        for (Double value : values) {
            if (value != null && Double.isFinite(value) && value > 0) {
                return value;
            }
        }
        return 0.0;
    }

    private enum ChartPeriod {
        DAILY,
        WEEKLY,
        MONTHLY,
        YEARLY
    }

    private enum BucketMode {
        MONTHLY,
        YEARLY
    }

    private record SortableItem(LocalDate date, TsetmcMarketModels.ClosingPriceChartDataItem item) {
    }

    private static final class WeeklyBucket {
        private final LocalDate periodStart;
        private final LocalDate periodEnd;
        private final List<SortableItem> items = new ArrayList<>();

        private WeeklyBucket(LocalDate periodStart, LocalDate periodEnd) {
            this.periodStart = periodStart;
            this.periodEnd = periodEnd;
        }
    }
}
