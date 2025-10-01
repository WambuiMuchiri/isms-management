package com.isms.dto;

import com.isms.model.Projects;

public class SitesDTO {

	private int id;
	
	private String siteName;
	
	private String description;
	
	private String remarks;
	
    private Projects projects;


	public SitesDTO() {
		
	}


	public SitesDTO(int id, String siteName, String description, String remarks, Projects projects) {
		super();
		this.id = id;
		this.siteName = siteName;
		this.description = description;
		this.remarks = remarks;
		this.projects = projects;
	}


	public SitesDTO(String siteName, String description, String remarks, Projects projects) {
		super();
		this.siteName = siteName;
		this.description = description;
		this.remarks = remarks;
		this.projects = projects;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getSiteName() {
		return siteName;
	}


	public void setSiteName(String siteName) {
		this.siteName = siteName;
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


	public Projects getProjects() {
		return projects;
	}


	public void setProjects(Projects projects) {
		this.projects = projects;
	}


	@Override
	public String toString() {
		return "SitesDTO [siteName=" + siteName + "]";
	}
	
	
	
	
	
    
    
}
