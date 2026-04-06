package com.example.SchoolApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SchoolApp.model.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Teacher findByUsername(String username);
}
