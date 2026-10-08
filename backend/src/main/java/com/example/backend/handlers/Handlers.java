package com.example.backend.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service 
public class Handlers {
    



    @Autowired 
    public Auth_handler auth;

    @Autowired 
    public Api_Webhook_handler api_webhook;

    @Autowired 
    public Webhook_handler webhook;
}
