package com.example.SchoolApp.events;

import com.example.SchoolApp.model.Role;

public class CreateUserEvent {

    public record createUserEvent(
            Long referenceId,      // studentId, teacherId, staffId etc.
            String username,
            Role role
    ){}
}
