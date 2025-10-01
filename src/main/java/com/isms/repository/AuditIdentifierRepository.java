package com.isms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.isms.model.AuditIdentifier;

@Repository
public interface AuditIdentifierRepository extends JpaRepository< AuditIdentifier, Long>, JpaSpecificationExecutor<AuditIdentifier> {

	
    AuditIdentifier findByIdentifierId(int identifierId);
        
    // Find all identifiers linked to assignments
    @Query("SELECT ai FROM AuditIdentifier ai "
    		+ "JOIN Assignment a ON a.auditIdentifierId = ai "
    		+ "JOIN AuditEvents ae ON ae.auditIdentifierId = ai "
    		+ "ORDER BY ae.eventDate DESC")
    List<AuditIdentifier> findAllForAssignments();
}
