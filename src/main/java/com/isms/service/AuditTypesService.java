package com.isms.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.isms.model.AuditTypes;

public interface AuditTypesService {

    List<AuditTypes> getAllAuditTypes();

    void saveAuditTypes(AuditTypes auditTypes);

    AuditTypes getAuditTypes(long id);

    void deleteAuditTypes(long id);

    AuditTypes findAuditTypesByName(String name);

    Page<AuditTypes> getAuditTypesForDatatable(String queryString, Pageable pageable);
}