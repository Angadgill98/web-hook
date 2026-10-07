package com.example.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.Auth_dto;
import com.example.backend.handlers.Handlers;
import com.example.backend.handlers.Auth_handler.AuthResult;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
public class Auth_controller {

    private static final Logger logger = LoggerFactory.getLogger(Auth_controller.class);

    @Autowired
    private Handlers handler;

    @PostMapping("/sign-up")
    public Auth_dto.SignUp_Res SignUp(@RequestBody Auth_dto.SignUp_Req request) {
        Auth_dto.SignUp_Res res = new Auth_dto.SignUp_Res();

        AuthResult result = handler.auth.SignUp(request.name, request.mail, request.pass);

        res.success = result.success();
        res.message = result.message();


        return res;
    }

    @PostMapping("/sign-in")
    public Auth_dto.Signin_Res SignIn(@RequestBody Auth_dto.SignIn_Req request, HttpServletResponse response) {

        
        Auth_dto.Signin_Res res = new Auth_dto.Signin_Res();

        AuthResult result = handler.auth.SignIn(request.mail, request.pass);

        res.success = result.success();
        res.message = result.message();

        if (result.success()) {
            handler.auth.AddCookies(response,(long)result.data());
            logger.info("Sign-in successful for email: {}", request.mail);
        } else {
            logger.warn("Sign-in failed for email: {}", request.mail);
        }

        return res;
    }
}