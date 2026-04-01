package com.example.SchoolApp.events;

import com.example.SchoolApp.model.Student;
import java.time.LocalDate;

public class CreateAttendanceEvent {
    public record createAttendanceEvent(
       Student student,
       LocalDate timestamp,
       boolean status,
       String remarks
    ){}
}
