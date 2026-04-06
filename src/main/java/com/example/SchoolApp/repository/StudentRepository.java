package com.example.SchoolApp.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SchoolApp.model.Student;

import java.util.List;


public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByClassOfStudent(String classOfStudent);
}
