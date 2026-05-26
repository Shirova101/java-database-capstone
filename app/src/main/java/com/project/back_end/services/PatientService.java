package com.project.back_end.services;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import com.project.back_end.DTO.AppointmentDTO;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.PatientRepository;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private TokenService tokenService;


    // Save patient
    public int createPatient(Patient patient) {

        try {
            patientRepository.save(patient);
            return 1;

        } catch (Exception e) {

            e.printStackTrace();
            return 0;
        }
    }


    // Get all appointments for logged-in patient
    @Transactional
    public ResponseEntity<Map<String, Object>>
    getPatientAppointment(
            Long id,
            String token
    ) {

        Map<String, Object> response =
                new HashMap<>();

        try {

            String email =
                    tokenService
                            .extractIdentifier(token);

            Patient patient =
                    patientRepository
                            .findByEmail(email);

            if (
                    patient == null ||
                    !patient.getId().equals(id)
            ) {

                response.put(
                        "message",
                        "Unauthorized"
                );

                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(response);
            }

            List<AppointmentDTO> appointments =
                    appointmentRepository
                            .findByPatientId(id)
                            .stream()
                            .map(this::convertToDTO)
                            .collect(Collectors.toList());

            response.put(
                    "appointments",
                    appointments
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {

            response.put(
                    "message",
                    "Error loading appointments"
            );

            return ResponseEntity
                    .internalServerError()
                    .body(response);
        }
    }


    // Filter by past/future
    public ResponseEntity<Map<String, Object>>
    filterByCondition(
            String condition,
            Long id
    ) {

        Map<String, Object> response =
                new HashMap<>();

        try {

            int status;

            if (
                    condition.equalsIgnoreCase("past")
            ) {

                status = 1;

            } else if (
                    condition.equalsIgnoreCase("future")
            ) {

                status = 0;

            } else {

                response.put(
                        "message",
                        "Invalid condition"
                );

                return ResponseEntity
                        .badRequest()
                        .body(response);
            }

            List<AppointmentDTO> appointments =
                    appointmentRepository
                            .findByPatientIdAndStatus(
                                    id,
                                    status
                            )
                            .stream()
                            .map(this::convertToDTO)
                            .toList();

            response.put(
                    "appointments",
                    appointments
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {

            response.put(
                    "message",
                    "Filter failed"
            );

            return ResponseEntity
                    .internalServerError()
                    .body(response);
        }
    }


    // Filter by doctor name
    public ResponseEntity<Map<String, Object>>
    filterByDoctor(
            String name,
            Long patientId
    ) {

        Map<String, Object> response =
                new HashMap<>();

        try {

            List<AppointmentDTO> appointments =
                    appointmentRepository
                            .findByDoctorNameAndPatientId(
                                    name,
                                    patientId
                            )
                            .stream()
                            .map(this::convertToDTO)
                            .toList();

            response.put(
                    "appointments",
                    appointments
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {

            response.put(
                    "message",
                    "Doctor filter failed"
            );

            return ResponseEntity
                    .internalServerError()
                    .body(response);
        }
    }


    // Filter by doctor + condition
    public ResponseEntity<Map<String, Object>>
    filterByDoctorAndCondition(
            String condition,
            String name,
            long patientId
    ) {

        try {

            ResponseEntity<Map<String, Object>>
                    filtered =
                    filterByCondition(
                            condition,
                            patientId
                    );

            List<AppointmentDTO> appointments =
                    (
                        List<AppointmentDTO>
                    )
                    filtered
                            .getBody()
                            .get(
                                "appointments"
                            );

            List<AppointmentDTO> result =
                    appointments
                            .stream()
                            .filter(
                                a ->
                                    a.getDoctorName()
                                     .toLowerCase()
                                     .contains(
                                        name.toLowerCase()
                                     )
                            )
                            .toList();

            return ResponseEntity.ok(
                    Map.of(
                        "appointments",
                        result
                    )
            );

        } catch (Exception e) {

            return ResponseEntity
                    .internalServerError()
                    .body(
                        Map.of(
                            "message",
                            "Filter failed"
                        )
                    );
        }
    }


    // Get patient profile using token
    public ResponseEntity<Map<String, Object>>
    getPatientDetails(
            String token
    ) {

        Map<String, Object> response =
                new HashMap<>();

        try {

            String email =
                    tokenService
                            .extractIdentifier(token);

            Patient patient =
                    patientRepository
                            .findByEmail(email);

            if (patient == null) {

                response.put(
                        "message",
                        "Patient not found"
                );

                return ResponseEntity
                        .notFound()
                        .build();
            }

            response.put(
                    "patient",
                    patient
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {

            response.put(
                    "message",
                    "Error loading patient"
            );

            return ResponseEntity
                    .internalServerError()
                    .body(response);
        }
    }


    // Convert Appointment → DTO
    private AppointmentDTO convertToDTO(
            Appointment a
    ) {

        return new AppointmentDTO(
                a.getId(),

                a.getDoctor().getId(),
                a.getDoctor().getName(),

                a.getPatient().getId(),
                a.getPatient().getName(),
                a.getPatient().getEmail(),
                a.getPatient().getPhone(),
                a.getPatient().getAddress(),

                a.getAppointmentTime(),
                a.getStatus()
        );
    }

}