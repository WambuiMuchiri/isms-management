//package com.isms.service.impl;
//
//import java.sql.Date;
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.isms.filter.AssignmentDataFilter;
//import com.isms.model.Assignment;
//import com.isms.model.Installations;
//import com.isms.model.Personnels;
//import com.isms.repository.AssignmentRepository;
//import com.isms.repository.InstallationsRepository;
//import com.isms.repository.PersonnelsRepository;
//import com.isms.service.AssignmentService;
//
//@Service
//public class AssignmentServiceImpl implements AssignmentService {
//
//    @Autowired
//    private AssignmentRepository assignmentRepository;
//    
//    @Autowired
//    private InstallationsRepository installationsRepository;
//    
//    @Autowired
//    private PersonnelsRepository personnelsRepository;
//
//    @Override
//    public List<Assignment> getAllAssignment() {
//        return assignmentRepository.findAll();
//    }
//
//    @Override
//    public Assignment saveAssignment(Assignment assignment) {
//        return assignmentRepository.save(assignment);
//    }
//
//    @Override
//    public Assignment getAssignment(int id) {
//        Optional<Assignment> optional = assignmentRepository.findById(id);
//        Assignment assignment = null;
//        if (optional.isPresent()) {
//            assignment = optional.get();
//        } else {
//            throw new RuntimeException("Installation-Personnel assignment not found for id: " + id);
//        }
//        return assignment;
//    }
//
//    @Override
//    public void deleteAssignment(int id) {
//        assignmentRepository.deleteById(id);
//    }
//
//    @Override
//    public List<Assignment> getAssignmentByInstallation(int installationId) {
//        return assignmentRepository.findByInstallationId(installationId);
//    }
//
//    @Override
//    public List<Assignment> getAssignmentByPersonnel(int personnelId) {
//        return assignmentRepository.findByPersonnelId(personnelId);
//    }
//
//    @Override
//    @Transactional
//    public boolean assignPersonnelToInstallation(int installationId, int personnelId) {
//        // Check if both installation and personnel exist
//        Optional<Installations> installationOpt = installationsRepository.findById(installationId);
//        Optional<Personnels> personnelOpt = personnelsRepository.findById(personnelId);
//        
//        if (!installationOpt.isPresent() || !personnelOpt.isPresent()) {
//            return false;
//        }
//        
//        // Check if the assignment already exists
//        if (assignmentRepository.existsByInstallationIdAndPersonnelId(installationId, personnelId)) {
//            return false; // Already assigned
//        }
//        
//        // Create new assignment
//        Assignment assignment = new Assignment();
//        assignment.setInstallation(installationOpt.get());
//        assignment.setPersonnel(personnelOpt.get());
//        assignment.setAssignmentDate(Date.valueOf(LocalDate.now()));
//        
//        assignmentRepository.save(assignment);
//        return true;
//    }
//
//    @Override
//    @Transactional
//    public boolean removePersonnelFromInstallation(int installationId, int personnelId) {
//        // Check if the assignment exists
//        if (!assignmentRepository.existsByInstallationIdAndPersonnelId(installationId, personnelId)) {
//            return false; // Not assigned
//        }
//        
//        assignmentRepository.deleteByInstallationIdAndPersonnelId(installationId, personnelId);
//        return true;
//    }
//
//    @Override
//    public List<Personnels> getPersonnelsByInstallationId(int installationId) {
//        List<Assignment> assignments = assignmentRepository.findByInstallationId(installationId);
//        return assignments.stream()
//                          .map(Assignment::getPersonnel)
//                          .collect(Collectors.toList());
//    }
//
//    @Override
//    public List<Installations> getInstallationsByPersonnelId(int personnelId) {
//        List<Assignment> assignments = assignmentRepository.findByPersonnelId(personnelId);
//        return assignments.stream()
//                          .map(Assignment::getInstallation)
//                          .collect(Collectors.toList());
//    }
//
//    @Override
//    public Page<Assignment> getAssignmentForDatatable(String queryString, Pageable pageable) {
//        AssignmentDataFilter filter = new AssignmentDataFilter(queryString);
//        return assignmentRepository.findAll(filter, pageable);
//    }
//}


package com.isms.service.impl;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.isms.filter.AssignmentDataFilter;
import com.isms.model.Assignment;
import com.isms.model.AuditIdentifier;
import com.isms.model.Installations;
import com.isms.model.Personnels;
import com.isms.repository.AssignmentRepository;
import com.isms.repository.AuditIdentifierRepository;
import com.isms.repository.EquipmentsRepository;
import com.isms.repository.InstallationsRepository;
import com.isms.repository.PersonnelsRepository;
import com.isms.service.AssignmentService;

@Service
public class AssignmentServiceImpl implements AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;
    
    @Autowired
    private InstallationsRepository installationsRepository;
    
    @Autowired
    private PersonnelsRepository personnelsRepository;
    
    @Autowired
    private EquipmentsRepository equipmentsRepository;
    
    @Autowired
    private AuditIdentifierRepository auditIdentifierRepository;

    @Override
    public List<Assignment> getAllAssignment() {
        return assignmentRepository.findAll();
    }

    @Override
    public Assignment saveAssignment(Assignment assignment) {
        return assignmentRepository.save(assignment);
    }

    @Override
    public Assignment getAssignment(int id) {
        Optional<Assignment> optional = assignmentRepository.findById(id);
        Assignment assignment = null;
        if (optional.isPresent()) {
            assignment = optional.get();
        } else {
            throw new RuntimeException("Installation-Personnel assignment not found for id: " + id);
        }
        return assignment;
    }

    @Override
    public void deleteAssignment(int id) {
        assignmentRepository.deleteById(id);
    }

    @Override
    public List<Assignment> getAssignmentByInstallation(int installationId) {
        return assignmentRepository.findByInstallationId(installationId);
    }

    @Override
    public List<Assignment> getAssignmentByPersonnel(int personnelId) {
        return assignmentRepository.findByPersonnelId(personnelId);
    }
    
    @Override
    public List<Assignment> getAssignmentByEquipment(int equipmentId) {
    	return assignmentRepository.findByEquipmentId(equipmentId);
    }

    @Override
    @Transactional
    public boolean assignPersonnelToInstallation(int installationId, int personnelId, long auditIdentifierId, String remarks) {
        // Check if both installation and personnel exist
        Optional<Installations> installationOpt = installationsRepository.findById(installationId);
        Optional<Personnels> personnelOpt = personnelsRepository.findById(personnelId);
        Optional<AuditIdentifier> auditOpt = auditIdentifierRepository.findById(auditIdentifierId);
        
        if (!installationOpt.isPresent() || !personnelOpt.isPresent() || !auditOpt.isPresent()) {
            return false;
        }
        
        // Check if the assignment already exists
        if (assignmentRepository.existsByInstallationIdAndPersonnelId(installationId, personnelId)) {
            return false; // Already assigned
        }
        
        // Create new assignment
        Assignment assignment = new Assignment();
        assignment.setInstallation(installationOpt.get());
        assignment.setPersonnel(personnelOpt.get());
        assignment.setAuditIdentifierId(auditOpt.get());
        assignment.setAssignmentDate(Date.valueOf(LocalDate.now()));
        assignment.setRemarks(remarks);
        
        assignmentRepository.save(assignment);
        return true;
    }

    @Override
    @Transactional
    public boolean removePersonnelFromInstallation(int installationId, int personnelId) {
        // Check if the assignment exists
        if (!assignmentRepository.existsByInstallationIdAndPersonnelId(installationId, personnelId)) {
            return false; // Not assigned
        }
        
        assignmentRepository.deleteByInstallationIdAndPersonnelId(installationId, personnelId);
        return true;
    }

    @Override
    public List<Personnels> getPersonnelsByInstallationId(int installationId) {
        List<Assignment> assignments = assignmentRepository.findByInstallationId(installationId);
        return assignments.stream()
                          .map(Assignment::getPersonnel)
                          .collect(Collectors.toList());
    }

    @Override
    public List<Installations> getInstallationsByPersonnelId(int personnelId) {
        List<Assignment> assignments = assignmentRepository.findByPersonnelId(personnelId);
        return assignments.stream()
                          .map(Assignment::getInstallation)
                          .collect(Collectors.toList());
    }

    @Override
    public Page<Assignment> getAssignmentForDatatable(String queryString, Pageable pageable) {
        AssignmentDataFilter filter = new AssignmentDataFilter(queryString);
        return assignmentRepository.findAll(filter, pageable);
    }
}