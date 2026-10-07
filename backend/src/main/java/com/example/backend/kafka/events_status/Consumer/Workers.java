package com.example.backend.kafka.events_status.Consumer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.example.backend.handlers.Webhook_handler.WebhookEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
public class Workers {

    private int worker_count = 3;
    private BlockingQueue<String> queue;

    public Workers() {
        queue = new LinkedBlockingQueue<>();

        for (int i = 0; i < worker_count; i++) {
            int workerId = i;

            Thread.startVirtualThread(() -> {
                while (true) {
                    try {
                        String status = queue.take();

                        System.out.println("Worker " + workerId + " received status: " + status);

                        // Process status here

                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            });
        }
    }

    public void send(String status) {
        try {
            queue.put(status);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class Events_Processing_Pipeline {

    private Map<String, Map<String, List<WebhookEvent>>> events = new ConcurrentHashMap<>();
    private long eventCount = 0;
    private long batchSize = 100;

    public void InsertEvent(WebhookEvent event) {
        String eventId = event.id().toString();

        if (!events.containsKey(eventId)) {
            events.put(eventId, new ConcurrentHashMap<>());
        }

        Map<String, List<WebhookEvent>> eventStatuses = events.get(eventId);

        if (!eventStatuses.containsKey(event.status())) {
            eventStatuses.put(event.status(), new ArrayList<>());
        }

        List<WebhookEvent> statusEvents = eventStatuses.get(event.status());
        statusEvents.add(event);

        eventCount++;

        CheckBatch();
    }

    public void CheckBatch() {
        if (eventCount >= batchSize) {
            List<WebhookEvent> finalEvents = ProcessBatch();
            InsertEvents(finalEvents);
        }
    }

    public List<WebhookEvent> ProcessBatch() {
        List<WebhookEvent> finalEvents = new ArrayList<>();

        for (Map.Entry<String, Map<String, List<WebhookEvent>>> entry : events.entrySet()) {
            String eventId = entry.getKey();
            Map<String, List<WebhookEvent>> statuses = entry.getValue();

            if (statuses.containsKey("completed")) {
                WebhookEvent event = GetLatestEvent(statuses.get("completed"));
                finalEvents.add(event);

            } else if (statuses.containsKey("sent")) {
                WebhookEvent event = GetLatestEvent(statuses.get("sent"));
                finalEvents.add(event);

            } else if (statuses.containsKey("stored")) {
                WebhookEvent event = GetLatestEvent(statuses.get("stored"));
                finalEvents.add(event);

            } else if (statuses.containsKey("received")) {
                WebhookEvent event = GetLatestEvent(statuses.get("received"));
                finalEvents.add(event);
            }
        }

        return finalEvents;
    }

    private WebhookEvent GetLatestEvent(List<WebhookEvent> events) {
        WebhookEvent latest = events.get(0);

        for (WebhookEvent event : events) {
            if (event.arrivalOrder() > latest.arrivalOrder()) {
                latest = event;
            }
        }

        return latest;
    }


    public void InsertEvents(List<WebhookEvent> events) {
        String baseQuery = """
            INSERT INTO webhook_events
            """;

        String columns = """
            (id, data, webhook_id, user_id, user_event_order, arrival_order, tries, status)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;

        String onConflict = """
            ON CONFLICT (webhook_id)
            """;

        String doUpdate = """
            DO UPDATE SET
                data = EXCLUDED.data,
                user_id = EXCLUDED.user_id,
                user_event_order = EXCLUDED.user_event_order,
                tries = EXCLUDED.tries,
                status = EXCLUDED.status,
                arrival_order = EXCLUDED.arrival_order,
                last_updated = CURRENT_TIMESTAMP
            """;

        String incomingStatusPriority = """
            CASE EXCLUDED.status
                WHEN 'received' THEN 1
                WHEN 'stored' THEN 2
                WHEN 'sent' THEN 3
                WHEN 'completed' THEN 4
            END
            """;

        String existingStatusPriority = """
            CASE webhook_events.status
                WHEN 'received' THEN 1
                WHEN 'stored' THEN 2
                WHEN 'sent' THEN 3
                WHEN 'completed' THEN 4
            END
            """;

        String higherStatus = """
            %s > %s
            """.formatted(incomingStatusPriority, existingStatusPriority);

        String sameStatusHigherArrival = """
            %s = %s
            AND EXCLUDED.arrival_order > webhook_events.arrival_order
            """.formatted(incomingStatusPriority, existingStatusPriority);

        String whereCondition = """
            WHERE
                %s
                OR (
                    %s
                )
            """.formatted(higherStatus, sameStatusHigherArrival);
        
        String sql = baseQuery + columns + onConflict + doUpdate + whereCondition;  
        
        
    
    }

}


