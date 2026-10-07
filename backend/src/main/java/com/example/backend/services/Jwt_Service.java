package com.example.backend.services;


import java.time.Instant;
import java.time.temporal.ChronoUnit;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;


import org.springframework.stereotype.Service;


@Service
public class Jwt_Service {

    private static final String SECRET = "change-this-to-a-long-random-secret-key";

    private final JwtEncoder encoder;
    private final JwtDecoder decoder;

    public Jwt_Service() {
        SecretKey key = new SecretKeySpec(SECRET.getBytes(), "HmacSHA256");

        this.encoder = NimbusJwtEncoder.withSecretKey(key)
            .algorithm(MacAlgorithm.HS256)
            .build();

        this.decoder = NimbusJwtDecoder.withSecretKey(key)
            .macAlgorithm(MacAlgorithm.HS256)
            .build();
    }

    public String CreateAccessToken(long userId) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
            .subject(String.valueOf(userId))
            .issuedAt(now)
            .expiresAt(now.plus(15, ChronoUnit.MINUTES))
            .claim("type", "access")
            .build();

        return encoder.encode(
            JwtEncoderParameters.from(claims)
        ).getTokenValue();
    }

    public String CreateRefreshToken(long userId) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
            .subject(String.valueOf(userId))
            .issuedAt(now)
            .expiresAt(now.plus(30, ChronoUnit.DAYS))
            .claim("type", "refresh")
            .build();

        return encoder.encode(
            JwtEncoderParameters.from(claims)
        ).getTokenValue();
    }

    public boolean ValidateAccessToken(String token) {
        try {
            Jwt jwt = decoder.decode(token);

            return "access".equals(jwt.getClaimAsString("type"));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean ValidateRefreshToken(String token) {
        try {
            Jwt jwt = decoder.decode(token);

            return "refresh".equals(jwt.getClaimAsString("type"));
        } catch (Exception e) {
            return false;
        }
    }
}
