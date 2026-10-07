package com.example.backend.kafka.wh_evetns.consumer.v0;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.backend.handlers.Webhook_handler.WebhookEvent;

// @Service
public class Db {

    private Db_Workers workers;

    public Db() {
        workers = new Db_Workers();
    }

    @KafkaListener(topics = "wh_events", groupId = "webhook-db-group", containerFactory = "webhookKafkaListenerFactory")
    public void consume(WebhookEvent event) {
        workers.send(event);
    }
}