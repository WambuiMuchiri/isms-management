package com.isms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.isms.model.Assignment;

public interface AssignmentRepository extends JpaRepository<Assignment, Integer>, JpaSpecificationExecutor<Assignment> {
    
    // Find assignments by installation id
    List<Assignment> findByInstallationId(int installationId);
    
    // Find assignments by personnel id
    List<Assignment> findByPersonnelId(int personnelId);

	List<Assignment> findByEquipmentId(int equipmentId);
	
    // Check if a specific assignment exists
    boolean existsByInstallationIdAndPersonnelId(int installationId, int personnelId);
    
    // Delete all assignments for an installation
    void deleteByInstallationId(int installationId);
    
    // Delete all assignments for a personnel
    void deleteByPersonnelId(int personnelId);
    
    // Delete a specific assignment
    void deleteByInstallationIdAndPersonnelId(int installationId, int personnelId);
    
    // Get personnel count for an installation
    @Query("SELECT COUNT(ip) FROM Assignment ip WHERE ip.installation.id = :installationId")
    long countPersonnelByInstallationId(@Param("installationId") int installationId);
    
    // Get installation count for a personnel
    @Query("SELECT COUNT(a) FROM Assignment a WHERE a.personnel.id = :personnelId")
    long countInstallationsByPersonnelId(@Param("personnelId") int personnelId);

}