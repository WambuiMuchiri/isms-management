package com.isms.dto;

import java.sql.Date;

import com.isms.model.Categories;
import com.isms.model.Locations;

import jakarta.persistence.Transient;

public class PersonnelsDTO {

	private int id;
	
	private Date recordDate;

	private String firstName;
	
	private String lastName;
	
	private String personnelName;

	private String idNo;
	
	private String telNo;

	private String email;
	
	private String gender;

	private String status;

	private String detailedResidence;
	
	private String achievements;

	private Double salaryAmount;

	private String employeeNo;

	private String nssfNo;

	private String nhifNo;

	private String scannedDocument;

	private String personnelPicture;
	
    private Categories categories;

    private Locations locations;
    

    public PersonnelsDTO() {
		
	}


	public PersonnelsDTO(int id, Date recordDate, String firstName, String lastName, String personnelName, String idNo,
			String telNo, String email, String gender, String status, String detailedResidence, String achievements,
			Double salaryAmount, String employeeNo, String nssfNo, String nhifNo, String scannedDocument,
			String personnelPicture, Categories categories, Locations locations) {
		super();
		this.id = id;
		this.recordDate = recordDate;
		this.firstName = firstName;
		this.lastName = lastName;
		this.personnelName = personnelName;
		this.idNo = idNo;
		this.telNo = telNo;
		this.email = email;
		this.gender = gender;
		this.status = status;
		this.detailedResidence = detailedResidence;
		this.achievements = achievements;
		this.salaryAmount = salaryAmount;
		this.employeeNo = employeeNo;
		this.nssfNo = nssfNo;
		this.nhifNo = nhifNo;
		this.scannedDocument = scannedDocument;
		this.personnelPicture = personnelPicture;
		this.categories = categories;
		this.locations = locations;
	}


	public PersonnelsDTO(Date recordDate, String firstName, String lastName, String personnelName, String idNo,
			String telNo, String email, String gender, String status, String detailedResidence, String achievements,
			Double salaryAmount, String employeeNo, String nssfNo, String nhifNo, String scannedDocument,
			String personnelPicture, Categories categories, Locations locations) {
		super();
		this.recordDate = recordDate;
		this.firstName = firstName;
		this.lastName = lastName;
		this.personnelName = personnelName;
		this.idNo = idNo;
		this.telNo = telNo;
		this.email = email;
		this.gender = gender;
		this.status = status;
		this.detailedResidence = detailedResidence;
		this.achievements = achievements;
		this.salaryAmount = salaryAmount;
		this.employeeNo = employeeNo;
		this.nssfNo = nssfNo;
		this.nhifNo = nhifNo;
		this.scannedDocument = scannedDocument;
		this.personnelPicture = personnelPicture;
		this.categories = categories;
		this.locations = locations;
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


	public String getPersonnelName() {
		return personnelName;
	}


	public void setPersonnelName(String personnelName) {
		this.personnelName = personnelName;
	}


	public String getIdNo() {
		return idNo;
	}


	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}


	public String getTelNo() {
		return telNo;
	}


	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getGender() {
		return gender;
	}


	public void setGender(String gender) {
		this.gender = gender;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getDetailedResidence() {
		return detailedResidence;
	}


	public void setDetailedResidence(String detailedResidence) {
		this.detailedResidence = detailedResidence;
	}


	public String getAchievements() {
		return achievements;
	}


	public void setAchievements(String achievements) {
		this.achievements = achievements;
	}


	public Double getSalaryAmount() {
		return salaryAmount;
	}


	public void setSalaryAmount(Double salaryAmount) {
		this.salaryAmount = salaryAmount;
	}


	public String getEmployeeNo() {
		return employeeNo;
	}


	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}


	public String getNssfNo() {
		return nssfNo;
	}


	public void setNssfNo(String nssfNo) {
		this.nssfNo = nssfNo;
	}


	public String getNhifNo() {
		return nhifNo;
	}


	public void setNhifNo(String nhifNo) {
		this.nhifNo = nhifNo;
	}


	public String getScannedDocument() {
		return scannedDocument;
	}


	public void setScannedDocument(String scannedDocument) {
		this.scannedDocument = scannedDocument;
	}


	public String getPersonnelPicture() {
		return personnelPicture;
	}


	public void setPersonnelPicture(String personnelPicture) {
		this.personnelPicture = personnelPicture;
	}


	public Categories getCategories() {
		return categories;
	}


	public void setCategories(Categories categories) {
		this.categories = categories;
	}


	public Locations getLocations() {
		return locations;
	}


	public void setLocations(Locations locations) {
		this.locations = locations;
	}


	@Transient
	public String getPersonnelPicturePath() {
		if(personnelPicture == null || id == 0) return null;
		return "/personnel-pictures/"+id+"/"+personnelPicture;
	}

	@Transient
	public String getScannedDocumentPath() {
		if(scannedDocument == null || id == 0) return null;
		return "/personnel-scanned-documents/"+id+"/"+scannedDocument;
	}
	

	@Override
	public String toString() {
		return "PersonnelsDTO [personnelName=" + personnelName + "]";
	}
    
}
