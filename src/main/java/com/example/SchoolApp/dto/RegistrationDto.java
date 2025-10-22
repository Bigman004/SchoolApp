package com.example.SchoolApp.dto;
import jakarta.validation.constraints.NotEmpty;

import lombok.Builder;
import lombok.Data;

@Data
public class RegistrationDto {
	private long id;
	@NotEmpty
	private String registrationNumber;
	@NotEmpty
	private String password;

}
