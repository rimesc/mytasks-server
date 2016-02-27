package uk.co.zoneofavoidance.my.tasks.services;

import static java.util.Arrays.asList;
import static uk.co.zoneofavoidance.my.tasks.domain.State.DONE;
import static uk.co.zoneofavoidance.my.tasks.domain.State.IN_PROGRESS;
import static uk.co.zoneofavoidance.my.tasks.domain.State.ON_HOLD;
import static uk.co.zoneofavoidance.my.tasks.domain.State.TO_DO;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uk.co.zoneofavoidance.my.tasks.domain.State;
import uk.co.zoneofavoidance.my.tasks.domain.Task;
import uk.co.zoneofavoidance.my.tasks.exceptions.NotFoundException;
import uk.co.zoneofavoidance.my.tasks.repositories.ProjectRepository;
import uk.co.zoneofavoidance.my.tasks.repositories.TaskRepository;

@Service
@Transactional
public class TaskService {

   private final TaskRepository tasks;

   private final ProjectRepository projects;

   public TaskService(final TaskRepository tasks, final ProjectRepository projects) {
      this.tasks = tasks;
      this.projects = projects;
   }

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
      final List<Task> projectTasks = tasks.findByProjectId(projectId);
      if (projectTasks.isEmpty()) {
         checkProjectExists(projectId);
      }
      return projectTasks;
   }

   /**
    * Retrieves the tasks for a project, filtered by state.
    *
    * @param projectId ID of the project
    * @param states list of states to filter by
    * @return tasks for the project
    */
   @Transactional(readOnly = true)
   public List<Task> getForProject(final long projectId, final State... states) {
      final List<Task> projectTasks = tasks.findByProjectIdAndStateIn(projectId, asList(states));
      if (projectTasks.isEmpty()) {
         checkProjectExists(projectId);
      }
      return projectTasks;
   }

   /**
    * Retrieves the open tasks for a project.
    *
    * @param projectId ID of the project
    * @return open tasks for the project
    */
   @Transactional(readOnly = true)
   public List<Task> getOpenForProject(final long projectId) {
      return getForProject(projectId, TO_DO, IN_PROGRESS, ON_HOLD);
   }

   /**
    * Retrieves the closed tasks for a project.
    *
    * @param projectId ID of the project
    * @return closed tasks for the project
    */
   @Transactional(readOnly = true)
   public List<Task> getClosedForProject(final Long projectId) {
      return getForProject(projectId, DONE);
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

   private void checkProjectExists(final long projectId) {
      if (projects.findOne(projectId) == null) {
         throw new NotFoundException("project");
      }
   }

}
