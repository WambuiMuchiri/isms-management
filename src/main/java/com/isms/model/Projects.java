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
@Table(name = "projects", catalog = "isms_db", schema = "", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"projectName"})})
@XmlRootElement
@NamedQueries({
	@NamedQuery(name ="Projects.findAll", query = "SELECT c FROM Projects c"),
	@NamedQuery(name = "Projects.findById", query = "SELECT c FROM Projects c WHERE c.id = :id"),
	@NamedQuery(name = "Projects.findByProjectName", query = "SELECT c FROM Projects c WHERE c.projectName = :projectName")})

@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="id")
public class Projects implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "project_name", nullable = false)
	private String projectName;
		
	@Column(name = "project_cost", nullable = false)
	private Double projectCost;
	
	@Column(name = "description", nullable = false, length = 1000)
	private String description;
	
	@Column(name = "remarks", nullable = true, length = 1000)
	private String remarks;
	

    @JoinColumn(name = "client_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    @JsonIgnore
    private Clients clients;
    
    @JoinColumn(name = "audit_identifier_id", referencedColumnName = "identifier_id", nullable = false)
    @ManyToOne(optional = false)
    @JsonIgnore
    private AuditIdentifier auditIdentifierId;

    
	public Projects() {
		
	}


	public Projects(int id, String projectName, Double projectCost, String description, String remarks,
			Clients clients) {
		super();
		this.id = id;
		this.projectName = projectName;
		this.projectCost = projectCost;
		this.description = description;
		this.remarks = remarks;
		this.clients = clients;
	}


	public Projects(String projectName, Double projectCost, String description, String remarks, Clients clients) {
		super();
		this.projectName = projectName;
		this.projectCost = projectCost;
		this.description = description;
		this.remarks = remarks;
		this.clients = clients;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getProjectName() {
		return projectName;
	}


	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}


	public Double getProjectCost() {
		return projectCost;
	}


	public void setProjectCost(Double projectCost) {
		this.projectCost = projectCost;
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


	@Override
	public String toString() {
		return "Projects [projectName=" + projectName + "]";
	}
	
	
	
	
	
    
    
}
