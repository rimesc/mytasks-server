package uk.co.zoneofavoidance.my.tasks.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uk.co.zoneofavoidance.my.tasks.domain.Project;
import uk.co.zoneofavoidance.my.tasks.exceptions.NotFoundException;
import uk.co.zoneofavoidance.my.tasks.repositories.ProjectRepository;
import uk.co.zoneofavoidance.my.tasks.repositories.TaskRepository;

/**
 * Service that provides transactional access to projects.
 */
@Service
@Transactional
public class ProjectService {

   private final ProjectRepository projects;
   private final TaskRepository tasks;

   @Autowired
   public ProjectService(final ProjectRepository projects, final TaskRepository tasks) {
      this.projects = projects;
      this.tasks = tasks;
   }

   /**
    * Retrieves a project by ID.
    *
    * @param projectId ID of the project to retrieve
    * @return project with the given ID
    * @throws NotFoundException if no project with the given ID can be found
    */
   @Transactional(readOnly = true)
   public Project get(final long projectId) throws NotFoundException {
      return findProjectOrThrow(projectId);
   }

   /**
    * Retrieves all projects.
    *
    * @return all projects
    */
   @Transactional(readOnly = true)
   public List<Project> list() {
      return projects.findAll();
   }

   /**
    * Saves a project.
    *
    * @param project project to save
    * @return the saved project
    */
   public Project save(final Project project) {
      return projects.save(project);
   }

   /**
    * Deletes a project by ID.
    *
    * @param projectId ID of the project to delete
    */
   public void delete(final long projectId) {
      final Project project = findProjectOrThrow(projectId);
      tasks.deleteByProjectId(projectId);
      projects.delete(project);
   }

   private Project findProjectOrThrow(final Long projectId) {
      return Optional.ofNullable(projects.findOne(projectId)).orElseThrow(() -> new NotFoundException("project"));
   }

}
