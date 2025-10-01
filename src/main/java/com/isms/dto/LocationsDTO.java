package com.isms.dto;

public class LocationsDTO {

	private int id;
		
	private String locationName;
	
	private String locationCode;
		
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public String getLocationCode() {
		return locationCode;
	}
	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}

	public LocationsDTO() {
		
	}
	public LocationsDTO(int id, String locationName, String locationCode) {
		super();
		this.id = id;
		this.locationName = locationName;
		this.locationCode = locationCode;
	}
	public LocationsDTO(String locationName, String locationCode) {
		super();
		this.locationName = locationName;
		this.locationCode = locationCode;
	}
	
	@Override
	public String toString() {
		return "LocationsDTO [locationCode=" + locationCode + "]";	
		}	
	
}
