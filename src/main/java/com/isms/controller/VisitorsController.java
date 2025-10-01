package com.isms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.isms.service.VisitorsService;

@Controller
@RequestMapping("/Visitors")
public class VisitorsController {

	@Autowired
	private VisitorsService visitorsService;
	
	@GetMapping("/visits")
	public String getAllVisitorsVisits(Model model) {
		model.addAttribute("listVisits", visitorsService.getAllVisits());
		return "visitors/visits_list";
	}
	
}
