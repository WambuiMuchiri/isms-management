package com.isms.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.isms.model.AuditEvents;

public interface AuditEventsService {

    List<AuditEvents> getAllAuditEvents();

    void saveAuditEvents(AuditEvents auditEvents);

    AuditEvents getAuditEvent(int id);

    void deleteAuditEvent(int id);
    
    String getCreatorByAuditIdentifierId(Integer identifierId);
    
    List<AuditEvents> getAuditEventByAuditIdentifierId(Integer identifierId);

    Page<AuditEvents> getAuditEventsForDatatable(String queryString, Pageable pageable);
}
