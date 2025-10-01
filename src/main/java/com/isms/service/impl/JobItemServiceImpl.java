// com.isms.service.JobItemService
package com.isms.service.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.isms.model.Assignment;
import com.isms.model.Clients;
import com.isms.model.Equipments;
import com.isms.model.Installations;
import com.isms.model.JobItem;
import com.isms.model.Projects;
import com.isms.model.Sites;
import com.isms.repository.AssignmentRepository; // assume you have this
import com.isms.repository.JobItemRepository;
import com.isms.service.JobCodeService;
import com.isms.service.JobItemService;

@Service
public class JobItemServiceImpl implements JobItemService {
    @Autowired
	public JobItemRepository jobItemRepo;
    @Autowired
    public AssignmentRepository assignmentRepo;
    @Autowired
    public JobCodeService jobCodeService;


    @Transactional
    public JobItem createForAssignment(Integer assignmentId) {
        Assignment a = assignmentRepo.findById(assignmentId)
            .orElseThrow(() -> new IllegalArgumentException("Assignment not found: " + assignmentId));

        Equipments eq = a.getEquipment();
        if (eq == null) throw new IllegalStateException("Assignment has no equipment");

        // project via installation -> site -> project
        Installations inst = a.getInstallation();
        if (inst == null) throw new IllegalStateException("Assignment has no installation");
        Sites site = inst.getSites();
        if (site == null) throw new IllegalStateException("Installation has no site");
        Projects project = site.getProjects();
        if (project == null) throw new IllegalStateException("Site has no project");
        Clients client = project.getClients();
        if (client == null) throw new IllegalStateException("Project has no client");

        BigDecimal equipmentCost = BigDecimal.valueOf(eq.getEquipmentValue() != null ? eq.getEquipmentValue() : 0.0);
        BigDecimal projectCost   = BigDecimal.valueOf(project.getProjectCost() != null ? project.getProjectCost() : 0.0);
                
        String jobCode = jobCodeService.nextJobCode(project.getId());

        JobItem item = new JobItem();
        item.setAssignment(a);
        item.setJobCode(jobCode);
        item.setEquipmentCost(equipmentCost);
        item.setProjectCost(projectCost);
        // totalCost auto-calculated in @PrePersist, but set explicitly is fine:
        item.setTotalCost(equipmentCost.add(projectCost));
        item.setClientId(client.getId());

        return jobItemRepo.save(item);
    }
}
