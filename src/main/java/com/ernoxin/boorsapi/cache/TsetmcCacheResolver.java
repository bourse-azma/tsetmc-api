package com.ernoxin.boorsapi.cache;

import com.ernoxin.boorsapi.config.CacheProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component("tsetmcCacheResolver")
@RequiredArgsConstructor
public class TsetmcCacheResolver implements CacheResolver {

    private final CacheManager cacheManager;
    private final CacheProperties cacheProperties;

    @Override
    public Collection<? extends Cache> resolveCaches(CacheOperationInvocationContext<?> context) {
        Cache cache = cacheManager.getCache(resolveCacheName(context.getMethod().getName()));
        if (cache == null) {
            throw new IllegalStateException("Cache not found for method: " + context.getMethod().getName());
        }
        return List.of(cache);
    }

    private String resolveCacheName(String methodName) {
        return switch (methodName) {
            case "getMarketOverview" -> cacheProperties.names().marketOverview();
            case "getSelectedIndexes" -> cacheProperties.names().selectedIndexes();
            case "getInstrumentEffects" -> cacheProperties.names().instrumentEffects();
            case "getMostVisited" -> cacheProperties.names().mostVisited();
            case "getClosingPriceInfo" -> cacheProperties.names().closingPrice();
            case "getBestLimits" -> cacheProperties.names().bestLimits();
            case "getClientType" -> cacheProperties.names().clientType();
            case "getClosingPriceDaily" -> cacheProperties.names().closingPriceDaily();
            case "getClosingPriceChartData" -> cacheProperties.names().closingPriceChart();
            case "getInstrumentInfo" -> cacheProperties.names().instrumentInfo();
            case "getRelatedCompanies" -> cacheProperties.names().relatedCompanies();
            case "getCodalByInstrument" -> cacheProperties.names().codalByInstrument();
            case "getCodalLatest" -> cacheProperties.names().codalLatest();
            case "getMarketMessagesByFlow" -> cacheProperties.names().marketMessagesByFlow();
            case "getInstrumentStateTop" -> cacheProperties.names().instrumentStateTop();
            case "getTrades" -> cacheProperties.names().trades();
            case "getMarketMessagesByInstrument" -> cacheProperties.names().marketMessagesByInstrument();
            case "getEtfInfo" -> cacheProperties.names().etfInfo();
            case "getCodalStatementContentByInstrument" -> cacheProperties.names().codalStatementContent();
            default -> throw new IllegalStateException("Unsupported cached method: " + methodName);
        };
    }
}
