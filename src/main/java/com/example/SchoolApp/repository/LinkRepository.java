package com.example.SchoolApp.repository;

import com.example.SchoolApp.model.LinkHash;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkRepository extends JpaRepository<LinkHash, Long> {

    boolean existsByLink(String result);

    LinkHash findByLink(String link);
}
