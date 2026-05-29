package com.example.SchoolApp.wrapper;

import com.example.SchoolApp.dto.SchoolDto;
import com.example.SchoolApp.dto.StudentDto;
import com.example.SchoolApp.dto.TeacherDto;
import com.example.SchoolApp.model.School;
import com.example.SchoolApp.model.Student;
import com.example.SchoolApp.model.Teacher;

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
                        .parentEmail(std.getParentEmail())
                        .classOfStudent(std.getClassOfStudent())
                        .schoolId(std.getSchoolId())
                .LGA(std.getLGA())
                        .regNumber(std.getRegNumber())
                .build();
    }
    static public Student mapToStudent(StudentDto std) {
        Student student = new Student();
        student.setHomeAddress(std.getHomeAddress());
        student.setFirstName(std.getFirstName());
        student.setLastName(std.getLastName());
        student.setSchoolId(std.getSchoolId());
        student.setLGA(std.getLGA());
        student.setParentEmail(std.getParentEmail());
        student.setDateOfBirth(std.getDateOfBirth());
        student.setParentPhone(std.getParentPhone());
        student.setRegNumber(std.getRegNumber());
        student.setStateOfOrigin(std.getStateOfOrigin());
        student.setClassOfStudent(std.getClassOfStudent());
        return student;
    }
    static public Teacher mapToTeacher(TeacherDto teacher) {
        return Teacher.builder()
                .email(teacher.getTeacherEmail())
                .teacherClass(teacher.getTeacherClass())
                .name(teacher.getName())
                .Id(teacher.getId())
                .username(teacher.getUsername())
                .build();
    }
    static public TeacherDto mapToTeacherDto(Teacher teacher) {
        return TeacherDto.builder()
                .teacherEmail(teacher.getEmail())
                .name(teacher.getName())
                .teacherClass(teacher.getTeacherClass())
                .username(teacher.getUsername())
                .Id(teacher.getId())
                .build();
    }
    static public SchoolDto mapToSchoolDto(School school) {
        return SchoolDto.builder()
                .id(school.getId())
                .schoolName(school.getSchoolName())
                .username(school.getUsername())
                .schoolAddress(school.getSchoolAddress())
                .schoolCode(school.getSchoolCode())
                .build();
    }
    static public School mapToSchool(SchoolDto schoolDto) {
        return School.builder()
                .schoolAddress(schoolDto.getSchoolAddress())
                .schoolCode(schoolDto.getSchoolCode())
                .schoolName(schoolDto.getSchoolName())
                .lastName(schoolDto.getLastName())
                .firstName(schoolDto.getFirstName())
                .username(schoolDto.getUsername())
                .email(schoolDto.getEmail())
                .build();
    }

}
