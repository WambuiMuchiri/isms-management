package com.isms.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.isms.model.AuditIdentifier;

public interface AuditIdentifierService {

    List<AuditIdentifier> getAllAuditIdentifiers();

    void saveAuditIdentifier(AuditIdentifier auditIdentifier);

    AuditIdentifier getAuditIdentifier(long id);

    void deleteAuditIdentifier(long id);
    
    AuditIdentifier getAuditIdentifierWithEvents(int identifierId);

    Page<AuditIdentifier> getAuditIdentifierForDatatable(String queryString, Pageable pageable);
}
