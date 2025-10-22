package com.example.SchoolApp.security;

import java.util.Arrays;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.SchoolApp.model.UserEntity;
import com.example.SchoolApp.repository.UserRepository;

@Service
public class CustomUserDetailService implements UserDetailsService {
	private UserRepository userRepository;
	
	public CustomUserDetailService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity user = userRepository.findByRegistrationNumber(username);
		if(user != null) {
			User antUser = new User(
					user.getRegistrationNumber(),
					user.getPassword(),
					Arrays.asList(new SimpleGrantedAuthority(user.getRole().getName()))
					);
			return antUser;
		}
		else {
			throw new UsernameNotFoundException("invalid username");
		}
	}
	

}
