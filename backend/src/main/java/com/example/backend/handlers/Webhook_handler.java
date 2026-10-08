package com.example.backend.handlers;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.backend.models.Webhook;
import com.example.backend.repo.Repo;

@Service
public class Webhook_handler {

    @Autowired
    private Repo repo;

    private static final Logger logger = LoggerFactory.getLogger(Webhook_handler.class);

    @Autowired
    private KafkaTemplate<String, WebhookEvent> web_hook_event_producer_template;

    @Autowired
    private KafkaTemplate<String, String> web_hook_status_template;

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

        web_hook_event_producer_template.send(
            "wh_events",
            String.valueOf(webhook.getId()),
            event
        );
    }

    public void sendStatus(String key, String status) {
        web_hook_status_template.send(
            "events_status",
            key,
            status
        );
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