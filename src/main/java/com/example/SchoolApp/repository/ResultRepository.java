package com.example.SchoolApp.repository;

import java.util.Arrays;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SchoolApp.dto.ResultDto;
import com.example.SchoolApp.model.Result;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ResultRepository extends JpaRepository<Result, Long> {

	List<Result> findAllByTermAndTypeAndStudentId(String term, String type, Long studentId);

	List<Result> findAllByStudentIdAndTermAndType(Long studentId, String term, String type);

	@Query(value = "SELECT AVG(score) FROM Result r " +
			"WHERE class_of_student = :className AND term = :term" +
			" AND school_id = :schoolId", nativeQuery = true)
	Double sumTestScore(@Param("className") String className,
						 @Param("term") String term, @Param("schoolId") Long schoolId);
}
