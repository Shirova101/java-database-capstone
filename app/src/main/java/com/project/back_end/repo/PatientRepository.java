package com.project.back_end.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.back_end.models.Patient;

@Repository
public interface PatientRepository
        extends JpaRepository<Patient, Long> {

    // Find patient by email
    Patient findByEmail(
            String email
    );


    // Find patient by email OR phone
    Patient findByEmailOrPhoneNumber(
            String email,
            String phoneNumber
    );

}