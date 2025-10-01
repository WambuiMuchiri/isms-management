package com.isms.dto;

import com.isms.model.Sites;

public class InstallationsDTO {

	private int id;
	
	private String installationName;
	
	private String description;
	
	private String remarks;
	
    private Sites sites;


	public InstallationsDTO() {
		
	}


	public InstallationsDTO(int id, String installationName, String description, String remarks, Sites sites) {
		super();
		this.id = id;
		this.installationName = installationName;
		this.description = description;
		this.remarks = remarks;
		this.sites = sites;
	}


	public InstallationsDTO(String installationName, String description, String remarks, Sites sites) {
		super();
		this.installationName = installationName;
		this.description = description;
		this.remarks = remarks;
		this.sites = sites;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getInstallationName() {
		return installationName;
	}


	public void setInstallationName(String installationName) {
		this.installationName = installationName;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getRemarks() {
		return remarks;
	}


	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}


	public Sites getSites() {
		return sites;
	}


	public void setSites(Sites sites) {
		this.sites = sites;
	}


	@Override
	public String toString() {
		return "InstallationsDTO [installationName=" + installationName + "]";
	}
	
}
