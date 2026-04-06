package com.example.SchoolApp.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;;
@Builder
@Data
public class StudentDto {
	private long Id;
	private String regNumber; //going to act as the student username
	@NotEmpty(message ="Student should have a first name")
	private String firstName;
	@NotEmpty(message ="Student should have a last name")
	private String LastName;
	@NotEmpty(message ="Student date of birth is required")
	private String dateOfBirth;
	@NotEmpty(message ="Phone number of parent is required")
	private String parentPhone;
	private String homeAddress;
	@NotEmpty(message="state or origin should not be empty")
	private String stateOfOrigin;
	private String LGA;
	private String classOfStudent;
}
