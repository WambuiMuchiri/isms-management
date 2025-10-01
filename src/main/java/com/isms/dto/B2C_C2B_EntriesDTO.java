package com.isms.dto;

import java.util.Date;

import lombok.Data;

@Data
public class B2C_C2B_EntriesDTO {

    private int internalId;

    private String transactionType;

    private String transactionId;

    private String billRefNumber;

    private String msisdn;

    private String amount;

    private String conversationId;

    private String originatorConversationId;

    private Date entryDate;

    private String resultCode;

    private String rawCallbackPayloadResponse;
}