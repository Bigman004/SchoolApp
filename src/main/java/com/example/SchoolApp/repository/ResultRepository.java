package com.example.SchoolApp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SchoolApp.dto.ResultDto;
import com.example.SchoolApp.model.Result;

public interface ResultRepository extends JpaRepository<Result, Long> {

	List<Result> findAllByTerm(String term);
	
}
