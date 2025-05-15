package cdc.cdcpostgres;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CdcEventRepository extends JpaRepository<CdcEvent, Long> {
    Optional<CdcEvent> findByPrimaryKeyAndTableNameAndOperation(String primaryKey, String tableName, String operation);
}
