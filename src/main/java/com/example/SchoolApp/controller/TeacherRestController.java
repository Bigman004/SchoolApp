package com.example.SchoolApp.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.SchoolApp.dto.TeacherDto;
import com.example.SchoolApp.security.SecurityUtill;
import com.example.SchoolApp.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SchoolApp.dto.StudentDto;
import com.example.SchoolApp.service.StudentService;
import com.example.SchoolApp.service.TeacherService;

@RestController
@RequestMapping("/api")
public class TeacherRestController {
	
	private StudentService studentService;
	private TeacherService teacherService;
	private OwnerService ownerService;

	@Autowired
	public TeacherRestController(StudentService studentService,
	                             TeacherService teacherService, OwnerService ownerService) {
		this.studentService = studentService;
		this.teacherService = teacherService;
	}

	@Secured("ADMIN")
	@PostMapping("/Admin/save_teacher")
	public String saveTeacher(@RequestBody TeacherDto teacher){
		teacherService.addTeacher(teacher);
		return HttpStatus.ACCEPTED.toString();
	}
	@GetMapping("/view")
	public ResponseEntity<ArrayList<StudentDto>> teacherView(){
		String username = SecurityUtill.getSessionLoader();
		String studentClass = teacherService.getTeacher(username).getTeacherClass();
		ArrayList<StudentDto> studentList = studentService.StudentList(studentClass);
		return new ResponseEntity<ArrayList<StudentDto>>(studentList, HttpStatus.OK);
	}
	@GetMapping("/teacher")
	@Secured("TEACHER")
	public ResponseEntity<?> teacherResponse(){
		String registrationNumber = SecurityUtill.getSessionLoader();
		return new ResponseEntity<>(teacherService.getTeacher(registrationNumber), HttpStatus.OK);
	}

	@GetMapping("/admin_page")
	@Secured("ADMIN")
	public ResponseEntity<?> adminPageView(){
		String username = SecurityUtill.getSessionLoader();
		System.out.println(username);
		return new ResponseEntity<>(teacherService.getAllTeachers()
				.stream().map(teacher -> new OwnerRequestTeachers(
						studentService.getclassSize(teacher.getTeacherClass())
						,teacher)).
						collect(Collectors.toList()), HttpStatus.OK);
	}


	private class OwnerRequestTeachers {
		TeacherDto teacher;
		int numberOfStudents;
		public OwnerRequestTeachers(int numberOfStudents,
									TeacherDto teacher) {
			this.numberOfStudents = numberOfStudents;
			this.teacher = teacher;
		}
		public int getNumberOfStudents() {return numberOfStudents;}
		public TeacherDto getTeacher() {return teacher;}
	}
}
