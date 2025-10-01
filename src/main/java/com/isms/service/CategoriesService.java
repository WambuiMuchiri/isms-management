package com.isms.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.isms.model.Categories;

public interface CategoriesService {
	
	List<Categories> getAllCategories();
	
	void saveCategory(Categories category);
	
	Categories getCategory(int id);
	
	void deleteCategory(int id);
	
	Page<Categories> getCategoriesForDatatable(String queryString, Pageable pageable);

}
