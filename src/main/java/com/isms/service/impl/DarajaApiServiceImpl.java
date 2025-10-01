package com.isms.service.impl;

import static com.isms.helper.Constants.AUTHORIZATION_HEADER_STRING;
import static com.isms.helper.Constants.BASIC_AUTH_STRING;
import static com.isms.helper.Constants.BEARER_AUTH_STRING;
import static com.isms.helper.Constants.CACHE_CONTROL_HEADER;
import static com.isms.helper.Constants.CACHE_CONTROL_HEADER_VALUE;
import static com.isms.helper.Constants.JSON_MEDIA_TYPE;
import static com.isms.helper.Constants.SHORT_CODE_IDENTIFIER;
import static com.isms.helper.Constants.TRANSACTION_STATUS_QUERY_COMMAND;
import static com.isms.helper.Constants.TRANSACTION_STATUS_VALUE;

import java.io.IOException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.isms.filter.MobileMoneyDataFilter;
import com.isms.repository.B2CC2BEntriesRepository;
import com.isms.security.MpesaConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isms.dto.AccessTokenResponse;
import com.isms.dto.B2CTransactionRequest;
import com.isms.dto.CheckAccountBalanceRequest;
import com.isms.dto.CommonSyncResponse;
import com.isms.dto.InternalB2CTransactionRequest;
import com.isms.dto.InternalTransactionStatusRequest;
import com.isms.dto.TransactionStatusRequest;
import com.isms.dto.TransactionStatusSyncResponse;
import com.isms.helper.Constants;
import com.isms.helper.HelperUtility;
import com.isms.model.B2C_C2B_Entries;
import com.isms.service.DarajaApiService;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
@Slf4j
public class DarajaApiServiceImpl implements DarajaApiService {
	
	private final MpesaConfiguration mpesaConfiguration;
	private final OkHttpClient okHttpClient;
	private final ObjectMapper objectMapper;
	

    public DarajaApiServiceImpl(MpesaConfiguration mpesaConfiguration, OkHttpClient okHttpClient, ObjectMapper objectMapper) {
        this.mpesaConfiguration = mpesaConfiguration;
        this.okHttpClient = okHttpClient;
        this.objectMapper = objectMapper;
    }
    
    @Autowired
    public B2CC2BEntriesRepository b2CC2BEntriesRepository; 

    /**
     * @return Returns Daraja API Access Token Response
     */
    @Override
    public AccessTokenResponse getAccessToken() {

        // get the Base64 rep of consumerKey + ":" + consumerSecret
        String encodedCredentials = HelperUtility.toBase64String(String.format("%s:%s", mpesaConfiguration.getConsumerKey(),
                mpesaConfiguration.getConsumerSecret()));
        log.info("Ecoded cred:::-----> ",encodedCredentials);
        Request request = new Request.Builder()
                .url(String.format("%s?grant_type=%s", mpesaConfiguration.getOauthEndpoint(), mpesaConfiguration.getGrantType()))
                .get()
                .addHeader(AUTHORIZATION_HEADER_STRING, String.format("%s %s", BASIC_AUTH_STRING, encodedCredentials))
                .addHeader(CACHE_CONTROL_HEADER, CACHE_CONTROL_HEADER_VALUE)
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            assert response.body() != null;

            // use Jackson to Decode the ResponseBody ...
            return objectMapper.readValue(response.body().string(), AccessTokenResponse.class);
        } catch (IOException e) {
            log.error(String.format("Could not get access token. -> %s", e.getLocalizedMessage()));
            return null;
        }
    }


    @Override
    public CommonSyncResponse performB2CTransaction(InternalB2CTransactionRequest internalB2CTransactionRequest) {
        AccessTokenResponse accessTokenResponse = getAccessToken();
        log.info(String.format("Access Token: %s", accessTokenResponse.getAccessToken()));

        B2CTransactionRequest b2CTransactionRequest = new B2CTransactionRequest();

        b2CTransactionRequest.setCommandID(internalB2CTransactionRequest.getCommandID());
        b2CTransactionRequest.setAmount(internalB2CTransactionRequest.getAmount());
        b2CTransactionRequest.setPartyB(internalB2CTransactionRequest.getPartyB());
        b2CTransactionRequest.setRemarks(internalB2CTransactionRequest.getRemarks());
        b2CTransactionRequest.setOccassion(internalB2CTransactionRequest.getOccassion());

        // get the security credentials ... 
        b2CTransactionRequest.setSecurityCredential(HelperUtility.getSecurityCredentials(mpesaConfiguration.getB2cInitiatorPassword()));
        
        log.info(String.format("Security Creds: %s", b2CTransactionRequest.getSecurityCredential()));

        // set the result url ...
        b2CTransactionRequest.setResultURL(mpesaConfiguration.getB2cResultUrl());
        b2CTransactionRequest.setQueueTimeOutURL(mpesaConfiguration.getB2cQueueTimeoutUrl());
        b2CTransactionRequest.setInitiatorName(mpesaConfiguration.getB2cInitiatorName());
        b2CTransactionRequest.setPartyA(mpesaConfiguration.getShortCode());

        RequestBody body = RequestBody.create(JSON_MEDIA_TYPE,
                Objects.requireNonNull(HelperUtility.toJson(b2CTransactionRequest)));
        System.out.println("hello---->"+HelperUtility.toJson(b2CTransactionRequest));
        Request request = new Request.Builder()
                .url(mpesaConfiguration.getB2cTransactionEndpoint())
                .post(body)
                .addHeader(AUTHORIZATION_HEADER_STRING, String.format("%s %s", BEARER_AUTH_STRING, accessTokenResponse.getAccessToken()))
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();

            assert response.body() != null;

            return objectMapper.readValue(response.body().string(), CommonSyncResponse.class);
        } catch (IOException e) {
            log.error(String.format("Could not perform B2C transaction ->%s", e.getLocalizedMessage()));
            return null;
        }

    }


    @Override
    public TransactionStatusSyncResponse getTransactionResult(InternalTransactionStatusRequest internalTransactionStatusRequest) {

        TransactionStatusRequest transactionStatusRequest = new TransactionStatusRequest();
        transactionStatusRequest.setTransactionID(internalTransactionStatusRequest.getTransactionID());

        transactionStatusRequest.setInitiator(mpesaConfiguration.getB2cInitiatorName());
        transactionStatusRequest.setSecurityCredential(HelperUtility.getSecurityCredentials(mpesaConfiguration.getB2cInitiatorPassword()));
        transactionStatusRequest.setCommandID(TRANSACTION_STATUS_QUERY_COMMAND);
        transactionStatusRequest.setPartyA(mpesaConfiguration.getShortCode());
        transactionStatusRequest.setIdentifierType(SHORT_CODE_IDENTIFIER);
        transactionStatusRequest.setResultURL(mpesaConfiguration.getB2cResultUrl());
        transactionStatusRequest.setQueueTimeOutURL(mpesaConfiguration.getB2cQueueTimeoutUrl());
        transactionStatusRequest.setRemarks(TRANSACTION_STATUS_VALUE);
        transactionStatusRequest.setOccasion(TRANSACTION_STATUS_VALUE);

        AccessTokenResponse accessTokenResponse = getAccessToken();

        RequestBody body = RequestBody.create(JSON_MEDIA_TYPE,
                Objects.requireNonNull(HelperUtility.toJson(transactionStatusRequest)));

        Request request = new Request.Builder()
                .url(mpesaConfiguration.getTransactionResultUrl())
                .post(body)
                .addHeader(AUTHORIZATION_HEADER_STRING, String.format("%s %s", BEARER_AUTH_STRING, accessTokenResponse.getAccessToken()))
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            assert response.body() != null;
            // use Jackson to Decode the ResponseBody ...

            return objectMapper.readValue(response.body().string(), TransactionStatusSyncResponse.class);
        } catch (IOException e) {
            log.error(String.format("Could not fetch transaction result -> %s", e.getLocalizedMessage()));
            return null;
        }


    }

    @Override
    public CommonSyncResponse checkAccountBalance() {

        CheckAccountBalanceRequest checkAccountBalanceRequest = new CheckAccountBalanceRequest();
        checkAccountBalanceRequest.setInitiator(mpesaConfiguration.getB2cInitiatorName());
        checkAccountBalanceRequest.setSecurityCredential(HelperUtility.getSecurityCredentials(mpesaConfiguration.getB2cInitiatorPassword()));
        checkAccountBalanceRequest.setCommandID(Constants.ACCOUNT_BALANCE_COMMAND);
        checkAccountBalanceRequest.setPartyA(mpesaConfiguration.getShortCode());
        checkAccountBalanceRequest.setIdentifierType(Constants.SHORT_CODE_IDENTIFIER);
        checkAccountBalanceRequest.setRemarks("Check Account Balance");
        checkAccountBalanceRequest.setQueueTimeOutURL(mpesaConfiguration.getB2cQueueTimeoutUrl());
        checkAccountBalanceRequest.setResultURL(mpesaConfiguration.getB2cResultUrl());

        AccessTokenResponse accessTokenResponse = getAccessToken();

        RequestBody body = RequestBody.create(JSON_MEDIA_TYPE,
                Objects.requireNonNull(HelperUtility.toJson(checkAccountBalanceRequest)));

        Request request = new Request.Builder()
                .url(mpesaConfiguration.getCheckAccountBalanceUrl())
                .post(body)
                .addHeader(AUTHORIZATION_HEADER_STRING, String.format("%s %s", BEARER_AUTH_STRING, accessTokenResponse.getAccessToken()))
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            assert response.body() != null;
            // use Jackson to Decode the ResponseBody ...

            return objectMapper.readValue(response.body().string(), CommonSyncResponse.class);
        } catch (IOException e) {
            log.error(String.format("Could not fetch the account balance -> %s", e.getLocalizedMessage()));
            return null;
        }

    }


	@Override
	public Page<B2C_C2B_Entries> getPaymentsDataForDatatable(String queryString, Pageable pageRequest) {
		MobileMoneyDataFilter moneyFilter = new MobileMoneyDataFilter(queryString);
		return b2CC2BEntriesRepository.findAll(moneyFilter, pageRequest);
	}


}
