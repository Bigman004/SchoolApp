package com.example.SchoolApp.monitoring;

import com.example.SchoolApp.events.CreateLogEvent;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.LoggingEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Slf4j
@Service
public class LogService {

    private LogRepository logRepository;

    @Autowired
    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }
    @EventListener
    public void onApplicationEvent(CreateLogEvent event) {
        LogMonitor logMonitor = LogMonitor.builder()
                .responseBody(event.requestBody())
                .username(event.username())
                .requestUri(event.requestUri())
                .authHeader(event.authHeader())
                .remoteIp(event.remoteIp())
                .responseStatus(event.responseStatus())
                .requestMethod(event.requestMethod())
                .originDomain(event.originDomain())
                .remotePort(event.remotePort())
                .eventTime(LocalDate.now())
                .event(LocalTime.now())
                .success(event.success())
                .build();
        log.info(logMonitor.getRequestUri()+ ":   login to developer console to see more details");
        logRepository.save(logMonitor);
    }

    public Page<LogMonitor> getTodayLogs(Pageable pageable, String searchBy, String searchParams,
                                         LocalDate searchDate) {
        if (searchDate == null) {
            searchDate = LocalDate.now();
        }
        if (searchBy.isEmpty()|| searchParams.isEmpty())
         return logRepository.findAllByEventTime(pageable, searchDate);
        else{
            return switch (searchBy) {
                case "method" ->
                        logRepository.findAllByRequestMethodAndEventTime(pageable, searchParams, searchDate);
                case "username" -> logRepository.findAllByUsernameAndEventTime(pageable, searchParams, searchDate);
                case "uri" -> logRepository.findAllByRequestUriAndEventTime(pageable, searchParams, searchDate);
                case "status" ->
                        logRepository.findAllByResponseStatusAndEventTime(pageable, Integer.parseInt(searchParams), searchDate);
                default -> logRepository.findAllByEventTime(pageable, searchDate);
            };

        }
    }
}
