package com.example.SchoolApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.SchoolApp.dto.RegistrationDto;
import com.example.SchoolApp.model.Role;
import com.example.SchoolApp.model.UserEntity;
import com.example.SchoolApp.repository.RoleRepository;
import com.example.SchoolApp.repository.UserRepository;

@Service
public class UserService {
	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private PasswordEncoder passwordEncoder;
	private AuthenticationManager authenticationManager;
	private JWTService tokenService;
	
	@Autowired
	public UserService(UserRepository userRepository, RoleRepository roleRepository,
			PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager,
			JWTService tokenService) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager; 
		this.tokenService = tokenService;
	}
	public UserEntity saveStudent(RegistrationDto registrationDto) {
		Role role = roleRepository.findByName("STUDENT");
		UserEntity user = new UserEntity();
		user.setRegistrationNumber(registrationDto.getRegistrationNumber());
		user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
		user.setRole(role);
		return userRepository.save(user);

	}
	public UserEntity saveTeacher(RegistrationDto registrationDto) {
		Role role = roleRepository.findByName("TEACHER");
		UserEntity user  =  new UserEntity();
		user.setRegistrationNumber(registrationDto.getRegistrationNumber());
		user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
		user.setRole(role);
		return userRepository.save(user);
	}
	public String verifyUser(RegistrationDto user) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(user.getRegistrationNumber(), 
						user.getPassword()));
		if(authentication.isAuthenticated())
			return tokenService.generateToken(user.getRegistrationNumber());
		return "failure";
		
	}
	
}
