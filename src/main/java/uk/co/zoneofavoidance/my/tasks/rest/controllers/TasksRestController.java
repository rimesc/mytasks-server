package uk.co.zoneofavoidance.my.tasks.rest.controllers;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static uk.co.zoneofavoidance.my.tasks.util.BeanUtils.setIfNotNull;

import java.util.List;

import javax.validation.Valid;

import org.pegdown.PegDownProcessor;
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
import uk.co.zoneofavoidance.my.tasks.domain.Tag;
import uk.co.zoneofavoidance.my.tasks.domain.Task;
import uk.co.zoneofavoidance.my.tasks.exceptions.NotFoundException;
import uk.co.zoneofavoidance.my.tasks.repositories.ProjectRepository;
import uk.co.zoneofavoidance.my.tasks.repositories.TaskRepository;
import uk.co.zoneofavoidance.my.tasks.rest.request.TaskForm;
import uk.co.zoneofavoidance.my.tasks.rest.request.UpdateTaskRequest;
import uk.co.zoneofavoidance.my.tasks.rest.response.BindingErrorsResponse;
import uk.co.zoneofavoidance.my.tasks.rest.response.ErrorResponse;
import uk.co.zoneofavoidance.my.tasks.rest.response.ReadMeResponse;
import uk.co.zoneofavoidance.my.tasks.rest.response.TaskResponse;
import uk.co.zoneofavoidance.my.tasks.rest.response.TasksResponse;

/**
 * REST controller for the tasks API.
 */
@RestController
@RequestMapping("/api/tasks")
public class TasksRestController {

   private final ProjectRepository projects;

   private final TaskRepository tasks;

   private final PegDownProcessor markdown;

   @Autowired
   public TasksRestController(final ProjectRepository projects, final TaskRepository tasks, final PegDownProcessor markdown) {
      this.projects = projects;
      this.tasks = tasks;
      this.markdown = markdown;
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
   public TasksResponse getTasks(@RequestParam("project") final Long projectId, @RequestParam(name = "state", required = false) final State[] state) {
      final Project project = projects.findOne(projectId);
      if (project == null) {
         throw new NotFoundException("project");
      }
      final List<Task> tasksForProject = state != null ? tasks.findByProjectIdAndStateIn(projectId, asList(state)) : tasks.findByProjectId(projectId);
      return new TasksResponse(tasksForProject.stream().map(this::convert).collect(toList()));
   }

   /**
    * End-point for creating a new task.
    *
    * @param form form containing the details of the new task
    * @return a REST response containing the details of the new task
    */
   @RequestMapping(method = POST, consumes = "application/json", produces = "application/json")
   @ResponseStatus(ACCEPTED)
   public TaskResponse postNewProject(@Valid @RequestBody final TaskForm form) {
      final Project project = projects.findOne(form.getProject());
      if (project == null) {
         throw new NotFoundException("project");
      }
      final Task task = tasks.save(Task.create(project, form.getSummary(), form.getDescription(), form.getPriority()));
      return convert(task);
   }

   /**
    * End-point for obtaining the details of a single task.
    *
    * @param taskId path variable containing the ID of the requested task
    * @return a REST response containing the task details
    */
   @RequestMapping(path = "{taskId}", method = GET, produces = "application/json")
   public TaskResponse getTask(@PathVariable final Long taskId) {
      final Task task = tasks.findOne(taskId);
      if (task == null) {
         throw new NotFoundException("task");
      }
      return convert(task);
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
   public TaskResponse postTask(@PathVariable final Long taskId, @Valid @RequestBody final UpdateTaskRequest request) {
      final Task task = tasks.findOne(taskId);
      if (task == null) {
         throw new NotFoundException("task");
      }
      setIfNotNull(request.getSummary(), task::setSummary);
      setIfNotNull(request.getDescription(), task::setDescription);
      setIfNotNull(request.getPriority(), task::setPriority);
      setIfNotNull(request.getState(), task::setState);
      return convert(tasks.save(task));
   }

   /**
    * End-point for deleting a task.
    *
    * @param taskId path variable containing the ID of the task to delete
    */
   @RequestMapping(path = "{taskId}", method = DELETE)
   @ResponseStatus(NO_CONTENT)
   public void deleteTask(@PathVariable final Long taskId) {
      final Task task = tasks.findOne(taskId);
      if (task == null) {
         throw new NotFoundException("task");
      }
      tasks.delete(taskId);
   }

   /**
    * End-point for obtaining the 'read-me' of a task.
    *
    * @param taskId path variable containing the ID of the requested task
    * @return a REST response containing the 'read-me' in both raw markdown and
    *         HTML formats
    */
   @RequestMapping(path = "{taskId}/readme", method = GET, produces = "application/json")
   public ReadMeResponse getTaskReadMe(@PathVariable final Long taskId) {
      final Task task = tasks.findOne(taskId);
      if (task == null) {
         throw new NotFoundException("task");
      }
      if (task.getDescription() == null) {
         throw new NotFoundException("note");
      }
      return new ReadMeResponse(markdown.markdownToHtml(task.getDescription()), task.getDescription());
   }

   private TaskResponse convert(final Task task) {
      return new TaskResponse(task.getId(), task.getSummary(), task.getDescription(), task.getPriority(), task.getState(), task.getTags().stream().map(Tag::getName).distinct().collect(toList()), task.getCreated(), task.getUpdated(), task.getProject().getId(), "/api/tasks/" + task.getId());
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

}
