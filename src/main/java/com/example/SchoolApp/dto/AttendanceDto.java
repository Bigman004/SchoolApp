package com.example.SchoolApp.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttendanceDto {
	private Long Id;
	
	private String remarks;
	
	private boolean status;
	
	private String studentFirstName;
	
	private String studentLastName;
	 
	private LocalDate timestamp; 
}
