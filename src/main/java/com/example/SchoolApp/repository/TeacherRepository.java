package com.example.SchoolApp.repository;

import com.example.SchoolApp.dto.TeacherDto;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SchoolApp.model.Teacher;

import java.util.Arrays;
import java.util.List;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Teacher findByUsername(String username);

    Teacher findByTeacherClass(String teacherClass);

    Teacher findBySchoolIdAndTeacherClass(long schoolId, String teacherClass);

    List<Teacher> findAllBySchoolId(long schoolId);
}
