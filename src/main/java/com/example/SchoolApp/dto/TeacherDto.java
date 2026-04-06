package com.example.SchoolApp.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeacherDto {
    private String name;
    private String teacherClass;
    private String teacherEmail;
    private String username;
    private long Id;
}
