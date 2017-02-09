package uk.co.zoneofavoidance.my.tasks.controllers;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static uk.co.zoneofavoidance.my.tasks.util.BeanUtils.setIfNotNull;

import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import uk.co.zoneofavoidance.my.tasks.domain.Project;
import uk.co.zoneofavoidance.my.tasks.domain.State;
import uk.co.zoneofavoidance.my.tasks.domain.Task;
import uk.co.zoneofavoidance.my.tasks.exceptions.NotFoundException;
import uk.co.zoneofavoidance.my.tasks.request.TaskForm;
import uk.co.zoneofavoidance.my.tasks.response.BindingErrorsResponse;
import uk.co.zoneofavoidance.my.tasks.response.ErrorResponse;
import uk.co.zoneofavoidance.my.tasks.response.JsonConversions;
import uk.co.zoneofavoidance.my.tasks.response.NotesJson;
import uk.co.zoneofavoidance.my.tasks.response.ResourceJson;
import uk.co.zoneofavoidance.my.tasks.response.TaskJson;
import uk.co.zoneofavoidance.my.tasks.services.ProjectService;
import uk.co.zoneofavoidance.my.tasks.services.TagService;
import uk.co.zoneofavoidance.my.tasks.services.TaskService;

/**
 * REST controller for the tasks API.
 */
@RestController
@RequestMapping("/api/tasks")
public class TasksController {

   static final String BASE_PATH = "/api/tasks";

   private final ProjectService projects;

   private final TaskService tasks;

   private final TagService tags;

   private final JsonConversions conversions;

   @Autowired
   public TasksController(final ProjectService projects, final TaskService tasks, final TagService tags, final JsonConversions conversions) {
      this.projects = projects;
      this.tasks = tasks;
      this.tags = tags;
      this.conversions = conversions;
   }

   /**
    * End-point for obtaining a list of tasks for a project.
    *
    * @param projectId path variable containing the ID of the requested project
    * @param state repeatable request parameter specifying states to which to
    *           filter the results
    * @return a REST response containing a list of tasks
    */
   @RequestMapping(method = GET, produces = "application/json")
   public List<ResourceJson<TaskJson>> getTasks(@RequestParam("project") final Long projectId, @RequestParam(name = "state", required = false) final State[] state) {
      final Project project = projects.get(projectId);
      if (project == null) {
         throw new NotFoundException("project");
      }
      final List<Task> tasksForProject = state != null ? tasks.getForProject(projectId, state) : tasks.getForProject(projectId);
      return tasksForProject.stream().map(task -> new ResourceJson<>(conversions.toAbridgedJson(task), path(task))).collect(toList());
   }

   /**
    * End-point for obtaining the details of a single task.
    *
    * @param taskId path variable containing the ID of the requested task
    * @return a REST response containing the task details
    */
   @RequestMapping(path = "{taskId}", method = GET, produces = "application/json")
   public ResourceJson<TaskJson> getTask(@PathVariable final Long taskId) {
      final Task task = tasks.get(taskId);
      return new ResourceJson<TaskJson>(conversions.toJson(task), path(task));
   }

   /**
    * End-point for updating a task.
    *
    * @param taskId path variable containing the ID of the task to update
    * @param request JSON request body containing the updated details of the
    *           task
    * @return a REST response containing the details of the updated task
    */
   @RequestMapping(path = "{taskId}", method = POST, produces = "application/json")
   public ResourceJson<TaskJson> postTask(@PathVariable final Long taskId, @Valid @RequestBody final TaskForm request) {
      final Task task = tasks.get(taskId);
      setIfNotNull(request.getSummary(), task::setSummary);
      setIfNotNull(request.getPriority(), task::setPriority);
      setIfNotNull(request.getState(), task::setState);
      setIfNotNull(request.getTags(), tags::get, task::setTags);
      return new ResourceJson<TaskJson>(conversions.toJson(tasks.save(task)), path(task));
   }

   /**
    * End-point for deleting a task.
    *
    * @param taskId path variable containing the ID of the task to delete
    */
   @RequestMapping(path = "{taskId}", method = DELETE)
   @ResponseStatus(NO_CONTENT)
   public void deleteTask(@PathVariable final Long taskId) {
      tasks.delete(taskId);
   }

   /**
    * End-point for obtaining the notes for a task.
    *
    * @param taskId path variable containing the ID of the requested task
    * @return a REST response containing the 'read-me' in both raw markdown and
    *         HTML formats
    */
   @RequestMapping(path = "{taskId}/notes", method = GET, produces = "application/json")
   public ResourceJson<NotesJson> getTaskNotes(@PathVariable final Long taskId) {
      final Task task = tasks.get(taskId);
      return new ResourceJson<>(conversions.json(task.getDescription()), path(task, "notes"));
   }

   /**
    * End-point for updating the notes for a task.
    *
    * @param taskId path variable containing the ID of the requested task
    * @param notes request body containing the updated markdown
    * @return a REST response containing the updated notes in both raw markdown
    *         and HTML formats
    */
   @RequestMapping(path = "{taskId}/notes", method = POST, consumes = "text/markdown", produces = "application/json")
   public ResourceJson<NotesJson> postTaskNotes(@PathVariable final Long taskId, @RequestBody final String notes) {
      final Task task = tasks.get(taskId);
      task.setDescription(notes);
      final Task updatedTask = tasks.save(task);
      return new ResourceJson<>(conversions.json(task.getDescription()), path(updatedTask, "notes"));
   }

   /**
    * Error handler for invalid task forms.
    *
    * @param ex exception
    * @return a REST response containing details of the error
    */
   @ExceptionHandler(MethodArgumentNotValidException.class)
   public ResponseEntity<BindingErrorsResponse> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex) {
      return new ResponseEntity<>(BindingErrorsResponse.create(ex.getBindingResult()), BAD_REQUEST);
   }

   /**
    * Error handler for invalid task forms.
    *
    * @param ex exception
    * @return a REST response containing details of the error
    */
   @ExceptionHandler(BindException.class)
   public ResponseEntity<BindingErrorsResponse> handleBindingError(final BindException ex) {
      return new ResponseEntity<>(BindingErrorsResponse.create(ex.getBindingResult()), BAD_REQUEST);
   }

   /**
    * Error handler for when a task is not found.
    *
    * @param ex exception
    * @return a REST response containing details of the error
    */
   @ExceptionHandler(NotFoundException.class)
   public ResponseEntity<ErrorResponse> handleNotFound(final NotFoundException ex) {
      return new ResponseEntity<>(ErrorResponse.create(NOT_FOUND, ex.getMessage()), NOT_FOUND);
   }

   /**
    * Error handler for invalid (non-numeric) task IDs.
    *
    * @param ex exception
    * @return a REST response containing details of the error
    */
   @ExceptionHandler(MethodArgumentTypeMismatchException.class)
   public ResponseEntity<ErrorResponse> handleBadProjectId(final MethodArgumentTypeMismatchException ex) {
      return new ResponseEntity<>(ErrorResponse.create(BAD_REQUEST, "Invalid task ID: " + ex.getValue()), BAD_REQUEST);
   }

   static String path(final Task task, final String... extraPath) {
      return BASE_PATH + "/" + task.getId() + Arrays.stream(extraPath).map(p -> "/" + p).collect(joining());
   }

}
