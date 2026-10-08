package com.example.backend.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;

import com.example.backend.models.WebhookEvent;

@Configuration
public class Kafka_Config {

    @Bean
    public NewTopic whEventsTopic() {
        return new NewTopic("wh_events", 3, (short) 1);
    }

    @Bean
    public NewTopic eventsStatusTopic() {
        return new NewTopic("events_status", 3, (short) 1);
    }

    // WebhookEvent producer

    @Bean
    public ProducerFactory<String, WebhookEvent> webhookProducerFactory() {
        Map<String, Object> config = new HashMap<>();

        config.put("bootstrap.servers", "localhost:9092");
        config.put("key.serializer", StringSerializer.class);
        config.put("value.serializer", JacksonJsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, WebhookEvent> web_hook_event_producer_template() {
        return new KafkaTemplate<>(webhookProducerFactory());
    }

    // Event status producer

    @Bean
    public ProducerFactory<String, WebhookEvent> statusProducerFactory() {
        Map<String, Object> config = new HashMap<>();

        config.put("bootstrap.servers", "localhost:9092");
        config.put("key.serializer", StringSerializer.class);
        config.put("value.serializer", JacksonJsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, WebhookEvent> web_hook_status_remplate() {
        return new KafkaTemplate<>(statusProducerFactory());
    }

    // WebhookEvent consumer

    @Bean
    public ConsumerFactory<String, WebhookEvent> webhookConsumerFactory() {
        Map<String, Object> config = new HashMap<>();

        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "webhook-send-group");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JacksonJsonDeserializer.class);

        JacksonJsonDeserializer<WebhookEvent> deserializer = new JacksonJsonDeserializer<>(WebhookEvent.class);
        deserializer.addTrustedPackages("com.example.backend");

        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, WebhookEvent> webhookKafkaListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, WebhookEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(webhookConsumerFactory());

        return factory;
    }

    // Event status consumer

    @Bean
    public ConsumerFactory<String, WebhookEvent> statusConsumerFactory() {
        Map<String, Object> config = new HashMap<>();

        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "status-group");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JacksonJsonDeserializer.class);

        JacksonJsonDeserializer<WebhookEvent> deserializer = new JacksonJsonDeserializer<>(WebhookEvent.class);
        deserializer.addTrustedPackages("com.example.backend");

        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, WebhookEvent> statusKafkaListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, WebhookEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(statusConsumerFactory());

        return factory;
    }
}