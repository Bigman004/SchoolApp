package com.example.SchoolApp.repository;

import com.example.SchoolApp.model.School;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, Long> {
    School findbyUsername(String registrationNumber);
}
