CREATE TABLE webhook_events (
    id UUID PRIMARY KEY,
    data BYTEA NOT NULL,
    webhook_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    user_event_order BIGINT,
    arrival_order BIGINT NOT NULL,
    tries INTEGER NOT NULL DEFAULT 0,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_updated TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    UNIQUE (webhook_id),

    FOREIGN KEY (webhook_id) REFERENCES webhook(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);