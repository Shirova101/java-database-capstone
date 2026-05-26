package com.project.back_end.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.project.back_end.models.Prescription;
import com.project.back_end.repo.PrescriptionRepository;

@Service
public class PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;


    // Save prescription
    public ResponseEntity<Map<String, String>>
    savePrescription(
            Prescription prescription
    ) {

        Map<String, String> response =
                new HashMap<>();

        try {

            List<Prescription> existing =
                    prescriptionRepository
                            .findByAppointmentId(
                                    prescription
                                            .getAppointmentId()
                            );

            if (!existing.isEmpty()) {

                response.put(
                        "message",
                        "Prescription already exists"
                );

                return ResponseEntity
                        .badRequest()
                        .body(response);
            }

            prescriptionRepository
                    .save(
                            prescription
                    );

            response.put(
                    "message",
                    "Prescription saved"
            );

            return ResponseEntity
                    .status(
                            HttpStatus.CREATED
                    )
                    .body(response);

        } catch (Exception e) {

            e.printStackTrace();

            response.put(
                    "message",
                    "Failed to save prescription"
            );

            return ResponseEntity
                    .internalServerError()
                    .body(response);
        }
    }


    // Get prescription by appointment id
    public ResponseEntity<Map<String, Object>>
    getPrescription(
            Long appointmentId
    ) {

        Map<String, Object> response =
                new HashMap<>();

        try {

            List<Prescription> prescriptions =
                    prescriptionRepository
                            .findByAppointmentId(
                                    appointmentId
                            );

            response.put(
                    "prescriptions",
                    prescriptions
            );

            return ResponseEntity
                    .ok(response);

        } catch (Exception e) {

            e.printStackTrace();

            response.put(
                    "message",
                    "Error retrieving prescription"
            );

            return ResponseEntity
                    .internalServerError()
                    .body(response);
        }
    }

}