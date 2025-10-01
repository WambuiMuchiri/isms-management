package com.isms.model;

import java.io.Serializable;
import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

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
import jakarta.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "assignments", catalog = "isms_db", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Assignment.findAll", query = "SELECT a FROM Assignment a"),
    @NamedQuery(name = "Assignment.findById", query = "SELECT a FROM Assignment a WHERE a.id = :id")})

@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
public class Assignment implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "assignment_date", nullable = false)
    private Date assignmentDate;
    
    @Column(name = "remarks", nullable = true, length = 1000)
    private String remarks;
    
    @JoinColumn(name = "installation_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    @JsonIgnore
    private Installations installation;
    
    @JoinColumn(name = "personnel_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    @JsonIgnore
    private Personnels personnel;
    
    @JoinColumn(name = "equipment_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    @JsonIgnore
    private Equipments equipment;
    
    @JoinColumn(name = "audit_identifier_id", referencedColumnName = "identifier_id", nullable = false)
    @ManyToOne(optional = false)
    @JsonIgnore
    private AuditIdentifier auditIdentifierId;
    
    public Assignment() {
    }
    
    public Assignment(Date assignmentDate, String remarks, Installations installation, Personnels personnel, Equipments equipment) {
        this.assignmentDate = assignmentDate;
        this.remarks = remarks;
        this.installation = installation;
        this.personnel = personnel;
        this.equipment = equipment;
    }
    
    public Assignment(int id, Date assignmentDate, String remarks, Installations installation, Personnels personnel, Equipments equipment) {
        this.id = id;
        this.assignmentDate = assignmentDate;
        this.remarks = remarks;
        this.installation = installation;
        this.personnel = personnel;
        this.equipment = equipment;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public Date getAssignmentDate() {
        return assignmentDate;
    }
    
    public void setAssignmentDate(Date assignmentDate) {
        this.assignmentDate = assignmentDate;
    }
    
    public String getRemarks() {
        return remarks;
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    public Installations getInstallation() {
        return installation;
    }
    
    public void setInstallation(Installations installation) {
        this.installation = installation;
    }
    
    public Personnels getPersonnel() {
        return personnel;
    }
    
    public void setPersonnel(Personnels personnel) {
        this.personnel = personnel;
    }
    
    public Equipments getEquipment() {
		return equipment;
	}

	public void setEquipment(Equipments equipment) {
		this.equipment = equipment;
	}

	public AuditIdentifier getAuditIdentifierId() {
        return auditIdentifierId;
    }
    
    public void setAuditIdentifierId(AuditIdentifier auditIdentifierId) {
        this.auditIdentifierId = auditIdentifierId;
    }
    
    @Override
    public String toString() {
        return "InstallationPersonnel [id=" + id + ", installation=" + installation.getInstallationName() 
            + ", personnel=" + personnel.getPersonnelName() + "]";
    }
}