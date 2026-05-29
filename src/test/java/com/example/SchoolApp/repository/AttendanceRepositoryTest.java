package com.example.SchoolApp.repository;

import com.example.SchoolApp.model.Attendance;
import com.example.SchoolApp.model.Student;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.time.LocalDate;
import java.util.List;
import static org.mockito.Mockito.when;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class AttendanceRepositoryTest {

    @Mock
    private AttendanceRepository attendanceRepository;

    @Test
    public void testFindByStudentIdAndClassName() {
        List<Attendance> list = attendanceRepository.saveAll(List.of(Attendance.builder()
                        .student(new Student())
                        .className("primary 1")
                        .timestamp(LocalDate.now())
                                .schoolId(1L)
                        .build(),
                Attendance.builder()
                .student(new Student())
                .className("primary 1")
                        .schoolId(1L)
                .timestamp(LocalDate.now().minusDays(1))
                .build())
        );
        when(attendanceRepository.countDistinctSchoolId(1L)).thenReturn(1);
        Assertions.assertEquals(1, attendanceRepository.countDistinctSchoolId(1L));

    }
}
