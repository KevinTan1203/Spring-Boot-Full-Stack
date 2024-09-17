package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.models.User;
import com.example.demo.repo.UserRepo;

@Service
public class UserService implements UserDetailsService {
	// The Spring Framework property will automatically use the UserDetailServices to retireve 
	// information about a user


	private final UserRepo userRepo;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder) {
		this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
	}

	public UserRepo getUserRepo() {
		return userRepo;
	}

	public PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

	public User registerNewuser(User user) {
		// Before we save the user, we need to hash the password
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepo.save(user);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found");
		}
		return user;
	}

	public User findUserByUsername(String username) {
		return userRepo.findByUsername(username);
	}
}
