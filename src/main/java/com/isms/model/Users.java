package com.isms.model;

import java.beans.Transient;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.xml.bind.annotation.XmlRootElement;

//@Data
@Entity
@Table(name="users", catalog="isms_db", schema="", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"user_name", "email"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Users.findAll", query = "SELECT u FROM Users u"),
    @NamedQuery(name = "Users.findById", query = "SELECT u FROM Users u WHERE u.id = :id"),
    @NamedQuery(name = "Users.findByFirstName", query = "SELECT u FROM Users u WHERE u.firstName = :firstName"),
    @NamedQuery(name = "Users.findByLastName", query = "SELECT u FROM Users u WHERE u.lastName = :lastName"),
    @NamedQuery(name = "Users.findByUsername", query = "SELECT u FROM Users u WHERE u.username = :username"),
    @NamedQuery(name = "Users.findByEmail", query = "SELECT u FROM Users u WHERE u.email = :email"),
    @NamedQuery(name = "Users.findByStatus", query = "SELECT u FROM Users u WHERE u.status = :status")})
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name="first_name", nullable=false)
	private String firstName;
	
	@Column(name="last_name", nullable=false)
	private String lastName;
	
	@Column(name="user_name", nullable=false)
	private String username;
	
	@Column(name="email", nullable=false)
	private String email;

	@Column(name="status", nullable=false)
	private String status;
	
	@Column(name="password", nullable=false)
	private String password;
	
	@ManyToMany(fetch = FetchType.EAGER, 
			cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"
    ))
    private Set<Roles> roles = new HashSet<>();
	
	@Column(name = "remarks", nullable = true)
	private String remarks;

	@Column(name = "user_logo", nullable = false, length = 255)
	private String userLogo;
	

    @JoinColumn(name = "audit_identifier_id", referencedColumnName = "identifier_id", nullable = false)
    @ManyToOne(optional = false)
    private AuditIdentifier auditIdentifierId;
    
    
    //Constructors  
	public Users() {
		
	}
  
    
	public Users(int id, String firstName, String lastName, String username, String email, String status,
			String password, Set<Roles> roles, String remarks, String userLogo) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.email = email;
		this.status = status;
		this.password = password;
		this.roles = roles;
		this.remarks = remarks;
		this.userLogo = userLogo;
	}
	
	
    
    public Users(String firstName, String lastName, String username, String email, String status, String password,
			Set<Roles> roles, String remarks, String userLogo) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.email = email;
		this.status = status;
		this.password = password;
		this.roles = roles;
		this.remarks = remarks;
		this.userLogo = userLogo;
	}



    //getters and setters
    
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public Set<Roles> getRoles() {
		return roles;
	}


	public void setRoles(Set<Roles> roles) {
		this.roles = roles;
	}


	public String getRemarks() {
		return remarks;
	}


	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}


	public String getUserLogo() {
		return userLogo;
	}


	public void setUserLogo(String userLogo) {
		this.userLogo = userLogo;
	}


	public AuditIdentifier getAuditIdentifierId() {
		return auditIdentifierId;
	}


	public void setAuditIdentifierId(AuditIdentifier auditIdentifierId) {
		this.auditIdentifierId = auditIdentifierId;
	}

//
//	@Override
//	public String toString() {
//		return "Users [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", username=" + username
//				+ ", email=" + email + ", status=" + status + ", password=" + password + ", roles=" + roles
//				+ ", remarks=" + remarks + ", auditIdentifierId=" + auditIdentifierId + "]";
//	}


	@Transient
	public String getUserLogoPath() {
		if(userLogo == null || id == 0) return null;
		return "/user-logos/"+id+"/"+userLogo;
	}

    
    
	@Override
	public String toString() {
		return "Users [id=" + id + "]";
	}

}
