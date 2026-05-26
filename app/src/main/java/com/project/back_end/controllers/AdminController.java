package com.project.back_end.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.back_end.models.Admin;
import com.project.back_end.services.Service;

@RestController
@RequestMapping("${api.path}admin")
public class AdminController {

    // Shared business logic service
    private final Service service;

    // Constructor injection
    public AdminController(Service service) {
        this.service = service;
    }

    /*
     * POST /admin
     * Validates admin credentials
     * Returns JWT token if login succeeds
     */
    @PostMapping
    public ResponseEntity<Map<String, String>> adminLogin(
            @RequestBody Admin admin
    ) {

        return service.validateAdmin(admin);
    }

}