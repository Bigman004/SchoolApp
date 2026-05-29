package com.example.SchoolApp.monitoring;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Entity
@Table(name ="logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogMonitor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String requestUri;
    private Boolean authHeader;
    private String requestBody;
    private String remoteIp;
    private String requestMethod;
    private Integer responseStatus;
    private String remoteHost;
    private String remotePort;
    private String originDomain;
    private String responseBody;
    private boolean success;
    private LocalDate eventTime;
    private LocalTime event;
}
