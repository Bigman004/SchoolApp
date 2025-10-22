package com.example.SchoolApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SchoolApp.model.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
	UserEntity findByRegistrationNumber(String registrationNumber);
}
