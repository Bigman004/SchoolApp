package com.example.SchoolApp.monitoring;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface LogRepository extends JpaRepository<LogMonitor, Long> {
    Page<LogMonitor> findAllByEventTime(Pageable pageable, LocalDate time);

    Page<LogMonitor> findAllByUsernameAndEventTime(Pageable pageable, String username, LocalDate time);

    Page<LogMonitor> findAllByRequestMethodAndEventTime(Pageable pageable, String requestMethod, LocalDate time);

    Page<LogMonitor> findAllByResponseStatusAndEventTime(Pageable pageable, int responseStatus, LocalDate time);

    Page<LogMonitor> findAllByRequestUriAndEventTime(Pageable pageable, String requestUri, LocalDate time);

}
