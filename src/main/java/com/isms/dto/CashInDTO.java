package com.isms.dto;

import java.sql.Date;

import com.isms.model.Clients;
import com.isms.model.Personnels;

public class CashInDTO {

	private int id;
	private Date recordDate;
	private Double amount;
	private String remarks;
	private Clients clients;
	private Personnels personnels;
	
	
	//Constructors
	public CashInDTO() {
		
	}


	public CashInDTO(int id, Date recordDate, Double amount, String remarks, Clients clients, Personnels personnels) {
		super();
		this.id = id;
		this.recordDate = recordDate;
		this.amount = amount;
		this.remarks = remarks;
		this.clients = clients;
		this.personnels = personnels;
	}


	public CashInDTO(Date recordDate, Double amount, String remarks, Clients clients, Personnels personnels) {
		super();
		this.recordDate = recordDate;
		this.amount = amount;
		this.remarks = remarks;
		this.clients = clients;
		this.personnels = personnels;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public Date getRecordDate() {
		return recordDate;
	}


	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}


	public Double getAmount() {
		return amount;
	}


	public void setAmount(Double amount) {
		this.amount = amount;
	}


	public String getRemarks() {
		return remarks;
	}


	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}


	public Clients getClients() {
		return clients;
	}


	public void setClients(Clients clients) {
		this.clients = clients;
	}


	public Personnels getPersonnels() {
		return personnels;
	}


	public void setPersonnels(Personnels personnels) {
		this.personnels = personnels;
	}


	@Override
	public String toString() {
		return "CashInDTO [id=" + id + "]";
	}
	
	

}
