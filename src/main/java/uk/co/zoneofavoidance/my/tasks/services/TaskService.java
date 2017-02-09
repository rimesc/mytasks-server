package uk.co.zoneofavoidance.my.tasks.services;

import static java.util.Arrays.asList;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uk.co.zoneofavoidance.my.tasks.domain.State;
import uk.co.zoneofavoidance.my.tasks.domain.Task;
import uk.co.zoneofavoidance.my.tasks.exceptions.NotFoundException;
import uk.co.zoneofavoidance.my.tasks.repositories.ProjectRepository;
import uk.co.zoneofavoidance.my.tasks.repositories.TaskRepository;

/**
 * Service that provides transactional access to tasks.
 */
@Service
@Transactional
public class TaskService {

   private final TaskRepository tasks;

   private final ProjectRepository projects;

   @Autowired
   public TaskService(final TaskRepository tasks, final ProjectRepository projects) {
      this.tasks = tasks;
      this.projects = projects;
   }

   /**
    * Retrieves a task by ID.
    *
    * @param taskId ID of the task to retrieve
    * @return task with the given ID
    * @throws NotFoundException if no task with the given ID can be found
    */
   @Transactional(readOnly = true)
   public Task get(final long taskId) throws NotFoundException {
      return findTaskOrThrow(taskId);
   }

   /**
    * Retrieves the tasks for a project.
    *
    * @param projectId ID of the project
    * @return tasks for the project
    * @throws NotFoundException if no project with the given ID can be found
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
    * Saves a task.
    *
    * @param task task to save
    * @return the saved task
    */
   public Task save(final Task task) {
      return tasks.save(task);
   }

   /**
    * Deletes a task by ID.
    *
    * @param taskId ID of the task to delete
    */
   public void delete(final long taskId) {
      final Task task = findTaskOrThrow(taskId);
      tasks.delete(task);
   }

   private void checkProjectExists(final long projectId) {
      Optional.ofNullable(projects.findOne(projectId)).orElseThrow(() -> new NotFoundException("project"));
   }

   private Task findTaskOrThrow(final long taskId) {
      return Optional.ofNullable(tasks.findOne(taskId)).orElseThrow(() -> new NotFoundException("task"));
   }

}
