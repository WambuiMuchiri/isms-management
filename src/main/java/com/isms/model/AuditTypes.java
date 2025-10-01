package com.isms.model;

import java.io.Serializable;
import java.util.Collection;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

@Entity(name = "AuditTypes")
@Table(name = "audit_types", catalog = "isms_db", schema = "", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AuditTypes.findAll", query = "SELECT a FROM AuditTypes a"),
    @NamedQuery(name = "AuditTypes.findById", query = "SELECT a FROM AuditTypes a WHERE a.id = :id"),
    @NamedQuery(name = "AuditTypes.findByName", query = "SELECT a FROM AuditTypes a WHERE a.name = :name")})
public class AuditTypes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private long id;

    @Basic(optional = false)
    @Column(nullable = false, length = 300)
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "auditTypeId")
    private Collection<AuditEvents> auditEventsCollection;

    public AuditTypes() {
    }

    public AuditTypes(long id) {
        this.id = id;
    }

    public AuditTypes(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public Collection<AuditEvents> getAuditEventsCollection() {
        return auditEventsCollection;
    }

    public void setAuditEventsCollection(Collection<AuditEvents> auditEventsCollection) {
        this.auditEventsCollection = auditEventsCollection;
    }

    @Override
    public String toString() {
        return "com.cradletechnologies.houses.model.AuditTypes[ id=" + id + " ]";
    }

}

