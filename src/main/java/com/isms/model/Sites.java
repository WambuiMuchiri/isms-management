package com.isms.model;

import java.io.Serializable;

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
import jakarta.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "sites", catalog = "isms_db", schema = "", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"siteName"})})
@XmlRootElement
@NamedQueries({
	@NamedQuery(name ="Sites.findAll", query = "SELECT s FROM Sites s"),
	@NamedQuery(name = "Sites.findById", query = "SELECT s FROM Sites s WHERE s.id = :id"),
	@NamedQuery(name = "Sites.findBySiteName", query = "SELECT s FROM Sites s WHERE s.siteName = :siteName")})

@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="id")
public class Sites implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "site_name", nullable = true)
	private String siteName;
	
	@Column(name = "description", nullable = false, length = 1000)
	private String description;
	
	@Column(name = "remarks", nullable = true, length = 1000)
	private String remarks;
	

    @JoinColumn(name = "project_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    @JsonIgnore
    private Projects projects;
    
    @JoinColumn(name = "audit_identifier_id", referencedColumnName = "identifier_id", nullable = false)
    @ManyToOne(optional = false)
    @JsonIgnore
    private AuditIdentifier auditIdentifierId;

    
	public Sites() {
		
	}


	public Sites(int id, String siteName, String description, String remarks, Projects projects) {
		super();
		this.id = id;
		this.siteName = siteName;
		this.description = description;
		this.remarks = remarks;
		this.projects = projects;
	}


	public Sites(String siteName, String description, String remarks, Projects projects) {
		super();
		this.siteName = siteName;
		this.description = description;
		this.remarks = remarks;
		this.projects = projects;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getSiteName() {
		return siteName;
	}


	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getRemarks() {
		return remarks;
	}


	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}


	public Projects getProjects() {
		return projects;
	}


	public void setProjects(Projects projects) {
		this.projects = projects;
	}


	public AuditIdentifier getAuditIdentifierId() {
		return auditIdentifierId;
	}


	public void setAuditIdentifierId(AuditIdentifier auditIdentifierId) {
		this.auditIdentifierId = auditIdentifierId;
	}


	@Override
	public String toString() {
		return "Sites [siteName=" + siteName + "]";
	}
	
	
}
