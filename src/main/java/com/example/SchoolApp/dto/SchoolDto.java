package com.example.SchoolApp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchoolDto {
    private Long id;
    private String schoolName;
    private String schoolCode;
    private String schoolAddress;
    private String firstName;
    private String lastName;
    private String email;
}

