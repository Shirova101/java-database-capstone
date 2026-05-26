package com.project.back_end.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.back_end.models.Doctor;

@Repository
public interface DoctorRepository
        extends JpaRepository<Doctor, Long> {

    // Find doctor by email
    Doctor findByEmail(
            String email
    );


    // Search doctors by partial name
    @Query("""
        SELECT d
        FROM Doctor d
        WHERE d.name
        LIKE CONCAT('%', :name, '%')
    """)
    List<Doctor>
    findByNameLike(
            @Param("name") String name
    );


    // Search by name + speciality (case-insensitive)
    @Query("""
        SELECT d
        FROM Doctor d
        WHERE LOWER(d.name)
        LIKE LOWER(
            CONCAT('%', :name, '%')
        )
        AND LOWER(d.speciality)
        =
        LOWER(:speciality)
    """)
    List<Doctor>
    findByNameContainingIgnoreCaseAndSpecialityIgnoreCase(
            @Param("name") String name,
            @Param("speciality") String speciality
    );


    // Filter by speciality only
    List<Doctor>
    findBySpecialityIgnoreCase(
            String speciality
    );

}