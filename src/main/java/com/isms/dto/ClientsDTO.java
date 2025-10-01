package com.isms.dto;

import com.isms.model.Locations;

public class ClientsDTO {

	private int id;

	private String clientName;
	
	private String emailAddress;
	
	private String telNo;
	
	private Locations locations;
	
	
	public ClientsDTO() {

	}

	
	public ClientsDTO(int id, String clientName, String emailAddress, String telNo, Locations locations) {
		super();
		this.id = id;
		this.clientName = clientName;
		this.emailAddress = emailAddress;
		this.telNo = telNo;
		this.locations = locations;
	}


	public ClientsDTO(String clientName, String emailAddress, String telNo, Locations locations) {
		super();
		this.clientName = clientName;
		this.emailAddress = emailAddress;
		this.telNo = telNo;
		this.locations = locations;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}

	
	public String getClientName() {
		return clientName;
	}


	public void setClientName(String clientName) {
		this.clientName = clientName;
	}


	public String getEmailAddress() {
		return emailAddress;
	}


	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}


	public String getTelNo() {
		return telNo;
	}


	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}


	public Locations getLocations() {
		return locations;
	}


	public void setLocations(Locations locations) {
		this.locations = locations;
	}


	@Override
	public String toString() {
		return "ClientsDTO [id=" + id + ", clientName=" + clientName + "]";
	}
	
}