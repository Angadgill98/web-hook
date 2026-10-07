package com.example.backend.handlers;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

import com.example.backend.models.Webhook;
import com.example.backend.repo.Repo;
import com.example.backend.repo.User_table_repo;

public class Webhook_handler {
    
    @Autowired 
    private Repo repo;

    private static final Logger logger = LoggerFactory.getLogger(User_table_repo.class);

    @Autowired
    private KafkaTemplate<String, WebhookEvent> kafkaTemplate; 

    public void init(String webhook_url, byte[] body) {
        Webhook webhook = repo.webhook_repo.GetWebhookByUrl(webhook_url);

        long arrivalOrder = 1;
        UUID eventId = UUID.randomUUID();

        WebhookEvent event = new WebhookEvent(
            eventId,
            body,
            webhook.getId(),
            webhook.getUserId(),
            webhook.getReceiverEndpoint(),
            null,
            arrivalOrder,
            1,
            "receive",
            OffsetDateTime.now(),
            OffsetDateTime.now()
        );

        kafkaTemplate.send("wh_events", String.valueOf(webhook.getId()), event);
    }

    public record WebhookEvent(
        UUID id,
        byte[] data,
        long webhookId,
        long userId,
        String receiverEndpoint,
        Long userEventOrder,
        long arrivalOrder,
        int tries,
        String status,
        OffsetDateTime createdAt,
        OffsetDateTime lastUpdated
    ) {}
}