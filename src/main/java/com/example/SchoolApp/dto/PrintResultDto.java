package com.example.SchoolApp.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PrintResultDto {
    private ResultDto testResult;
    private ResultDto examResult;
    private String studentFirstName;
    private String studentLastName;
    private String studentClass;
    private String regNumber;
}
