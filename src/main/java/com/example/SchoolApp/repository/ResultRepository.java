package com.example.SchoolApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.SchoolApp.model.Result;

public interface ResultRepository extends JpaRepository<Result, Long> {
	
}
