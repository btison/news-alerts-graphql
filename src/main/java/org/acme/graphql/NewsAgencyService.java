package org.acme.graphql;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.operators.multi.processors.UnicastProcessor;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.kafka.client.consumer.KafkaConsumer;
import io.vertx.mutiny.kafka.client.producer.KafkaProducerRecord;
import org.acme.graphql.kafka.KafkaConsumerService;
import org.acme.graphql.kafka.KafkaProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;

@ApplicationScoped
public class NewsAgencyService {

    private final static Logger LOGGER = LoggerFactory.getLogger(NewsAgencyService.class);

    @Inject
    KafkaProducerService kafkaProducerService;

    @Inject
    KafkaConsumerService kafkaConsumerService;

    private final Map<String, User> users = new HashMap<>();

    private final Map<String, Publisher> publishers = new HashMap<>();

    private final Map<String, UnicastProcessor<String>> processors = new HashMap<>();

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public List<Publisher> getAllPublishers() {
        return new ArrayList<>(publishers.values());
    }

    public User addUser(NewUser newUser) {
        User user = new User();
        user.id = UUID.randomUUID().toString();
        user.name = newUser.name;
        user.age = newUser.age;
        users.put(user.id, user);
        return user;
    }

    public Publisher addPublisher(NewPublisher newPublisher) {
        Publisher publisher = new Publisher();
        publisher.id = UUID.randomUUID().toString();
        publisher.name = newPublisher.name;
        publisher.agency = newPublisher.agency;
        publishers.put(publisher.id, publisher);
        return publisher;
    }

    public News publishNewsUpdate(NewsUpdate newsUpdate) {
        if (!publishers.containsKey(newsUpdate.publisherId)) {
            throw new PublisherNotRegisteredException("Publisher '" + newsUpdate.publisherId + "' is not registered.");
        }
        News news = new News();
        news.id = UUID.randomUUID().toString();
        news.brief = newsUpdate.brief;
        news.headline = newsUpdate.headline;
        news.content = newsUpdate.content;
        news.publisher = publishers.get(newsUpdate.publisherId);

        KafkaProducerRecord<String, String> producerRecord = KafkaProducerRecord.create(topicForAgency(news.publisher.agency), news.id, news.toJson().encode());
        kafkaProducerService.kafkaproducer().send(producerRecord).subscribe()
                .with(recordMetadata -> LOGGER.info("News item with id " + news.id + " is produced to topic '" + producerRecord.topic() + "'."));
        return news;
    }

    public Multi<News> subscribe(String userId, Agency agency) {
        if (!users.containsKey(userId)) {
            throw new UserNotRegisteredException("User '" + userId + "' is not registered.");
        }
        String key = userId + "_" + agency;
        KafkaConsumer<String, String> consumer = kafkaConsumerService.createConsumer(key, userId);
        UnicastProcessor<String> processor = UnicastProcessor.create();
        processors.put(key, processor);
        consumer.handler(record -> processor.onNext(record.value()));
        consumer.subscribe(topicForAgency(agency)).subscribe()
                .with(v -> LOGGER.info("subscribed to topic " + topicForAgency(agency) + " with groupId " + userId));
        return processor.onItem().transform(s -> new JsonObject(s).mapTo(News.class))
                .onCancellation().invoke(() -> {
                    processors.remove(key);
                    consumer.close().subscribe().with(v -> LOGGER.info("Closing consumer for topic " + topicForAgency(agency) + " and groupId " + userId));
                    kafkaConsumerService.removeConsumer(key);
                });
    }

    private String topicForAgency(Agency agency) {
        return switch (agency) {
            case CBC -> kafkaProducerService.topicAgencyCbc();
            case BNN -> kafkaProducerService.topicAgencyBnn();
            case FIRST_NEWS -> kafkaProducerService.topicAgencyFirstNews();
        };
    }
}
