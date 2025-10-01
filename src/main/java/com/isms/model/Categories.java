package com.isms.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

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
import jakarta.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "categories", catalog = "isms_db", schema = "", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Categories.findAll", query = "SELECT c FROM Categories c"),
    @NamedQuery(name = "Categories.findById", query = "SELECT c FROM Categories c WHERE c.id = :id"),
    @NamedQuery(name = "Categories.findByInitials", query = "SELECT c FROM Categories c WHERE c.initials = :initials"),
	@NamedQuery(name = "Categories.findByName", query = "SELECT c FROM Categories c WHERE c.name = :name")})

@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="id")
public class Categories implements Serializable {

	 private static final long serialVersionUID = 1L;
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Basic(optional = false)
	    @Column(nullable = false)
	    private int id;
	    
	    @Column(name = "initials", nullable = false, length = 400)
	    private String initials;
	    
	    @Column(name = "name", nullable = false, length = 400)
	    private String name;
   
	    @JoinColumn(name = "audit_identifier_id", referencedColumnName = "identifier_id", nullable = false)
	    @ManyToOne(optional = false)
	    @JsonIgnore
	    private AuditIdentifier auditIdentifierId;

	    	    
		public Categories() {
			
		}

		
		public Categories(int id, String initials, String name) {
			super();
			this.id = id;
			this.initials = initials;
			this.name = name;
		}


		public Categories(String initials, String name) {
			super();
			this.initials = initials;
			this.name = name;
		}


		public int getId() {
			return id;
		}


		public void setId(int id) {
			this.id = id;
		}


		public String getInitials() {
			return initials;
		}


		public void setInitials(String initials) {
			this.initials = initials;
		}


		public String getName() {
			return name;
		}


		public void setName(String name) {
			this.name = name;
		}


		public AuditIdentifier getAuditIdentifierId() {
			return auditIdentifierId;
		}


		public void setAuditIdentifierId(AuditIdentifier auditIdentifierId) {
			this.auditIdentifierId = auditIdentifierId;
		}


		@Override
		public String toString() {
			return "Categories [name=" + name + "]";
		}
		
		
}
