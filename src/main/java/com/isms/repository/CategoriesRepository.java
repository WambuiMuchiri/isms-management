package com.isms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.isms.model.Categories;

public interface CategoriesRepository extends JpaRepository<Categories, Integer> , JpaSpecificationExecutor<Categories>{
	
}
