package com.project.back_end.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Patient;
import com.project.back_end.services.PatientService;
import com.project.back_end.services.Service;

@RestController
@RequestMapping("${api.path}patient")
public class PatientController {

    private final PatientService patientService;
    private final Service service;


    public PatientController(
            PatientService patientService,
            Service service
    ) {
        this.patientService = patientService;
        this.service = service;
    }


    // Get patient details
    @GetMapping("/{token}")
    public ResponseEntity<Map<String, Object>> getPatient(
            @PathVariable String token
    ) {

        ResponseEntity<Map<String, String>> validation =
                service.validateToken(token, "patient");

        if (!validation.getBody().isEmpty()) {

            return ResponseEntity
                    .status(validation.getStatusCode())
                    .body(
                            Map.of(
                                    "message",
                                    validation.getBody().get("message")
                            )
                    );
        }

        return patientService.getPatientDetails(token);
    }


    // Register patient
    @PostMapping
    public ResponseEntity<Map<String, String>> createPatient(
            @RequestBody Patient patient
    ) {

        boolean valid =
                service.validatePatient(patient);

        if (!valid) {

            return ResponseEntity
                    .status(409)
                    .body(
                            Map.of(
                                    "message",
                                    "Patient with email id or phone no already exist"
                            )
                    );
        }

        return patientService.createPatient(patient);
    }


    // Login
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(
            @RequestBody Login login
    ) {

        return service.validatePatientLogin(login);
    }


    // Get appointments
    @GetMapping("/{id}/{user}/{token}")
    public ResponseEntity<Map<String, Object>> getPatientAppointment(
            @PathVariable Long id,
            @PathVariable String user,
            @PathVariable String token
    ) {

        ResponseEntity<Map<String, String>> validation =
                service.validateToken(
                        token,
                        user
                );

        if (!validation.getBody().isEmpty()) {

            return ResponseEntity
                    .status(validation.getStatusCode())
                    .body(
                            Map.of(
                                    "message",
                                    validation.getBody().get("message")
                            )
                    );
        }

        return patientService.getPatientAppointment(
                id,
                token,
                user
        );
    }


    // Filter appointments
    @GetMapping("/filter/{condition}/{name}/{token}")
    public ResponseEntity<Map<String, Object>> filterPatientAppointment(
            @PathVariable String condition,
            @PathVariable String name,
            @PathVariable String token
    ) {

        ResponseEntity<Map<String, String>> validation =
                service.validateToken(
                        token,
                        "patient"
                );

        if (!validation.getBody().isEmpty()) {

            return ResponseEntity
                    .status(validation.getStatusCode())
                    .body(
                            Map.of(
                                    "message",
                                    validation.getBody().get("message")
                            )
                    );
        }

        return service.filterPatient(
                condition,
                name,
                token
        );
    }

}