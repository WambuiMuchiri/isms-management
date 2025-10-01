package com.isms.model;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Lob;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Entity
@Table(name = "b2c_entries", catalog = "isms_db", schema = "", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"transactionId"}),
    @UniqueConstraint(columnNames = {"conversationId"}),
    @UniqueConstraint(columnNames = {"originatorConversationId"})
})
@Data
public class B2C_C2B_Entries implements Serializable  {

	private static final long serialVersionUID = 1l;
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int internalId;

//    @Column(name = "transaction_type", nullable = false)
    private String transactionType;

    @Column(unique = true)
    private String transactionId;

    private String billRefNumber;

    private String msisdn;

    private String amount;

    @Column(unique = true)
    private String conversationId;

    @Column(unique = true)
    private String originatorConversationId;

    private Date entryDate;

    private String resultCode;

    @Lob
    private String rawCallbackPayloadResponse;
}