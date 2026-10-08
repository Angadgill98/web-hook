package com.example.backend.handlers;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.backend.models.User;
import com.example.backend.repo.Repo;
import com.example.backend.repo.User_table_repo;
import com.example.backend.services.Services;

import jakarta.servlet.http.HttpServletResponse;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service 
public class Auth_handler {
    

    @Autowired
    private PasswordEncoder bcrypt;

    @Autowired
    public Repo repo;

    @Autowired 
    private Services services;


    private static final Logger logger =LoggerFactory.getLogger(Auth_handler.class);


    public AuthResult SignUp(String name, String mail, String pass) {
        String hashedPass = bcrypt.encode(pass);

        try {
            boolean success = this.repo.user_repo.Createuser(name, mail, hashedPass);

            if (success) {
                logger.info("User created successfully: {}", mail);
                return new AuthResult(true, "User created successfully",0);
            }

            logger.warn("User creation failed: {}", mail);
            return new AuthResult(false, "User creation failed",0);

        } catch (DuplicateKeyException e) {
            logger.warn("User already exists: {}", mail);
            return new AuthResult(false, "User already exists",0);
        }
    }


    public AuthResult SignIn(String mail, String pass) {
        User user = repo.user_repo.GetUserByMail(mail);

        if (user == null) {
            logger.warn("Login failed: user not found for email {}", mail);
            return new AuthResult(false, "Invalid email or password", 0);
        }

        if (!bcrypt.matches(pass, user.getPass())) {
            logger.warn("Login failed: incorrect password for email {}", mail);
            return new AuthResult(false, "Invalid email or password", 0);
        }

        logger.info("User signed in successfully: {}", mail);
        return new AuthResult(true, "Login successful", user.getId());
    }


    public void AddCookies(HttpServletResponse response, long userId) {
        String accessToken = services.jwt.CreateAccessToken(userId);
        String refreshToken = services.jwt.CreateRefreshToken(userId);

        ResponseCookie accessCookie = ResponseCookie.from("access_token", accessToken)
            .httpOnly(true)
            .secure(false)
            .path("/")
            .maxAge(Duration.ofMinutes(15))
            .sameSite("Lax")
            .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", refreshToken)
            .httpOnly(true)
            .secure(false)
            .path("/")
            .maxAge(Duration.ofDays(30))
            .sameSite("Lax")
            .build();

        response.addHeader("Set-Cookie", accessCookie.toString());
        response.addHeader("Set-Cookie", refreshCookie.toString());
    }

    public record AuthResult(boolean success, String message, Object data) {}
}


