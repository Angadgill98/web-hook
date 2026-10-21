package com.example.backend.repo;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.backend.models.WebhookEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class Webhook_events_table_repo {

    @Autowired
    private JdbcTemplate jdbc;

    private static final Logger logger = LoggerFactory.getLogger(Webhook_events_table_repo.class);

    public boolean InsertEvents(String sql, List<WebhookEvent> events) {
        try {
            jdbc.batchUpdate(
                sql,
                events,
                events.size(),
                (ps, event) -> {
                    ps.setObject(1, event.getId());
                    ps.setBytes(2, event.getData());
                    ps.setLong(3, event.getWebhookId());
                    ps.setLong(4, event.getUserId());
                    ps.setLong(5, event.getUserEventOrder());
                    ps.setLong(6, event.getArrivalOrder());
                    ps.setInt(7, event.getTries());
                    ps.setString(8, event.getStatus());
                    ps.setString(9, event.getHttpMethod());
                    ps.setString(10, event.getReceiverEndpoint());
                }
            );

            logger.info("Inserted {} webhook events", events.size());
            return true;

        } catch (Exception e) {
            logger.error("Failed to insert {} webhook events", events.size(), e);
            return false;
        }
    }

    public List<WebhookEvent> GetEventsByWebhookIdAndUserId(long webhook_id, long user_id) {
        String sql = """
            SELECT *
            FROM webhook_events
            WHERE webhook_id = ?
            AND user_id = ?
            AND status IN ('stored', 'failed')
            ORDER BY arrival_order ASC
            """;

        try {
            return jdbc.query(sql, (rs, rowNum) -> {
                WebhookEvent event = new WebhookEvent();
                event.setId((UUID) rs.getObject("id"));
                event.setData(rs.getBytes("data"));
                event.setWebhookId(rs.getLong("webhook_id"));
                event.setUserId(rs.getLong("user_id"));
                event.setUserEventOrder(rs.getLong("user_event_order"));
                event.setArrivalOrder(rs.getLong("arrival_order"));
                event.setTries(rs.getInt("tries"));
                event.setStatus(rs.getString("status"));
                return event;
            }, webhook_id, user_id);

        } catch (Exception e) {
            logger.error("Failed to fetch events for webhook {} and user {}", webhook_id, user_id, e);
            return List.of();
        }
    }

}