package com.example.SchoolApp.service;

import com.example.SchoolApp.dto.SchoolDto;
import com.example.SchoolApp.events.CreateUserEvent;
import com.example.SchoolApp.model.School;
import com.example.SchoolApp.repository.SchoolRepository;
import com.example.SchoolApp.wrapper.ModelWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SchoolService {

    private SchoolRepository schoolRepository;
    private ApplicationEventPublisher publisher;

    @Autowired
    public SchoolService(SchoolRepository schoolRepository,  ApplicationEventPublisher publisher) {
        this.schoolRepository = schoolRepository;

        this.publisher = publisher;
    }

    public String save(SchoolDto schoolDto) {
        School school = ModelWrapper.mapToSchool(schoolDto);
        String username = generateUsername(school.getFirstName());
        school.setSchoolCode(UUID.randomUUID().toString());
        school.setUsername(username);
        schoolRepository.save(school);
        publisher.publishEvent(new CreateUserEvent(school.getId(), username,
                "ADMIN", "default_password"));
        return username;
    }
    public String generateUsername(String name) {
        return name + UUID.randomUUID().toString().substring(0, 5);
    }

    public School getSchool(String registrationNumber) {
        return schoolRepository.findbyRegistrationNumber(registrationNumber);
    }
}
