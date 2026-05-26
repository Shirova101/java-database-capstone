package com.project.back_end.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Doctor;
import com.project.back_end.services.DoctorService;
import com.project.back_end.services.Service;
import java.time.LocalDate;
import java.util.HashMap;


@RestController
@RequestMapping("${api.path}doctor")
public class DoctorController {

    private final DoctorService doctorService;

    private final Service service;


    // Constructor injection
    public DoctorController(
            DoctorService doctorService,
            Service service
    ) {
        this.doctorService = doctorService;
        this.service = service;
    }


    /*
     * Get doctor availability
     */
    @GetMapping("/availability/{user}/{doctorId}/{date}/{token}")
    public ResponseEntity<Map<String, Object>> getDoctorAvailability(
            @PathVariable String user,
            @PathVariable Long doctorId,
            @PathVariable LocalDate date,
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
        Map<String,Object>
        response =
        new HashMap<>();

        response.put(
        "doctors_availabilty",
        doctorService.getDoctorAvailability(
                doctorId,
                date
        )
        );

        return ResponseEntity
        .ok(response);

    }


    /*
     * Get all doctors
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getDoctors() {

        Map<String,Object>
        response =
        new HashMap<>();

        response.put(
        "doctors",
        doctorService.getDoctors()
        );

        return ResponseEntity
        .ok(response);
    }


    /*
     * Add doctor (admin only)
     */
    @PostMapping("/{token}")
        public ResponseEntity<Map<String, String>> saveDoctor(
                @RequestBody Doctor doctor,
                @PathVariable String token
        ) {

        ResponseEntity<Map<String, String>> validation =
                service.validateToken(
                        token,
                        "admin"
                );

        if (!validation.getBody().isEmpty()) {
                return validation;
        }

        int result =
                doctorService.saveDoctor(
                        doctor
                );

        if (result == 1) {

                return ResponseEntity.ok(
                        Map.of(
                                "message",
                                "Doctor saved successfully"
                        )
                );

        }

        if (result == -1) {

                return ResponseEntity.badRequest()
                        .body(
                                Map.of(
                                        "message",
                                        "Doctor already exists"
                                )
                        );

        }

        return ResponseEntity.internalServerError()
                .body(
                        Map.of(
                                "message",
                                "Error saving doctor"
                        )
                );
        }


    /*
     * Doctor login
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> doctorLogin(
            @RequestBody Login login
    ) {

        return doctorService.validateDoctor(
                login
        );
    }


    /*
     * Update doctor
     */
    @PutMapping("/{token}")
        public ResponseEntity<Map<String, String>> updateDoctor(
                @RequestBody Doctor doctor,
                @PathVariable String token
        ) {

        ResponseEntity<Map<String, String>> validation =
                service.validateToken(
                        token,
                        "admin"
                );

        if (!validation.getBody().isEmpty()) {
                return validation;
        }

        int result =
                doctorService.updateDoctor(
                        doctor
                );

        if (result == 1) {

                return ResponseEntity.ok(
                        Map.of(
                                "message",
                                "Doctor updated"
                        )
                );

        }

        if (result == -1) {

                return ResponseEntity.badRequest()
                        .body(
                                Map.of(
                                        "message",
                                        "Doctor not found"
                                )
                        );

        }

        return ResponseEntity.internalServerError()
                .body(
                        Map.of(
                                "message",
                                "Update failed"
                        )
                );
        }


    /*
     * Delete doctor
     */
    @DeleteMapping("/{id}/{token}")
        public ResponseEntity<Map<String, String>> deleteDoctor(
                @PathVariable Long id,
                @PathVariable String token
        ) {

        ResponseEntity<Map<String, String>> validation =
                service.validateToken(
                        token,
                        "admin"
                );

        if (!validation.getBody().isEmpty()) {
                return validation;
        }

        int result =
                doctorService.deleteDoctor(
                        id
                );

        if (result == 1) {

                return ResponseEntity.ok(
                        Map.of(
                                "message",
                                "Doctor deleted"
                        )
                );

        }

        if (result == -1) {

                return ResponseEntity.badRequest()
                        .body(
                                Map.of(
                                        "message",
                                        "Doctor not found"
                                )
                        );

        }

        return ResponseEntity.internalServerError()
                .body(
                        Map.of(
                                "message",
                                "Delete failed"
                        )
                );
        }


    /*
     * Filter doctors
     */
    @GetMapping("/filter/{name}/{time}/{speciality}")
    public ResponseEntity<Map<String, Object>> filterDoctors(
            @PathVariable String name,
            @PathVariable String time,
            @PathVariable String speciality
    ) {

        return ResponseEntity.ok(

                service.filterDoctor(
                        name,
                        speciality,
                        time
                )

        );
    }

}