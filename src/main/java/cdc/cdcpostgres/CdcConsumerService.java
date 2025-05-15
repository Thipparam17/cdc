package cdc.cdcpostgres;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class CdcConsumerService {

    private final CdcEventService cdcEventService;

    public CdcConsumerService(CdcEventService cdcEventService) {
        this.cdcEventService = cdcEventService;
    }

//    @KafkaListener(topics = "debezium.public.customer", groupId = "cdc-group")
//    public void listen(ConsumerRecord<String, String> record) {
//        String message = record.value();  // May be null for DELETE events
//        System.out.println("Received Kafka record: " + message);
//
//        // Handle null payload gracefully (DELETE events)
//        if (message != null) {
//            cdcEventService.processKafkaMessage(message);
//        } else {
//            System.out.println("Received null message (likely a DELETE event)");
//            // You can optionally pass a special flag to handle deletes
//            cdcEventService.processKafkaDelete(record.key());
//        }
//    }
@KafkaListener(topicPattern = "debezium\\.public\\..*", groupId = "cdc-group")
public void listen(ConsumerRecord<String, String> record) {
    String message = record.value();
    String topic = record.topic();

    System.out.println("Received Kafka record from topic " + topic + ": " + message);

    if (message != null) {
        cdcEventService.processKafkaMessage(message, topic); // Pass topic
    } else {
        cdcEventService.processKafkaDelete(record.key(), topic); // Pass topic
    }
}



}
