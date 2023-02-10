package org.acme.graphql.kafka;

import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.kafka.client.producer.KafkaProducer;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class KafkaProducerService {

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

    @ConfigProperty(name = "kafka.producer.key-serializer")
    String keySerializer;

    @ConfigProperty(name = "kafka.producer.value-serializer")
    String valueSerializer;

    @ConfigProperty(name = "kafka.producer.acks")
    String acks;

    @ConfigProperty(name = "kafka.topic.agency.cbc")
    String topicAgencyCbc;

    @ConfigProperty(name = "kafka.topic.agency.bnn")
    String topicAgencyBnn;

    @ConfigProperty(name = "kafka.topic.agency.firstnews")
    String topicAgencyFirstNews;

    KafkaProducer<String, String> kafkaProducer;

    @PostConstruct
    void initialize() {
        Map<String, String> config = new HashMap<>();
        config.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, securityProtocol);
        config.put(SaslConfigs.SASL_MECHANISM, saslMechanism);
        config.put(SaslConfigs.SASL_JAAS_CONFIG,saslJaasConfig);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);
        config.put(ProducerConfig.ACKS_CONFIG, acks);

        kafkaProducer = KafkaProducer.create(vertx, config);
    }

    public KafkaProducer<String, String> kafkaproducer() {
        return kafkaProducer;
    }

    public String topicAgencyCbc() {
        return topicAgencyCbc;
    }

    public String topicAgencyBnn() {
        return topicAgencyBnn;
    }

    public String topicAgencyFirstNews() {
        return topicAgencyFirstNews;
    }

}
