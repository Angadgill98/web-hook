package com.example.backend.kafka.wh_evetns.consumer;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.backend.models.WebhookEvent;

@Service 
public class Send_workers {

    @Autowired
    private KafkaTemplate<String, WebhookEvent> web_hook_status_remplate;


    private HttpClient client = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(10))
        .build();

    private int worker_count = 3;
    private int max_retries = 3;

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

                        SendResult result = HandleSend(event);

                        HandleResult(result,event);

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

    private SendResult HandleSend(WebhookEvent event) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(event.getReceiverEndpoint()))
                    .timeout(Duration.ofSeconds(30))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofByteArray(event.getData()))
                    .build();

            HttpResponse<byte[]> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofByteArray()
            );

            int statusCode = response.statusCode();

            if (statusCode >= 200 && statusCode < 300) {
                return new SendResult(
                        true,
                        response.body(),
                        null
                );
            }

            return new SendResult(
                    false,
                    response.body(),
                    "HTTP request failed with status code: " + statusCode
            );

        } catch (java.net.http.HttpTimeoutException e) {
            return new SendResult(
                    false,
                    ("Request timed out: " + e.getMessage()).getBytes(),
                    "Request timed out: " + e.getMessage()
            );

        } catch (java.net.ConnectException e) {
            return new SendResult(
                    false,
                    ("Connection failed: " + e.getMessage()).getBytes(),
                    "Connection failed: " + e.getMessage()
            );

        } catch (java.net.UnknownHostException e) {
            return new SendResult(
                    false,
                    ("Unknown host: " + e.getMessage()).getBytes(),
                    "Unknown host: " + e.getMessage()
            );

        } catch (Exception e) {
            return new SendResult(
                    false,
                    (e.getClass().getSimpleName() + ": " + e.getMessage()).getBytes(),
                    e.getClass().getSimpleName() + ": " + e.getMessage()
            );
        }
    }

    private static class SendResult {

        private final boolean success;
        private final byte[] response;
        private final String error;

        public SendResult(boolean success, byte[] response, String error) {
            this.success = success;
            this.response = response;
            this.error = error;
        }

        public boolean isSuccess() {
            return success;
        }

        public byte[] getResponse() {
            return response;
        }

        public String getError() {
            return error;
        }
    }
   
    private void HandleResult(SendResult result, WebhookEvent event) {

        if (result.getResponse() != null) {
            byte[][] oldResponse = event.getResponse();

            byte[][] newResponse;

            if (oldResponse == null) {
                newResponse = new byte[1][];
            } else {
                newResponse = new byte[oldResponse.length + 1][];
                System.arraycopy(oldResponse, 0, newResponse, 0, oldResponse.length);
            }

            newResponse[newResponse.length - 1] = result.getResponse();

            event.setResponse(newResponse);
        }

        if (result.isSuccess()) {
            event.setStatus("sent");
            event.setLastUpdated(OffsetDateTime.now());
            web_hook_status_remplate.send("events_status", event);
            return;
        }

        if (event.getTries() >= max_retries) {
            event.setStatus("failed");
            event.setLastUpdated(OffsetDateTime.now());

            web_hook_status_remplate.send("events_status", event);
            return;
        }

        event.setTries(event.getTries() + 1);

        send(event);
    }

    

    
}