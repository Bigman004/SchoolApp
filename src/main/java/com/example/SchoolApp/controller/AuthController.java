package com.example.SchoolApp.controller;

import java.util.List;

import com.example.SchoolApp.dto.TeacherDto;
import com.example.SchoolApp.model.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.SchoolApp.dto.RegistrationDto;
import com.example.SchoolApp.model.UserEntity;
import com.example.SchoolApp.service.TeacherService;
import com.example.SchoolApp.service.UserService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/")
public class AuthController {
	private UserService userService;
	private TeacherService teacherService;
	
	@Autowired
	public AuthController(UserService userService, TeacherService teacherService) {
		this.userService = userService;
		this.teacherService = teacherService;
	}
	
	@GetMapping("/register")
	public String registerFrom(Model model) {
		RegistrationDto registrationDto = new RegistrationDto();
		model.addAttribute("user", registrationDto);
		return "login";
	}
	
	@PostMapping("/create")
	public String createTeacher(@RequestBody TeacherDto teacher) {
		teacherService.addTeacher(teacher);
		return "teacherPage";
	}
	@GetMapping("login")
	public ResponseEntity<RegistrationDto> loginApi() {
		RegistrationDto user = new RegistrationDto();
		return new ResponseEntity<RegistrationDto>(user, HttpStatus.OK);
		
	}
	
	@PostMapping("login")
	public ResponseEntity<?> login(@RequestBody RegistrationDto user, 
			HttpServletResponse response) {
		System.out.println(user);
		String token = userService.verifyUser(user);
		System.out.println(token);
		return new ResponseEntity<>(token, HttpStatus.OK);
	
	}
	 @GetMapping("/debug")
	    public ResponseEntity<?> listUsers() {
	        List<UserEntity> users = userService.listUser();
	        return ResponseEntity.ok("Total users: " + users.size());
	    }
	
	@Secured("TEACHER")
	@PostMapping("/change_password")
	public ResponseEntity<?> changePassword(@RequestBody RegistrationDto user,
			@RequestParam(value ="password") String password){
		System.out.println(password +" "+ user );
		System.out.println(userService.changePassword(password, user));
		return new ResponseEntity<String>("change password success", HttpStatus.OK);
	}
}
