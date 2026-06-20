package com.ernoxin.bourseapi.client;

import com.ernoxin.bourseapi.common.exception.UpstreamApiException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class TsetmcMarketClient {

    private final RestTemplate tsetmcRestTemplate;

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
        try {
            return tsetmcRestTemplate.getForObject(path, JsonNode.class);
        } catch (RestClientResponseException ex) {
            HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
            throw new UpstreamApiException(
                    "Upstream API returned status " + ex.getStatusCode().value() + ": " + ex.getStatusText(),
                    status != null ? status : HttpStatus.BAD_GATEWAY
            );
        } catch (RestClientException ex) {
            throw new UpstreamApiException(
                    "Upstream API request failed: " + ex.getClass().getSimpleName() + " - " + ex.getMessage(),
                    HttpStatus.BAD_GATEWAY
            );
        }
    }
}
