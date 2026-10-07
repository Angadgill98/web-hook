CREATE TABLE webhook (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    route_name VARCHAR(255) NOT NULL,
    webhook_url VARCHAR(255) NOT NULL,
    receiver_endpoint VARCHAR(255) NOT NULL,
    latest_event_order BIGINT NOT NULL DEFAULT 0,

    CONSTRAINT fk_webhook_routes_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);