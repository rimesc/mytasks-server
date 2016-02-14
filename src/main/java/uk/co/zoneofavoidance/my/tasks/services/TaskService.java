package uk.co.zoneofavoidance.my.tasks.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uk.co.zoneofavoidance.my.tasks.domain.Task;
import uk.co.zoneofavoidance.my.tasks.exceptions.NotFoundException;
import uk.co.zoneofavoidance.my.tasks.repositories.TaskRepository;

@Service
@Transactional
public class TaskService {

   @Autowired
   private TaskRepository tasks;

   /**
    * Retrieves a task by ID.
    *
    * @param id ID of the task to retrieve
    * @return task with the given ID
    * @throws NotFoundException if no task with the given ID can be found
    */
   @Transactional(readOnly = true)
   public Task get(final long id) throws NotFoundException {
      return Optional.ofNullable(tasks.findOne(id)).orElseThrow(() -> new NotFoundException("task"));
   }

   /**
    * Retrieves the tasks for a project.
    *
    * @param id ID of the project
    * @return tasks for the project
    */
   @Transactional(readOnly = true)
   public List<Task> getForProject(final long projectId) {
      return tasks.findByProjectId(projectId);
   }

   /**
    * Saves a task.
    *
    * @param task task to save
    * @return the saved task
    */
   public Task save(final Task task) {
      return tasks.save(task);
   }

}
