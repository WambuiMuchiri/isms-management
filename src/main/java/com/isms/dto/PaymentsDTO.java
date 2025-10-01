package com.isms.dto;

import java.sql.Date;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class PaymentsDTO {

	private int id;
	private Date paymentDate;
	private Double amountPaid;
	private String paidBy;
	private String receiptNo;
	private String paymentProof;
	private Double balanceBF;
	private Double balanceCF;
	
//	@NotNull(message = "Client is required")
//	@Min(value = 1, message = "Client must be selected")
	private int clients;
	
	
	public PaymentsDTO() {
		
	}


	public PaymentsDTO(int id, Date paymentDate, Double amountPaid, String paidBy, String paymentProof, int clients) {
		super();
		this.id = id;
		this.paymentDate = paymentDate;
		this.amountPaid = amountPaid;
		this.paidBy = paidBy;
		this.paymentProof = paymentProof;
		this.clients = clients;
	}
	

	public PaymentsDTO(int id, Date paymentDate, Double amountPaid, String paidBy, int clients) {
		super();
		this.id = id;
		this.paymentDate = paymentDate;
		this.amountPaid = amountPaid;
		this.paidBy = paidBy;
		this.clients = clients;
	}


	public PaymentsDTO(Date paymentDate, Double amountPaid, String paidBy, String paymentProof, int clients) {
		super();
		this.paymentDate = paymentDate;
		this.amountPaid = amountPaid;
		this.paidBy = paidBy;
		this.paymentProof = paymentProof;
		this.clients = clients;
	}
	
	

	public PaymentsDTO(int id, Date paymentDate, Double amountPaid, String paidBy, String paymentProof,
			Double balanceBF, Double balanceCF, int clients) {
		super();
		this.id = id;
		this.paymentDate = paymentDate;
		this.amountPaid = amountPaid;
		this.paidBy = paidBy;
		this.paymentProof = paymentProof;
		this.balanceBF = balanceBF;
		this.balanceCF = balanceCF;
		this.clients = clients;
	}


	public PaymentsDTO(Date paymentDate, Double amountPaid, String paidBy, String paymentProof, Double balanceBF,
			Double balanceCF, int clients) {
		super();
		this.paymentDate = paymentDate;
		this.amountPaid = amountPaid;
		this.paidBy = paidBy;
		this.paymentProof = paymentProof;
		this.balanceBF = balanceBF;
		this.balanceCF = balanceCF;
		this.clients = clients;
	}
	
	
	public PaymentsDTO(int id, Date paymentDate, Double amountPaid, String paidBy, String receiptNo,
			String paymentProof, Double balanceBF, Double balanceCF, int clients) {
		super();
		this.id = id;
		this.paymentDate = paymentDate;
		this.amountPaid = amountPaid;
		this.paidBy = paidBy;
		this.receiptNo = receiptNo;
		this.paymentProof = paymentProof;
		this.balanceBF = balanceBF;
		this.balanceCF = balanceCF;
		this.clients = clients;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public Double getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(Double amountPaid) {
		this.amountPaid = amountPaid;
	}

	public String getPaidBy() {
		return paidBy;
	}

	public void setPaidBy(String paidBy) {
		this.paidBy = paidBy;
	}

	public String getReceiptNo() {
		return receiptNo;
	}

	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}

	public String getPaymentProof() {
		return paymentProof;
	}

	public void setPaymentProof(String paymentProof) {
		this.paymentProof = paymentProof;
	}

	public Double getBalanceBF() {
		return balanceBF;
	}

	public void setBalanceBF(Double balanceBF) {
		this.balanceBF = balanceBF;
	}

	public Double getBalanceCF() {
		return balanceCF;
	}

	public void setBalanceCF(Double balanceCF) {
		this.balanceCF = balanceCF;
	}

	public int getClients() {
		return clients;
	}

	public void setClients(int clients) {
		this.clients = clients;
	}

	@Transient
	public String getPaymentProofPath() {
		if(paymentProof == null || id == 0) return null;
		return "/payment-proofs/"+id+"/"+paymentProof;
	}


	@Override
	public String toString() {
		return "PaymentsDTO [id=" + id + "]";
	}



}
