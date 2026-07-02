package com.ernoxin.bourseapi.service;

import com.ernoxin.bourseapi.common.util.JalaliDateTimeFormatter;
import com.ernoxin.bourseapi.domain.TsetmcMarketReportModels;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.ernoxin.bourseapi.service.TsetmcJsonNodeSupport.*;

@Component
class TsetmcCodalDataMapper {

    TsetmcMarketReportModels.CodalNoticesResult toCodalNotices(JsonNode root) {
        List<TsetmcMarketReportModels.CodalNotice> notices = new ArrayList<>();
        for (JsonNode noticeNode : root.path("preparedData")) {
            notices.add(new TsetmcMarketReportModels.CodalNotice(
                    longOrNull(noticeNode, "id"),
                    textOrNull(noticeNode, "symbol"),
                    textOrNull(noticeNode, "name"),
                    textOrNull(noticeNode, "title"),
                    textOrNull(noticeNode, "publishDateTime_Gregorian"),
                    JalaliDateTimeFormatter.gregorianDevenToJalaliDeven(intOrNull(noticeNode, "publishDateTime_DEven")),
                    intOrNull(noticeNode, "hasHtmlReport"),
                    intOrNull(noticeNode, "hasExcelReport"),
                    intOrNull(noticeNode, "hasPDFReport"),
                    intOrNull(noticeNode, "hasXMLReport"),
                    textOrNull(noticeNode, "tracingNo")
            ));
        }
        return new TsetmcMarketReportModels.CodalNoticesResult(notices);
    }

    TsetmcMarketReportModels.CodalStatementContentResult toCodalStatementContent(JsonNode root) {
        List<TsetmcMarketReportModels.CodalStatementContentItem> statementContents = new ArrayList<>();
        for (JsonNode statementNode : root.path("statemetnContent")) {
            statementContents.add(new TsetmcMarketReportModels.CodalStatementContentItem(
                    textOrNull(statementNode, "title"),
                    textOrNull(statementNode, "sentDateTime_Gregorian"),
                    textOrNull(statementNode, "publishDateTime_Gregorian"),
                    intOrNull(statementNode, "publishDateTime_DEven"),
                    intOrNull(statementNode, "reportSubType"),
                    intOrNull(statementNode, "pageID"),
                    rawTextOrNull(statementNode, "content")
            ));
        }
        return new TsetmcMarketReportModels.CodalStatementContentResult(statementContents);
    }
}
