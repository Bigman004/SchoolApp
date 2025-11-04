package com.example.SchoolApp.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SchoolApp.dto.StudentDto;
import com.example.SchoolApp.model.Student;
import com.example.SchoolApp.model.Teacher;
import com.example.SchoolApp.service.StudentService;
import com.example.SchoolApp.service.TeacherService;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/teacher")
public class StudentController {
	private StudentService studentService;
	private TeacherService teacherService;
	
	@Autowired
	public StudentController(StudentService studentService, TeacherService teacherService) {
		this.studentService = studentService;
		this.teacherService = teacherService;
	}
	@GetMapping("/")
	@Secured("TEACHER")
	public ResponseEntity<?> teacherPage() {
		ArrayList<StudentDto> studentList = studentService.StudentList();
		
		return new ResponseEntity<>(new WrapperRequest(studentList, teacherService.getTeacher()), HttpStatus.OK);
	}
	@GetMapping("/teacherInfo")
	@Secured("TEACHER")
	public ResponseEntity<?> teacherDetails(){
		return new ResponseEntity<>(teacherService.getTeacher(), HttpStatus.OK);
	}
	@Secured("TEACHER")
	@GetMapping("/add_student")
	public ResponseEntity<?> add_Student(Model model) {
		StudentDto student = StudentDto.builder().build();
		return new ResponseEntity<>(student, HttpStatus.OK);
	}
	@Secured("TEACHER")
	@PostMapping("/add_student")
	public ResponseEntity<?> addNewStudent(@Valid @RequestBody StudentDto student,
			BindingResult result) {
		if(result.hasErrors()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		studentService.addStudent(student);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
	@GetMapping("/loginHTML")
	public String showLoginPage() {
		return "login";
	}
	@GetMapping("/edit/{studentId}")
	public ResponseEntity<?> editStudent(@PathVariable("studentId") Long Id, Model model ) {
		StudentDto student = studentService.getStudentDtoById(Id);
		model.addAttribute("student", student);
		return new ResponseEntity<>(student, HttpStatus.OK);
	}
	@PostMapping("/edit/{studentId}")
	public ResponseEntity<?> updateStudent(@PathVariable("studentId") Long Id,
			@RequestBody StudentDto student) {
		studentService.updateStudent(Id, student);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
	
	private class WrapperRequest{
		private List<StudentDto> list;
		private Teacher teacher;
		WrapperRequest(List<StudentDto> list, Teacher teacher ){
			this.list = list;
			this.teacher = teacher;
		}
		public List<StudentDto> getList() {return list;}
		public Teacher getTeacher() {return teacher;}
	}
}
