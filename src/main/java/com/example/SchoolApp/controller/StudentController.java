package com.example.SchoolApp.controller;

import java.time.LocalDate;
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

import com.example.SchoolApp.dto.AttendanceDto;
import com.example.SchoolApp.dto.DateTransferDto;
import com.example.SchoolApp.dto.StudentDto;
import com.example.SchoolApp.model.Attendance;
import com.example.SchoolApp.model.Student;
import com.example.SchoolApp.model.Teacher;
import com.example.SchoolApp.service.AttendanceService;
import com.example.SchoolApp.service.StudentService;
import com.example.SchoolApp.service.TeacherService;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/teacher")
public class StudentController {
	private StudentService studentService;
	private TeacherService teacherService;
	private AttendanceService attendanceService;
	
	@Autowired
	public StudentController(StudentService studentService,
			TeacherService teacherService,
			AttendanceService attendanceService) {
		this.studentService = studentService;
		this.teacherService = teacherService;
		this.attendanceService = attendanceService;
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
	@Secured("TEACHER")
	@GetMapping("/edit/{studentId}")
	public ResponseEntity<?> editStudent(@PathVariable("studentId") Long Id, Model model ) {
		StudentDto student = studentService.getStudentDtoById(Id);
		model.addAttribute("student", student);
		return new ResponseEntity<>(student, HttpStatus.OK);
	}
	@Secured("TEACHER")
	@PostMapping("/edit/{studentId}")
	public ResponseEntity<?> updateStudent(@PathVariable("studentId") Long Id,
			@RequestBody StudentDto student) {
		studentService.updateStudent(Id, student);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
	@Secured("TEACHER")
	@PostMapping("mark/{studentId}")
	public ResponseEntity<?> markAttendance(@PathVariable("studentId") Long Id,
			@RequestBody String attended){
		boolean mark = false;
		if (attended.equals("true"))
			mark = true;
		else
			mark = false;
		System.out.println(attended);
		boolean success = attendanceService.markAttendance(Id, "", mark);
		return new ResponseEntity<>(success, HttpStatus.ACCEPTED);
	}
	@Secured("TEACHER")
	@PostMapping("/attendance")
	public ResponseEntity<?> getAttendanceList(String classOfStudent){
		return new ResponseEntity<>(attendanceService.attendanceList(),
				HttpStatus.ACCEPTED);
	}
	@Secured("TEACHER")
	@PostMapping("attendance/date")
	public ResponseEntity<?> reviewAttendanceByDate(@RequestBody DateTransferDto date){
		System.out.println(date.getYear() + "-" + date.getMonth()+ "-" + date.getDay());
		LocalDate specific = LocalDate.of(date.getYear(), date.getMonth(), date.getDay());
		if(specific.isBefore(LocalDate.now()) || specific.isEqual(LocalDate.now())) {
			System.out.println(specific);
			return new ResponseEntity<>(attendanceService.getAttendanceDate(specific), HttpStatus.ACCEPTED);
		}
		return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		
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
