package com.isms.model;

import java.io.Serializable;

import jakarta.persistence.Basic;
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
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "clients", catalog = "isms_db", schema = "", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "email_address" }) })
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "Clients.findAll", query = "SELECT c FROM Clients c"),
		@NamedQuery(name = "Clients.findById", query = "SELECT c FROM Clients c WHERE c.id = :id"),
		@NamedQuery(name = "Clients.findByClientName", query = "SELECT c FROM Clients c WHERE c.clientName = :clientName"),
		@NamedQuery(name = "Clients.findByTelNo", query = "SELECT c FROM Clients c WHERE c.telNo = :telNo"),
		@NamedQuery(name = "Clients.findByEmailAddress", query = "SELECT c FROM Clients c WHERE c.emailAddress = :emailAddress"), })
public class Clients implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(nullable = false)
	private int id;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 255)
	@Column(name = "client_name", nullable = false, length = 255)
	private String clientName;

	@Basic(optional = false)
	@Size(min = 1, max = 400)
	@Column(name = "email_address", length = 400)
	private String emailAddress;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 100)
	@Column(name = "tel_no", nullable = false, length = 100)
	private String telNo;

	@JoinColumn(name = "location_id", referencedColumnName = "id", nullable = false)
	@ManyToOne(optional = false)
	private Locations locations;

	@JoinColumn(name = "audit_identifier_id", referencedColumnName = "identifier_id", nullable = false)
	@ManyToOne(optional = false)
	private AuditIdentifier auditIdentifierId;

	public Clients() {

	}

	public Clients(int id) {
		super();
		this.id = id;
	}

	public Clients(int id, @NotNull @Size(min = 1, max = 400) String clientName,
			@NotNull @Size(min = 1, max = 400) String emailAddress, @NotNull @Size(min = 1, max = 400) String telNo,
			Locations locations) {
		super();
		this.id = id;
		this.clientName = clientName;
		this.emailAddress = emailAddress;
		this.telNo = telNo;
		this.locations = locations;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getTelNo() {
		return telNo;
	}

	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}

	public Locations getLocations() {
		return locations;
	}

	public void setLocations(Locations locations) {
		this.locations = locations;
	}

	public AuditIdentifier getAuditIdentifierId() {
		return auditIdentifierId;
	}

	public void setAuditIdentifierId(AuditIdentifier auditIdentifierId) {
		this.auditIdentifierId = auditIdentifierId;
	}

	// check out
	@Override
	public String toString() {
		return "Clients[ id=" + id + " ]";
	}

}
