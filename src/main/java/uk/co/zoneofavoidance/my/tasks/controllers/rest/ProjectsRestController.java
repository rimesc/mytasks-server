package uk.co.zoneofavoidance.my.tasks.controllers.rest;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uk.co.zoneofavoidance.my.tasks.controllers.ProjectForm;
import uk.co.zoneofavoidance.my.tasks.controllers.rest.response.BindingErrorsResponse;
import uk.co.zoneofavoidance.my.tasks.controllers.rest.response.ProjectSummaryResponse;
import uk.co.zoneofavoidance.my.tasks.controllers.rest.response.ProjectsResponse;
import uk.co.zoneofavoidance.my.tasks.domain.Project;
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
      return new ProjectsResponse(projects.findAll().stream().map(this::convert).collect(toList()));
   }

   @RequestMapping(method = POST, consumes = "application/json", produces = "application/json")
   public ProjectSummaryResponse postProjects(@Valid @RequestBody final ProjectForm form) {
      final Project project = projects.save(Project.create(form.getName(), form.getDescription()));
      return convert(project);
   }

   private ProjectSummaryResponse convert(final Project project) {
      return new ProjectSummaryResponse(project.getId(), project.getName(), project.getDescription(), project.getNumberOfOpenTasks(), "/api/projects/" + project.getId());
   }

   @ExceptionHandler(MethodArgumentNotValidException.class)
   public ResponseEntity<BindingErrorsResponse> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex) {
      return new ResponseEntity<>(BindingErrorsResponse.create(ex.getBindingResult()), BAD_REQUEST);
   }

}
