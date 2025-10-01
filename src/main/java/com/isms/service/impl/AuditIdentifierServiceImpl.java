package com.isms.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.isms.filter.AuditIdentifierDataFilter;
import com.isms.model.AuditEvents;
import com.isms.model.AuditIdentifier;
import com.isms.repository.AuditIdentifierRepository;
import com.isms.service.AuditEventsService;
import com.isms.service.AuditIdentifierService;

import jakarta.transaction.Transactional;

@Service
public class AuditIdentifierServiceImpl implements AuditIdentifierService {

    @Autowired
    private AuditIdentifierRepository auditIdentifierRepository;
    
    @Autowired
    private AuditEventsService auditEventsService;

    @Override
    public List<AuditIdentifier> getAllAuditIdentifiers() {
        return this.auditIdentifierRepository.findAll();
    }

    @Override
    public void saveAuditIdentifier(AuditIdentifier auditIdentifier) {
        this.auditIdentifierRepository.save(auditIdentifier);
    }

    @Override
    public AuditIdentifier getAuditIdentifier(long id) {

        Optional<AuditIdentifier> optional = auditIdentifierRepository.findById(id);
        AuditIdentifier auditIdentifier = null;
        if (optional.isPresent()) {
            auditIdentifier = optional.get();
        } else {
            throw new RuntimeException("ACL not found for id : " + id);
        }
        return auditIdentifier;

    }

    @Override
    public void deleteAuditIdentifier(long id) {
        this.auditIdentifierRepository.deleteById(id);
    }

    
    // Ensure auditEvents are eagerly loaded
    @Transactional
    public AuditIdentifier getAuditIdentifierWithEvents(int identifierId) {
        AuditIdentifier identifier = auditIdentifierRepository.findByIdentifierId(identifierId);
        
        if (identifier != null && auditEventsService != null) {
            // Fetch events using the service since identifier doesn't directly contain events
            List<AuditEvents> events = auditEventsService.getAuditEventByAuditIdentifierId(identifierId);
            if (events != null) {
                // Force initialization by accessing the collection
                events.size();
            }
        }
        
        return identifier;
    }

    @Override
    public Page<AuditIdentifier> getAuditIdentifierForDatatable(String queryString, Pageable pageable) {
        AuditIdentifierDataFilter auditIdentifierDataFilter = new AuditIdentifierDataFilter(queryString);
        return auditIdentifierRepository.findAll(auditIdentifierDataFilter, pageable);
    }

}
