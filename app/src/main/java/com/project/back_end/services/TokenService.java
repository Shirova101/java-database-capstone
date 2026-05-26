package com.project.back_end.services;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.project.back_end.models.Admin;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;

@Component
public class TokenService {

    @Value("${jwt.secret}")
    private String secret;


    private final AdminRepository adminRepository;

    private final DoctorRepository doctorRepository;

    private final PatientRepository patientRepository;


    public TokenService(
            AdminRepository adminRepository,
            DoctorRepository doctorRepository,
            PatientRepository patientRepository
    ) {

        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }


    // Build signing key
    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(
                secret.getBytes(
                        StandardCharsets.UTF_8
                )
        );
    }


    // Generate JWT
    public String generateToken(
            Long identifier
    ) {

        return Jwts
                .builder()
                .subject(
                        String.valueOf(
                                identifier
                        )
                )
                .issuedAt(
                        new Date()
                )
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                +
                                1000L
                                * 60
                                * 60
                                * 24
                                * 7
                        )
                )
                .signWith(
                        getSigningKey()
                )
                .compact();
    }


    // Extract identifier from token
    public String extractIdentifier(
            String token
    ) {

        Claims claims =
                Jwts
                        .parser()
                        .verifyWith(
                                getSigningKey()
                        )
                        .build()
                        .parseSignedClaims(
                                token
                        )
                        .getPayload();

        return claims.getSubject();
    }


    // Convenience helper
    public Long getId(
            String token
    ) {

        return Long.parseLong(
                extractIdentifier(
                        token
                )
        );
    }


    // Validate token + verify user exists
    public boolean validateToken(
            String token,
            String user
    ) {

        try {

            String identifier =
                    extractIdentifier(
                            token
                    );

            Long id =
                    Long.parseLong(
                            identifier
                    );

            switch (
                    user.toLowerCase()
            ) {

                case "admin":

                    Admin admin =
                            adminRepository
                                    .findById(id)
                                    .orElse(null);

                    return admin != null;


                case "doctor":

                    Doctor doctor =
                            doctorRepository
                                    .findById(id)
                                    .orElse(null);

                    return doctor != null;


                case "patient":

                    Patient patient =
                            patientRepository
                                    .findById(id)
                                    .orElse(null);

                    return patient != null;


                default:
                    return false;
            }

        } catch (Exception e) {

            return false;
        }
    }

}