package com.isms.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.isms.filter.AuditEventsDataFilter;
import com.isms.model.AuditEvents;
import com.isms.model.CreatorInfo;
import com.isms.repository.AuditEventsRepository;
import com.isms.repository.UserRepository;
import com.isms.security.UserPrincipal;
import com.isms.service.AuditEventsService;

@Service
public class AuditEventsServiceImpl implements AuditEventsService {

    @Autowired
    private AuditEventsRepository auditEventsRepository;
    
    @Autowired
    private UserRepository usersRepository;

    @Override
    public List<AuditEvents> getAllAuditEvents() {
        return this.auditEventsRepository.findAll();
    }

    @Override
    public void saveAuditEvents(AuditEvents auditEvents) {
        UserPrincipal cradleUserDetails = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();        
        auditEvents.setUserId(usersRepository.findByUsername(cradleUserDetails.getUsername()));
        this.auditEventsRepository.save(auditEvents);
    }

    @Override
    public AuditEvents getAuditEvent(int id) {
        Optional<AuditEvents> optional = auditEventsRepository.findById(id);
        AuditEvents auditEvents = null;
        if (optional.isPresent()) {
            auditEvents = optional.get();
        } else {
            throw new RuntimeException("AuditEvents not found for id : " + id);
        }
        return auditEvents;
    }

    @Override
    public void deleteAuditEvent(int id) {
        this.auditEventsRepository.deleteById(id);
    }

	@Override
	public String getCreatorByAuditIdentifierId(Integer identifierId) {
	    CreatorInfo creatorInfo = auditEventsRepository.findCreatorByAuditIdentifierId(identifierId);
	    return creatorInfo != null ? creatorInfo.getUsername() : "System";
	}

	@Override
	public List<AuditEvents> getAuditEventByAuditIdentifierId(Integer identifierId) {
		List<AuditEvents> eventInfo = auditEventsRepository.findAuditEventByAuditIdentifierId(identifierId);
		    return eventInfo != null ? eventInfo : null;
	}

    @Override
    public Page<AuditEvents> getAuditEventsForDatatable(String queryString, Pageable pageable) {
        AuditEventsDataFilter auditEventsDataFilter = new AuditEventsDataFilter(queryString);
        return auditEventsRepository.findAll(auditEventsDataFilter, pageable);
    }

}

