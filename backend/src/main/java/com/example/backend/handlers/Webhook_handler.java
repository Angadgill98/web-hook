package com.example.backend.handlers;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.backend.models.Webhook;
import com.example.backend.models.WebhookEvent;
import com.example.backend.repo.Repo;

@Service
public class Webhook_handler {

    @Autowired
    private Repo repo;

    private static final Logger logger = LoggerFactory.getLogger(Webhook_handler.class);

    @Autowired
    private KafkaTemplate<String, WebhookEvent> web_hook_event_producer_template;

    @Autowired
    private KafkaTemplate<String, WebhookEvent> web_hook_status_template;

    public void init(String webhook_url, byte[] body) {
        Webhook webhook = repo.webhook_repo.GetWebhookByUrl(webhook_url);

        long userEventOrder=1;//this to be provied by uuser if not then use te arriaval ro der in out sys
        long arrivalOrder = 1;
        UUID eventId = UUID.randomUUID();

        WebhookEvent event = new WebhookEvent(
            eventId,
            body,
            webhook.getHttpMethod(),
            webhook.getReceiverEndpoint(),
            null,
            webhook.getId(),
            webhook.getUserId(),
            userEventOrder,
            arrivalOrder,
            1,
            "received",
            OffsetDateTime.now(),
            OffsetDateTime.now()
        );

        web_hook_event_producer_template.send(
            "wh_events",
            String.valueOf(webhook.getId()),
            event
        );


        web_hook_status_template.send(
            "events_status",
            String.valueOf(webhook.getId()),
            event
        );
    }


    
}