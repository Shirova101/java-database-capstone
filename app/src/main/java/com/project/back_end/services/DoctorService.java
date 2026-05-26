package com.project.back_end.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private TokenService tokenService;


    // Get available slots after removing booked times
    @Transactional
    public List<String> getDoctorAvailability(
            Long doctorId,
            LocalDate date
    ) {

        Doctor doctor =
                doctorRepository
                        .findById(doctorId)
                        .orElse(null);

        if (doctor == null)
            return List.of();

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();

        List<Appointment> appointments =
                appointmentRepository
                        .findByDoctorIdAndAppointmentTimeBetween(
                                doctorId,
                                start,
                                end
                        );

        List<String> booked =
                appointments.stream()
                        .map(a ->
                                a.getAppointmentTimeOnly()
                                        .toString()
                        )
                        .toList();

        return doctor
                .getAvailableTimes()
                .stream()
                .filter(
                        slot ->
                                booked.stream()
                                        .noneMatch(
                                                slot::contains
                                        )
                )
                .toList();
    }


    // Save doctor
    public int saveDoctor(
            Doctor doctor
    ) {

        try {

            if (
                    doctorRepository
                            .findByEmail(
                                    doctor.getEmail()
                            ) != null
            ) {
                return -1;
            }

            doctorRepository.save(doctor);

            return 1;

        } catch (Exception e) {

            return 0;
        }
    }


    // Update doctor
    public int updateDoctor(
            Doctor doctor
    ) {

        try {

            if (
                    doctorRepository
                            .findById(
                                    doctor.getId()
                            )
                            .isEmpty()
            ) {
                return -1;
            }

            doctorRepository.save(doctor);

            return 1;

        } catch (Exception e) {

            return 0;
        }
    }


    @Transactional
    public List<Doctor> getDoctors() {

        return doctorRepository.findAll();
    }


    // Delete doctor + appointments
    public int deleteDoctor(
            long id
    ) {

        try {

            Doctor doctor =
                    doctorRepository
                            .findById(id)
                            .orElse(null);

            if (doctor == null)
                return -1;

            appointmentRepository
                    .deleteAllByDoctorId(id);

            doctorRepository.delete(doctor);

            return 1;

        } catch (Exception e) {

            return 0;
        }
    }


    // Doctor login
    public ResponseEntity<Map<String, String>>
    validateDoctor(
            Login login
    ) {

        Map<String, String> response =
                new HashMap<>();

        Doctor doctor =
                doctorRepository
                        .findByEmail(
                                login.getIdentifier()
                        );

        if (doctor == null) {

            response.put(
                    "message",
                    "Doctor not found"
            );

            return ResponseEntity
                    .badRequest()
                    .body(response);
        }

        if (
                !doctor
                        .getPassword()
                        .equals(
                                login.getPassword()
                        )
        ) {

            response.put(
                    "message",
                    "Invalid password"
            );

            return ResponseEntity
                    .badRequest()
                    .body(response);
        }

        String token =
                tokenService
                        .generateToken(
                                doctor.getId()
                        );

        response.put(
                "token",
                token
        );

        return ResponseEntity.ok(response);
    }


    @Transactional
    public Map<String, Object>
    findDoctorByName(
            String name
    ) {

        Map<String, Object> result =
                new HashMap<>();

        result.put(
                "doctors",
                doctorRepository
                        .findByNameLike(name)
        );

        return result;
    }


    public Map<String, Object>
    filterDoctorsByNameSpecilityandTime(
            String name,
            String specialty,
            String amOrPm
    ) {

        List<Doctor> doctors =
                doctorRepository
                        .findByNameContainingIgnoreCaseAndSpecialityIgnoreCase(
                                name,
                                specialty
                        );

        return Map.of(
                "doctors",
                filterDoctorByTime(
                        doctors,
                        amOrPm
                )
        );
    }


    public Map<String, Object>
    filterDoctorByNameAndTime(
            String name,
            String amOrPm
    ) {

        List<Doctor> doctors =
                doctorRepository
                        .findByNameLike(name);

        return Map.of(
                "doctors",
                filterDoctorByTime(
                        doctors,
                        amOrPm
                )
        );
    }


    public Map<String, Object>
    filterDoctorByNameAndSpecility(
            String name,
            String specialty
    ) {

        return Map.of(
                "doctors",
                doctorRepository
                        .findByNameContainingIgnoreCaseAndSpecialityIgnoreCase(
                                name,
                                specialty
                        )
        );
    }


    public Map<String, Object>
    filterDoctorByTimeAndSpecility(
            String specialty,
            String amOrPm
    ) {

        List<Doctor> doctors =
                doctorRepository
                        .findBySpecialityIgnoreCase(
                                specialty
                        );

        return Map.of(
                "doctors",
                filterDoctorByTime(
                        doctors,
                        amOrPm
                )
        );
    }


    public Map<String, Object>
    filterDoctorBySpecility(
            String specialty
    ) {

        return Map.of(
                "doctors",
                doctorRepository
                        .findBySpecialityIgnoreCase(
                                specialty
                        )
        );
    }


    public Map<String, Object>
    filterDoctorsByTime(
            String amOrPm
    ) {

        return Map.of(
                "doctors",
                filterDoctorByTime(
                        doctorRepository.findAll(),
                        amOrPm
                )
        );
    }


    // Shared time filtering helper
    private List<Doctor>
    filterDoctorByTime(
            List<Doctor> doctors,
            String amOrPm
    ) {

        return doctors
                .stream()
                .filter(
                        doctor -> doctor.getAvailableTimes()
                                        .stream()
                                        .anyMatch(
                                                slot -> {

                                                    int hour =
                                                            Integer.parseInt(
                                                                    slot
                                                                            .split(":")[0]
                                                            );

                                                    return amOrPm.equalsIgnoreCase("AM")
                                                            ? hour < 12
                                                            : hour >= 12;

                                                }
                                        )
                )
                .collect(
                        Collectors.toList()
                );
    }

}