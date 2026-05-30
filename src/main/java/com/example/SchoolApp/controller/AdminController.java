package com.example.SchoolApp.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.SchoolApp.dto.TeacherDto;
import com.example.SchoolApp.model.Teacher;
import com.example.SchoolApp.security.SecurityUtill;
import com.example.SchoolApp.service.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.example.SchoolApp.dto.StudentDto;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class AdminController {
	
	private final StudentService studentService;
	private final TeacherService teacherService;
	private final UserService userService;
	private final AttendanceService attendanceService;
	private final ResultService resultService;
	private final SchoolService schoolService;



	@Secured("ADMIN")
	@PostMapping("/Admin/save_teacher")
	public String saveTeacher(@RequestBody TeacherDto teacher){
		String username = SecurityUtill.getSessionLoader();
		Long schoolId = userService.findByUserName(username).getReferenceID();
		teacherService.addTeacher(teacher, schoolId);
		return HttpStatus.ACCEPTED.toString();
	}
	@Secured("ADMIn")
	@PostMapping("/add_student")
	public ResponseEntity<?> addNewStudent(@Valid @RequestBody StudentDto student,
	                                       BindingResult result) {
		String registrationNumber = SecurityUtill.getSessionLoader();
		Long schoolId = schoolService.(registrationNumber).getSchoolId();
		if(result.hasErrors()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		Teacher teacher = teacherService.getTeacher(registrationNumber);
		student.setClassOfStudent(teacher.getTeacherClass());
		student.setSchoolId(schoolId);
		studentService.addStudent(student);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
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
		Long schoolId = userService.findByUserName(username).getReferenceID();

		return new ResponseEntity<>(teacherService.getAllTeachers(schoolId)
				.stream().map(
						teacher -> new OwnerRequestTeachers(
								studentService.getclassSize(teacher.getTeacherClass(), schoolId),
								teacher,
								attendanceService.amountOfDay(schoolId))
				).
						collect(Collectors.toList()), HttpStatus.OK);
	}

	@GetMapping("/class_page/{className}")
	@Secured("ADMIN")
	public ResponseEntity<?> classPageView(@PathVariable String className){
		String username = SecurityUtill.getSessionLoader();
		Long schoolId = userService.findByUserName(username).getReferenceID();
		return new ResponseEntity<>(
				new OwnerRequestClassData(
				className,
				studentService.getStudentsByClass(className,  schoolId),
				teacherService.getTeacherByClassName(className, schoolId),
						attendanceService.getAverageAttendanceDate(className, schoolId),
						resultService.getAverageScore(className, schoolId)

				),
				HttpStatus.OK
		);
	}

	@Getter
	private static class OwnerRequestTeachers{
		TeacherDto teacher;
		int numberOfStudents;
		int schoolOpens;
		public OwnerRequestTeachers(int numberOfStudents,
									TeacherDto teacher, int schoolOpens) {

			this.numberOfStudents = numberOfStudents;
			this.teacher = teacher;
			this.schoolOpens = schoolOpens;
		}
	}
	@Getter
	@AllArgsConstructor
    private static class OwnerRequestClassData{
		String className;
		List<StudentDto> students;
		TeacherDto teacher;
		int averageAttendance;
		double averageResult;
    }
}
