package com.isms.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.isms.exception.ResourceNotFoundException;
import com.isms.filter.UsersDataFilter;
import com.isms.model.Roles;
import com.isms.model.Users;
import com.isms.repository.RolesRepository;
import com.isms.repository.UserRepository;
import com.isms.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
	private UserRepository userRepository;
	public UserServiceImpl(UserRepository userRepository) {
		super();
		this.userRepository=userRepository;
	}
	
	@Autowired
	private RolesRepository roleRepository;
	
	@Autowired
	BCryptPasswordEncoder encoder;
	
	@Override
	public Users saveUser(Users user) {
		//user.setPassword(encoder.encode(user.getPassword()));
		//user.setPasswordConfirm(encoder.encode(user.getPasswordConfirm()));
		return userRepository.save(user);
	}
	
	@Override
	public List<Users> getAllUsers() {
		return userRepository.findAll();
	}
	
	@Override
	public Users getUserById(int id) {
		//check whether a user exists in the DB or not
		return userRepository.findById(id).orElseThrow(()->
		new ResourceNotFoundException("User does not exist with the userId : ", "Id", id));
	}

	@Override
	public Users getUserByEmail(String email) {
	    return userRepository.findByEmail(email);
	}
	
	@Override
	public Users updateUser(Users user, int id) {
		//First of all check whether a user exists or not in the DB with the userId
		Users existingUser = userRepository.findById(id).orElseThrow(()->
		new ResourceNotFoundException("User does not exist with the userId : ", "Id", id));
		
		existingUser.setFirstName(user.getFirstName());
		existingUser.setLastName(user.getLastName());
		existingUser.setUsername(user.getUsername());
		existingUser.setEmail(user.getEmail());
		existingUser.setStatus(user.getStatus());
		existingUser.setPassword(user.getPassword());
		//existingUser.setPasswordConfirm(user.getPasswordConfirm());
		existingUser.setRoles(user.getRoles());
		existingUser.setRemarks(user.getRemarks());

		//save the user data into the DB
		userRepository.save(existingUser);
		return existingUser;
	}

	@Override
	public void deleteUser(int id) {
		//check whether the user exists in the DB or not with the userId
		userRepository.findById(id).orElseThrow(()->
		new ResourceNotFoundException("User does not exist with the userId : ", "Id", id));
		userRepository.deleteById(id);
	}
	
	@Override
	public void addRoleToUser(String username, String roleName) {
		Users user = userRepository.findByUsername(username);
		Roles role = roleRepository.findByName(roleName);
		log.info("adding new {} role to {} user", role.getName(), user.getUsername());
		user.getRoles().add(role);
		
	}
	@Override
	public Users getUserOld(int id) {
		//Need refinement
        Map<Integer, Users> users = new HashMap<>();
        userRepository.findAll().forEach(user -> {
            users.put(user.getId(), user);
        });
        Users user = users.get(id);
        if (user == null) {
            throw new RuntimeException("Users not found for id : " + id);
        }
        return user;
    }
	@Override
	public Page<Users> getUsersForDatatable(String queryString, Pageable pageable) {
		UsersDataFilter usersDataFilter = new UsersDataFilter(queryString);
        return userRepository.findAll(usersDataFilter, pageable);

	}
	
	
	@Override
	public Users getUser(String username) {
		return userRepository.findByUsername(username);
	}


}
