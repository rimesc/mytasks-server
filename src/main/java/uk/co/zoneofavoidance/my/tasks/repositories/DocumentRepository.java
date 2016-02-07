package uk.co.zoneofavoidance.my.tasks.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import uk.co.zoneofavoidance.my.tasks.domain.Document;

public interface DocumentRepository extends JpaRepository<Document, Long> {

}
