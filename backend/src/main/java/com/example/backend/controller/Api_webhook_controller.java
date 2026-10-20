package com.example.backend.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.Webhook_dto;
import com.example.backend.handlers.Api_Webhook_handler.WebhookResult;
import com.example.backend.handlers.Handlers;
import com.example.backend.models.Webhook;

@RestController
@RequestMapping("/api/webhook")
public class Api_webhook_controller {

    private static final Logger logger = LoggerFactory.getLogger(Api_webhook_controller.class);

    @Autowired
    private Handlers handler;

    @PostMapping("/create")
    public Webhook_dto.CreateWebHook_Res CreateWebHook(@RequestBody Webhook_dto.CreateWebHook_Req request,Authentication authentication) {
        Webhook_dto.CreateWebHook_Res res=new Webhook_dto.CreateWebHook_Res();
        long userId = Long.parseLong(authentication.getName());

        WebhookResult result = handler.api_webhook.CreateWebHook(
            userId,
            request.routeName,
            request.receiverUrl,
            request.httpMethod
        );

        logger.info("Create webhook request completed for user: {}", userId);
        res.success=result.success();
        res.message=result.message();
        res.webhookUrl=result.webhookUrl();
        return res;
    }

    @GetMapping("/get-all-webhooks")
    public Webhook_dto.GetAllWebHooks_Res GetAllWebhooks(Authentication authentication) {
        Webhook_dto.GetAllWebHooks_Res res= new Webhook_dto.GetAllWebHooks_Res(); 
        long user_id = Long.parseLong(authentication.getName());

        List<Webhook> webhooks = handler.api_webhook.GetWebhooksByUserId(user_id);
        res.success=true;
        res.webhooks=webhooks;
        return res;
    }

    @PostMapping("/get-webhook-history")
    public Webhook_dto.GetWebhookHistory_Res GetWebhookHistory(@RequestBody Webhook_dto.GetWebhookHistory_Req req, Authentication authentication) {
        Webhook_dto.GetWebhookHistory_Res res = new Webhook_dto.GetWebhookHistory_Res();

        long user_id = Long.parseLong(authentication.getName());

        res.success = true;
        res.events = handler.api_webhook.GetWebhookHistoryById(req.webhook_id, user_id);

        return res;
    }

    @PutMapping("/update-rec-url")
    public boolean UpdateReceiverUrl(@RequestBody Webhook_dto.UpdateReceiverUrl_Req request, Authentication authentication) {
        long userId = Long.parseLong(authentication.getName());

        boolean success = handler.api_webhook.UpdateReceiverUrl(
            userId,
            request.webhookId,
            request.receiverUrl
        );

        logger.info("Update receiver URL request completed for webhook: {}", request.webhookId);

        return success;
    }

    @PutMapping("/update-hook-name")
    public boolean UpdateRouteName(@RequestBody Webhook_dto.UpdateRouteName_Req request, Authentication authentication) {
        long userId = Long.parseLong(authentication.getName());

        boolean success = handler.api_webhook.UpdateRouteName(
            userId,
            request.webhookId,
            request.routeName
        );

        logger.info("Update route name request completed for webhook: {}", request.webhookId);

        return success;
    }

    @DeleteMapping("/delete-webhook")
    public boolean DeleteWebHook(@RequestBody Webhook_dto.DeleteWebHook_Req request, Authentication authentication) {
        long userId = Long.parseLong(authentication.getName());

        boolean success = handler.api_webhook.DeleteWebHook(userId, request.webhookId);

        logger.info("Delete webhook request completed for webhook: {}", request.webhookId);

        return success;
    }
}