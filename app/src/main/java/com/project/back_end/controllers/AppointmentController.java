package com.project.back_end.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.back_end.models.Appointment;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.Service;

@RestController
@RequestMapping("${api.path}appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    private final Service service;


    // Constructor injection
    public AppointmentController(
            AppointmentService appointmentService,
            Service service
    ) {
        this.appointmentService = appointmentService;
        this.service = service;
    }


    /*
     * GET appointments for doctor dashboard
     */
    @GetMapping("/{date}/{patientName}/{token}")
    public ResponseEntity<Map<String, Object>> getAppointments(
            @PathVariable String date,
            @PathVariable String patientName,
            @PathVariable String token
    ) {

        ResponseEntity<Map<String, String>> validation =
                service.validateToken(token, "doctor");

        if (!validation.getBody().isEmpty()) {
            return ResponseEntity
                    .status(validation.getStatusCode())
                    .body(Map.of(
                            "message",
                            validation.getBody().get("message")
                    ));
        }

        return appointmentService.getAppointment(
                date,
                patientName,
                token
        );
    }


    /*
     * Book appointment
     */
    @PostMapping("/{token}")
    public ResponseEntity<Map<String, String>> bookAppointment(
            @PathVariable String token,
            @RequestBody Appointment appointment
    ) {

        ResponseEntity<Map<String, String>> validation =
                service.validateToken(token, "patient");

        if (!validation.getBody().isEmpty()) {
            return validation;
        }

        int appointmentStatus =
                service.validateAppointment(
                        appointment
                );

        if (appointmentStatus == -1) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(
                            Map.of(
                                    "message",
                                    "Doctor not found"
                            )
                    );
        }

        if (appointmentStatus == 0) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "message",
                                    "Selected time unavailable"
                            )
                    );
        }

        return appointmentService.bookAppointment(
                appointment
        );
    }


    /*
     * Update appointment
     */
    @PutMapping("/{token}")
    public ResponseEntity<Map<String, String>> updateAppointment(
            @PathVariable String token,
            @RequestBody Appointment appointment
    ) {

        ResponseEntity<Map<String, String>> validation =
                service.validateToken(
                        token,
                        "patient"
                );

        if (!validation.getBody().isEmpty()) {
            return validation;
        }

        return appointmentService.updateAppointment(
                appointment
        );
    }


    /*
     * Cancel appointment
     */
    @DeleteMapping("/{id}/{token}")
    public ResponseEntity<Map<String, String>> cancelAppointment(
            @PathVariable Long id,
            @PathVariable String token
    ) {

        ResponseEntity<Map<String, String>> validation =
                service.validateToken(
                        token,
                        "patient"
                );

        if (!validation.getBody().isEmpty()) {
            return validation;
        }

        return appointmentService.cancelAppointment(
                id
        );
    }

}