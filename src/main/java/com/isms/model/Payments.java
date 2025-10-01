package com.isms.model;

import java.io.Serializable;
import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import jakarta.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "payments", catalog = "isms_db", schema = "", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "receipt_no" }) })
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "Payments.findAll", query = "SELECT p FROM Payments p"),
		@NamedQuery(name = "Payments.findById", query = "SELECT p FROM Payments p WHERE p.id = :id"),
		@NamedQuery(name = "Payments.findByPaymentDate", query = "SELECT p FROM Payments p WHERE p.paymentDate = :paymentDate"),
		@NamedQuery(name = "Payments.findByAmountPaid", query = "SELECT p FROM Payments p WHERE p.amountPaid = :amountPaid"),
		@NamedQuery(name = "Payments.findByPaidBy", query = "SELECT p FROM Payments p WHERE p.paidBy = :paidBy"),
		@NamedQuery(name = "Payments.findByPaymentProof", query = "SELECT p FROM Payments p WHERE p.paymentProof = :paymentProof") })

public class Payments implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "payment_date", nullable = false, length = 200)
	private Date paymentDate;

	@Column(name = "amount_paid", nullable = false, length = 100)
	private Double amountPaid;

	@Column(name = "paid_by", nullable = false, length = 255)
	private String paidBy;

	@Column(name = "receipt_no", nullable = false, length = 50)
	private String receiptNo;

	@Column(name = "payment_proof", nullable = true, length = 255)
	private String paymentProof;

	@JoinColumn(name = "client_id", referencedColumnName = "id", nullable = false)
	@ManyToOne(optional = false)
	private Clients clients;

	@JoinColumn(name = "audit_identifier_id", referencedColumnName = "identifier_id", nullable = false)
	@ManyToOne(optional = false)
	private AuditIdentifier auditIdentifierId;

	public Payments() {

	}

	public Payments(int id, Date paymentDate, Double amountPaid, String paidBy, String paymentProof, Clients clients) {
		super();
		this.id = id;
		this.paymentDate = paymentDate;
		this.amountPaid = amountPaid;
		this.paidBy = paidBy;
		this.paymentProof = paymentProof;
		this.clients = clients;
	}

	public Payments(Date paymentDate, Double amountPaid, String paidBy, String paymentProof, Clients clients) {
		super();
		this.paymentDate = paymentDate;
		this.amountPaid = amountPaid;
		this.paidBy = paidBy;
		this.paymentProof = paymentProof;
		this.clients = clients;
	}
	

	public Payments(int id, Date paymentDate, Double amountPaid, String paidBy, String receiptNo, String paymentProof,
			Clients clients) {
		super();
		this.id = id;
		this.paymentDate = paymentDate;
		this.amountPaid = amountPaid;
		this.paidBy = paidBy;
		this.receiptNo = receiptNo;
		this.paymentProof = paymentProof;
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

	public Clients getClients() {
		return clients;
	}

	public void setClients(Clients clients) {
		this.clients = clients;
	}

	public AuditIdentifier getAuditIdentifierId() {
		return auditIdentifierId;
	}

	public void setAuditIdentifierId(AuditIdentifier auditIdentifierId) {
		this.auditIdentifierId = auditIdentifierId;
	}

	@Transient
	public String getPaymentProofPath() {
		if (paymentProof == null || id == 0)
			return null;
		return "/payment-proofs/" + id + "/" + paymentProof;
	}

	// To String
	@Override
	public String toString() {
		return "Payments [id=" + id + "]";
	}

}
