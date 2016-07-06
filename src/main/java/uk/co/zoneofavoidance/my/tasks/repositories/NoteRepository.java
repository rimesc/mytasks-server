package uk.co.zoneofavoidance.my.tasks.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import uk.co.zoneofavoidance.my.tasks.domain.Note;

/**
 * Repository for accessing notes.
 */
public interface NoteRepository extends JpaRepository<Note, Long> {

}
