package com.isms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.isms.model.Users;

public interface UserRepository extends JpaRepository<Users, Integer>, JpaSpecificationExecutor<Users> {

	Users findByUsername(String username);
	Users findByEmail(String email);
}
