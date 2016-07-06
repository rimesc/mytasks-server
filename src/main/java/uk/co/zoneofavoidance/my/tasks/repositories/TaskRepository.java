package uk.co.zoneofavoidance.my.tasks.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import uk.co.zoneofavoidance.my.tasks.domain.State;
import uk.co.zoneofavoidance.my.tasks.domain.Task;

/**
 * Repository for accessing tasks.
 */
@Transactional
public interface TaskRepository extends JpaRepository<Task, Long> {

   /**
    * Find a task by the ID of its parent project.
    *
    * @param projectId ID of the parent project
    * @return a list of tasks
    */
   List<Task> findByProjectId(long projectId);

   /**
    * Find a task by the ID of its parent project and its state.
    *
    * @param projectId ID of the parent project
    * @param states list of states to be matched
    * @return a list of tasks
    */
   List<Task> findByProjectIdAndStateIn(long projectId, List<State> states);

   /**
    * Delete tasks by the ID of their parent project.
    *
    * @param projectId ID of the parent project
    */
   void deleteByProjectId(long projectId);

}
