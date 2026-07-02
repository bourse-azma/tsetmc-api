package com.ernoxin.bourseapi.domain;

import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public final class TsetmcMarketReportModels {

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
