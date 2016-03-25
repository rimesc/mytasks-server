package uk.co.zoneofavoidance.my.tasks.controllers.rest.response;

import java.util.List;

public class ProjectsResponse {

   private final List<ProjectResponse.Summary> projects;

   public ProjectsResponse(final List<ProjectResponse.Summary> projects) {
      this.projects = projects;
   }

   public List<ProjectResponse.Summary> getProjects() {
      return projects;
   }

}
