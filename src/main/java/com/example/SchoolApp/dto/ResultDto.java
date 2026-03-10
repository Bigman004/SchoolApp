package com.example.SchoolApp.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.example.SchoolApp.model.Student;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;



@Data
public class ResultDto {
	private Long Id;
	private String term;
	private Integer math;
	private Integer english;
	private Integer socialStudies;
	private Integer basicScience;
	@JsonProperty("crk")
	private Integer CRK;
	private Integer civicEducation;
	@JsonProperty("phe")
	private Integer PHE;
	private Integer handwriting;
	private Integer rhymes;
	private Integer verbalReasoning;
	private Integer quantitative;
	private Integer computer;
	private Integer creativeArt;

	
	private Long studentId;
}


