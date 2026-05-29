package com.example.SchoolApp.controller;

import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.example.SchoolApp.dto.SchoolDto;
import com.example.SchoolApp.model.LinkHash;
import com.example.SchoolApp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.SchoolApp.dto.RegistrationDto;
import com.example.SchoolApp.model.UserEntity;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/")
public class AuthController {
	private UserService userService;
	private TeacherService teacherService;
	private SchoolService schoolService;
	private JWTService jwtService;
	private LinkService linkService;
	
	@Autowired
	public AuthController(UserService userService,
						  TeacherService teacherService, JWTService jwtService
    	,LinkService linkService, SchoolService schoolService) {
		this.userService = userService;
		this.teacherService = teacherService;
        this.schoolService = schoolService;
		this.jwtService = jwtService;
		this.linkService = linkService;
	}
	@PostMapping("/send_password_link")
	public ResponseEntity<?> sendPasswordLink(@RequestParam String username) {
		String result = "";
		String randomtoken = UUID.randomUUID().toString();
		String link =  "http://localhost:8080/template/send_link/"+ randomtoken;
		try{
			MessageDigest sha = MessageDigest.getInstance("SHA-256");
			byte[] hash = sha.digest(randomtoken.getBytes());
			result = new String(Hex.encode(hash));
		}
		catch(Exception e){
			return  ResponseEntity.badRequest().build();
		}
		try {
			String email = null;
			if (userService.getUserRole(username).equals("TEACHER")) {
				email = teacherService.getTeacher(username).getEmail();
			}
			linkService.save(LinkHash.builder()
					.email(email)
					.username(username)
					.link(result) // save the hash
					.created(LocalDateTime.now())
					.build());
			linkService.sendEmail(LinkHash.builder()
					.email(email)
					.username(username)
					.link(link) //send the link
					.created(LocalDateTime.now())
					.build());

			return new ResponseEntity<>(email + ": " + randomtoken + "\n" + "hash: " + result,
					HttpStatus.OK);
		}
		catch(Exception e){
			return new ResponseEntity<>("error processing request", HttpStatus.FORBIDDEN);
		}

	}
	@GetMapping("/register")
	public String registerFrom(Model model) {
		RegistrationDto registrationDto = new RegistrationDto();
		model.addAttribute("user", registrationDto);
		return "login";
	}

	@GetMapping("login")
	public ResponseEntity<RegistrationDto> loginApi() {
		RegistrationDto user = new RegistrationDto();
		return new ResponseEntity<RegistrationDto>(user, HttpStatus.OK);
		
	}
	
	@PostMapping("login")
	public ResponseEntity<?> login(@RequestBody RegistrationDto user, 
			HttpServletResponse response) {
		String token = userService.verifyUser(user);
		String username = jwtService.extractUsername(token);
		return new ResponseEntity<>(
				new LoginResponseWrapper(token,
				userService.getUserRole(username)),
				HttpStatus.OK);
	
	}
	 @GetMapping("/debug")
	    public ResponseEntity<?> listUsers() {
	        List<UserEntity> users = userService.listUser();
	        return ResponseEntity.ok("Total users: " + users.size());
	    }
	
	@Secured({"TEACHER", "ADMIN", "DEVELOPER"})
	@PostMapping("/change_password")
	public ResponseEntity<?> changePassword(@RequestBody RegistrationDto user,
			@RequestParam(value ="password") String password){

		if(userService.changePassword(password, user))
			return new ResponseEntity<String>("change password success", HttpStatus.OK);
		return new ResponseEntity<>("change password failed", HttpStatus.FORBIDDEN);
	}
	@PostMapping("developer/add_school")
	public ResponseEntity<?> addSchool(@RequestBody SchoolDto schoolDto) {
		return new ResponseEntity<>(schoolService.save(schoolDto),HttpStatus.OK);
	}
	class LoginResponseWrapper {
		String token;
		String message;
		public LoginResponseWrapper(String token, String message) {
			this.token = token;
			this.message = message;
		}
		public String getToken() {
			return token;
		}
		public String getMessage() {
			return message;
		}

	}
}
