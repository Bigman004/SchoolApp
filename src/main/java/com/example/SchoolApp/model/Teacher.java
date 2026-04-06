package com.example.SchoolApp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name ="teacher", schema = "public")
@Data
@Builder
@AllArgsConstructor
public class Teacher{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long Id;
	private String name;
	private String teacherClass;
	private String username;   				//reference username
	private String email;

	public Teacher() {

	}
}
