package com.isms.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.isms.exception.ResourceNotFoundException;
import com.isms.filter.ProjectsDataFilter;
import com.isms.model.Projects;
import com.isms.repository.ProjectsRepository;
import com.isms.service.ProjectsService;

@Service
public class ProjectsServiceImpl implements ProjectsService{

	private ProjectsRepository projectsRepository;
	public ProjectsServiceImpl(ProjectsRepository projectsRepository) {
		super();
		this.projectsRepository = projectsRepository;
	}
	
	
	@Override
	public List<Projects> getAllProjects() {
		return this.projectsRepository.findAll();
	}
	
	
	@Override
	public Projects saveProject(Projects project) {
		return this.projectsRepository.save(project);
	}
	
	
	@Override
	public Projects getProject(int id) {
		Optional<Projects> optional = projectsRepository.findById(id);
        Projects project = null;
        if (optional.isPresent()) {
            project = optional.get();
        } else {
            throw new RuntimeException("Project not found for id : " + id);
        }
        return project;
    }
	
	
	@Override
	public void deleteProject(int id) {
		projectsRepository.findById(id).orElseThrow(()->
		new ResourceNotFoundException("Project does not exist in the db with the id : ", "Id", id));
		projectsRepository.deleteById(id);
		
	}
	
	
	@Override
	public Page<Projects> getProjectsForDatatable(String queryString, Pageable pageable) {
		ProjectsDataFilter projectsDataFilter = new ProjectsDataFilter(queryString);
		return projectsRepository.findAll(projectsDataFilter, pageable);
	}

}
