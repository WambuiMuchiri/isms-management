package com.isms.controller;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.isms.dto.AssignmentDTO;
import com.isms.helper.SaveEventsHelper;
import com.isms.messages.EmailTemplate;
import com.isms.model.Assignment;
import com.isms.model.AuditIdentifier;
import com.isms.model.Equipments;
import com.isms.model.Installations;
import com.isms.model.Personnels;
import com.isms.service.AssignmentService;
import com.isms.service.AuditEventsService;
import com.isms.service.AuditIdentifierService;
import com.isms.service.AuditTypesService;
import com.isms.service.EmailService;
import com.isms.service.EquipmentsService;
import com.isms.service.InstallationsService;
import com.isms.service.MailTemplateService;
import com.isms.service.PersonnelsService;
import com.isms.service.RequestService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/assignments")
public class AssignmentController {

	@Autowired
	private AssignmentService assignmentService;
	@Autowired
	private InstallationsService installationsService;
	@Autowired
	private PersonnelsService personnelsService;
	@Autowired
	private EquipmentsService equipmentsService;
	@Autowired
	private AuditIdentifierService auditIdentifierService;
	@Autowired
	private AuditEventsService auditEventsService;
	@Autowired
	private AuditTypesService auditTypesService;
	@Autowired
	private RequestService requestService;
	@Autowired
	public MailTemplateService mailTemplateService;

	private final Map<Integer, String> creatorMap = new HashMap<>();

	@GetMapping(value = { "/index", "/", "" })
	public String viewAssignments(@RequestParam(required = false) Integer installationId,
			@RequestParam(required = false) Integer personnelId, @RequestParam(required = false) Integer equipmentId,
			@RequestParam(required = false) Integer auditId, Model model) {

		List<Assignment> assignments = getFilteredAssignments(installationId, personnelId, equipmentId);

		if (auditId != null) {
			assignments.removeIf(a -> a.getAuditIdentifierId().getIdentifierId() != auditId);
		}

		populateCreatorMap(assignments);
		addDropdownDataToModel(model);
		model.addAttribute("assignments", assignments);
		model.addAttribute("assignmentDTO", new AssignmentDTO());
		addFilterSelectionToModel(model, installationId, personnelId, equipmentId, auditId);

		return "assignments/assignment";
	}

	@ModelAttribute("creatorHelper")
	public Function<Assignment, String> creatorHelper() {
		return assignment -> {
			if (assignment.getAuditIdentifierId() == null || creatorMap == null)
				return "System";
			Integer id = assignment.getAuditIdentifierId().getIdentifierId();
			return creatorMap.getOrDefault(id, "System");
		};
	}

	@GetMapping("/batch")
	public String batchAssignmentForm(Model model) {
		addDropdownDataToModel(model);
		List<Assignment> recentAssignments = assignmentService.getAllAssignment().stream()
				.sorted(Comparator.comparingInt(Assignment::getId).reversed()).limit(10).toList();
		populateCreatorMap(recentAssignments);
		model.addAttribute("assignments", recentAssignments);
		model.addAttribute("assignmentDTO", new AssignmentDTO());
		return "assignments/batch_assignment";
	}

	@PostMapping("/batch-create")
	public String batchCreateAssignments(@Valid @ModelAttribute("assignmentDTO") AssignmentDTO assignmentDTO,
			BindingResult bindingResult, @RequestParam List<Integer> selectedPersonnelIds,
			@RequestParam List<Integer> selectedEquipmentIds, RedirectAttributes redirectAttributes,
			@RequestHeader("User-Agent") String userAgent, HttpServletRequest request, Model model) {

		if (bindingResult.hasErrors()) {
			addDropdownDataToModel(model);
			model.addAttribute("error", "Please fix the validation errors.");
			return "assignments/batch_assignment";
		}

		if (selectedPersonnelIds.isEmpty() || selectedEquipmentIds.isEmpty()) {
			redirectAttributes.addFlashAttribute("error", "Personnel and Equipment selections are required.");
			return "redirect:/assignments/batch";
		}

		Installations installation = installationsService.getInstallation(assignmentDTO.getInstallationId());
		if (installation == null) {
			redirectAttributes.addFlashAttribute("error", "Selected installation not found.");
			return "redirect:/assignments/batch";
		}

		AuditIdentifier batchAuditIdentifier = new AuditIdentifier();
		auditIdentifierService.saveAuditIdentifier(batchAuditIdentifier);

		auditEventsService.saveAuditEvents(new SaveEventsHelper(new Date(),
				"Batch assignment started for installation: " + installation.getInstallationName() + " with equipment: "
						+ selectedEquipmentIds + " and " + selectedPersonnelIds.size() + " personnel",
				requestService.getClientIp(request), userAgent, batchAuditIdentifier,
				auditTypesService.findAuditTypesByName("CREATE")));

		int successCount = 0;

		for (Integer equipmentId : selectedEquipmentIds) {
			Equipments equipment = equipmentsService.getEquipment(equipmentId);
			if (equipment == null)
				continue;

			for (Integer personnelId : selectedPersonnelIds) {
				try {
					Personnels personnel = personnelsService.getPersonnel(personnelId);
					if (personnel == null)
						continue;

					AuditIdentifier auditIdentifier = new AuditIdentifier();
					auditIdentifierService.saveAuditIdentifier(auditIdentifier);

					Assignment assignment = new Assignment();
					assignment.setInstallation(installation);
					assignment.setPersonnel(personnel);
					assignment.setEquipment(equipment);
					assignment.setRemarks(assignmentDTO.getRemarks());
					assignment.setAssignmentDate(assignmentDTO.getAssignmentDate());
					assignment.setAuditIdentifierId(auditIdentifier);

					assignmentService.saveAssignment(assignment);

					auditEventsService.saveAuditEvents(new SaveEventsHelper(new Date(),
							"Assignment created: " + personnel.getPersonnelName() + " assigned to "
									+ installation.getInstallationName() + " with equipment "
									+ equipment.getEquipmentName() + " (Batch)",
							requestService.getClientIp(request), userAgent, auditIdentifier,
							auditTypesService.findAuditTypesByName("CREATE")));

					if (assignmentDTO.isNotifyPersonnel()) {
						System.out.println("Notify: " + personnel.getPersonnelName());

						String formatted = assignmentDTO.getAssignmentDate().toString();
						
						Map<String, Object> modelVars = new HashMap<>();
												
						modelVars.put("subject", "NEW TASK - ISMS");
						modelVars.put("clients", personnel.getPersonnelName());
						modelVars.put("payload", assignmentDTO.getRemarks());
						modelVars.put("date", assignmentDTO.getAssignmentDate()); 
//						modelVars.put("date", formatted);
						modelVars.put("actionUrl",
						    request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
						    + "/assignments/view/" + assignment.getId());
						modelVars.put("senderName", "ISMS Team");
						modelVars.put("logoUrl", /* optional */ null); // e.g., https://yourcdn/logo.png

						String html = mailTemplateService.renderAssignment(modelVars);
							
					}
					successCount++;
				} catch (Exception e) {
					System.err.println("Error for personnel ID " + personnelId + ": " + e.getMessage());
				}
			}
		}

		auditEventsService.saveAuditEvents(new SaveEventsHelper(new Date(),
				"Batch completed: " + successCount + " of " + selectedPersonnelIds.size() + " processed",
				requestService.getClientIp(request), userAgent, batchAuditIdentifier,
				auditTypesService.findAuditTypesByName("CREATE")));

		redirectAttributes.addFlashAttribute(successCount > 0 ? "success" : "error",
				successCount > 0 ? successCount + " assignments created successfully!"
						: "Failed to create assignments.");

		return "redirect:/assignments/index";
	}

	@PostMapping("/delete")
	public String deleteAssignment(@RequestParam Integer id, RedirectAttributes redirectAttributes,
			@RequestHeader("User-Agent") String userAgent, HttpServletRequest request) {

		Assignment assignment = assignmentService.getAssignment(id);

		if (assignment != null) {
			AuditIdentifier auditIdentifier = new AuditIdentifier();
			auditIdentifierService.saveAuditIdentifier(auditIdentifier);

			auditEventsService.saveAuditEvents(new SaveEventsHelper(new Date(),
					"Assignment deleted: " + assignment.getPersonnel().getPersonnelName() + " from "
							+ assignment.getInstallation().getInstallationName(),
					requestService.getClientIp(request), userAgent, auditIdentifier,
					auditTypesService.findAuditTypesByName("DELETE")));

			assignmentService.deleteAssignment(id);
			redirectAttributes.addFlashAttribute("success", "Assignment removed successfully!");
		} else {
			redirectAttributes.addFlashAttribute("error", "Assignment not found!");
		}
		return "redirect:/assignments/index";
	}

	@ModelAttribute("hasValidAuditData")
	public boolean hasValidAuditData() {
		return auditIdentifierService.getAllAuditIdentifiers().stream()
				.anyMatch(audit -> audit != null && auditEventsService.getAuditEvent(audit.getIdentifierId()) != null);
	}

	private void addDropdownDataToModel(Model model) {
		model.addAttribute("installations", installationsService.getAllInstallations());
		model.addAttribute("personnels", personnelsService.getAllPersonnels());
		model.addAttribute("equipments", equipmentsService.getAllEquipments());
	}

	private void addFilterSelectionToModel(Model model, Integer instId, Integer persId, Integer eqId, Integer auditId) {
		if (instId != null)
			model.addAttribute("selectedInstallation", instId);
		if (persId != null)
			model.addAttribute("selectedPersonnel", persId);
		if (eqId != null)
			model.addAttribute("selectedEquipment", eqId);
		if (auditId != null)
			model.addAttribute("selectedAudit", auditId);
	}

	private void populateCreatorMap(List<Assignment> assignments) {
		for (Assignment assignment : assignments) {
			if (assignment.getAuditIdentifierId() != null) {
				Integer id = assignment.getAuditIdentifierId().getIdentifierId();
				try {
					String creator = auditEventsService.getCreatorByAuditIdentifierId(id);
					creatorMap.put(id, creator != null ? creator : "System");
				} catch (Exception e) {
					creatorMap.put(id, "Not-Found");
				}
			}
		}
	}

	private List<Assignment> getFilteredAssignments(Integer installationId, Integer personnelId, Integer equipmentId) {
		if (installationId != null && personnelId != null) {
			List<Assignment> list = assignmentService.getAssignmentByInstallation(installationId);
			list.removeIf(a -> a.getPersonnel().getId() != personnelId);
			return list;
		} else if (installationId != null) {
			return assignmentService.getAssignmentByInstallation(installationId);
		} else if (personnelId != null) {
			return assignmentService.getAssignmentByPersonnel(personnelId);
		} else if (equipmentId != null) {
			return assignmentService.getAssignmentByEquipment(equipmentId);
		}
		return assignmentService.getAllAssignment();
	}

}