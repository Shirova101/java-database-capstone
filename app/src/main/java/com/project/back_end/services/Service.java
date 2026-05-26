package com.project.back_end.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Admin;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;

public class Service {

    private final TokenService tokenService;

    private final AdminRepository adminRepository;

    private final DoctorRepository doctorRepository;

    private final PatientRepository patientRepository;

    private final DoctorService doctorService;

    private final PatientService patientService;


    public Service(
            TokenService tokenService,
            AdminRepository adminRepository,
            DoctorRepository doctorRepository,
            PatientRepository patientRepository,
            DoctorService doctorService,
            PatientService patientService
    ) {

        this.tokenService = tokenService;
        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }


    // Validate JWT
    public ResponseEntity<Map<String, String>>
    validateToken(
            String token,
            String user
    ) {

        Map<String, String> response =
                new HashMap<>();

        try {

            boolean valid =
                    tokenService
                            .validateToken(
                                    token,
                                    user
                            );

            if (!valid) {

                response.put(
                        "message",
                        "Invalid or expired token"
                );

                return ResponseEntity
                        .status(
                                HttpStatus.UNAUTHORIZED
                        )
                        .body(response);
            }

            return ResponseEntity.ok(
                    response
            );

        } catch (Exception e) {

            response.put(
                    "message",
                    "Token validation failed"
            );

            return ResponseEntity
                    .status(
                            HttpStatus.UNAUTHORIZED
                    )
                    .body(response);
        }
    }


    // Admin login
    public ResponseEntity<Map<String, String>>
    validateAdmin(
            Admin receivedAdmin
    ) {

        Map<String, String> response =
                new HashMap<>();

        try {

            Admin admin =
                    adminRepository
                            .findByUsername(
                                    receivedAdmin
                                            .getUsername()
                            );

            if (
                    admin == null
            ) {

                response.put(
                        "message",
                        "Admin not found"
                );

                return ResponseEntity
                        .status(
                                HttpStatus.UNAUTHORIZED
                        )
                        .body(response);
            }

            if (
                    !admin
                            .getPassword()
                            .equals(
                                    receivedAdmin
                                            .getPassword()
                            )
            ) {

                response.put(
                        "message",
                        "Invalid password"
                );

                return ResponseEntity
                        .status(
                                HttpStatus.UNAUTHORIZED
                        )
                        .body(response);
            }

            String token =
                    tokenService
                            .generateToken(
                                    admin.getId()
                            );

            response.put(
                    "token",
                    token
            );

            return ResponseEntity.ok(
                    response
            );

        } catch (Exception e) {

            response.put(
                    "message",
                    "Login failed"
            );

            return ResponseEntity
                    .internalServerError()
                    .body(response);
        }
    }


    // Doctor filtering
    public Map<String, Object>
    filterDoctor(
            String name,
            String specialty,
            String time
    ) {

        name =
                name == null
                        ? ""
                        : name;

        boolean hasName =
                !name.isBlank();

        boolean hasSpecialty =
                specialty != null
                        && !specialty.isBlank();

        boolean hasTime =
                time != null
                        && !time.isBlank();

        if (
                hasName
                        &&
                        hasSpecialty
                        &&
                        hasTime
        ) {

            return doctorService
                    .filterDoctorsByNameSpecilityandTime(
                            name,
                            specialty,
                            time
                    );
        }

        if (
                hasName
                        &&
                        hasSpecialty
        ) {

            return doctorService
                    .filterDoctorByNameAndSpecility(
                            name,
                            specialty
                    );
        }

        if (
                hasName
                        &&
                        hasTime
        ) {

            return doctorService
                    .filterDoctorByNameAndTime(
                            name,
                            time
                    );
        }

        if (
                hasSpecialty
                        &&
                        hasTime
        ) {

            return doctorService
                    .filterDoctorByTimeAndSpecility(
                            specialty,
                            time
                    );
        }

        if (hasName) {

            return doctorService
                    .findDoctorByName(
                            name
                    );
        }

        if (hasSpecialty) {

            return doctorService
                    .filterDoctorBySpecility(
                            specialty
                    );
        }

        if (hasTime) {

            return doctorService
                    .filterDoctorsByTime(
                            time
                    );
        }

        return Map.of(
                "doctors",
                doctorService.getDoctors()
        );
    }


    // Validate appointment slot
    public int validateAppointment(
            Appointment appointment
    ) {

        Doctor doctor =
                doctorRepository
                        .findById(
                                appointment
                                        .getDoctor()
                                        .getId()
                        )
                        .orElse(null);

        if (
                doctor == null
        ) {

            return -1;
        }

        List<String> available =
                doctorService
                        .getDoctorAvailability(
                                doctor.getId(),
                                appointment
                                        .getAppointmentDate()
                        );

        LocalTime requested =
                appointment
                        .getAppointmentTimeOnly();

        boolean exists =
                available
                        .stream()
                        .anyMatch(
                                slot ->
                                        slot.startsWith(
                                                requested.toString()
                                        )
                        );

        return exists
                ? 1
                : 0;
    }


    // Prevent duplicate patients
    public boolean validatePatient(
            Patient patient
    ) {

        Patient existing =
                patientRepository
                        .findByEmailOrPhone(
                                patient.getEmail(),
                                patient.getPhoneNumber()
                        );

        return existing == null;
    }


    // Patient login
    public ResponseEntity<Map<String, String>>
    validatePatientLogin(
            Login login
    ) {

        Map<String, String> response =
                new HashMap<>();

        Patient patient =
                patientRepository
                        .findByEmail(
                                login
                                        .getIdentifier()
                        );

        if (
                patient == null
        ) {

            response.put(
                    "message",
                    "Patient not found"
            );

            return ResponseEntity
                    .status(
                            HttpStatus.UNAUTHORIZED
                    )
                    .body(response);
        }

        if (
                !patient
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
                    .status(
                            HttpStatus.UNAUTHORIZED
                    )
                    .body(response);
        }

        String token =
                tokenService
                        .generateToken(
                                patient.getId()
                        );

        response.put(
                "token",
                token
        );

        return ResponseEntity.ok(
                response
        );
    }


    // Filter patient appointments
    public ResponseEntity<Map<String, Object>>
    filterPatient(
            String condition,
            String name,
            String token
    ) {

        String email =
                tokenService
                        .extractIdentifier(
                                token
                        );

        Patient patient =
                patientRepository
                        .findByEmail(
                                email
                        );

        Long id =
                patient.getId();

        if (
                condition != null
                        &&
                        !condition.isBlank()
                        &&
                        name != null
                        &&
                        !name.isBlank()
        ) {

            return patientService
                    .filterByDoctorAndCondition(
                            condition,
                            name,
                            id
                    );
        }

        if (
                condition != null
                        &&
                        !condition.isBlank()
        ) {

            return patientService
                    .filterByCondition(
                            condition,
                            id
                    );
        }

        if (
                name != null
                        &&
                        !name.isBlank()
        ) {

            return patientService
                    .filterByDoctor(
                            name,
                            id
                    );
        }

        return patientService
                .getPatientAppointment(
                        id,
                        token
                );
    }

}