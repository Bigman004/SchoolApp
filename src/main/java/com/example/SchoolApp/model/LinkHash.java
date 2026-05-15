package com.example.SchoolApp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name ="reset_link")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinkHash {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String email;
    private String link;
    private String username;
    private LocalDateTime created;

    public boolean isExpired(){
        return created.plusMinutes(10).isBefore(LocalDateTime.now());
    }
}
