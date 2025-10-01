package com.isms.dto;

import java.util.Date;

public class BillingsDTO {

	private int id;

	private String clientName;

	private Double balanceBf;
	
	public BillingsDTO() {

	}

	public BillingsDTO(int id, String clientName, Double balanceBf) {
		super();
		this.id = id;
		this.clientName = clientName;
		this.balanceBf = balanceBf;
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

	public Double getBalanceBf() {
		return balanceBf;
	}

	public void setBalanceBf(Double balanceBf) {
		this.balanceBf = balanceBf;
	}

	@Override
	public String toString() {
		return "BillingsDTO [id=" + id + ", clientName=" + clientName + ", balanceBf="
				+ balanceBf + "]";
	}

	
}
