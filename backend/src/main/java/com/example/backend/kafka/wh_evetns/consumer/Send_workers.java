package com.example.backend.kafka.wh_evetns.consumer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.example.backend.handlers.Webhook_handler.WebhookEvent;

public class Send_workers {

    private int worker_count = 3;
    private BlockingQueue<WebhookEvent> queue;

    public Send_workers() {
        queue = new LinkedBlockingQueue<>();

        for (int i = 0; i < worker_count; i++) {
            int workerId = i;

            Thread.startVirtualThread(() -> {
                while (true) {
                    try {
                        WebhookEvent event = queue.take();

                        System.out.println("Send worker " + workerId + " received event: " + event);

                        // Send event to receiver endpoint here

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
}