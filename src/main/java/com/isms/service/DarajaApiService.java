package com.isms.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.isms.dto.AccessTokenResponse;
import com.isms.dto.CommonSyncResponse;
import com.isms.dto.InternalB2CTransactionRequest;
import com.isms.dto.InternalTransactionStatusRequest;
import com.isms.dto.TransactionStatusSyncResponse;
import com.isms.model.B2C_C2B_Entries;

public interface DarajaApiService {

	AccessTokenResponse getAccessToken();

	CommonSyncResponse performB2CTransaction(InternalB2CTransactionRequest internalB2CTransactionRequest);
	
	CommonSyncResponse checkAccountBalance();
	
	TransactionStatusSyncResponse getTransactionResult(InternalTransactionStatusRequest internalTransactionStatusRequest);

	Page<B2C_C2B_Entries> getPaymentsDataForDatatable(String queryString, Pageable pageRequest);
}
