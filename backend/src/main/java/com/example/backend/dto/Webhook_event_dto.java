package com.example.backend.dto;

public class Webhook_event_dto {

    

    public static class Response {
        public String message;
        public String webhookUrl;

        public Response(String message, String webhookUrl) {
            this.message = message;
            this.webhookUrl = webhookUrl;
        }
    }
}