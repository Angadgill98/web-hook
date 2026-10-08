package com.example.backend.models;

import java.time.OffsetDateTime;
import java.util.UUID;

public class WebhookEvent {

    private UUID id;
    private byte[] data;
    private String httpMethod;
    private String receiverEndpoint;
    private byte[][] response;
    private long webhookId;
    private long userId;
    private Long userEventOrder;
    private long arrivalOrder;
    private int tries;
    private String status;
    private OffsetDateTime createdAt;
    private OffsetDateTime lastUpdated;

    public WebhookEvent() {
    
    }

    public WebhookEvent(
            UUID id,
            byte[] data,
            String httpMethod,
            String receiverEndpoint,
            byte[][] response,
            long webhookId,
            long userId,
            Long userEventOrder,
            long arrivalOrder,
            int tries,
            String status,
            OffsetDateTime createdAt,
            OffsetDateTime lastUpdated) {

        this.id = id;
        this.data = data;
        this.httpMethod = httpMethod;
        this.receiverEndpoint = receiverEndpoint;
        this.response = response;
        this.webhookId = webhookId;
        this.userId = userId;
        this.userEventOrder = userEventOrder;
        this.arrivalOrder = arrivalOrder;
        this.tries = tries;
        this.status = status;
        this.createdAt = createdAt;
        this.lastUpdated = lastUpdated;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getReceiverEndpoint() {
        return receiverEndpoint;
    }

    public void setReceiverEndpoint(String receiverEndpoint) {
        this.receiverEndpoint = receiverEndpoint;
    }

    public byte[][] getResponse() {
        return response;
    }

    public void setResponse(byte[][] response) {
        this.response = response;
    }

    public long getWebhookId() {
        return webhookId;
    }

    public void setWebhookId(long webhookId) {
        this.webhookId = webhookId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Long getUserEventOrder() {
        return userEventOrder;
    }

    public void setUserEventOrder(Long userEventOrder) {
        this.userEventOrder = userEventOrder;
    }

    public long getArrivalOrder() {
        return arrivalOrder;
    }

    public void setArrivalOrder(long arrivalOrder) {
        this.arrivalOrder = arrivalOrder;
    }

    public int getTries() {
        return tries;
    }

    public void setTries(int tries) {
        this.tries = tries;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(OffsetDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}