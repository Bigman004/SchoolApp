package com.example.SchoolApp.service;

import com.example.SchoolApp.dto.TeacherDto;
import com.example.SchoolApp.events.CreateUserEvent;
import com.example.SchoolApp.wrapper.ModelWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.example.SchoolApp.dto.RegistrationDto;
import com.example.SchoolApp.model.Teacher;
import com.example.SchoolApp.repository.TeacherRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeacherService {
	private TeacherRepository teacherRepo;
	private ApplicationEventPublisher publisher;
	
	@Autowired
	public TeacherService(TeacherRepository teacherRepo, ApplicationEventPublisher publisher) {
		this.teacherRepo = teacherRepo;
		this.publisher = publisher;

	}
	public void addTeacher(TeacherDto teacherDto) {
		String registrationP = "teacher22-22-";
		Teacher teacher = ModelWrapper.mapToTeacher(teacherDto);
		teacherRepo.save(teacher);
		publisher.publishEvent(new CreateUserEvent(teacher.getId(),
				registrationP+teacher.getId(),
				"TEACHER",
				"teacher@2025"));
		teacher.setUsername(registrationP + teacher.getId());
		teacherRepo.save(teacher);
		
	}
	public List<TeacherDto> getAllTeachers() {
		return teacherRepo.findAll().stream().
				map(teacher -> ModelWrapper.mapToTeacherDto(teacher))
				.collect(Collectors.toList());

	}
	public Teacher getTeacher(String registrationNumber) {

		return teacherRepo.findByUsername(registrationNumber);
	}
}
