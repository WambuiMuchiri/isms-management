package com.isms.dto;

import java.sql.Date;

public class AssignmentDTO {
   
    private int id;
    private Date assignmentDate;
    private String remarks;
    private int installationId;
    private int personnelId;
    private int equipmentId;
    private boolean notifyPersonnel;
    
    public AssignmentDTO() {
    }
    
   
    
    public AssignmentDTO(int id, Date assignmentDate, String remarks, int installationId, int personnelId,
			int equipmentId, boolean notifyPersonnel) {
		super();
		this.id = id;
		this.assignmentDate = assignmentDate;
		this.remarks = remarks;
		this.installationId = installationId;
		this.personnelId = personnelId;
		this.equipmentId = equipmentId;
		this.notifyPersonnel = notifyPersonnel;
	}

    
	public AssignmentDTO(Date assignmentDate, String remarks, int installationId, int personnelId, int equipmentId,
			boolean notifyPersonnel) {
		super();
		this.assignmentDate = assignmentDate;
		this.remarks = remarks;
		this.installationId = installationId;
		this.personnelId = personnelId;
		this.equipmentId = equipmentId;
		this.notifyPersonnel = notifyPersonnel;
	}



	public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public Date getAssignmentDate() {
        return assignmentDate;
    }
    
    public void setAssignmentDate(Date assignmentDate) {
        this.assignmentDate = assignmentDate;
    }
    
    public String getRemarks() {
        return remarks;
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    public int getInstallationId() {
        return installationId;
    }
    
    public void setInstallationId(int installationId) {
        this.installationId = installationId;
    }
    
    public int getPersonnelId() {
        return personnelId;
    }
    
    public void setPersonnelId(int personnelId) {
        this.personnelId = personnelId;
    }
    
    public int getEquipmentId() {
		return equipmentId;
	}



	public void setEquipmentId(int equipmentId) {
		this.equipmentId = equipmentId;
	}



	public boolean isNotifyPersonnel() {
		return notifyPersonnel;
	}

	public void setNotifyPersonnel(boolean notifyPersonnel) {
		this.notifyPersonnel = notifyPersonnel;
	}

	@Override
    public String toString() {
        return "InstallationPersonnelDTO [id=" + id + ", assignmentDate=" + assignmentDate + ", remarks=" + remarks +
               ", installationId=" + installationId + ", personnelId=" + personnelId + "]";
    }
}
