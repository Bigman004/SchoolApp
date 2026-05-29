package com.example.SchoolApp.repository;

import com.example.SchoolApp.model.Result;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.mockito.Mockito.when;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ResultRepositoryTest {
    @Mock
    private ResultRepository resultRepository;

    @Test
    public void testRetrieveResult_returnList() {

        List<Result> list = resultRepository.saveAll(
                List.of(
                        Result.builder()
                                .type("test")
                                .term("1st term")
                                .subjectName("Mathematics")
                                .score(12)
                                .studentId(1L)
                                .schoolId(1L)
                                .build()
                )
        );
        when(resultRepository.findAllByStudentIdAndTermAndType(
                1L, "1st term", "test"))
                .thenReturn(list);
        List<Result> results
                = resultRepository.findAllByStudentIdAndTermAndType(
                        1L, "1st term", "test"
        );

        Assertions.assertNotNull(results);
        Assertions.assertTrue(results.containsAll(list));
    }
}
