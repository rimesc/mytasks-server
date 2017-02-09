package uk.co.zoneofavoidance.my.tasks.services;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uk.co.zoneofavoidance.my.tasks.domain.Project;
import uk.co.zoneofavoidance.my.tasks.exceptions.NotFoundException;
import uk.co.zoneofavoidance.my.tasks.exceptions.PermissionDeniedException;
import uk.co.zoneofavoidance.my.tasks.repositories.ProjectRepository;
import uk.co.zoneofavoidance.my.tasks.repositories.TaskRepository;
import uk.co.zoneofavoidance.my.tasks.security.AuthenticatedUser;

/**
 * Service that provides transactional access to projects.
 */
@Service
@Transactional
public class ProjectService extends BaseService {

   private final ProjectRepository projects;
   private final TaskRepository tasks;

   @Autowired
   public ProjectService(final ProjectRepository projects, final TaskRepository tasks, final AuthenticatedUser user) {
      super(user);
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
      return checkPermissions(findProjectOrThrow(projectId), "view");
   }

   /**
    * Retrieves all projects owner by the current user.
    *
    * @return all the user's projects
    */
   @Transactional(readOnly = true)
   public List<Project> list() {
      return projects.findAll().stream().filter(project -> isPermitted(project.getOwner())).collect(toList());
   }

   /**
    * Saves a project.
    *
    * @param project project to save
    * @return the saved project
    */
   public Project save(final Project project) {
      if (project.getId() == null && project.getOwner() == null) {
         project.setOwner(authenticatedUser().getId());
      }
      checkPermissions(project, project.getId() == null ? "create" : "edit");
      return projects.save(project);
   }

   /**
    * Deletes a project by ID.
    *
    * @param projectId ID of the project to delete
    */
   public void delete(final long projectId) {
      final Project project = checkPermissions(findProjectOrThrow(projectId), "delete");
      tasks.deleteByProjectId(projectId);
      projects.delete(project);
   }

   private Project findProjectOrThrow(final Long projectId) {
      return Optional.ofNullable(projects.findOne(projectId)).orElseThrow(() -> new NotFoundException("project"));
   }

   private Project checkPermissions(final Project project, final String action) {
      if (!isPermitted(project.getOwner())) {
         throw new PermissionDeniedException("project", action);
      }
      return project;
   }

}
