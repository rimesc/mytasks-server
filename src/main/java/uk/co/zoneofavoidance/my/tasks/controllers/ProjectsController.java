package uk.co.zoneofavoidance.my.tasks.controllers;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static uk.co.zoneofavoidance.my.tasks.controllers.ProjectsController.BASE_PATH;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.groups.Default;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
import uk.co.zoneofavoidance.my.tasks.domain.Task;
import uk.co.zoneofavoidance.my.tasks.exceptions.NotFoundException;
import uk.co.zoneofavoidance.my.tasks.repositories.ProjectRepository;
import uk.co.zoneofavoidance.my.tasks.repositories.TaskRepository;
import uk.co.zoneofavoidance.my.tasks.request.ProjectForm;
import uk.co.zoneofavoidance.my.tasks.request.TaskForm;
import uk.co.zoneofavoidance.my.tasks.request.validation.Create;
import uk.co.zoneofavoidance.my.tasks.response.BindingErrorsResponse;
import uk.co.zoneofavoidance.my.tasks.response.ErrorResponse;
import uk.co.zoneofavoidance.my.tasks.response.JsonConversions;
import uk.co.zoneofavoidance.my.tasks.response.NotesJson;
import uk.co.zoneofavoidance.my.tasks.response.ProjectJson;
import uk.co.zoneofavoidance.my.tasks.response.ResourceJson;
import uk.co.zoneofavoidance.my.tasks.response.TaskJson;
import uk.co.zoneofavoidance.my.tasks.services.TagService;

/**
 * REST controller for the projects API.
 */
@RestController
@RequestMapping(BASE_PATH)
public class ProjectsController {

   static final String BASE_PATH = "/api/projects";

   private final ProjectRepository projects;

   private final TaskRepository tasks;

   private final TagService tags;

   private final JsonConversions conversions;

   @Autowired
   public ProjectsController(final ProjectRepository projects, final TaskRepository tasks, final TagService tags, final JsonConversions conversions) {
      this.projects = projects;
      this.tasks = tasks;
      this.tags = tags;
      this.conversions = conversions;
   }

   /**
    * End-point for obtaining a list of projects.
    *
    * @return a REST response containing a list of projects
    */
   @RequestMapping(method = GET, produces = "application/json")
   public List<ResourceJson<ProjectJson>> getProjects() {
      return projects.findAll().stream().map(project -> new ResourceJson<>(conversions.toAbridgedJson(project), path(project))).collect(toList());
   }

   /**
    * End-point for obtaining the details of a single project.
    *
    * @param projectId path variable containing the ID of the requested project
    * @return a REST response containing the project details
    */
   @RequestMapping(path = "{projectId}", method = GET, produces = "application/json")
   public ResourceJson<ProjectJson> getProject(@PathVariable final Long projectId) {
      final Project project = findProjectOrThrow(projectId);
      return new ResourceJson<ProjectJson>(conversions.toJson(project), path(project));
   }

   /**
    * End-point for obtaining the notes for a project.
    *
    * @param projectId path variable containing the ID of the requested project
    * @return a REST response containing the notes in both raw markdown and HTML
    *         formats
    */
   @RequestMapping(path = "{projectId}/notes", method = GET, produces = "application/json")
   public ResourceJson<NotesJson> getProjectNotes(@PathVariable final Long projectId) {
      final Project project = findProjectOrThrow(projectId);
      if (project.getReadMe() == null) {
         throw new NotFoundException("note");
      }
      return new ResourceJson<>(conversions.json(project.getReadMe()), path(project, "notes"));
   }

   /**
    * End-point for updating the notes for a project.
    *
    * @param projectId path variable containing the ID of the requested project
    * @param notes request body containing the updated markdown
    * @return a REST response containing the updated notes in both raw markdown
    *         and HTML formats
    */
   @RequestMapping(path = "{projectId}/notes", method = POST, consumes = "text/markdown", produces = "application/json")
   public ResourceJson<NotesJson> postProjectNotes(@PathVariable final Long projectId, @RequestBody(required = false) final String notes) {
      final Project project = findProjectOrThrow(projectId);
      if (notes == null) {
         project.setReadMe(null);
      }
      else if (project.getReadMe() == null) {
         project.setReadMe(new Note(notes));
      }
      else {
         project.getReadMe().setText(notes);
      }
      final Project updatedProject = projects.save(project);
      return new ResourceJson<>(conversions.json(updatedProject.getReadMe()), path(updatedProject, "notes"));
   }

   /**
    * End-point for creating a new project.
    *
    * @param form form containing the details of the new project
    * @return a REST response containing the details of the new project
    */
   @RequestMapping(method = POST, consumes = "application/json", produces = "application/json")
   @ResponseStatus(CREATED)
   public ResourceJson<ProjectJson> postNewProject(@Validated({ Create.class, Default.class }) @RequestBody final ProjectForm form) {
      final Project project = projects.save(Project.create(form.getName(), form.getDescription()));
      return new ResourceJson<ProjectJson>(conversions.toJson(project), path(project));
   }

   /**
    * End-point for updating a project.
    *
    * @param projectId path variable containing the ID of the project to update
    * @param form form containing the updated details of the project
    * @return a REST response containing the details of the updated project
    */
   @RequestMapping(path = "{projectId}", method = POST, consumes = "application/json", produces = "application/json")
   public ResourceJson<ProjectJson> postEditProject(@PathVariable final Long projectId, @Valid @RequestBody final ProjectForm form) {
      final Project project = findProjectOrThrow(projectId);
      project.setName(form.getName());
      project.setDescription(form.getDescription());
      return new ResourceJson<ProjectJson>(conversions.toJson(projects.save(project)), path(project));
   }

   /**
    * End-point for deleting a new project.
    *
    * @param projectId path variable containing the ID of the project to delete
    */
   @RequestMapping(path = "{projectId}", method = DELETE)
   @ResponseStatus(NO_CONTENT)
   public void deleteProject(@PathVariable final Long projectId) {
      findProjectOrThrow(projectId);
      tasks.deleteByProjectId(projectId);
      projects.delete(projectId);
   }

   /**
    * End-point for obtaining the tasks for a project.
    *
    * @param projectId path variable containing the ID of the requested project
    * @return a REST response containing the tasks for the project
    */
   @RequestMapping(path = "{projectId}/tasks", method = GET, produces = "application/json")
   public List<ResourceJson<TaskJson>> getProjectTasks(@PathVariable final Long projectId) {
      findProjectOrThrow(projectId);
      final List<Task> tasksForProject = tasks.findByProjectId(projectId);
      return tasksForProject.stream().map(task -> new ResourceJson<>(conversions.toAbridgedJson(task), TasksController.path(task))).collect(toList());
   }

   /**
    * End-point for adding a new task to a project.
    *
    * @param projectId path variable containing the ID of the requested project
    * @param form JSON request body containing the details of the new task
    * @return a REST response containing the details of the new task
    */
   @RequestMapping(path = "{projectId}/tasks", method = POST, consumes = "application/json", produces = "application/json")
   @ResponseStatus(CREATED)
   public ResourceJson<TaskJson> postNewProjectTask(@PathVariable final Long projectId, @RequestBody @Validated({ Create.class, Default.class }) final TaskForm form) {
      final Project project = findProjectOrThrow(projectId);
      final Task task = tasks.save(Task.create(project, form.getSummary(), "", form.getPriority(), Optional.ofNullable(form.getTags()).orElse(emptySet()).stream().map(tags::get).collect(toSet())));
      return new ResourceJson<>(conversions.toJson(task), TasksController.path(task));
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

   static String path(final Project project, final String... extraPath) {
      return BASE_PATH + "/" + project.getId() + Arrays.stream(extraPath).map(p -> "/" + p).collect(joining());
   }

   private Project findProjectOrThrow(final Long projectId) {
      return Optional.ofNullable(projects.findOne(projectId)).orElseThrow(() -> new NotFoundException("project"));
   }

}
