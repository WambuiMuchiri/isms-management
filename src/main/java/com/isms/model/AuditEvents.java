package com.isms.model;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.xml.bind.annotation.XmlRootElement;

@Entity//(name="AuditEvents")
@Table(name = "audit_events", catalog = "isms_db", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AuditEvents.findAll", query = "SELECT a FROM AuditEvents a"),
    @NamedQuery(name = "AuditEvents.findById", query = "SELECT a FROM AuditEvents a WHERE a.id = :id"),
    @NamedQuery(name = "AuditEvents.findByEventDate", query = "SELECT a FROM AuditEvents a WHERE a.eventDate = :eventDate"),
    @NamedQuery(name = "AuditEvents.findByIpAddress", query = "SELECT a FROM AuditEvents a WHERE a.ipAddress = :ipAddress"),
    @NamedQuery(name = "AuditEvents.findByBrowser", query = "SELECT a FROM AuditEvents a WHERE a.browser = :browser")})
	public class AuditEvents implements Serializable {

	    private static final long serialVersionUID = 1L;
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Basic(optional = false)
	    @Column(nullable = false)
	    private Integer id;
	    
	    @Basic(optional = false)
	    @Column(name = "event_date", nullable = false)
	    @Temporal(TemporalType.TIMESTAMP)
	    private Date eventDate;
	    
	    @Basic(optional = false)
	    @Lob
	    @Column(nullable = false, length = 65535)
	    private String description;

	    @Basic(optional = false)
	    @Column(name = "ip_address", nullable = false, length = 50)
	    private String ipAddress;

	    @Basic(optional = false)
	    @Column(nullable = false, length = 150)
	    private String browser;

	    @JoinColumn(name = "audit_type_id", referencedColumnName = "id", nullable = false)
	    @ManyToOne(optional = false)
	    private AuditTypes auditTypeId;

	    @JoinColumn(name = "user_id", referencedColumnName = "id")
	    @ManyToOne(optional = false)
	    private Users userId;

	    @JoinColumn(name = "audit_identifier_id", referencedColumnName = "identifier_id", nullable = false)
	    @ManyToOne(optional = false)
	    private AuditIdentifier auditIdentifierId;

	    public AuditEvents() {
	    }

	    public AuditEvents(Integer id) {
	        this.id = id;
	    }

		public AuditEvents(Integer id, Date eventDate, String description, String ipAddress, String browser,
				Users userId) {
			super();
			this.id = id;
			this.eventDate = eventDate;
			this.description = description;
			this.ipAddress = ipAddress;
			this.browser = browser;
			this.userId = userId;
		}

		public AuditEvents(Integer id, Date eventDate, Users userId) {
			super();
			this.id = id;
			this.eventDate = eventDate;
			this.userId = userId;
		}

		public AuditEvents(Integer id, Date eventDate, String description, String ipAddress, String browser) {
	        this.id = id;
	        this.eventDate = eventDate;
	        this.description = description;
	        this.ipAddress = ipAddress;
	        this.browser = browser;
	    }

	    public AuditEvents(Date eventDate, String description, String ipAddress, String browser) {
	        this.eventDate = eventDate;
	        this.description = description;
	        this.ipAddress = ipAddress;
	        this.browser = browser;
	    }

	    
	    
	    public AuditEvents(Date eventDate, String description, String ipAddress, String browser, Users userId) {
			super();
			this.eventDate = eventDate;
			this.description = description;
			this.ipAddress = ipAddress;
			this.browser = browser;
			this.userId = userId;
		}

		public Integer getId() {
	        return id;
	    }

	    public void setId(Integer id) {
	        this.id = id;
	    }

	    public Date getEventDate() {
	        return eventDate;
	    }

	    public void setEventDate(Date eventDate) {
	        this.eventDate = eventDate;
	    }

	    public String getDescription() {
	        return description;
	    }

	    public void setDescription(String description) {
	        this.description = description;
	    }

	    public String getIpAddress() {
	        return ipAddress;
	    }

	    public void setIpAddress(String ipAddress) {
	        this.ipAddress = ipAddress;
	    }

	    public String getBrowser() {
	        return browser;
	    }

	    public void setBrowser(String browser) {
	        this.browser = browser;
	    }

	    public AuditTypes getAuditTypeId() {
	        return auditTypeId;
	    }

	    public void setAuditTypeId(AuditTypes auditTypeId) {
	        this.auditTypeId = auditTypeId;
	    }

	    public Users getUserId() {
	        return userId;
	    }

	    public void setUserId(Users userId) {
	        this.userId = userId;
	    }

	    public AuditIdentifier getAuditIdentifierId() {
	        return auditIdentifierId;
	    }

	    public void setAuditIdentifierId(AuditIdentifier auditIdentifierId) {
	        this.auditIdentifierId = auditIdentifierId;
	    }


	    @Override
	    public int hashCode() {
	        int hash = 0;
	        hash += (id != null ? id.hashCode() : 0);
	        return hash;
	    }

	    @Override
	    public boolean equals(Object object) {
	        // TODO: Warning - this method won't work in the case the id fields are not set
	        if (!(object instanceof AuditEvents)) {
	            return false;
	        }
	        AuditEvents other = (AuditEvents) object;
	        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
	            return false;
	        }
	        return true;
	    }

	    @Override
	    public String toString() {
	        return "AuditEvents[ id=" + id + " ]";
	    }

	}
