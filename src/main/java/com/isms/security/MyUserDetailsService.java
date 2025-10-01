package com.isms.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.isms.model.Users;
import com.isms.repository.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		System.out.println("Hi Using this User-name : "+username);
		Users users = userRepo.findByUsername(username);
		System.out.println("Hi Using this First-name : "+users.getFirstName());
		if (users == null)
			throw new UsernameNotFoundException("User 404");

		return new UserPrincipal(users);
	}

}
