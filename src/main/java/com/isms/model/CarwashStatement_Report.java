package com.isms.model;

import java.util.Date;

//@AllArgsConstructor
//@NoArgsConstructor
//@ToDate
//@Getter
//@Setter
public class CarwashStatement_Report {

	private Date recordDate;
	private Double jobItem;
	private Double payment;
	private Double total;
	
	public CarwashStatement_Report() {
		
	}

	public CarwashStatement_Report(Date recordDate, Double jobItem, Double payment, Double total) {
		super();
		this.recordDate = recordDate;
		this.jobItem = jobItem;
		this.payment = payment;
		this.total = total;
	}

	public CarwashStatement_Report(Double jobItem, Double payment, Double total) {
		super();
		this.jobItem = jobItem;
		this.payment = payment;
		this.total = total;
	}

	public Date getRecordDate() {
		return recordDate;
	}

	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}

	public Double getJobItem() {
		return jobItem;
	}

	public void setJobItem(Double jobItem) {
		this.jobItem = jobItem;
	}

	public Double getPayment() {
		return payment;
	}

	public void setPayment(Double payment) {
		this.payment = payment;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

}
