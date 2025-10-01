package com.isms.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.isms.filter.AuditTypesDataFilter;
import com.isms.model.AuditTypes;
import com.isms.repository.AuditTypesRepository;
import com.isms.service.AuditTypesService;

@Service
public class AuditTypesServiceImpl implements AuditTypesService {

    @Autowired
    private AuditTypesRepository auditTypesRepository;

    @Override
    public List<AuditTypes> getAllAuditTypes() {
        return this.auditTypesRepository.findAll();
    }

    @Override
    public void saveAuditTypes(AuditTypes auditTypes) {
        this.auditTypesRepository.save(auditTypes);
    }

    @Override
    public AuditTypes getAuditTypes(long id) {
        Optional<AuditTypes> optional = auditTypesRepository.findById(id);
        AuditTypes auditTypes = null;
        if (optional.isPresent()) {
            auditTypes = optional.get();
        } else {
            throw new RuntimeException("AuditTypes not found for id : " + id);
        }
        return auditTypes;
    }

    @Override
    public void deleteAuditTypes(long id) {
        this.auditTypesRepository.deleteById(id);
    }

    @Override
    public AuditTypes findAuditTypesByName(String name) {
        AuditTypes auditTypes = auditTypesRepository.findByName(name);
        return auditTypes;
    }

    @Override
    public Page<AuditTypes> getAuditTypesForDatatable(String queryString, Pageable pageable) {
        AuditTypesDataFilter auditTypesDataFilter = new AuditTypesDataFilter(queryString);
        return auditTypesRepository.findAll(auditTypesDataFilter, pageable);
    }

}
