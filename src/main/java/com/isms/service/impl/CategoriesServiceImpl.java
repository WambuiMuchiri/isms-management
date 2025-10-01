package com.isms.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.isms.exception.ResourceNotFoundException;
import com.isms.filter.CategoriesDataFilter;
import com.isms.model.Categories;
import com.isms.repository.CategoriesRepository;
import com.isms.service.CategoriesService;

@Service
public class CategoriesServiceImpl implements CategoriesService{

	private CategoriesRepository categoriesRepository;
	public CategoriesServiceImpl(CategoriesRepository categoriesRepository) {
		super();
		this.categoriesRepository = categoriesRepository;
	}
	
	
	@Override
	public List<Categories> getAllCategories() {
		return this.categoriesRepository.findAll();
	}
	
	
	@Override
	public void saveCategory(Categories category) {
		this.categoriesRepository.save(category);
	}
	
	
	@Override
	public Categories getCategory(int id) {
		Optional<Categories> optional = categoriesRepository.findById(id);
        Categories category = null;
        if (optional.isPresent()) {
            category = optional.get();
        } else {
            throw new RuntimeException("Category not found for id : " + id);
        }
        return category;
    }
	
	
	@Override
	public void deleteCategory(int id) {
		categoriesRepository.findById(id).orElseThrow(()->
		new ResourceNotFoundException("Category does not exist in the db with the id : ", "Id", id));
		categoriesRepository.deleteById(id);
		
	}
	
	
	@Override
	public Page<Categories> getCategoriesForDatatable(String queryString, Pageable pageable) {
		CategoriesDataFilter categoriesDataFilter = new CategoriesDataFilter(queryString);
		return categoriesRepository.findAll(categoriesDataFilter, pageable);
	}

}
