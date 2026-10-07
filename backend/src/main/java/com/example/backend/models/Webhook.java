package com.example.backend.models;

public class Webhook {

    private long id;
    private long userId;
    private String routeName;
    private String webhookUrl;
    private String receiverEndpoint;
    private long latestEventOrder;

    public Webhook() {
    }

    public Webhook(long id, long userId, String routeName, String webhookUrl, String receiverEndpoint, long latestEventOrder) {
        this.id = id;
        this.userId = userId;
        this.routeName = routeName;
        this.webhookUrl = webhookUrl;
        this.receiverEndpoint = receiverEndpoint;
        this.latestEventOrder = latestEventOrder;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getWebhookUrl() {
        return webhookUrl;
    }

    public void setWebhookUrl(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    public String getReceiverEndpoint() {
        return receiverEndpoint;
    }

    public void setReceiverEndpoint(String receiverEndpoint) {
        this.receiverEndpoint = receiverEndpoint;
    }

    public long getLatestEventOrder() {
        return latestEventOrder;
    }

    public void setLatestEventOrder(long latestEventOrder) {
        this.latestEventOrder = latestEventOrder;
    }
}