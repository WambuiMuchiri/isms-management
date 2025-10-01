package com.isms.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.isms.model.Roles;
import com.isms.model.Users;

public class UserPrincipal implements UserDetails {

	private Users users;

	public UserPrincipal(Users users) {
		super();
		this.users = users;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		//return Collections.singleton(new SimpleGrantedAuthority("USER"));
		
		Set<Roles> roles = users.getRoles();
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
			for (Roles role : roles) {
				authorities.add(new SimpleGrantedAuthority(role.getName()));
			}				
				return authorities;
	}

	@Override
	public String getPassword() {
		return users.getPassword();
	}

	@Override
	public String getUsername() {
		return users.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	public String getFirstName() {
        return this.users.getFirstName();
    }

    public String getLastName() {
        return this.users.getLastName();
    }

    public String getFullName() {
        return this.users.getFirstName() + " " + this.users.getLastName();
    }

    public String getUserImage() {
        return this.users.getUserLogoPath();
    }

    public Users getUser() {
        return this.users;
    }
}
