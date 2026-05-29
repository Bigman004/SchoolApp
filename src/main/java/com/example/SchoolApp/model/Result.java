package com.example.SchoolApp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "result", schema = "public")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Result {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long Id;
	private String term;
	private String subjectName;
	private Integer score;
	private String type;
	private Long studentId;
	private String classOfStudent;
	private Long schoolId;
}
