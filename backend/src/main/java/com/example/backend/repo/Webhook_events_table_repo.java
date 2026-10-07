package com.example.backend.repo;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class Webhook_events_table_repo {

    @Autowired
    private JdbcTemplate jdbc;

    private static final Logger logger = LoggerFactory.getLogger(Webhook_events_table_repo.class);

    public boolean InsertEvent(UUID id, byte[] data, long webhookId, long userId, Long userEventOrder, long arrivalOrder, int tries, String status) {
        String sql = "insert into webhook_events (id,data,webhook_id,user_id,user_event_order,arrival_order,tries,status) values (?,?,?,?,?,?,?,?)";
        try {
            int rows = jdbc.update(sql, id, data, webhookId, userId, userEventOrder, arrivalOrder, tries, status);
            return rows == 1;
        } catch (Exception e) {
            logger.error("Failed to insert webhook event {}", id, e);
            return false;
        }
    }

    public boolean UpdateStatus(UUID id, String status) {
        String sql = "update webhook_events set status = ?, last_updated = CURRENT_TIMESTAMP where id = ?";
        try {
            int rows = jdbc.update(sql, status, id);
            return rows == 1;
        } catch (Exception e) {
            logger.error("Failed to update status for webhook event {}", id, e);
            return false;
        }
    }

    public boolean UpdateTries(UUID id, int tries) {
        String sql = "update webhook_events set tries = ?, last_updated = CURRENT_TIMESTAMP where id = ?";
        try {
            int rows = jdbc.update(sql, tries, id);
            return rows == 1;
        } catch (Exception e) {
            logger.error("Failed to update tries for webhook event {}", id, e);
            return false;
        }
    }

    public boolean UpsertEvent(UUID id, byte[] data, long webhookId, long userId, Long userEventOrder, long arrivalOrder, int tries, String status) {
        String sql = "insert into webhook_events (id,data,webhook_id,user_id,user_event_order,arrival_order,tries,status) values (?,?,?,?,?,?,?,?) " +
                     "on conflict (webhook_id,arrival_order) do update set data = excluded.data, user_id = excluded.user_id, user_event_order = excluded.user_event_order, tries = excluded.tries, status = excluded.status, last_updated = CURRENT_TIMESTAMP";
        try {
            int rows = jdbc.update(sql, id, data, webhookId, userId, userEventOrder, arrivalOrder, tries, status);
            return rows == 1;
        } catch (Exception e) {
            logger.error("Failed to upsert webhook event {}", id, e);
            return false;
        }
    }
}