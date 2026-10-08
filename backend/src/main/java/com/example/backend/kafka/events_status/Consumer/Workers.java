package com.example.backend.kafka.events_status.Consumer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backend.models.WebhookEvent;
import com.example.backend.repo.Repo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service 
public class Workers {

    private int worker_count = 3;
    private BlockingQueue<WebhookEvent> queue;

    private Events_Processing_Pipeline pipeline=new Events_Processing_Pipeline();

    public Workers() {
        queue = new LinkedBlockingQueue<>();

        for (int i = 0; i < worker_count; i++) {
            int workerId = i;

            Thread.startVirtualThread(() -> {
                while (true) {
                    try {
                        WebhookEvent event = queue.take();

                        System.out.println("Worker " + workerId + " received status: " + event);

                        // Process status here

                        pipeline.InsertEvent(event);

                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            });
        }
    }

    public void send(WebhookEvent status) {
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

    @Autowired 
    private Repo repo;

    public void InsertEvent(WebhookEvent event) {
        String eventId = event.getId().toString();

        if (!events.containsKey(eventId)) {
            events.put(eventId, new ConcurrentHashMap<>());
        }

        Map<String, List<WebhookEvent>> eventStatuses = events.get(eventId);

        if (!eventStatuses.containsKey(event.getStatus())) {
            eventStatuses.put(event.getStatus(), new ArrayList<>());
        }

        List<WebhookEvent> statusEvents = eventStatuses.get(event.getStatus());
        statusEvents.add(event);

        eventCount++;

        CheckBatch();
    }

    public void CheckBatch() {
        if (eventCount >= batchSize) {
            List<WebhookEvent> finalEvents = ProcessBatch();
            InsertEvents(finalEvents);
            //here later upda teh status to teh stored and then sent those ot kafka
        }
    }

    public List<WebhookEvent> ProcessBatch() {
        List<WebhookEvent> finalEvents = new ArrayList<>();

        for (Map.Entry<String, Map<String, List<WebhookEvent>>> entry : events.entrySet()) {
            String eventId = entry.getKey();
            Map<String, List<WebhookEvent>> statuses = entry.getValue();

            if (statuses.containsKey("stored")) {
                // WebhookEvent event = GetLatestEvent(statuses.get("stored"));
                // finalEvents.add(event);

            } else if (statuses.containsKey("sent")) {
                WebhookEvent event = GetLatestEvent(statuses.get("sent"));
                finalEvents.add(event);

            } else if (statuses.containsKey("failed")) {
                WebhookEvent event = GetLatestEvent(statuses.get("failed"));
                finalEvents.add(event);

            } else if (statuses.containsKey("received")) {
                WebhookEvent event = GetLatestEvent(statuses.get("received"));
                finalEvents.add(event);
            }

            
        }

        return finalEvents;
    }
   
   
    //whwen we added the arrivallorder logivc
    // private WebhookEvent GetLatestEvent(List<WebhookEvent> events) {
    //     WebhookEvent latest = events.get(0);

    //     for (WebhookEvent event : events) {
    //         if (event.getArrivalOrder() > latest.getArrivalOrder()) {
    //             latest = event;
    //         }
    //     }

    //     return latest;
    // }


    private WebhookEvent GetLatestEvent(List<WebhookEvent> events) {
        return events.get(events.size() - 1);
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
                status = CASE
                    WHEN EXCLUDED.status = 'sent' THEN 'stored'
                    ELSE EXCLUDED.status
                END,
                arrival_order = EXCLUDED.arrival_order,
                last_updated = CURRENT_TIMESTAMP
            """;

        String incomingStatusPriority = """
            CASE EXCLUDED.status
                WHEN 'received' THEN 1
                WHEN 'failed' THEN 2
                WHEN 'sent' THEN 3
                WHEN 'stored' THEN 4
            END
            """;

        String existingStatusPriority = """
            CASE webhook_events.status
                WHEN 'received' THEN 1
                WHEN 'failed' THEN 2
                WHEN 'sent' THEN 3
                WHEN 'stored' THEN 4
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

        repo.webhook_events_repo.InsertEvents(sql, events);
    }

}


