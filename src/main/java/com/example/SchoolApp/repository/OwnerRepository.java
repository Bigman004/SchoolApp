package com.example.SchoolApp.repository;

import com.example.SchoolApp.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OwnerRepository extends JpaRepository<Owner,Long> {
}
