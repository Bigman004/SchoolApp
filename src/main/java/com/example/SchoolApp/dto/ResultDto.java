package com.example.SchoolApp.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.example.SchoolApp.model.Student;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultDto {
	private Long Id;
	private String term;
	private String subjectName;
	@Min(0)
	@Max(100)
	private Integer score;
	private String type;
	private Long studentId;
	private String classOfStudent;
	private Long schoolId;
}


