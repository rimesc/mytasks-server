package uk.co.zoneofavoidance.my.tasks.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import uk.co.zoneofavoidance.my.tasks.domain.State;
import uk.co.zoneofavoidance.my.tasks.domain.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

   List<Task> findByProjectId(long projectId);

   List<Task> findByProjectIdAndStateIn(long projectId, List<State> states);

}
