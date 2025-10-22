package com.example.SchoolApp.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SchoolApp.model.Student;


public interface StudentRepository extends JpaRepository<Student, Long> {

}
