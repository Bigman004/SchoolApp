package com.example.SchoolApp.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	@Autowired
	public TeacherRestController(StudentService studentService, TeacherService teacherService) {
		this.studentService = studentService;
		this.teacherService = teacherService;
	}
	@GetMapping("/view")
	public ResponseEntity<ArrayList<StudentDto>> teacherView(){
		ArrayList<StudentDto> studentList = studentService.StudentList();
		return new ResponseEntity<ArrayList<StudentDto>>(studentList, HttpStatus.OK);
	}
	@PostMapping("/")
	public ResponseEntity<StudentDto> addStudent(@RequestBody StudentDto student){
		studentService.addStudent(student);
		return new ResponseEntity<>(student, HttpStatus.CREATED);
	}
	@GetMapping("/teacher")
	public ResponseEntity<?> teacherResponse(){
		return new ResponseEntity<>(teacherService.getTeacher(), HttpStatus.OK);
	}
}
