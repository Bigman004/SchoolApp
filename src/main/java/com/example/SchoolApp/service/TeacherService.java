package com.example.SchoolApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SchoolApp.dto.RegistrationDto;
import com.example.SchoolApp.model.Teacher;
import com.example.SchoolApp.repository.TeacherRepository;

@Service
public class TeacherService {
	private TeacherRepository teacherRepo;
	private UserService userService;
	
	@Autowired
	public TeacherService(TeacherRepository teacherRepo, UserService userService) {
		this.teacherRepo = teacherRepo;
		this.userService = userService;
		
	}
	public void addTeacher() {
		String defaultPassword = "teacher@2025";
		String teacherRegNo = "teacher22-22-2";
		Teacher teacher = new Teacher();
		RegistrationDto user = new RegistrationDto();
		user.setRegistrationNumber(teacherRegNo);
		user.setPassword(defaultPassword);
		teacher.setName("Mr Ayomide");
		teacher.setUser(userService.saveTeacher(user));
		teacherRepo.save(teacher);
		
	}
	public Teacher getTeacher() {
		return teacherRepo.findById((long) 3).get();
	}
}
