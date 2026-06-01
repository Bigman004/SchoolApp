package com.example.SchoolApp.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Builder
@Data
public class PrintResultDto {
    private Map<String, Integer> testResult;
    private Map<String, Integer> examResult;
    private String studentFirstName;
    private String studentLastName;
    private String studentClass;
    private String regNumber;
    private String schoolName;
}
