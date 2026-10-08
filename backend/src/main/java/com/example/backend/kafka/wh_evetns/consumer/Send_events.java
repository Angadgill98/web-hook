package com.example.backend.kafka.wh_evetns.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.backend.models.WebhookEvent;


@Service
public class Send_events {

    @Autowired 
    private Send_workers sendWorkers;
    @KafkaListener(topics = "wh_events", groupId = "webhook-send-group", containerFactory = "webhookKafkaListenerFactory")
    public void consume(WebhookEvent event) {
        sendWorkers.send(event);
    }
}