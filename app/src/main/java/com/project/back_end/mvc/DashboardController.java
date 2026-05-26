package com.project.back_end.mvc;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.project.back_end.services.Service;
import com.project.back_end.services.TokenService;
import org.springframework.http.ResponseEntity;

@Controller
public class DashboardController {

    // Shared service for token validation
    @Autowired
    private Service service;


    // Admin dashboard route
    @GetMapping("/adminDashboard/{token}")
    public String adminDashboard(
            @PathVariable String token
    ) {

        ResponseEntity<Map<String, String>> response =
                service.validateToken(
                        token,
                        "admin"
                );

        if (response.getStatusCode().is2xxSuccessful()) {
            return "admin/adminDashboard";
        }

        return "redirect:/";
    }


    // Doctor dashboard route
    @GetMapping("/doctorDashboard/{token}")
    public String doctorDashboard(
            @PathVariable String token
    ) {

        ResponseEntity<Map<String, String>> response =
                service.validateToken(
                        token,
                        "doctor"
                );

        if (response.getStatusCode().is2xxSuccessful()) {
            return "doctor/doctorDashboard";
        }

        return "redirect:/";
    }

}