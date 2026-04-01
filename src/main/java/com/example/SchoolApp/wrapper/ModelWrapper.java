package com.example.SchoolApp.wrapper;

import com.example.SchoolApp.dto.StudentDto;
import com.example.SchoolApp.model.Student;

public class ModelWrapper {
    static public StudentDto mapToStudentDto(Student std) {
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
    static public Student mapToStudent(StudentDto std) {
        Student student = new Student();
        student.setHomeAddress(std.getHomeAddress());
        student.setFirstName(std.getFirstName());
        student.setLastName(std.getLastName());
        student.setLGA(std.getLGA());
        student.setDateOfBirth(std.getDateOfBirth());
        student.setParentPhone(std.getParentPhone());
        student.setRegNumber(std.getRegNumber());
        student.setStateOfOrigin(std.getStateOfOrigin());
        return student;
    }
}
