package com.isms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.isms.model.B2C_C2B_Entries;


public interface B2CC2BEntriesRepository extends JpaRepository<B2C_C2B_Entries, Integer>, JpaSpecificationExecutor<B2C_C2B_Entries> {

	// Find Record By ConversationID or OriginatorConversationID ...
    B2C_C2B_Entries findByConversationIdOrOriginatorConversationId(String conversationId, String originatorConversationId);

    // Find Transaction By TransactionId ....
    B2C_C2B_Entries findByTransactionId(String transactionId);

    B2C_C2B_Entries findByBillRefNumber(String billRefNumber);
}
