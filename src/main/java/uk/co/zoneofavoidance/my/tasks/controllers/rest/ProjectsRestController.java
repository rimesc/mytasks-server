package uk.co.zoneofavoidance.my.tasks.controllers.rest;

import static java.util.stream.Collectors.toList;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

   private ProjectSummaryResponse convert(final Project project) {
      return new ProjectSummaryResponse(project.getId(), project.getName(), project.getDescription(), project.getNumberOfOpenTasks(), "/api/projects/" + project.getId());
   }

}
