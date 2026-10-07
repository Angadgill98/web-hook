package com.example.backend.kafka.wh_evetns.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.backend.handlers.Webhook_handler.WebhookEvent;

@Service
public class Send_events {

    @KafkaListener(topics = "wh_events", groupId = "webhook-send-group", containerFactory = "webhookDbListenerFactory")
    public void consume(WebhookEvent event) {
        System.out.println("Send worker received event: " + event);
    }
}