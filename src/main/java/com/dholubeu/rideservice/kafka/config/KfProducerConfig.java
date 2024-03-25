package com.dholubeu.rideservice.kafka.config;

import com.dholubeu.rideservice.kafka.Message;
import com.dholubeu.rideservice.kafka.property.KfProperties;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.support.serializer.JsonSerializer;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KfProducerConfig {

    private final KfProperties kfProperties;

    @Bean
    public NewTopic topic() {
        return TopicBuilder.name(kfProperties.getTopic())
                .partitions(kfProperties.getPartitions())
                .replicas(kfProperties.getReplicas())
                .build();
    }

    @Bean
    public SenderOptions<String, Message> sendOptions() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kfProperties.getPort());
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return SenderOptions.create(config);
    }

    @Bean
    public KafkaSender<String, Message> kafkaSender() {
        return KafkaSender.create(sendOptions());
    }

}