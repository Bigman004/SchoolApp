package com.example.SchoolApp.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.SchoolApp.model.Attendance;
import java.util.List;
import java.time.LocalDateTime;


public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
	// In AttendanceRepository
	@Query("SELECT COUNT(a) > 0 FROM Attendance a WHERE a.student.id = :studentId AND DATE(a.timestamp) = :date")
	boolean existsByStudentIdAndDate(@Param("studentId") Long studentId, @Param("date") LocalDate date);
	
	List<Attendance> findByTimestamp(LocalDate timestamp);

	boolean existsByTimestamp(LocalDate timestamp);
}
