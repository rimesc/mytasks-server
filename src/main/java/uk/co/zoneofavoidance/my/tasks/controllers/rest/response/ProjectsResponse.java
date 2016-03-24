package uk.co.zoneofavoidance.my.tasks.controllers.rest.response;

import java.util.List;

public class ProjectsResponse {

   private final List<ProjectSummaryResponse> projects;

   public ProjectsResponse(final List<ProjectSummaryResponse> projects) {
      this.projects = projects;
   }

   public List<ProjectSummaryResponse> getProjects() {
      return projects;
   }

}
