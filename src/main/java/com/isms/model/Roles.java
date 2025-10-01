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

//@Getter
//@Setter
@Entity
@Table(name = "role", catalog = "isms_db", schema = "", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"name"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Roles.findAll", query = "SELECT r FROM Roles r"),
    @NamedQuery(name = "Roles.findById", query = "SELECT r FROM Roles r WHERE r.id = :id"),
    @NamedQuery(name = "Roles.findByName", query = "SELECT r FROM Roles r WHERE r.name = :name")})
public class Roles implements Serializable {

    private static final long serialVersionUID = 1L;	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "description", nullable = true, length = 1000)
	private String description;


    @JoinColumn(name = "audit_identifier_id", referencedColumnName = "identifier_id", nullable = false)
    @ManyToOne(optional = false)
    private AuditIdentifier auditIdentifierId;
    
    
    //constructors
	public Roles() {
		
	}
    
	public Roles(int id, String name, String description) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
	}

	
	public Roles(String name, String description) {
		super();
		this.name = name;
		this.description = description;
	}

	//getters and setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public AuditIdentifier getAuditIdentifierId() {
		return auditIdentifierId;
	}

	public void setAuditIdentifierId(AuditIdentifier auditIdentifierId) {
		this.auditIdentifierId = auditIdentifierId;
	}



	
	
    
    //to string
	@Override
	public String toString() {
		return this.name;
	}



	//	Uniderictional Many To Many relationship for the user and role entities.
//	@ManyToMany(mappedBy = "roles")
//    private Set<Users> user;
	
	
}
