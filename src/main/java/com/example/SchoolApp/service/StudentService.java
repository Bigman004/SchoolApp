package com.example.SchoolApp.service;

import java.util.ArrayList;
import java.util.List;
import com.example.SchoolApp.wrapper.ModelWrapper;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.example.SchoolApp.dto.StudentDto;
import com.example.SchoolApp.dto.RegistrationDto;
import com.example.SchoolApp.model.Result;
import com.example.SchoolApp.model.Student;
import com.example.SchoolApp.repository.StudentRepository;

@Service
public class StudentService {
	private StudentRepository studentRepo;
	private UserService userService;
	
	@Autowired
	public StudentService(StudentRepository studentRepo,
			UserService userService) {
		this.studentRepo = studentRepo;
		this.userService = userService;
	}
	//this will also save a user to the userRepository
	public void addStudent(Student std) {
		String[] term = {"1st term", "2nd term", "3rd term"};
		String registrationPreffix = "22-22op/";
		String defaultPassword = "std@2025";
		RegistrationDto user = new RegistrationDto();
		user.setPassword(defaultPassword);
		studentRepo.save(std);
		user.setRegistrationNumber(registrationPreffix+std.getId());
		std.setUser(userService.saveStudent(user));
		studentRepo.save(std);
		return;
	}
	/**
	 * 
	 * @param std
	 * create a student with the default password
	 */
	public void addStudent(StudentDto student) {
		String[] term = {"1st term", "2nd term", "3rd term"};
		String registrationPreffix = "22-22op/";
		String defaultPassword = "std@2025";
		RegistrationDto user = new RegistrationDto();
		Student std = ModelWrapper.mapToStudent(student);
		user.setPassword(defaultPassword);
		
		studentRepo.save(std);
		ArrayList<Result> results = new ArrayList<Result>();
		Result result;
		for(int i = 0; i< term.length; i++) {
			result = new Result();
			result.setTerm(term[i]);;
			result.setStudent(std);
			results.add(result);
			std.getResults().add(result); 
		}
		std.setResults(results);
		user.setRegistrationNumber(registrationPreffix+std.getId());
		std.setUser(userService.saveStudent(user));
		studentRepo.save(std);
		return;

			}
	public ArrayList<StudentDto> StudentList(){
		List<Student> list = studentRepo.findAll(Sort.by("id"));
		ArrayList<StudentDto> stdList = new ArrayList<>();
		for(Student std:list) {
			stdList.add(ModelWrapper.mapToStudentDto(std));
		}
		return stdList;
		
	}
	private String generateRegNo(long Id) {
		String preffix = "221132"+ Long.toString(Id);
		return preffix;
	}
	
	public void updateStudent(Long id, StudentDto std) {
		Student student = studentRepo.findById(id).orElseThrow();
		student.setFirstName(std.getFirstName());
		student.setLastName(std.getLastName());
		student.setStateOfOrigin(std.getStateOfOrigin());
		student.setParentPhone(std.getParentPhone());
		student.setHomeAddress(std.getHomeAddress());
		student.setLGA(std.getLGA());
		studentRepo.save(student);
		return;
	}
	public StudentDto getStudentDtoById(Long Id) {
		Student std = studentRepo.findById(Id).get();
		return StudentDto.builder()
				.Id(std.getId())
				.dateOfBirth(std.getDateOfBirth())
				.firstName(std.getFirstName())
				.LastName(std.getLastName())
				.stateOfOrigin(std.getStateOfOrigin())
				.parentPhone(std.getParentPhone())
				.homeAddress(std.getHomeAddress())
				.LGA(std.getLGA())
				.build();

	}
	
	

}
