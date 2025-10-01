package com.isms.api;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.isms.helper.SaveEventsHelper;
import com.isms.model.Assignment;
import com.isms.model.AuditIdentifier;
import com.isms.model.Installations;
import com.isms.model.Personnels;
import com.isms.service.AssignmentService;
import com.isms.service.AuditEventsService;
import com.isms.service.AuditIdentifierService;
import com.isms.service.AuditTypesService;
import com.isms.service.InstallationsService;
import com.isms.service.PersonnelsService;
import com.isms.service.RequestService;
import com.isms.utils.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentApi {

    @Autowired
    private AssignmentService assignmentService;
    
    @Autowired
    private AuditIdentifierService auditIdentifierService;
    
    @Autowired
    private AuditEventsService auditEventsService;
    
    @Autowired
    private AuditTypesService auditTypesService;
    
    @Autowired
    private RequestService requestService;
    
    @Autowired
    private InstallationsService installationsService;
    
    @Autowired
    private PersonnelsService personnelsService;
    
    @GetMapping
    public ResponseEntity<List<Assignment>> getAllAssignments() {
        // Create audit identifier for this query operation
        AuditIdentifier auditIdentifier = new AuditIdentifier();
        auditIdentifierService.saveAuditIdentifier(auditIdentifier);
        
        List<Assignment> assignments = assignmentService.getAllAssignment();
        
        // No need to log read operations if that's your policy
        // But you could add it here if needed
        
        return ResponseEntity.ok(assignments);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Assignment> getAssignmentById(
            @PathVariable int id,
            HttpServletRequest request,
            @RequestHeader(value = "User-Agent") String userAgent) {
        try {
            // Create audit identifier for this query operation
            AuditIdentifier auditIdentifier = new AuditIdentifier();
            auditIdentifierService.saveAuditIdentifier(auditIdentifier);
            
            Assignment assignment = assignmentService.getAssignment(id);
            
            // Log the read operation
            SaveEventsHelper saveEventsHelper = new SaveEventsHelper(
                new Date(),
                "Assignment details retrieved for ID: " + id,
                requestService.getClientIp(request),
                userAgent,
                auditIdentifier,
                auditTypesService.findAuditTypesByName("READ")
            );
            
            auditEventsService.saveAuditEvents(saveEventsHelper);
            
            return ResponseEntity.ok(assignment);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse> createAssignment(
            @RequestParam("installationId") int installationId,
            @RequestParam("personnelId") int personnelId,
            @RequestParam(value = "remarks", required = false) String remarks,
            HttpServletRequest request,
            @RequestHeader(value = "User-Agent") String userAgent) {
        
        try {
            // Create a new AuditIdentifier for this assignment
            AuditIdentifier auditIdentifier = new AuditIdentifier();
            auditIdentifierService.saveAuditIdentifier(auditIdentifier);
            
            // Fetch related entities for logging
            Installations installation = installationsService.getInstallation(installationId);
            Personnels personnel = personnelsService.getPersonnel(personnelId);
            
            if (installation == null || personnel == null) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Installation or Personnel not found"));
            }
            
            // Create the assignment with the audit identifier
            boolean success = assignmentService.assignPersonnelToInstallation(
                installationId, personnelId, auditIdentifier.getIdentifierId(), remarks);
            
            if (success) {
                // Log the audit event
                SaveEventsHelper saveEventsHelper = new SaveEventsHelper(
                    new Date(),
                    "New Assignment created: " + personnel.getPersonnelName() + " assigned to " + installation.getInstallationName(),
                    requestService.getClientIp(request),
                    userAgent,
                    auditIdentifier,
                    auditTypesService.findAuditTypesByName("CREATE")
                );
                
                auditEventsService.saveAuditEvents(saveEventsHelper);
                
                return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse(true, "Assignment created successfully", auditIdentifier.getIdentifierId()));
            } else {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Failed to create assignment"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(false, "Error creating assignment: " + e.getMessage()));
        }
    }
    
    @DeleteMapping
    public ResponseEntity<ApiResponse> deleteAssignment(
            @RequestParam("installationId") int installationId,
            @RequestParam("personnelId") int personnelId,
            HttpServletRequest request,
            @RequestHeader(value = "User-Agent") String userAgent) {
        
        try {
            // Fetch the assignment first for audit logging
            List<Assignment> assignments = assignmentService.getAssignmentByInstallation(installationId);
            Assignment targetAssignment = assignments.stream()
                .filter(a -> a.getPersonnel().getId() == personnelId)
                .findFirst()
                .orElse(null);
            
            if (targetAssignment == null) {
                return ResponseEntity.notFound().build();
            }
            
            // Create a new AuditIdentifier for the delete operation
            AuditIdentifier auditIdentifier = new AuditIdentifier();
            auditIdentifierService.saveAuditIdentifier(auditIdentifier);
            
            // Get the related entities for logging
            Installations installation = targetAssignment.getInstallation();
            Personnels personnel = targetAssignment.getPersonnel();
            
            // Remove the assignment
            boolean success = assignmentService.removePersonnelFromInstallation(installationId, personnelId);
            
            if (success) {
                // Log the audit event
                SaveEventsHelper saveEventsHelper = new SaveEventsHelper(
                    new Date(),
                    "Assignment deleted: " + personnel.getPersonnelName() + " from " + installation.getInstallationName(),
                    requestService.getClientIp(request),
                    userAgent,
                    auditIdentifier,
                    auditTypesService.findAuditTypesByName("DELETE")
                );
                
                auditEventsService.saveAuditEvents(saveEventsHelper);
                
                return ResponseEntity.ok(new ApiResponse(true, "Assignment deleted successfully"));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(false, "Error deleting assignment: " + e.getMessage()));
        }
    }
    
    @GetMapping("/installation/{installationId}")
    public ResponseEntity<List<Assignment>> getAssignmentsByInstallation(@PathVariable int installationId) {
        // Create audit identifier for this query operation
        AuditIdentifier auditIdentifier = new AuditIdentifier();
        auditIdentifierService.saveAuditIdentifier(auditIdentifier);
        
        return ResponseEntity.ok(assignmentService.getAssignmentByInstallation(installationId));
    }
    
    @GetMapping("/personnel/{personnelId}")
    public ResponseEntity<List<Assignment>> getAssignmentsByPersonnel(@PathVariable int personnelId) {
        // Create audit identifier for this query operation
        AuditIdentifier auditIdentifier = new AuditIdentifier();
        auditIdentifierService.saveAuditIdentifier(auditIdentifier);
        
        return ResponseEntity.ok(assignmentService.getAssignmentByPersonnel(personnelId));
    }
    
    @GetMapping("/installation/{installationId}/personnel")
    public ResponseEntity<List<Personnels>> getPersonnelsByInstallation(
            @PathVariable int installationId,
            HttpServletRequest request,
            @RequestHeader(value = "User-Agent") String userAgent) {
        
        // Create audit identifier
        AuditIdentifier auditIdentifier = new AuditIdentifier();
        auditIdentifierService.saveAuditIdentifier(auditIdentifier);
        
        // Get installation for logging
        Installations installation = installationsService.getInstallation(installationId);
        
        if (installation != null) {
            // Log the read operation
            SaveEventsHelper saveEventsHelper = new SaveEventsHelper(
                new Date(),
                "Retrieved personnel list for installation: " + installation.getInstallationName(),
                requestService.getClientIp(request),
                userAgent,
                auditIdentifier,
                auditTypesService.findAuditTypesByName("READ")
            );
            
            auditEventsService.saveAuditEvents(saveEventsHelper);
        }
        
        return ResponseEntity.ok(assignmentService.getPersonnelsByInstallationId(installationId));
    }
    
    @GetMapping("/personnel/{personnelId}/installations")
    public ResponseEntity<List<Installations>> getInstallationsByPersonnel(
            @PathVariable int personnelId,
            HttpServletRequest request,
            @RequestHeader(value = "User-Agent") String userAgent) {
        
        // Create audit identifier
        AuditIdentifier auditIdentifier = new AuditIdentifier();
        auditIdentifierService.saveAuditIdentifier(auditIdentifier);
        
        // Get personnel for logging
        Personnels personnel = personnelsService.getPersonnel(personnelId);
        
        if (personnel != null) {
            // Log the read operation
            SaveEventsHelper saveEventsHelper = new SaveEventsHelper(
                new Date(),
                "Retrieved installation list for personnel: " + personnel.getPersonnelName(),
                requestService.getClientIp(request),
                userAgent,
                auditIdentifier,
                auditTypesService.findAuditTypesByName("READ")
            );
            
            auditEventsService.saveAuditEvents(saveEventsHelper);
        }
        
        return ResponseEntity.ok(assignmentService.getInstallationsByPersonnelId(personnelId));
    }
    
    @GetMapping("/datatable")
    public ResponseEntity<Page<Assignment>> getAssignmentsForDatatable(
            @RequestParam(value = "search", required = false, defaultValue = "") String search,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "id,asc") String sort,
            HttpServletRequest request,
            @RequestHeader(value = "User-Agent") String userAgent) {
        
        // Create audit identifier
        AuditIdentifier auditIdentifier = new AuditIdentifier();
        auditIdentifierService.saveAuditIdentifier(auditIdentifier);
        
        String[] sortParams = sort.split(",");
        String sortField = sortParams[0];
        Sort.Direction direction = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc") ? 
                                   Sort.Direction.DESC : Sort.Direction.ASC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        Page<Assignment> assignments = assignmentService.getAssignmentForDatatable(search, pageable);
        
        // Log the datatable query
        SaveEventsHelper saveEventsHelper = new SaveEventsHelper(
            new Date(),
            "Assignment datatable query - Page: " + page + ", Size: " + size + ", Sort: " + sort + ", Search: " + search,
            requestService.getClientIp(request),
            userAgent,
            auditIdentifier,
            auditTypesService.findAuditTypesByName("READ")
        );
        
        auditEventsService.saveAuditEvents(saveEventsHelper);
        
        return ResponseEntity.ok(assignments);
    }
    
    /**
     * Bulk assign personnel to multiple installations
     */
    @PostMapping("/bulk")
    public ResponseEntity<ApiResponse> bulkAssignPersonnel(
            @RequestParam("personnelId") int personnelId,
            @RequestParam("installationIds") List<Integer> installationIds,
            @RequestParam(value = "remarks", required = false) String remarks,
            HttpServletRequest request,
            @RequestHeader(value = "User-Agent") String userAgent) {
        
        // Create audit identifier for the bulk operation
        AuditIdentifier auditIdentifier = new AuditIdentifier();
        auditIdentifierService.saveAuditIdentifier(auditIdentifier);
        
        try {
            Personnels personnel = personnelsService.getPersonnel(personnelId);
            if (personnel == null) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Personnel not found"));
            }
            
            StringBuilder successLog = new StringBuilder();
            StringBuilder failedLog = new StringBuilder();
            int successCount = 0;
            
            for (Integer installationId : installationIds) {
                Installations installation = installationsService.getInstallation(installationId);
                if (installation == null) {
                    failedLog.append("Installation ID ").append(installationId).append(" not found. ");
                    continue;
                }
                
                boolean success = assignmentService.assignPersonnelToInstallation(
                    installationId, personnelId, auditIdentifier.getIdentifierId(), remarks);
                
                if (success) {
                    successCount++;
                    successLog.append(installation.getInstallationName()).append(", ");
                } else {
                    failedLog.append(installation.getInstallationName()).append(", ");
                }
            }
            
            // Trim trailing commas and spaces
            if (successLog.length() > 0) {
                successLog.setLength(successLog.length() - 2);
            }
            if (failedLog.length() > 0) {
                failedLog.setLength(failedLog.length() - 2);
            }
            
            // Log the audit event
            String auditDescription = "Bulk assignment: " + personnel.getPersonnelName() + 
                                     " assigned to " + successCount + " installations.";
            if (successLog.length() > 0) {
                auditDescription += " Success: " + successLog.toString() + ".";
            }
            if (failedLog.length() > 0) {
                auditDescription += " Failed: " + failedLog.toString() + ".";
            }
            
            SaveEventsHelper saveEventsHelper = new SaveEventsHelper(
                new Date(),
                auditDescription,
                requestService.getClientIp(request),
                userAgent,
                auditIdentifier,
                auditTypesService.findAuditTypesByName("CREATE")
            );
            
            auditEventsService.saveAuditEvents(saveEventsHelper);
            
            if (successCount > 0) {
                return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse(true, 
                          "Successfully assigned " + successCount + " out of " + installationIds.size() + " installations", 
                          auditIdentifier.getIdentifierId()));
            } else {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Failed to create any assignments"));
            }
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(false, "Error creating bulk assignments: " + e.getMessage()));
        }
    }
}