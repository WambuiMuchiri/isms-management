package com.isms.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "mpesa.daraja")
public class MpesaConfiguration {

	private String consumerKey;
	private String consumerSecret;
	private String grantType;
	private String oauthEndpoint;
	private String shortCode;
	private String confirmationURL;
	private String validationURL;
	private String b2cTransactionEndpoint;
	private String b2cResultUrl;
	private String b2cQueueTimeoutUrl;
	private String b2cInitiatorName;
	private String b2cInitiatorPassword;
	private String transactionResultUrl;
    private String checkAccountBalanceUrl;

    @Override
    public String toString() {
        return String.format("{consumerKey='%s', consumerSecret='%s', grantType='%s', oauthEndpoint='%s'}",
                consumerKey, consumerSecret, grantType, oauthEndpoint);
    }

}
