package uk.co.zoneofavoidance.my.tasks.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import uk.co.zoneofavoidance.my.tasks.domain.Project;

/**
 * Repository for accessing projects.
 */
public interface ProjectRepository extends JpaRepository<Project, Long> {

}
