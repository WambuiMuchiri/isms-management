package com.isms.dto;

import com.isms.model.Clients;

public class ProjectsDTO {

	private int id;
	
	private String projectName;
	
	private Double projectCost;
	
	private String description;
	
	private String remarks;
	
    private Clients clients;


	public ProjectsDTO() {
		
	}


	public ProjectsDTO(int id, String projectName, Double projectCost, String description, String remarks, Clients clients) {
		super();
		this.id = id;
		this.projectName = projectName;
		this.projectCost = projectCost;
		this.description = description;
		this.remarks = remarks;
		this.clients = clients;
	}


	public ProjectsDTO(String projectName, Double projectCost, String description, String remarks, Clients clients) {
		super();
		this.projectName = projectName;
		this.projectCost = projectCost;
		this.description = description;
		this.remarks = remarks;
		this.clients = clients;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getProjectName() {
		return projectName;
	}


	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	
	public Double getProjectCost() {
		return projectCost;
	}


	public void setProjectCost(Double projectCost) {
		this.projectCost = projectCost;
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


	public Clients getClients() {
		return clients;
	}


	public void setClients(Clients clients) {
		this.clients = clients;
	}


	@Override
	public String toString() {
		return "ProjectsDTO [projectName=" + projectName + "]";
	}
	
	
	
	
	
    
    
}
