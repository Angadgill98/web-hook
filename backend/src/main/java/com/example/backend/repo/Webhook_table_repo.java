package com.example.backend.repo;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.backend.models.Webhook;


@Repository  
public class Webhook_table_repo {

    private static final Logger logger = LoggerFactory.getLogger(Webhook_table_repo.class);

    @Autowired
    private JdbcTemplate jdbc;

    public boolean CreateWebHook(long user_id, String route_name, String webhook_url, String reciver_url) {
        String query = """
            INSERT INTO webhook (user_id, route_name, webhook_url, receiver_endpoint)
            VALUES (?, ?, ?, ?)
            """;

        try {
            int rows = jdbc.update(query, user_id, route_name, webhook_url, reciver_url);

            if (rows == 1) {
                logger.info("Webhook created successfully for user: {}", user_id);
                return true;
            }

            logger.warn("Webhook creation affected {} rows for user: {}", rows, user_id);
            return false;

        } catch (Exception e) {
            logger.error("Failed to create webhook for user: {}", user_id, e);
            return false;
        }
    }

    public boolean UpdateReceiverUrl(long user_id, long webhook_id, String receiver_url) {
        String query = """
            UPDATE webhook
            SET receiver_endpoint = ?
            WHERE id = ? AND user_id = ?
            """;

        try {
            int rows = jdbc.update(query, receiver_url, webhook_id, user_id);

            if (rows == 1) {
                logger.info("Receiver URL updated for webhook: {} by user: {}", webhook_id, user_id);
                return true;
            }

            logger.warn("No webhook found for user: {} with webhook: {}", user_id, webhook_id);
            return false;

        } catch (Exception e) {
            logger.error("Failed to update receiver URL for webhook: {} by user: {}", webhook_id, user_id, e);
            return false;
        }
    }

    public boolean UpdateRouteName(long user_id, long webhook_id, String route_name) {
        String query = """
            UPDATE webhook
            SET route_name = ?
            WHERE id = ? AND user_id = ?
            """;

        try {
            int rows = jdbc.update(query, route_name, webhook_id, user_id);

            if (rows == 1) {
                logger.info("Route name updated for webhook: {} by user: {}", webhook_id, user_id);
                return true;
            }

            logger.warn("No webhook found for user: {} with webhook: {}", user_id, webhook_id);
            return false;

        } catch (Exception e) {
            logger.error("Failed to update route name for webhook: {} by user: {}", webhook_id, user_id, e);
            return false;
        }
    }

    public boolean DeleteWebHook(long user_id, long webhook_id) {
        String query = """
            DELETE FROM webhook
            WHERE id = ? AND user_id = ?
            """;

        try {
            int rows = jdbc.update(query, webhook_id, user_id);

            if (rows == 1) {
                logger.info("Webhook deleted successfully: {} by user: {}", webhook_id, user_id);
                return true;
            }

            logger.warn("No webhook found for user: {} with webhook: {}", user_id, webhook_id);
            return false;

        } catch (Exception e) {
            logger.error("Failed to delete webhook: {} by user: {}", webhook_id, user_id, e);
            return false;
        }
    }

   public Webhook GetWebhookByUrl(String webhook_url) {
        String query = """
            SELECT id, user_id, route_name, webhook_url, receiver_endpoint, latest_event_order
            FROM webhook
            WHERE webhook_url = ?
            """;

        try {
            return jdbc.queryForObject(
                query,
                (rs, rowNum) -> new Webhook(
                    rs.getLong("id"),
                    rs.getLong("user_id"),
                    rs.getString("route_name"),
                    rs.getString("webhook_url"),
                    rs.getString("receiver_endpoint"),
                    rs.getLong("latest_event_order")
                ),
                webhook_url
            );

        } catch (Exception e) {
            logger.error("Failed to get webhook for URL: {}", webhook_url, e);
            return null;
        }
    }




    public List<Webhook> GetWebhooksByUserId(long user_id) {
        String query = """
            SELECT id, user_id, route_name, webhook_url, receiver_endpoint, latest_event_order
            FROM webhook
            WHERE user_id = ?
            ORDER BY id
            """;

        try {
            return jdbc.query(
                query,
                (rs, rowNum) -> new Webhook(
                    rs.getLong("id"),
                    rs.getLong("user_id"),
                    rs.getString("route_name"),
                    rs.getString("webhook_url"),
                    rs.getString("receiver_endpoint"),
                    rs.getLong("latest_event_order")
                ),
                user_id
            );

        } catch (Exception e) {
            logger.error("Failed to get webhooks for user: {}", user_id, e);
            return List.of();
        }
    }
    
}