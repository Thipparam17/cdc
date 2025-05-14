package cdc.cdcpostgres;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CdcEventService {

    @Autowired
    private CdcEventRepository repository;

    private final ObjectMapper mapper = new ObjectMapper();

    public void processKafkaMessage(String message) {
        try {
            // Parse the incoming Kafka message
            JsonNode node = mapper.readTree(message);

            String op = node.get("op").asText(); // c (create), u (update), d (delete)
            String table = node.get("source").get("table").asText();
            String pk = getId(node); // Extract the primary key

            String before = node.has("before") ? node.get("before").toString() : null;
            String after = node.has("after") ? node.get("after").toString() : null;

            // Create a new CdcEvent object
            CdcEvent event = new CdcEvent();
            event.setTableName(table);
            event.setOperation(op);
            event.setPrimaryKey(pk);
            event.setBeforeData(before);
            event.setAfterData(after);
            event.setEventTime(LocalDateTime.now());

            // Check if the event already exists to avoid duplicates
            boolean isDuplicate = checkForDuplicateEvent(event);
            if (isDuplicate) {
                System.out.println("Duplicate event detected, skipping insertion: " + event);
            } else {
                // Save the event if not a duplicate
                repository.save(event);
                System.out.println("Event saved: " + event);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getId(JsonNode node) {
        // Extract the ID from the 'after' or 'before' node in the Kafka message
        if (node.has("after") && node.get("after").has("id"))
            return node.get("after").get("id").asText();
        else if (node.has("before") && node.get("before").has("id"))
            return node.get("before").get("id").asText();
        return null;
    }

    private boolean checkForDuplicateEvent(CdcEvent event) {
        // Check if an event with the same primary key, table name, and operation already exists
        Optional<CdcEvent> existingEvent = repository.findByPrimaryKeyAndTableNameAndOperation(
                event.getPrimaryKey(), event.getTableName(), event.getOperation());

        return existingEvent.isPresent();
    }
    public void processKafkaDelete(String key) {
        System.out.println("Handling delete for key: " + key);
        // Your logic to delete from DB using the key
    }

}
