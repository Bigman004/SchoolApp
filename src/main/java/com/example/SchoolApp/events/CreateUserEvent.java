package com.example.SchoolApp.events;

import com.example.SchoolApp.model.Role;



    public record CreateUserEvent(
            Long referenceId,      // studentId, teacherId, staffId etc.
            String role,
            String password
    ){}

