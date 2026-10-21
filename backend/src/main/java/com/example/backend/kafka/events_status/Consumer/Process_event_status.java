package com.example.backend.kafka.events_status.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.backend.models.WebhookEvent;

@Service
public class Process_event_status {


    @Autowired 
    private  Workers workers;

    @KafkaListener(topics = "events_status", containerFactory = "statusKafkaListenerFactory")
    public void consume(WebhookEvent status) {

        System.out.println("Received event status: " + status);
        workers.send(status);
    }
}