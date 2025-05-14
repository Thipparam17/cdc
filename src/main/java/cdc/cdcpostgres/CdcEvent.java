package cdc.cdcpostgres;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"primaryKey", "tableName", "operation"}))
public class CdcEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tableName;
    private String operation;
    private String primaryKey;

    @Column(columnDefinition = "TEXT")
    private String beforeData;

    @Column(columnDefinition = "TEXT")
    private String afterData;

    private LocalDateTime eventTime;

    // Pre-insert logging
    @PrePersist
    public void logEventData() {
        System.out.println("Inserting event with data: " + this.toString());
    }
}
