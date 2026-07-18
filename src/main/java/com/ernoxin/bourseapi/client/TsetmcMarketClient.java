package com.ernoxin.bourseapi.client;

import com.ernoxin.bourseapi.common.exception.UpstreamApiException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class TsetmcMarketClient {

    private final RestTemplate tsetmcRestTemplate;
    @Value("${external.tsetmc.retry-attempts:2}")
    private int retryAttempts;
    @Value("${external.tsetmc.retry-delay-ms:500}")
    private long retryDelayMs;

    public JsonNode getMarketOverview(int marketId) {
        return getJson("/MarketData/GetMarketOverview/" + marketId);
    }

    public JsonNode getSelectedIndexes(int marketId) {
        return getJson("/Index/GetIndexB1LastAll/SelectedIndexes/" + marketId);
    }

    public JsonNode getInstrumentEffects(int marketId, int limit) {
        return getJson("/Index/GetInstEffect/0/" + marketId + "/" + limit);
    }

    public JsonNode getMostVisited(int marketId, int limit) {
        return getJson("/ClosingPrice/GetTradeTop/MostVisited/" + marketId + "/" + limit);
    }

    public JsonNode getClosingPriceInfo(String instrumentCode) {
        return getJson("/ClosingPrice/GetClosingPriceInfo/" + instrumentCode);
    }

    public JsonNode getBestLimits(String instrumentCode) {
        return getJson("/BestLimits/" + instrumentCode);
    }

    public JsonNode getClientType(String instrumentCode, int clientTypeParam1, int clientTypeParam2) {
        return getJson("/ClientType/GetClientType/" + instrumentCode + "/" + clientTypeParam1 + "/" + clientTypeParam2);
    }

    public JsonNode getClosingPriceDaily(String instrumentCode, int days) {
        return getJson("/ClosingPrice/GetClosingPriceDailyList/" + instrumentCode + "/" + days);
    }

    public JsonNode getClosingPriceChartData(String instrumentCode, String period) {
        return getJson("/ClosingPrice/GetChartData/" + instrumentCode + "/" + period);
    }

    public JsonNode getInstrumentInfo(String instrumentCode) {
        return getJson("/Instrument/GetInstrumentInfo/" + instrumentCode);
    }

    public JsonNode getRelatedCompanies(String sectorCode) {
        return getJson("/ClosingPrice/GetRelatedCompany/" + sectorCode);
    }

    public JsonNode getCodalByInstrument(int limit, String instrumentCode) {
        return getJson("/Codal/GetPreparedDataByInsCode/" + limit + "/" + instrumentCode);
    }

    public JsonNode getCodalLatest(int limit) {
        return getJson("/Codal/GetPreparedData/" + limit);
    }

    public JsonNode getMarketMessagesByFlow(int flow, int limit) {
        return getJson("/Msg/GetMsgByFlow/" + flow + "/" + limit);
    }

    public JsonNode getInstrumentStateTop(int limit) {
        return getJson("/MarketData/GetInstrumentStateTop/" + limit);
    }

    public JsonNode getTrades(String instrumentCode) {
        return getJson("/Trade/GetTrade/" + instrumentCode);
    }

    public JsonNode getMarketMessagesByInstrument(String instrumentCode) {
        return getJson("/Msg/GetMsgByInsCode/" + instrumentCode);
    }

    public JsonNode getEtfByInsCode(String instrumentCode) {
        return getJson("/Fund/GetETFByInsCode/" + instrumentCode);
    }

    public JsonNode getCodalStatementContentByInstrument(int reportType, int reportSubType, int pageId, String instrumentCode) {
        return getJson("/Codal/GetStatementContentByInsCode/"
                + reportType + "/"
                + reportSubType + "/"
                + pageId + "/"
                + instrumentCode);
    }

    private JsonNode getJson(String path) {
        return getJson(path, Math.max(0, retryAttempts));
    }

    private JsonNode getJson(String path, int retriesLeft) {
        try {
            return tsetmcRestTemplate.getForObject(path, JsonNode.class);
        } catch (RestClientResponseException ex) {
            int statusCode = ex.getStatusCode().value();
            if (retriesLeft > 0 && (statusCode == 429 || statusCode >= 500)) {
                waitBeforeRetry();
                return getJson(path, retriesLeft - 1);
            }
            HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
            throw new UpstreamApiException(
                    "Upstream API returned status " + ex.getStatusCode().value() + ": " + ex.getStatusText(),
                    status != null ? status : HttpStatus.BAD_GATEWAY
            );
        } catch (RestClientException ex) {
            if (retriesLeft > 0) {
                waitBeforeRetry();
                return getJson(path, retriesLeft - 1);
            }
            throw new UpstreamApiException(
                    "Upstream API request failed: " + ex.getClass().getSimpleName() + " - " + ex.getMessage(),
                    HttpStatus.BAD_GATEWAY
            );
        }
    }

    private void waitBeforeRetry() {
        try {
            Thread.sleep(Math.max(0, retryDelayMs));
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new UpstreamApiException("Upstream API retry was interrupted", HttpStatus.BAD_GATEWAY);
        }
    }
}
