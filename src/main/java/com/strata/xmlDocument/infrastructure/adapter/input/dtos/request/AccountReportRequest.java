package com.strata.xmlDocument.infrastructure.adapter.input.dtos.request;

import lombok.Data;
import java.util.List;

@Data
public class AccountReportRequest {
    // Group Header
    private String messageRecipientName;
    private String messageRecipientBIC;
    private String originalQueryMessageId;
    private String originalQueryMessageType;
    private String originalQueryCreationDateTime;

    // Report
    private String reportId;
    private String fromDateTime;
    private String toDateTime;

    // Account
    private String accountNumber;
    private String accountCurrency;
    private String schemeCode;
    private String proprietaryScheme;
    private String accountServicerBIC;
    private String accountServicerMemberId;

    // Balance
    private String balanceType;
    private String balanceAmount;
    private String balanceCurrency;
    private String creditDebitIndicator;
    private String balanceDateTime;

    // Entries
    private List<EntryRequest> entries;

    @Data
    public static class EntryRequest {
        private String amount;
        private String currency;
        private String creditDebitIndicator;
        private String status;
        private String bookingDate;
        private String valueDate;
        private String accountServicerReference;
        private String domainCode;
        private String familyCode;
        private String subFamilyCode;
        private String instructedAgentBIC;
    }
}
