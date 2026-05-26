package com.project.back_end.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.back_end.models.Prescription;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.PrescriptionService;
import com.project.back_end.services.Service;

@RestController
@RequestMapping("${api.path}prescription")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    private final Service service;

    private final AppointmentService appointmentService;


    public PrescriptionController(
            PrescriptionService prescriptionService,
            Service service,
            AppointmentService appointmentService
    ) {

        this.prescriptionService = prescriptionService;
        this.service = service;
        this.appointmentService = appointmentService;
    }


    // Save prescription
    @PostMapping("/{token}")
    public ResponseEntity<Map<String, String>> savePrescription(
            @PathVariable String token,
            @RequestBody Prescription prescription
    ) {

        ResponseEntity<Map<String, String>> validation =
                service.validateToken(
                        token,
                        "doctor"
                );

        if (!validation.getBody().isEmpty()) {
            return validation;
        }

        // mark appointment completed
        appointmentService.updateAppointmentStatus(
                prescription.getAppointmentId()
        );

        return prescriptionService.savePrescription(
                prescription
        );
    }


    // Get prescription
    @GetMapping("/{appointmentId}/{token}")
    public ResponseEntity<Map<String, Object>> getPrescription(
            @PathVariable Long appointmentId,
            @PathVariable String token
    ) {

        ResponseEntity<Map<String, String>> validation =
                service.validateToken(
                        token,
                        "doctor"
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

        return prescriptionService.getPrescription(
                appointmentId
        );
    }

}