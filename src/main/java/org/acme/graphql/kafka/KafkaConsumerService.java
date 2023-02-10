package org.acme.graphql.kafka;

import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.kafka.client.consumer.KafkaConsumer;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class KafkaConsumerService {

    @Inject
    Vertx vertx;

    @ConfigProperty(name = "kafka.bootstrap.servers")
    String bootstrapServers;

    @ConfigProperty(name = "kafka.security.protocol")
    String securityProtocol;

    @ConfigProperty(name = "kafka.sasl.mechanism")
    String saslMechanism;

    @ConfigProperty(name = "kafka.sasl.jaas.config")
    String saslJaasConfig;

    @ConfigProperty(name = "kafka.consumer.key-deserializer")
    String keyDeserializer;

    @ConfigProperty(name = "kafka.consumer.value-deserializer")
    String valueDeserializer;

    @ConfigProperty(name = "kafka.consumer.enable.auto.commit")
    String enableAutoCommit;

    @ConfigProperty(name = "kafka.consumer.auto.offset.reset")
    String offsetReset;

    Map<String, KafkaConsumer<String, String>> kafkaConsumers = new HashMap<>();

    public KafkaConsumer<String, String> createConsumer(String key, String consumerGroup) {
        Map<String, String> config = new HashMap<>();
        config.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, securityProtocol);
        config.put(SaslConfigs.SASL_MECHANISM, saslMechanism);
        config.put(SaslConfigs.SASL_JAAS_CONFIG,saslJaasConfig);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer);
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, offsetReset);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroup);
        KafkaConsumer<String, String> consumer = KafkaConsumer.create(vertx, config);
        kafkaConsumers.put(key, consumer);
        return consumer;
    }

    public void removeConsumer(String key) {
        kafkaConsumers.remove(key);
    }

    public KafkaConsumer<String, String> kafkaConsumer(String key) {
        return kafkaConsumers.get(key);
    }

}
