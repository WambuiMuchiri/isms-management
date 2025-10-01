package com.isms.model;

import java.io.Serializable;

import jakarta.persistence.Basic;
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
@Table(name = "audit_identifier", catalog = "isms_db", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AuditIdentifier.findAll", query = "SELECT a FROM AuditIdentifier a"),
    @NamedQuery(name = "AuditIdentifier.findByIdentifierId", query = "SELECT a FROM AuditIdentifier a WHERE a.identifierId = :identifierId")})


//@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="id")
public class AuditIdentifier implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "identifier_id", nullable = false)
    private Integer identifierId;

    	//
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "auditIdentifierId")
//    private Collection<Users> usersCollection;
//    
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "auditIdentifierId")
//    private Collection<Roles> rolesCollection;
//    
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "auditIdentifierId")
//    private Collection<Plots> plotsCollection;
//    
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "auditIdentifierId")
//    private Collection<Houses> housesCollection;
//    
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "auditIdentifierId")
//    private Collection<Clients> clientsCollection;
//    
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "auditIdentifierId")
//    private Collection<Payments> paymentsCollection;
//
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "auditIdentifierId")
//    private Collection<Billings> billingsCollection;
//   
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "auditIdentifierId")
//    private Collection<AuditEvents> auditEventsCollection;

    
    	//constructor
    public AuditIdentifier() {
    }

    public AuditIdentifier(Integer identifierId) {
        this.identifierId = identifierId;
    }

    
    	//Getters and setters
    public Integer getIdentifierId() {
        return identifierId;
    }

    public void setIdentifierId(Integer identifierId) {
        this.identifierId = identifierId;
    }
    
//
//    	//Users
//    @XmlTransient
//    public Collection<Users> getUsersCollection() {
//        return usersCollection;
//    }
//
//    public void setUsersCollection(Collection<Users> usersCollection) {
//        this.usersCollection = usersCollection;
//    }
//	    
//	
//		//Roles
//	@XmlTransient
//	public Collection<Roles> getRolesCollection() {
//	    return rolesCollection;
//	}
//	
//	public void setRolesCollection(Collection<Roles> rolesCollection) {
//	    this.rolesCollection = rolesCollection;
//	}
//
//
//    	//Plots
//    @XmlTransient
//    public Collection<Plots> getPlotsCollection() {
//        return plotsCollection;
//    }
//
//    public void setPlotsCollection(Collection<Plots> plotsCollection) {
//        this.plotsCollection = plotsCollection;
//    }
//    
//    	//Houses
//    @XmlTransient
//    public Collection<Houses> getHousesCollection() {
//        return housesCollection;
//    }
//
//    public void setHousesCollection(Collection<Houses> housesCollection) {
//        this.housesCollection = housesCollection;
//    }
//    
//    	//Clients
//    @XmlTransient
//    public Collection<Clients> getClientsCollection() {
//        return clientsCollection;
//    }
//
//    public void setClientsCollection(Collection<Clients> clientsCollection) {
//        this.clientsCollection = clientsCollection;
//    }
//
//		//Payments
//	@XmlTransient
//	public Collection<Payments> getPaymentsCollection() {
//	    return paymentsCollection;
//	}
//	
//	public void setPaymentsCollection(Collection<Payments> paymentsCollection) {
//	    this.paymentsCollection = paymentsCollection;
//	}
//
//		//Billings
//	@XmlTransient
//	public Collection<Billings> getBillingsCollection() {
//	    return billingsCollection;
//	}
//	
//	public void setBillingsCollection(Collection<Billings> billingsCollection) {
//	    this.billingsCollection = billingsCollection;
//	}
//	  
//    	//AuditEvents
//    @XmlTransient
//    public Collection<AuditEvents> getAuditEventsCollection() {
//        return auditEventsCollection;
//    }
//
//    public void setAuditEventsCollection(Collection<AuditEvents> auditEventsCollection) {
//        this.auditEventsCollection = auditEventsCollection;
//    }
//    
    
   	//Hashing
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (identifierId != null ? identifierId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AuditIdentifier)) {
            return false;
        }
        AuditIdentifier other = (AuditIdentifier) object;
        if ((this.identifierId == null && other.identifierId != null) || (this.identifierId != null && !this.identifierId.equals(other.identifierId))) {
            return false;
        }
        return true;
    }

    
    //to string
    @Override
    public String toString() {
        return "AuditIdentifier[ identifierId=" + identifierId + " ]";
    }

}


