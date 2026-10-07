package com.example.backend.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController 
@RequestMapping("/webhook")
public class Webhhook_controller {

    private static final Logger logger = LoggerFactory.getLogger(Api_webhook_controller.class);



    @PostMapping("/sent/{webhookUrl}") public void ReceiveWebHook(@PathVariable String webhookUrl_uuid,@RequestBody byte[] body) {
        
    }
}



