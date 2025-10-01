package com.isms.model;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "payment_job_items", catalog = "isms_db", schema = "")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name ="PaymentJobItem.findAll", query = "SELECT pj FROM PaymentJobItem pj"),
	@NamedQuery(name = "PaymentJobItem.findByClientId", query = "SELECT pj FROM PaymentJobItem pj WHERE pj.clientId = :clientId"),
	@NamedQuery(name = "PaymentJobItem.findByPersonnelId", query = "SELECT pj FROM PaymentJobItem pj WHERE pj.personnelId = :personnelId"),
	@NamedQuery(name = "PaymentJobItem.findByPaymentId", query = "SELECT pj FROM PaymentJobItem pj WHERE pj.paymentId = :paymentId"),
	@NamedQuery(name = "PaymentJobItem.findByJobItemId", query = "SELECT pj FROM PaymentJobItem pj WHERE pj.jobItemId = :jobItemId"),
	@NamedQuery(name = "PaymentJobItem.findByCashInId", query = "SELECT pj FROM PaymentJobItem pj WHERE pj.cashInId = :cashInId"),
	@NamedQuery(name = "PaymentJobItem.findByCashOutId", query = "SELECT pj FROM PaymentJobItem pj WHERE pj.cashOutId = :cashOutId")})

public class PaymentJobItem implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "record_date", nullable = false, length = 200)
	private Date recordDate;
	
	@Column(name = "client_id", nullable = true, length = 200)
	private int clientId;
	
	@Column(name = "personnel_id", nullable = true, length = 200)
	private int personnelId;
	
	@Column(name = "payment_id", nullable = false, length = 200)
	private int paymentId;
	
	@Column(name = "job_item_id", nullable = false, length = 200)
	private int jobItemId;
	
	@Column(name = "cash_in_id", nullable = false, length = 200)
	private int cashInId;
	
	@Column(name = "cash_out_id", nullable = false, length = 200)
	private int cashOutId;
	
	@Column(name = "amount", nullable = false, length = 200)
	private double amount;

	
	//Constructors
	public PaymentJobItem() {
		
	}


	public PaymentJobItem(int id, Date recordDate, int clientId, int personnelId, int paymentId, int jobItemId, int cashInId,
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


	public PaymentJobItem(int id, int clientId, int personnelId, int paymentId, int jobItemId, int cashInId, int cashOutId,
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
		return "PaymentJobItem [id=" + id + "]";
	}

}
