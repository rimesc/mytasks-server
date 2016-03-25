package uk.co.zoneofavoidance.my.tasks.controllers.rest.response;

import java.util.List;

public class ProjectsResponse {

   private final List<ProjectResponse> projects;

   public ProjectsResponse(final List<ProjectResponse> projects) {
      this.projects = projects;
   }

   public List<ProjectResponse> getProjects() {
      return projects;
   }

}
