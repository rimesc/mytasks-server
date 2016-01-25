package uk.co.zoneofavoidance.my.tasks.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import uk.co.zoneofavoidance.my.tasks.domain.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {

}
