package com.isms.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.isms.model.Assignment;
import com.isms.model.Installations;
import com.isms.model.Personnels;

public interface AssignmentService {
    
    // CRUD operations
    List<Assignment> getAllAssignment();
    
    Assignment saveAssignment(Assignment assignment);
    
    Assignment getAssignment(int id);
    
    void deleteAssignment(int id);
    
    // Relationship-specific operations
    List<Assignment> getAssignmentByInstallation(int installationId);
    
    List<Assignment> getAssignmentByPersonnel(int personnelId);
    
    List<Assignment> getAssignmentByEquipment(int equipmentId);
    
    boolean assignPersonnelToInstallation(int installationId, int personnelId, long auditIdentifierId, String remarks);
    
    boolean removePersonnelFromInstallation(int installationId, int personnelId);
    
    // Get personnel assigned to an installation
    List<Personnels> getPersonnelsByInstallationId(int installationId);
    
    // Get installations assigned to a personnel
    List<Installations> getInstallationsByPersonnelId(int personnelId);
    
    // For datatables
    Page<Assignment> getAssignmentForDatatable(String queryString, Pageable pageable);

}