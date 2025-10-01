package com.isms.model;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "installations", catalog = "isms_db", schema = "", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"installation_name"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name ="Installations.findAll", query = "SELECT i FROM Installations i"),
    @NamedQuery(name = "Installations.findById", query = "SELECT i FROM Installations i WHERE i.id = :id"),
    @NamedQuery(name = "Installations.findByInstallationName", query = "SELECT i FROM Installations i WHERE i.installationName = :installationName")})
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="id")
public class Installations implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "installation_name", nullable = true)
    private String installationName;
    
    @Column(name = "description", nullable = false, length = 1000)
    private String description;
    
    @Column(name = "remarks", nullable = true, length = 1000)
    private String remarks;
    
    @JoinColumn(name = "site_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    @JsonIgnore
    private Sites sites;
    
    @JoinColumn(name = "audit_identifier_id", referencedColumnName = "identifier_id", nullable = false)
    @ManyToOne(optional = false)
    @JsonIgnore
    private AuditIdentifier auditIdentifierId;
    
    @OneToMany(mappedBy = "installation")
    @JsonIgnore
    private List<Assignment> installationPersonnels;
    
    public Installations() {
    	
    }
    
    public Installations(int id, String installationName, String description, String remarks, Sites sites) {
        super();
        this.id = id;
        this.installationName = installationName;
        this.description = description;
        this.remarks = remarks;
        this.sites = sites;
    }
    
    public Installations(String installationName, String description, String remarks, Sites sites) {
        super();
        this.installationName = installationName;
        this.description = description;
        this.remarks = remarks;
        this.sites = sites;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getInstallationName() {
        return installationName;
    }
    
    public void setInstallationName(String installationName) {
        this.installationName = installationName;
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
    
    public Sites getSites() {
        return sites;
    }
    
    public void setSites(Sites sites) {
        this.sites = sites;
    }
    
    public AuditIdentifier getAuditIdentifierId() {
        return auditIdentifierId;
    }
    
    public void setAuditIdentifierId(AuditIdentifier auditIdentifierId) {
        this.auditIdentifierId = auditIdentifierId;
    }
    
    @XmlTransient
    public List<Assignment> getInstallationPersonnels() {
        return installationPersonnels;
    }
    
    public void setInstallationPersonnels(List<Assignment> installationPersonnels) {
        this.installationPersonnels = installationPersonnels;
    }
    
    @Override
    public String toString() {
        return "Installations [installationName=" + installationName + "]";
    }
}