package com.example.backend.kafka.wh_evetns.consumer.v0;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backend.handlers.Webhook_handler.WebhookEvent;
import com.example.backend.repo.Repo;

// @Service
public class Db_Workers {

    private int worker_count = 3;
    private BlockingQueue<WebhookEvent> queue;

    @Autowired
    private Repo repo;

    public Db_Workers() {
        queue = new LinkedBlockingQueue<>(1000);

        for (int i = 0; i < worker_count; i++) {
            int workerId = i;

            Thread.startVirtualThread(() -> {
                while (true) {
                    try {
                        WebhookEvent event = queue.take();

                        System.out.println("Worker " + workerId + " received event: " + event);

                        SaveEvent(event);

                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            });
        }
    }

    public void send(WebhookEvent event) {
        try {
            queue.put(event);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void SaveEvent(WebhookEvent event) {
        repo.webhook_events_repo.InsertEvent(
            event.id(),
            event.data(),
            event.webhookId(),
            event.userId(),
            event.userEventOrder(),
            event.arrivalOrder(),
            event.tries(),
            event.status()
        );
    }
}