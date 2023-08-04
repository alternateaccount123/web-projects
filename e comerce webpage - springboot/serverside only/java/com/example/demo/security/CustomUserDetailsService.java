package com.example.demo.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

import org.springframework.security.core.authority.SimpleGrantedAuthority;


import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private UserRepository userRepository;

	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	// Method required by the UserDetailsService interface to load a user by their
	// username (email in this case)
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// Retrieve the user from the UserRepository based on the provided email
		User user = userRepository.findByEmail(email);
		if (user != null) {
			// If the user exists, return a new UserDetails object representing the user.
			// It contains the user's email, password, and a collection of granted authorities (roles).
			return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
					mapRolesToAuthorities(user.getRoles()));
		} else {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
	}

	// A private helper method to convert a collection of Role objects to a
	// collection of GrantedAuthority objects.
	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		// Using Java streams to map each Role's name to a new SimpleGrantedAuthority
		// object and collect them into a list.
		// SimpleGrantedAuthority represents a role, and it implements the
		// GrantedAuthority interface.
		Collection<? extends GrantedAuthority> mappedRoles = roles.stream()
				.map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());

		// Return the collection of GrantedAuthority objects representing the user's
		// roles.
		return mappedRoles;
	}
}
