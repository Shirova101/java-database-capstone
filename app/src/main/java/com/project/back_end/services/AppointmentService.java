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
    @Transactional
    public int bookAppointment(
            Appointment appointment
    ) {

        try {

            appointmentRepository
                    .save(
                            appointment
                    );

            return 1;

        } catch (Exception e) {

            return 0;

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

        Map<String, Object> validation =
                service.validateAppointment(
                        appointment
                );

        if (!validation.isEmpty()) {

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
    public Map<String, Object>
    getAppointment(
            String pname,
            LocalDate date,
            String token
    ) {

        Map<String, Object> result =
                new HashMap<>();

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

        if (pname == null
                || pname.equals("null")
                || pname.isBlank()) {

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

        return result;
    }

}