package com.project.back_end.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Doctor;
import com.project.back_end.services.DoctorService;
import com.project.back_end.services.Service;

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
            @PathVariable String date,
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

        return doctorService.getDoctorAvailability(
                doctorId,
                date
        );
    }


    /*
     * Get all doctors
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getDoctors() {

        return doctorService.getDoctors();
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

        return doctorService.saveDoctor(
                doctor
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

        return doctorService.updateDoctor(
                doctor
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

        return doctorService.deleteDoctor(
                id
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