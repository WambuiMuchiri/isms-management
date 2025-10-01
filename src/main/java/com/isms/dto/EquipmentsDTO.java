package com.isms.dto;

import java.sql.Date;

import com.isms.model.Categories;

import jakarta.persistence.Transient;

public class EquipmentsDTO {

    private int id;
    
    private Date recordDate;

    private String equipmentName;

    private String serialNumber;
    
    private String status;
    
    private Double equipmentValue;

    private String equipmentPicture;
    
    private Categories categories;
    
    public EquipmentsDTO() {
    }


    public EquipmentsDTO(int id, Date recordDate, String equipmentName, String serialNumber, String status,
			Double equipmentValue, String equipmentPicture, Categories categories) {
		super();
		this.id = id;
		this.recordDate = recordDate;
		this.equipmentName = equipmentName;
		this.serialNumber = serialNumber;
		this.status = status;
		this.equipmentValue = equipmentValue;
		this.equipmentPicture = equipmentPicture;
		this.categories = categories;
	}


	public EquipmentsDTO(Date recordDate, String equipmentName, String serialNumber, String status, Double equipmentValue,
			String equipmentPicture, Categories categories) {
		super();
		this.recordDate = recordDate;
		this.equipmentName = equipmentName;
		this.serialNumber = serialNumber;
		this.status = status;
		this.equipmentValue = equipmentValue;
		this.equipmentPicture = equipmentPicture;
		this.categories = categories;
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


	public String getEquipmentName() {
		return equipmentName;
	}


	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}


	public String getSerialNumber() {
		return serialNumber;
	}


	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public Double getEquipmentValue() {
		return equipmentValue;
	}


	public void setEquipmentValue(Double equipmentValue) {
		this.equipmentValue = equipmentValue;
	}


	public String getEquipmentPicture() {
		return equipmentPicture;
	}


	public void setEquipmentPicture(String equipmentPicture) {
		this.equipmentPicture = equipmentPicture;
	}


	public Categories getCategories() {
		return categories;
	}


	public void setCategories(Categories categories) {
		this.categories = categories;
	}


	@Transient
    public String getEquipmentPicturePath() {
        if(equipmentPicture == null || id == 0) return null;
        return "/equipment-pictures/"+id+"/"+equipmentPicture;
    }


    @Override
    public String toString() {
        return "EquipmentsDTO [equipmentName=" + equipmentName + "]";
    }
}