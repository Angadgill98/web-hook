package com.example.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.Webhook_event_dto;
import com.example.backend.handlers.Handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController 
@RequestMapping("/webhook")
public class Webhhook_controller {

    private static final Logger logger = LoggerFactory.getLogger(Webhhook_controller.class);

    @Autowired
    private Handlers handler;


    @PostMapping("/sent/{webhookUrl}")
    public Webhook_event_dto.Response ReceiveWebHook(@PathVariable String webhookUrl, @RequestBody byte[] body) {

        handler.webhook.init(webhookUrl, body);

        return new Webhook_event_dto.Response(
                "Webhook received successfully",
                webhookUrl
        );
    }
}



