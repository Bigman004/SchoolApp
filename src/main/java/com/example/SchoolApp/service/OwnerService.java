package com.example.SchoolApp.service;

import com.example.SchoolApp.events.CreateUserEvent;
import com.example.SchoolApp.model.Owner;
import com.example.SchoolApp.repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class OwnerService {
    private OwnerRepository ownerRepository;
    private ApplicationEventPublisher publisher;

    @Autowired
    public OwnerService(OwnerRepository ownerRepository, ApplicationEventPublisher publisher) {
        this.ownerRepository = ownerRepository;
        this.publisher = publisher;
    }


    public void startApplication(){
        Owner owner = Owner.builder().
                name("Adediji Ayomide").
                email("adedijiay123@gmail.com")
                .build();
        ownerRepository.save(owner);
        publisher.publishEvent(new CreateUserEvent(owner.getId(), "ADMIN",
                "ADMIN", "default_password"));


    }
}
