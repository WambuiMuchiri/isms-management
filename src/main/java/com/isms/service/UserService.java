package com.isms.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.isms.model.Users;

public interface UserService {
	
	Users saveUser(Users user);
	
	List<Users> getAllUsers();
	
	Users getUserById(int id);
	
	Users getUserByEmail(String email);
	
    Users getUserOld(int id);
	
	Users updateUser(Users user, int id);
	
	void deleteUser(int id);
	
	void addRoleToUser(String username, String roleName);

    Page<Users> getUsersForDatatable(String queryString, Pageable pageable);
    
    Users getUser(String username);
		


}
