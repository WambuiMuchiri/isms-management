package com.isms.model;

import java.util.Date;

public class PaymentReceipt_Report {

	private int paymentId;
	private Date date;
	private String clientName;
	private String telNo;
	private Double amountPaid;
	private String remarks;
	private String balanceBF;
	private String balanceCF;
	
	public PaymentReceipt_Report() {
		
	}

	public PaymentReceipt_Report(int paymentId, Date date, String clientName, String telNo, Double amountPaid,
			String remarks, String balanceBF, String balanceCF) {
		super();
		this.paymentId = paymentId;
		this.date = date;
		this.clientName = clientName;
		this.telNo = telNo;
		this.amountPaid = amountPaid;
		this.remarks = remarks;
		this.balanceBF = balanceBF;
		this.balanceCF = balanceCF;
	}

	public PaymentReceipt_Report(int paymentId, Date date, String clientName, String telNo, Double amountPaid,
			String remarks) {
		super();
		this.paymentId = paymentId;
		this.date = date;
		this.clientName = clientName;
		this.telNo = telNo;
		this.amountPaid = amountPaid;
		this.remarks = remarks;
	}

	public int getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(int paymentId) {
		this.paymentId = paymentId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getTelNo() {
		return telNo;
	}

	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}

	public Double getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(Double amountPaid) {
		this.amountPaid = amountPaid;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getBalanceBF() {
		return balanceBF;
	}

	public void setBalanceBF(String balanceBF) {
		this.balanceBF = balanceBF;
	}

	public String getBalanceCF() {
		return balanceCF;
	}

	public void setBalanceCF(String balanceCF) {
		this.balanceCF = balanceCF;
	}

	@Override
	public String toString() {
		return "PaymentReceipt_Report [paymentId=" + paymentId + ", date=" + date + ", clientName=" + clientName
				+ ", telNo=" + telNo + ", amountPaid=" + amountPaid + ", balanceBF=" + balanceBF + ", balanceCF="
				+ balanceCF + "]";
	}


}
