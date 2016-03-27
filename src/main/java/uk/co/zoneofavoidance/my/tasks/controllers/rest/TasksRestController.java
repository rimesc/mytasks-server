package uk.co.zoneofavoidance.my.tasks.controllers.rest;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

import javax.servlet.http.HttpServletRequest;
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

import uk.co.zoneofavoidance.my.tasks.controllers.rest.request.UpdateTaskRequest;
import uk.co.zoneofavoidance.my.tasks.controllers.rest.response.BindingErrorsResponse;
import uk.co.zoneofavoidance.my.tasks.controllers.rest.response.ErrorResponse;
import uk.co.zoneofavoidance.my.tasks.controllers.rest.response.ReadMeResponse;
import uk.co.zoneofavoidance.my.tasks.controllers.rest.response.TaskResponse;
import uk.co.zoneofavoidance.my.tasks.controllers.rest.response.TasksResponse;
import uk.co.zoneofavoidance.my.tasks.domain.Project;
import uk.co.zoneofavoidance.my.tasks.domain.State;
import uk.co.zoneofavoidance.my.tasks.domain.Task;
import uk.co.zoneofavoidance.my.tasks.exceptions.NotFoundException;
import uk.co.zoneofavoidance.my.tasks.repositories.ProjectRepository;
import uk.co.zoneofavoidance.my.tasks.repositories.TaskRepository;

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

   @RequestMapping(method = GET, produces = "application/json")
   public TasksResponse getTasks(@RequestParam("project") final Long projectId, @RequestParam(name = "state", required = false) final State[] state) {
      final Project project = projects.findOne(projectId);
      if (project == null) {
         throw new NotFoundException("project");
      }
      final List<Task> tasksForProject = state != null ? tasks.findByProjectIdAndStateIn(projectId, asList(state)) : tasks.findByProjectId(projectId);
      return new TasksResponse(tasksForProject.stream().map(this::convert).collect(toList()));
   }

   @RequestMapping(path = "{taskId}", method = GET, produces = "application/json")
   public TaskResponse getTask(@PathVariable final Long taskId) {
      final Task task = tasks.findOne(taskId);
      if (task == null) {
         throw new NotFoundException("task");
      }
      return convert(task);
   }

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

   private <T> void setIfNotNull(final T valueOrNull, final Consumer<T> setter) {
      if (valueOrNull != null) {
         setter.accept(valueOrNull);
      }
   }

   @RequestMapping(path = "{taskId}", method = DELETE)
   @ResponseStatus(NO_CONTENT)
   public void deleteTask(@PathVariable final Long taskId) {
      final Task task = tasks.findOne(taskId);
      if (task == null) {
         throw new NotFoundException("task");
      }
      tasks.delete(taskId);
   }

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
      return new TaskResponse(task.getId(), task.getSummary(), task.getDescription(), task.getPriority(), task.getState(), task.getCreated(), task.getUpdated(), task.getProject().getId(), "/api/tasks/" + task.getId());
   }

   @ExceptionHandler(MethodArgumentNotValidException.class)
   public ResponseEntity<BindingErrorsResponse> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex) {
      return new ResponseEntity<>(BindingErrorsResponse.create(ex.getBindingResult()), BAD_REQUEST);
   }

   @ExceptionHandler(BindException.class)
   public ResponseEntity<BindingErrorsResponse> handleMethodArgumentNotValid(final BindException ex, final HttpServletRequest req) throws IOException {
      return new ResponseEntity<>(BindingErrorsResponse.create(ex.getBindingResult()), BAD_REQUEST);
   }

   @ExceptionHandler(NotFoundException.class)
   public ResponseEntity<ErrorResponse> handleNotFound(final NotFoundException ex) {
      return new ResponseEntity<>(ErrorResponse.create(NOT_FOUND, ex.getMessage()), NOT_FOUND);
   }

   @ExceptionHandler(MethodArgumentTypeMismatchException.class)
   public ResponseEntity<ErrorResponse> handleBadProjectId(final MethodArgumentTypeMismatchException ex) {
      return new ResponseEntity<>(ErrorResponse.create(BAD_REQUEST, "Invalid task ID: " + ex.getValue()), BAD_REQUEST);
   }

}
