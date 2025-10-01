package com.isms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.isms.model.AuditEvents;
import com.isms.model.CreatorInfo;


@Repository
public interface AuditEventsRepository extends JpaRepository<AuditEvents, Integer>, JpaSpecificationExecutor<AuditEvents> {

	@Query("SELECT e.id as id, e.eventDate as eventDate, e.userId.username as username FROM AuditEvents e " +
		       "WHERE e.auditIdentifierId.identifierId = :identifierId " +
		       "AND e.auditTypeId.name = 'CREATE' " +
		       "ORDER BY e.eventDate ASC LIMIT 1")
	CreatorInfo findCreatorByAuditIdentifierId(@Param("identifierId") Integer identifierId);

	
	@Query("SELECT e.id as id, e.eventDate as eventDate, e.description as description, e.ipAddress as ipAddress, e.browser as browser, e.userId.username as username " +
			   "FROM AuditEvents e " +
		       "WHERE e.auditIdentifierId.identifierId = :identifierId " +
		       "AND e.auditTypeId.name = 'CREATE' " +
		       "ORDER BY e.eventDate ASC LIMIT 1")
	List<AuditEvents> findAuditEventByAuditIdentifierId(Integer identifierId);
}
