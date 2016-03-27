package uk.co.zoneofavoidance.my.tasks.controllers.rest;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import uk.co.zoneofavoidance.my.tasks.controllers.rest.response.BindingErrorsResponse;
import uk.co.zoneofavoidance.my.tasks.controllers.rest.response.ErrorResponse;
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

   @Autowired
   public TasksRestController(final ProjectRepository projects, final TaskRepository tasks) {
      this.projects = projects;
      this.tasks = tasks;
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

   private TaskResponse convert(final Task task) {
      return new TaskResponse(task.getId(), task.getSummary(), task.getDescription(), task.getPriority(), task.getState(), task.getCreated(), task.getUpdated(), task.getProject().getId(), "/api/tasks/" + task.getId());
   }

   @ExceptionHandler(MethodArgumentNotValidException.class)
   public ResponseEntity<BindingErrorsResponse> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex) {
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
