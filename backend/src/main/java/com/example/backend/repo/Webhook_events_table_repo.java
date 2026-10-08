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

    public void InsertEvents(String sql, List<WebhookEvent> events) {
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
                }
            );

            logger.info("Inserted {} webhook events", events.size());

        } catch (Exception e) {
            logger.error("Failed to insert {} webhook events", events.size(), e);
        }
    }

}