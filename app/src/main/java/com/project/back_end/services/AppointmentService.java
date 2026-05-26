package com.project.back_end.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;


import com.project.back_end.models.Appointment;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import com.project.back_end.services.TokenService;
import org.springframework.http.HttpStatus;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private com.project.back_end.services.Service service;


    // Book appointment
   // Book appointment
        @Transactional
        public ResponseEntity<Map<String, String>>
        bookAppointment(
                Appointment appointment
        ) {

        Map<String, String> response =
                new HashMap<>();

        try {

                appointmentRepository
                        .save(
                                appointment
                        );

                response.put(
                        "message",
                        "Appointment booked successfully"
                );

                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(response);

        } catch (Exception e) {

                response.put(
                        "message",
                        "Failed to book appointment"
                );

                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(response);
        }
        }


    // Update appointment
    @Transactional
    public ResponseEntity<Map<String, String>>
    updateAppointment(
            Appointment appointment
    ) {

        Map<String, String> response =
                new HashMap<>();

        Optional<Appointment> existing =
                appointmentRepository
                        .findById(
                                appointment.getId()
                        );

        if (existing.isEmpty()) {

            response.put(
                    "message",
                    "Appointment not found"
            );

            return ResponseEntity
                    .badRequest()
                    .body(response);
        }

        int validation =
                service.validateAppointment(
                        appointment
                );

        if (validation == -1) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(
                            Map.of(
                                    "message",
                                    "Doctor not found"
                            )
                    );
        }
        if (validation == 0) {

            response.put(
                    "message",
                    "Invalid appointment"
            );

            return ResponseEntity
                    .badRequest() 
                    .body(response);
        }

        appointmentRepository
                .save(
                        appointment
                );

        response.put(
                "message",
                "Appointment updated"
        );

        return ResponseEntity
                .ok(response);
    }

        @Transactional
        public ResponseEntity<Map<String, String>>
        updateAppointmentStatus(
                Long appointmentId
        ) {

        Map<String, String> response =
                new HashMap<>();

        Optional<Appointment> existing =
                appointmentRepository
                        .findById(
                                appointmentId
                        );

        if (existing.isEmpty()) {

                response.put(
                        "message",
                        "Appointment not found"
                );

                return ResponseEntity
                        .badRequest()
                        .body(response);
        }

        Appointment appointment =
                existing.get();

        appointment.setStatus(1);
        // 0 = pending
        // 1 = completed

        appointmentRepository
                .save(
                        appointment
                );

        response.put(
                "message",
                "Appointment updated"
        );

        return ResponseEntity
                .ok(response);

        }


        // Cancel appointment
        @Transactional
        public ResponseEntity<Map<String, String>>
        cancelAppointment(
                long id,
                String token
        ) {

                Map<String, String> response =
                        new HashMap<>();

                Optional<Appointment> appointment =
                        appointmentRepository
                                .findById(id);

                if (appointment.isEmpty()) {

                        response.put(
                                "message",
                                "Appointment not found"
                        );

                        return ResponseEntity
                                .badRequest()
                                .body(response);
                }

                Long patientId =
                        tokenService.getId(
                                token
                        );

                if (!appointment
                        .get()
                        .getPatient()
                        .getId()
                        .equals(patientId)) {

                        response.put(
                                "message",
                                "Unauthorized"
                        );

                        return ResponseEntity
                                .badRequest()
                                .body(response);
                }

                appointmentRepository
                        .delete(
                                appointment.get()
                        );

                response.put(
                        "message",
                        "Appointment cancelled"
                );

                return ResponseEntity
                        .ok(response);
        }


    // Get appointments
        public ResponseEntity<Map<String, Object>>
        getAppointment(
                LocalDate date,
                String pname,
                String token
        ) {

        Map<String, Object> result =
                new HashMap<>();

        try {

                Long doctorId =
                        tokenService.getId(
                                token
                        );

                LocalDateTime start =
                        date.atStartOfDay();

                LocalDateTime end =
                        date
                                .plusDays(1)
                                .atStartOfDay();

                List<Appointment> appointments;

                if (
                        pname == null
                        || pname.equals("null")
                        || pname.isBlank()
                ) {

                appointments =
                        appointmentRepository
                                .findByDoctorIdAndAppointmentTimeBetween(
                                        doctorId,
                                        start,
                                        end
                                );

                } else {

                appointments =
                        appointmentRepository
                                .findByDoctorIdAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(
                                        doctorId,
                                        pname,
                                        start,
                                        end
                                );
                }

                result.put(
                        "appointments",
                        appointments
                );

                return ResponseEntity
                        .ok(result);

        } catch (Exception e) {

                result.put(
                        "message",
                        "Error fetching appointments"
                );

                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(result);
        }
}

}