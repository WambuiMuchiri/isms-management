package com.isms.dto;

import java.util.Date;

public class PaymentJobItemDTO {

	private int id;
	
	private Date recordDate;
	
	private int clientId;
	
	private int personnelId;
	
	private int paymentId;
	
	private int jobItemId;
	
	private int cashInId;
	
	private int cashOutId;
	
	private double amount;

	
	//Constructors
	public PaymentJobItemDTO() {
		
	}


	public PaymentJobItemDTO(int id, Date recordDate, int clientId, int personnelId, int paymentId, int jobItemId, int cashInId,
			int cashOutId, double amount) {
		super();
		this.id = id;
		this.recordDate = recordDate;
		this.clientId = clientId;
		this.personnelId = personnelId;
		this.paymentId = paymentId;
		this.jobItemId = jobItemId;
		this.cashInId = cashInId;
		this.cashOutId = cashOutId;
		this.amount = amount;
	}




	public PaymentJobItemDTO(int id, int clientId, int personnelId, int paymentId, int jobItemId, int cashInId, int cashOutId,
			double amount) {
		super();
		this.id = id;
		this.clientId = clientId;
		this.personnelId = personnelId;
		this.paymentId = paymentId;
		this.jobItemId = jobItemId;
		this.cashInId = cashInId;
		this.cashOutId = cashOutId;
		this.amount = amount;
	}




	//Getters and Setters
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public Date getRecordDate() {
		return recordDate;
	}


	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}


	public int getClientId() {
		return clientId;
	}


	public void setClientId(int clientId) {
		this.clientId = clientId;
	}


	public int getPersonnelId() {
		return personnelId;
	}


	public void setPersonnelId(int personnelId) {
		this.personnelId = personnelId;
	}


	public int getPaymentId() {
		return paymentId;
	}


	public void setPaymentId(int paymentId) {
		this.paymentId = paymentId;
	}


	public int getJobItemId() {
		return jobItemId;
	}


	public void setJobItemId(int jobItemId) {
		this.jobItemId = jobItemId;
	}


	public int getCashInId() {
		return cashInId;
	}


	public void setCashInId(int cashInId) {
		this.cashInId = cashInId;
	}


	public int getCashOutId() {
		return cashOutId;
	}


	public void setCashOutId(int cashOutId) {
		this.cashOutId = cashOutId;
	}

	
	public double getAmount() {
		return amount;
	}


	public void setAmount(double amount) {
		this.amount = amount;
	}


	
	//To String
	@Override
	public String toString() {
		return "PaymentJobItemDTO [id=" + id + "]";
	}

}
