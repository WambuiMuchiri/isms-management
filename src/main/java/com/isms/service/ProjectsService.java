package com.isms.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.isms.model.Projects;

public interface ProjectsService {
	
	List<Projects> getAllProjects();
	
	Projects saveProject(Projects project);
	
	Projects getProject(int id);
	
	void deleteProject(int id);
	
	Page<Projects> getProjectsForDatatable(String queryString, Pageable pageable);

}
