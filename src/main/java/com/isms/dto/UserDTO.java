package com.isms.dto;

import java.util.HashSet;
import java.util.Set;

import com.isms.model.Roles;

import jakarta.persistence.Transient;

public class UserDTO {

    private int id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String status;
    private String password;
    private String confirmPassword;
    private String remarks;
    private String userLogo;
    private Set<Roles> roles = new HashSet<>();

    //constructors
	public UserDTO() {
			
		}
  
    
	public UserDTO(int id, String firstName, String lastName, String username, String email, String status,
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
	
	
    
    public UserDTO(String firstName, String lastName, String username, String email, String status, String password,
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
    

	public UserDTO(int id, String firstName, String lastName, String username, String email, String status,
			String password, String confirmPassword, String remarks, String userLogo, Set<Roles> roles) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.email = email;
		this.status = status;
		this.password = password;
		this.confirmPassword = confirmPassword;
		this.remarks = remarks;
		this.userLogo = userLogo;
		this.roles = roles;
	}


	public UserDTO(int id, String firstName, String lastName, String username, String email, String status, Set<Roles> roles) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.email = email;
		this.status = status;
		this.roles = roles;
	}


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

	
	public String getConfirmPassword() {
		return confirmPassword;
	}


	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
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


	public Set<Roles> getRoles() {
		return roles;
	}


	public void setRoles(Set<Roles> roles) {
		this.roles = roles;
	}


	@Transient
	public String getUserLogoPath() {
		if(userLogo == null || id == 0) return null;
		return "/user-logos/"+id+"/"+userLogo;
	}


}
