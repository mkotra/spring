package pl.mkotra.spring.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import pl.mkotra.spring.avro.RadioStationsPulledEvent;

import java.util.UUID;

@EnableKafka
@Component
public class KafkaProducer {

    private final KafkaTemplate<String, RadioStationsPulledEvent> kafkaTemplate;
    private final String radioStationsPulledTopic;

    public KafkaProducer(
            @Value("${integration.radio-station-pulled-topic}")String radioStationsPulledTopic,
            KafkaTemplate<String, RadioStationsPulledEvent> kafkaTemplate) {
        this.radioStationsPulledTopic = radioStationsPulledTopic;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(int size) {
        // Create the Avro object
        String uuid = UUID.randomUUID().toString();

        RadioStationsPulledEvent event = new RadioStationsPulledEvent();
        event.setEventId(uuid);
        event.setSize(size);

        // Send the message to Kafka
        kafkaTemplate.send(radioStationsPulledTopic, uuid, event);
        System.out.println("Sent Avro message: " + event);
    }
}
