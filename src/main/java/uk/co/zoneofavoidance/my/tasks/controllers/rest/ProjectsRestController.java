package uk.co.zoneofavoidance.my.tasks.controllers.rest;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.validation.Valid;

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

import uk.co.zoneofavoidance.my.tasks.controllers.ProjectForm;
import uk.co.zoneofavoidance.my.tasks.controllers.rest.response.BindingErrorsResponse;
import uk.co.zoneofavoidance.my.tasks.controllers.rest.response.ErrorResponse;
import uk.co.zoneofavoidance.my.tasks.controllers.rest.response.ProjectResponse;
import uk.co.zoneofavoidance.my.tasks.controllers.rest.response.ProjectsResponse;
import uk.co.zoneofavoidance.my.tasks.domain.Note;
import uk.co.zoneofavoidance.my.tasks.domain.Project;
import uk.co.zoneofavoidance.my.tasks.exceptions.NotFoundException;
import uk.co.zoneofavoidance.my.tasks.repositories.ProjectRepository;

@RestController
@RequestMapping("/api/projects")
public class ProjectsRestController {

   private final ProjectRepository projects;

   @Autowired
   public ProjectsRestController(final ProjectRepository projects) {
      this.projects = projects;
   }

   @RequestMapping(method = GET, produces = "application/json")
   public ProjectsResponse getProjects() {
      return new ProjectsResponse(projects.findAll().stream().map(this::summarise).collect(toList()));
   }

   @RequestMapping(path = "{projectId}", method = GET, produces = "application/json")
   public ProjectResponse getProject(@PathVariable final Long projectId) {
      final Project project = projects.findOne(projectId);
      if (project == null) {
         throw new NotFoundException("project");
      }
      return convert(project);
   }

   @RequestMapping(method = POST, consumes = "application/json", produces = "application/json")
   @ResponseStatus(ACCEPTED)
   public ProjectResponse postNewProject(@Valid @RequestBody final ProjectForm form) {
      final Project project = projects.save(Project.create(form.getName(), form.getDescription()));
      return convert(project);
   }

   private ProjectResponse convert(final Project project) {
      final Note readMe = project.getReadMe();
      return ProjectResponse.create(project.getId(), project.getName(), project.getDescription(), project.getNumberOfOpenTasks(), "/api/projects/" + project.getId(), readMe == null ? null : readMe.getText());
   }

   private ProjectResponse.Summary summarise(final Project project) {
      return new ProjectResponse.Summary(project.getId(), project.getName(), project.getDescription(), project.getNumberOfOpenTasks(), "/api/projects/" + project.getId());
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
      return new ResponseEntity<>(ErrorResponse.create(BAD_REQUEST, "Invalid project ID: " + ex.getValue()), BAD_REQUEST);
   }

}
