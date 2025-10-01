package com.isms.model;

import java.util.Date;

public class Commissions_Report {

	private Date recordDate;
	private Double jobItem;
	private Double payment;
	private Double cashIn;
	private Double cashOut;
	private Double dayCommission;
	private Double receivedCommission;
	private Double paidCommission;
	
	public Commissions_Report() {
		
	}

	public Commissions_Report(Date recordDate, Double jobItem, Double payment, Double cashIn, Double cashOut,
			Double dayCommission, Double receivedCommission, Double paidCommission) {
		super();
		this.recordDate = recordDate;
		this.jobItem = jobItem;
		this.payment = payment;
		this.cashIn = cashIn;
		this.cashOut = cashOut;
		this.dayCommission = dayCommission;
		this.receivedCommission = receivedCommission;
		this.paidCommission = paidCommission;
	}

	public Date getRecordDate() {
		return recordDate;
	}

	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}

	public Double getJobItem() {
		return jobItem;
	}

	public void setJobItem(Double jobItem) {
		this.jobItem = jobItem;
	}

	public Double getPayment() {
		return payment;
	}

	public void setPayment(Double payment) {
		this.payment = payment;
	}

	public Double getCashIn() {
		return cashIn;
	}

	public void setCashIn(Double cashIn) {
		this.cashIn = cashIn;
	}

	public Double getCashOut() {
		return cashOut;
	}

	public void setCashOut(Double cashOut) {
		this.cashOut = cashOut;
	}

	public Double getDayCommission() {
		return dayCommission;
	}

	public void setDayCommission(Double dayCommission) {
		this.dayCommission = dayCommission;
	}

	public Double getReceivedCommission() {
		return receivedCommission;
	}

	public void setReceivedCommission(Double receivedCommission) {
		this.receivedCommission = receivedCommission;
	}

	public Double getPaidCommission() {
		return paidCommission;
	}

	public void setPaidCommission(Double paidCommission) {
		this.paidCommission = paidCommission;
	}
		
}
