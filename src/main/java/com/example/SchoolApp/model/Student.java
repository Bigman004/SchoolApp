package com.example.SchoolApp.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotEmpty;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "student")
public class Student {
	@jakarta.persistence.Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long Id;
	private String regNumber; //going to act as the student username
	@NotEmpty(message ="Student should have a first name")
	private String firstName;
	@NotEmpty(message ="Student should have a last name")
	private String LastName;
	private String dateOfBirth;
	@NotEmpty(message ="Phone number of parent is required")
	private String parentPhone;
	private String homeAddress;
	@NotEmpty(message="state or origin should not be empty")
	private String stateOfOrigin;
	private String LGA;
	@OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Result> results = new ArrayList<>();
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "userId", referencedColumnName="Id" )
	private UserEntity user;
	
}
