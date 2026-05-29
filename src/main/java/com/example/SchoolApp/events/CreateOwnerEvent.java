package com.example.SchoolApp.events;

public record CreateOwnerEvent(
        Long id,
        String schoolName,
        String schoolCode,
        String schoolAddress,
        long schoolId
    ) {
    }
