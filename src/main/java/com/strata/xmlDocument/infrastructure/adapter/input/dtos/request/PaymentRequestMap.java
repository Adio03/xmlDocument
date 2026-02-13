package com.strata.xmlDocument.infrastructure.adapter.input.dtos.request;

import lombok.Data;

@Data
public class PaymentRequestMap {
    private String instructedAgentBic;
    private String instructingAgentBic;
    private String debtorName;
    private String debtorAccountIban;
    private String debtorAccountName;
    private String creditorName;
    private String creditorAccountIban;
    private String creditorAccountName;
    private String interbankSettlementAmount;
    private String interbankSettlementCurrency;
    private String interbankSettlementDate;
    private String chargesBearer;
    private String clearingChannel;
    private String serviceLevel;
    private String localInstrument;
    private String categoryPurpose;
    private String debtorInstructionInfo;
    private String remittanceInformation;
    private String debtorAccountDesignation;
    private String debtorIdType;//bvn
    private String debtorIdValue;//bvn number
    private String debtorAccountTier;
    private String creditorAccountDesignation;
    private String creditorIdType;
    private String creditorIdValue;
    private String creditorAccountTier;
    private String transactionLocation;
    private String accountDesignation;
}
