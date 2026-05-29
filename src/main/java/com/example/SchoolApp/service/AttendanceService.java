package com.example.SchoolApp.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.example.SchoolApp.SchoolModels;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SchoolApp.dto.AttendanceDto;
import com.example.SchoolApp.dto.StudentDto;
import com.example.SchoolApp.model.Attendance;
import com.example.SchoolApp.model.Student;
import com.example.SchoolApp.repository.AttendanceRepository;
import com.example.SchoolApp.repository.StudentRepository;

import jakarta.persistence.EntityNotFoundException;


@Service
public class AttendanceService {
	private AttendanceRepository attendanceRepository;
	private StudentRepository studentRepo;
	
	@Autowired
	public AttendanceService(AttendanceRepository attendanceRepository,
			StudentRepository studentRepo) {
		this.attendanceRepository = attendanceRepository;
		this.studentRepo = studentRepo;
	}
	
	public boolean markAttendance(Long studentId, String remark, boolean attend) {
	    Student student = studentRepo.findById(studentId)
	        .orElseThrow(() -> new EntityNotFoundException("Student not found: " + studentId));



	    LocalDateTime now = LocalDateTime.now();
	    LocalDate today = now.toLocalDate();

	    // Query attendance by student and today's date
	    boolean alreadyMarked = attendanceRepository
	        .existsByStudentIdAndDate(studentId, today);

	    if (alreadyMarked) return false;

	    Attendance attendance = new Attendance();
	    attendance.setStudent(student);
	    attendance.setRemarks(remark);
	    attendance.setTimestamp(today);
	    attendance.setStatus(attend);
		attendance.setClassName(student.getClassOfStudent());
		attendance.setSchoolId(student.getSchoolId());
	    attendanceRepository.save(attendance);

	    return true;
	}
	
	public List<AttendanceDto> listAttendanceDto(){
		List<Attendance> list = attendanceRepository.findAll();
		return list.stream()
				.map(attendance -> mapToAttendanceDto(attendance))
				.collect(Collectors.toList());
	}

	private AttendanceDto mapToAttendanceDto(Attendance attendance) {
		
		return AttendanceDto.builder()
				.Id(attendance.getId())
				.remarks(attendance.getRemarks())
				.status(attendance.isStatus())
				.studentFirstName(attendance.getStudent().getFirstName())
				.studentLastName(attendance.getStudent().getLastName())
				.timestamp(attendance.getTimestamp())
				.build();
	}
	public ArrayList<ArrayList<AttendanceDto>> attendanceList() {
	    List<AttendanceDto> list = listAttendanceDto();
	    Map<LocalDate, ArrayList<AttendanceDto>> groupedMap = new LinkedHashMap<>();

	    for (AttendanceDto attendance : list) {
	        LocalDate date = attendance.getTimestamp();
	        groupedMap.computeIfAbsent(date, k -> new ArrayList<>()).add(attendance);
	    }

	    return new ArrayList<>(groupedMap.values());
	}

	public List<AttendanceDto> getAttendanceDate(LocalDate date) {
		List<Attendance> list = attendanceRepository.findByTimestamp(date);
		return list.stream()
				.map(this::mapToAttendanceDto)
				.collect(Collectors.toList());
	}
	public List<AttendanceDto> getAttendanceDate(LocalDate date, Long schoolId, String className) {
		List<Attendance> list = attendanceRepository
				.findBySchoolIdAndClassNameAndTimestamp(schoolId, className, date);
		return list.stream()
				.map(this::mapToAttendanceDto)
				.collect(Collectors.toList());
	}
	public  Integer amountOfDay(Long schoolId) {
		return attendanceRepository.countDistinctSchoolId(schoolId);
	}
	public int getAverageAttendanceDate(
			String className, Long schoolId
	) {
		int classAttendance = attendanceRepository
				.countDistinctSchoolIdAndClass(schoolId, className);
		return (classAttendance / amountOfDay(schoolId)) * 100;
	}
}
