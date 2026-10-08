package com.example.backend.handlers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backend.models.Webhook;
import com.example.backend.repo.Repo;
import com.example.backend.services.Services;


@Service 
public class Api_Webhook_handler {

    @Autowired
    public Repo repo;

    @Autowired
    private Services services;

    public WebhookResult CreateWebHook(long user_id, String route_name, String reciver_url) {
        String webhook_url = UUID.randomUUID().toString();

        boolean success = repo.webhook_repo.CreateWebHook(user_id, route_name, webhook_url, reciver_url);

        if (success) {
            return new WebhookResult(true, "Webhook created successfully", webhook_url);
        }

        return new WebhookResult(false, "Webhook creation failed", "");
    }

    public List<Webhook> GetWebhooksByUserId(long user_id) {
        return repo.webhook_repo.GetWebhooksByUserId(user_id);
    }

    public boolean UpdateReceiverUrl(long user_id, long webhook_id, String receiver_url) {
        return repo.webhook_repo.UpdateReceiverUrl(user_id, webhook_id, receiver_url);
    }

    public boolean UpdateRouteName(long user_id, long webhook_id, String route_name) {
        return repo.webhook_repo.UpdateRouteName(user_id, webhook_id, route_name);
    }

    public boolean DeleteWebHook(long user_id, long webhook_id) {
        return repo.webhook_repo.DeleteWebHook(user_id, webhook_id);
    }

    public record WebhookResult(boolean success, String message, String webhookUrl) {
    }
}