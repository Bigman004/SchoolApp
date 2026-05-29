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
	List<Attendance> findBySchoolIdAndClassNameAndTimestamp(
			Long schoolId, String className, LocalDate date
	);
	List<Attendance> findBySchoolIdAndClassName(Long schoolId, String className);

	@Query(value = "SELECT COUNT(*) " +
			"FROM (" +
			"SELECT DISTINCT a.timestamp FROM Attendance a" +
			" WHERE school_Id = :schoolId)", nativeQuery = true)
	Integer countDistinctSchoolId(@Param("schoolId")Long schoolId);


	@Query(value = "SELECT COUNT(*)" +
			"FROM(" +
			"SELECT DISTINCT a.timestamp FROM Attendance a" +
			" WHERE school_id = :schoolId AND class_name = :className AND " +
			"status = true)", nativeQuery = true)
	int countDistinctSchoolIdAndClass(@Param("schoolId")Long schoolId, @Param("className")String className);
}
