package com.isms.model;

public class ClientList_Report {

	private int id;
	private String clientName;
	private String telNo;
	private Double balance;
	
	public ClientList_Report() {
		
	}

	public ClientList_Report(int id, String clientName, String telNo, Double balance) {
		super();
		this.id = id;
		this.clientName = clientName;
		this.telNo = telNo;
		this.balance = balance;
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

	public String getTelNo() {
		return telNo;
	}

	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	@Override
	public String toString() {
		return "ClientList_Report [clientName=" + clientName + ", telNo=" + telNo + ", balance=" + balance + "]";
	}


}
