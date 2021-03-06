package uk.co.zoneofavoidance.my.tasks.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import uk.co.zoneofavoidance.my.tasks.domain.Project;

/**
 * Repository for accessing projects.
 */
@Transactional
public interface ProjectRepository extends JpaRepository<Project, Long> {

}
