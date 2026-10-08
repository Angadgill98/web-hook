package com.example.backend.dto;

public class Webhook_dto {

    public static class CreateWebHook_Req {
        public String routeName;
        public String receiverUrl;
        public String httpMethod;
    }

    public static class UpdateReceiverUrl_Req {
        public long webhookId;
        public String receiverUrl;
    }

    public static class UpdateRouteName_Req {
        public String routeName;
        public long webhookId;

    }

    public static class DeleteWebHook_Req {
        public long webhookId;
    }
}