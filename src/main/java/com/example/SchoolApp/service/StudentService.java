package com.example.SchoolApp.service;

import java.util.ArrayList;
import java.util.List;

import com.example.SchoolApp.events.CreateResultEvent;
import com.example.SchoolApp.events.CreateUserEvent;
import com.example.SchoolApp.wrapper.ModelWrapper;
import org.springframework.context.ApplicationEventPublisher;
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
	private ApplicationEventPublisher publisher;
	
	@Autowired
	public StudentService(StudentRepository studentRepo,
			  ApplicationEventPublisher publisher) {
		this.studentRepo = studentRepo;
		this.publisher = publisher;
	}
	//this will also save a user to the userRepository
	public void addStudent(Student std) {
		String defaultPassword = "std@2025";
		String registrationPreffix = "22-22op/";
		studentRepo.save(std);  // to generate ID
		publisher.publishEvent(new CreateResultEvent(std.getId()));
		publisher.publishEvent(new CreateUserEvent(std.getId(), registrationPreffix+ std.getId(),
				"STUDENT", defaultPassword));
		std.setRegNumber(registrationPreffix+ std.getId());
		studentRepo.save(std);
		return;
	}
	/**
	 * 
	 * @param std
	 * create a student with the default password
	 */
	public void addStudent(StudentDto student) {
		String registrationPreffix = "22-22op/";
		String defaultPassword = "std@2025";
		Student std = ModelWrapper.mapToStudent(student);
		studentRepo.save(std);    //to generate Id
		publisher.publishEvent(new CreateResultEvent(std.getId()));
		publisher.publishEvent(new CreateUserEvent(std.getId(), registrationPreffix+ std.getId(),
				"STUDENT", defaultPassword));
		std.setRegNumber(registrationPreffix+ std.getId());
		studentRepo.save(std);
		return;
	}
	public ArrayList<StudentDto> StudentList(String classOfStudent) {
		List<Student> list = studentRepo.findByClassOfStudent(classOfStudent);
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
