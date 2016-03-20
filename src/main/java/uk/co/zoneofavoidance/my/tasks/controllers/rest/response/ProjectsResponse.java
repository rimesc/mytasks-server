package uk.co.zoneofavoidance.my.tasks.controllers.rest.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProjectsResponse {

   @JsonProperty
   private final List<ProjectSummaryResponse> projects;

   public ProjectsResponse(final List<ProjectSummaryResponse> projects) {
      this.projects = projects;
   }

   public List<ProjectSummaryResponse> getProjects() {
      return projects;
   }

}
