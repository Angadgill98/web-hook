package com.example.backend.dto;

import java.util.List;

import com.example.backend.models.Webhook;
import com.example.backend.models.WebhookEvent;

public class Webhook_dto {

    public static class CreateWebHook_Req {
        public String routeName;
        public String receiverUrl;
        public String httpMethod;
    }

    public static class CreateWebHook_Res {
        public boolean success;
        public String message;
        public String webhookUrl;
    }

    public static class UpdateReceiverUrl_Req {
        public long webhookId;
        public String receiverUrl;
    }

    public static class UpdateReceiverUrl_Res {
        public boolean success;
        public int code;
        public String message;
    }

    public static class UpdateRouteName_Req {
        public String routeName;
        public long webhookId;
    }

    public static class UpdateRouteName_Res {
        public boolean success;
        public int code;
        public String message;
    }

    public static class DeleteWebHook_Req {
        public long webhookId;
    }

    public static class DeleteWebHook_Res {
        public boolean success;
        public int code;
        public String message;
    }

    public static class GetAllWebHooks_Res {
        public boolean success;
        public List<Webhook> webhooks;
    }


    public static class GetWebhookHistory_Req {
        public long webhook_id;
    }

    public static class GetWebhookHistory_Res {
        public boolean success;
        public List<WebhookEvent> events;
    }
}