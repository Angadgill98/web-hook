package com.example.backend.kafka.events_status.Consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class Process_event_status {

    @KafkaListener(topics = "events_status", groupId = "status-db-group", containerFactory = "statusDbListenerFactory")
    public void consume(String status) {
        System.out.println("Received event status: " + status);
    }
}