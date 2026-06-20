package com.ernoxin.bourseapi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.time.Duration;
import java.util.stream.Stream;

@ConfigurationProperties(prefix = "cache")
public record CacheProperties(
        boolean enabled,
        long defaultTtlMs,
        String keyPrefix,
        @NestedConfigurationProperty CacheNames names,
        @NestedConfigurationProperty CacheTtls ttls
) {
    public Duration ttlFor(String cacheName) {
        return Duration.ofMillis(ttlMsFor(cacheName));
    }

    public long ttlMsFor(String cacheName) {
        if (cacheName.equals(names().marketOverview())) {
            return ttls().marketOverviewMs();
        }
        if (cacheName.equals(names().selectedIndexes())) {
            return ttls().selectedIndexesMs();
        }
        if (cacheName.equals(names().instrumentEffects())) {
            return ttls().instrumentEffectsMs();
        }
        if (cacheName.equals(names().mostVisited())) {
            return ttls().mostVisitedMs();
        }
        if (cacheName.equals(names().closingPrice())) {
            return ttls().closingPriceMs();
        }
        if (cacheName.equals(names().bestLimits())) {
            return ttls().bestLimitsMs();
        }
        if (cacheName.equals(names().clientType())) {
            return ttls().clientTypeMs();
        }
        if (cacheName.equals(names().closingPriceDaily())) {
            return ttls().closingPriceDailyMs();
        }
        if (cacheName.equals(names().closingPriceChart())) {
            return ttls().closingPriceChartMs();
        }
        if (cacheName.equals(names().instrumentInfo())) {
            return ttls().instrumentInfoMs();
        }
        if (cacheName.equals(names().relatedCompanies())) {
            return ttls().relatedCompaniesMs();
        }
        if (cacheName.equals(names().codalByInstrument())) {
            return ttls().codalByInstrumentMs();
        }
        if (cacheName.equals(names().codalLatest())) {
            return ttls().codalLatestMs();
        }
        if (cacheName.equals(names().marketMessagesByFlow())) {
            return ttls().marketMessagesByFlowMs();
        }
        if (cacheName.equals(names().instrumentStateTop())) {
            return ttls().instrumentStateTopMs();
        }
        if (cacheName.equals(names().trades())) {
            return ttls().tradesMs();
        }
        if (cacheName.equals(names().marketMessagesByInstrument())) {
            return ttls().marketMessagesByInstrumentMs();
        }
        if (cacheName.equals(names().etfInfo())) {
            return ttls().etfInfoMs();
        }
        if (cacheName.equals(names().codalStatementContent())) {
            return ttls().codalStatementContentMs();
        }
        return defaultTtlMs();
    }

    public Stream<String> allCacheNames() {
        return Stream.of(
                names().marketOverview(),
                names().selectedIndexes(),
                names().instrumentEffects(),
                names().mostVisited(),
                names().closingPrice(),
                names().bestLimits(),
                names().clientType(),
                names().closingPriceDaily(),
                names().closingPriceChart(),
                names().instrumentInfo(),
                names().relatedCompanies(),
                names().codalByInstrument(),
                names().codalLatest(),
                names().marketMessagesByFlow(),
                names().instrumentStateTop(),
                names().trades(),
                names().marketMessagesByInstrument(),
                names().etfInfo(),
                names().codalStatementContent()
        ).distinct();
    }

    public record CacheNames(
            String marketOverview,
            String selectedIndexes,
            String instrumentEffects,
            String mostVisited,
            String closingPrice,
            String bestLimits,
            String clientType,
            String closingPriceDaily,
            String closingPriceChart,
            String instrumentInfo,
            String relatedCompanies,
            String codalByInstrument,
            String codalLatest,
            String marketMessagesByFlow,
            String instrumentStateTop,
            String trades,
            String marketMessagesByInstrument,
            String etfInfo,
            String codalStatementContent
    ) {
    }

    public record CacheTtls(
            long marketOverviewMs,
            long selectedIndexesMs,
            long instrumentEffectsMs,
            long mostVisitedMs,
            long closingPriceMs,
            long bestLimitsMs,
            long clientTypeMs,
            long closingPriceDailyMs,
            long closingPriceChartMs,
            long instrumentInfoMs,
            long relatedCompaniesMs,
            long codalByInstrumentMs,
            long codalLatestMs,
            long marketMessagesByFlowMs,
            long instrumentStateTopMs,
            long tradesMs,
            long marketMessagesByInstrumentMs,
            long etfInfoMs,
            long codalStatementContentMs
    ) {
    }
}
