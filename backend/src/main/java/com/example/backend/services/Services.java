package com.example.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service 
public class Services {
    @Autowired 
    public Jwt_Service jwt;
}
