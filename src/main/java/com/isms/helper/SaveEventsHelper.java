package com.isms.helper;

import java.util.Date;

import com.isms.model.AuditEvents;
import com.isms.model.AuditIdentifier;
import com.isms.model.AuditTypes;

import jakarta.persistence.Entity;

@Entity
public class SaveEventsHelper extends AuditEvents {

	
    public SaveEventsHelper() {
		super();
		
	}

	public SaveEventsHelper(Date eventDate, String description, String ipAddress, String browser, AuditIdentifier auditIdentifier, AuditTypes auditType) {
        super(eventDate, description, ipAddress, browser);
        super.setAuditIdentifierId(auditIdentifier);
        super.setAuditTypeId(auditType);
    }

}
