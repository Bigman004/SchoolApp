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
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "result", schema = "public")
@Data
public class Result {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long Id;
	private String term;
	private int math;
	private int english;
	private int socialStudies;
	private int basicScience;
	private int CRK;
	private int civicEducation;
	private int PHE;
	private int handwriting;
	private int rhymes;
	private int verbalReasoning;
	private int quantitative;
	private int computer;
	private int creativeArt;
	private String type;

	private Long studentId;
}
