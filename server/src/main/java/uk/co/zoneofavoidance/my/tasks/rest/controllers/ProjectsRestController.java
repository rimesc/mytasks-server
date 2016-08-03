package uk.co.zoneofavoidance.my.tasks.rest.controllers;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static uk.co.zoneofavoidance.my.tasks.util.BeanUtils.setIfNotNull;

import javax.validation.Valid;

import org.pegdown.PegDownProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import uk.co.zoneofavoidance.my.tasks.domain.Note;
import uk.co.zoneofavoidance.my.tasks.domain.Project;
import uk.co.zoneofavoidance.my.tasks.exceptions.NotFoundException;
import uk.co.zoneofavoidance.my.tasks.repositories.ProjectRepository;
import uk.co.zoneofavoidance.my.tasks.repositories.TaskRepository;
import uk.co.zoneofavoidance.my.tasks.rest.request.ProjectForm;
import uk.co.zoneofavoidance.my.tasks.rest.request.ReadMeRequest;
import uk.co.zoneofavoidance.my.tasks.rest.response.BindingErrorsResponse;
import uk.co.zoneofavoidance.my.tasks.rest.response.ErrorResponse;
import uk.co.zoneofavoidance.my.tasks.rest.response.ProjectResponse;
import uk.co.zoneofavoidance.my.tasks.rest.response.ProjectsResponse;
import uk.co.zoneofavoidance.my.tasks.rest.response.ReadMeResponse;

/**
 * REST controller for the projects API.
 */
@RestController
@RequestMapping("/api/projects")
public class ProjectsRestController {

   private final ProjectRepository projects;

   private final TaskRepository tasks;

   private final PegDownProcessor markdown;

   @Autowired
   public ProjectsRestController(final ProjectRepository projects, final TaskRepository tasks, final PegDownProcessor markdown) {
      this.projects = projects;
      this.tasks = tasks;
      this.markdown = markdown;
   }

   /**
    * End-point for obtaining a list of projects.
    *
    * @return a REST response containing a list of projects
    */
   @RequestMapping(method = GET, produces = "application/json")
   public ProjectsResponse getProjects() {
      return new ProjectsResponse(projects.findAll().stream().map(this::convert).collect(toList()));
   }

   /**
    * End-point for obtaining the details of a single project.
    *
    * @param projectId path variable containing the ID of the requested project
    * @return a REST response containing the project details
    */
   @RequestMapping(path = "{projectId}", method = GET, produces = "application/json")
   public ProjectResponse getProject(@PathVariable final Long projectId) {
      final Project project = projects.findOne(projectId);
      if (project == null) {
         throw new NotFoundException("project");
      }
      return convert(project);
   }

   /**
    * End-point for obtaining the 'read-me' of a project.
    *
    * @param projectId path variable containing the ID of the requested project
    * @return a REST response containing the 'read-me' in both raw markdown and
    *         HTML formats
    */
   @RequestMapping(path = "{projectId}/readme", method = GET, produces = "application/json")
   public ReadMeResponse getProjectReadMe(@PathVariable final Long projectId) {
      final Project project = projects.findOne(projectId);
      if (project == null) {
         throw new NotFoundException("project");
      }
      if (project.getReadMe() == null) {
         throw new NotFoundException("note");
      }
      return new ReadMeResponse(markdown.markdownToHtml(project.getReadMe().getText()), project.getReadMe().getText());
   }

   /**
    * End-point for updating the 'read-me' of a project.
    *
    * @param projectId path variable containing the ID of the requested project
    * @param request JSON request body containing the updated markdown
    * @return a REST response containing the updated 'read-me' in both raw
    *         markdown and HTML formats
    */
   @RequestMapping(path = "{projectId}/readme", method = POST, consumes = "application/json", produces = "application/json")
   public ReadMeResponse postProjectReadMe(@PathVariable final Long projectId, @RequestBody @Valid final ReadMeRequest request) {
      final Project project = projects.findOne(projectId);
      if (project == null) {
         throw new NotFoundException("project");
      }
      if (project.getReadMe() == null) {
         setIfNotNull(request.getMarkdown(), m -> project.setReadMe(new Note(request.getMarkdown())));
      }
      else {
         setIfNotNull(request.getMarkdown(), project.getReadMe()::setText);
      }
      final Project updatedProject = projects.save(project);
      return new ReadMeResponse(markdown.markdownToHtml(updatedProject.getReadMe().getText()), updatedProject.getReadMe().getText());
   }

   /**
    * End-point for creating a new project.
    *
    * @param form form containing the details of the new project
    * @return a REST response containing the details of the new project
    */
   @RequestMapping(method = POST, consumes = "application/json", produces = "application/json")
   @ResponseStatus(ACCEPTED)
   public ProjectResponse postNewProject(@Valid @RequestBody final ProjectForm form) {
      final Project project = projects.save(Project.create(form.getName(), form.getDescription()));
      return convert(project);
   }

   /**
    * End-point for updating a project.
    *
    * @param projectId path variable containing the ID of the project to update
    * @param form form containing the updated details of the project
    * @return a REST response containing the details of the updated project
    */
   @RequestMapping(path = "{projectId}", method = POST, consumes = "application/json", produces = "application/json")
   @ResponseStatus(ACCEPTED)
   public ProjectResponse postEditProject(@PathVariable final Long projectId, @Valid @RequestBody final ProjectForm form) {
      final Project project = projects.findOne(projectId);
      if (project == null) {
         throw new NotFoundException("project");
      }
      project.setName(form.getName());
      project.setDescription(form.getDescription());
      return convert(projects.save(project));
   }

   /**
    * End-point for deleting a new project.
    *
    * @param projectId path variable containing the ID of the project to delete
    */
   @RequestMapping(path = "{projectId}", method = DELETE)
   @ResponseStatus(NO_CONTENT)
   public void deleteProject(@PathVariable final Long projectId) {
      final Project project = projects.findOne(projectId);
      if (project == null) {
         throw new NotFoundException("project");
      }
      tasks.deleteByProjectId(projectId);
      projects.delete(projectId);
   }

   private ProjectResponse convert(final Project project) {
      return new ProjectResponse(project.getId(), project.getName(), project.getDescription(), project.getNumberOfOpenTasks(), "/api/projects/" + project.getId());
   }

   /**
    * Error handler for invalid project forms.
    *
    * @param ex exception
    * @return a REST response containing details of the error
    */
   @ExceptionHandler(MethodArgumentNotValidException.class)
   public ResponseEntity<BindingErrorsResponse> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex) {
      return new ResponseEntity<>(BindingErrorsResponse.create(ex.getBindingResult()), BAD_REQUEST);
   }

   /**
    * Error handler for when a project is not found.
    *
    * @param ex exception
    * @return a REST response containing details of the error
    */
   @ExceptionHandler(NotFoundException.class)
   public ResponseEntity<ErrorResponse> handleNotFound(final NotFoundException ex) {
      return new ResponseEntity<>(ErrorResponse.create(NOT_FOUND, ex.getMessage()), NOT_FOUND);
   }

   /**
    * Error handler for invalid (non-numeric) project IDs.
    *
    * @param ex exception
    * @return a REST response containing details of the error
    */
   @ExceptionHandler(MethodArgumentTypeMismatchException.class)
   public ResponseEntity<ErrorResponse> handleBadProjectId(final MethodArgumentTypeMismatchException ex) {
      return new ResponseEntity<>(ErrorResponse.create(BAD_REQUEST, "Invalid project ID: " + ex.getValue()), BAD_REQUEST);
   }

}