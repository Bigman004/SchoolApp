package com.example.SchoolApp.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import com.example.SchoolApp.dto.RegistrationDto;
import com.example.SchoolApp.model.Role;
import com.example.SchoolApp.model.UserEntity;
import com.example.SchoolApp.repository.RoleRepository;
import com.example.SchoolApp.repository.UserRepository;

import io.jsonwebtoken.Jwt;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

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
		user.setLogin(false);
		return userRepository.save(user);

	}
	public UserEntity saveTeacher(RegistrationDto registrationDto) {
		Role role = roleRepository.findByName("TEACHER");
		UserEntity user  =  new UserEntity();
		user.setRegistrationNumber(registrationDto.getRegistrationNumber());
		user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
		user.setLogin(false);
		user.setRole(role);
		return userRepository.save(user);
	}
	public String verifyUser(RegistrationDto user) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(user.getRegistrationNumber(), 
						user.getPassword()));
		if(authentication.isAuthenticated()) {
			UserEntity userEntity = userRepository.findByRegistrationNumber(user.getRegistrationNumber());
			userEntity.setLogin(true);
			userRepository.save(userEntity);
			return tokenService.generateToken(user.getRegistrationNumber());
		}
		return "failure";
		
	}
	/***
	 * this method changes the password in the user database verify the session for the user
	 *in the cookie
	 * @param request from the client computer to verify the session
	 * @param password retrieved from the front end
	 * @return
	 */
	public boolean changePassword(String password, RegistrationDto user) {
		UserEntity userEntity;
		Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(user.getRegistrationNumber(), 
							user.getPassword()));
		if(authentication.isAuthenticated()) {
			userEntity = userRepository.findByRegistrationNumber(user.getRegistrationNumber());
			userEntity.setPassword(passwordEncoder.encode(password));
			userRepository.save(userEntity);
			return true;
		}
		else
			return false;
		
	}
	public List<UserEntity> listUser() {
		// TODO Auto-generated method stub
		return userRepository.findAll();
	}
	
}
