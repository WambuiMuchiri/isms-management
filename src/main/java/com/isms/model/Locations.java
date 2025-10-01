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
@Table(name = "locations", catalog = "isms_db", schema = "", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"location_code"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Locations.findAll", query = "SELECT a FROM Locations a"),
    @NamedQuery(name = "Locations.findById", query = "SELECT a FROM Locations a WHERE a.id = :id"),
    @NamedQuery(name = "Locations.findByLocationCode", query = "SELECT a FROM Locations a WHERE a.locationCode = :locationCode"),
	@NamedQuery(name = "Locations.findByLocationName", query = "SELECT a FROM Locations a WHERE a.locationName = :locationName")})
public class Locations implements Serializable {

	 private static final long serialVersionUID = 1L;
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Basic(optional = false)
	    @Column(nullable = false)
	    private int id;
	    	    
	    @Column(name = "location_name", nullable = false, length = 400)
	    private String locationName;
	    
	    @Column(name = "location_code", nullable = false, length = 400)
	    private String locationCode;
	    
	    @JoinColumn(name = "audit_identifier_id", referencedColumnName = "identifier_id", nullable = false)
	    @ManyToOne(optional = false)
	    private AuditIdentifier auditIdentifierId;
 
		 public Locations() {			
			}
		 
		 public Locations(Integer id) {
		        this.id = id;
		    }

		    public Locations(Integer id, String locationName, String locationCode) {
		        this.id = id;
		        this.locationName = locationName;
		        this.locationCode = locationCode;
		    }

		    public int getId() {
		        return id;
		    }

		    public void setId(int id) {
		        this.id = id;
		    }
		    public String getLocationName() {
		        return locationName;
		    }

		    public void setLocationName(String locationName) {
		        this.locationName = locationName;
		    }

		    public String getLocationCode() {
		        return locationCode;
		    }

		    public void setLocationCode(String locationCode) {
		        this.locationCode = locationCode;
		    }


		    public AuditIdentifier getAuditIdentifierId() {
		        return auditIdentifierId;
		    }

		    public void setAuditIdentifierId(AuditIdentifier auditIdentifierId) {
		        this.auditIdentifierId = auditIdentifierId;
		    }
		   
		    @Override
		    public String toString() {
		        return "Locations[ locationCode=" + locationCode + " ]";
		    }
}
