package uk.co.zoneofavoidance.my.tasks.response;

import java.util.List;

/**
 * REST response containing a list of projects.
 */
public class ProjectsResponse {

   private final List<ProjectResponse> projects;

   /**
    * @param projects list of individual project responses
    */
   public ProjectsResponse(final List<ProjectResponse> projects) {
      this.projects = projects;
   }

   /**
    * @return the list of individual project responses
    */
   public List<ProjectResponse> getProjects() {
      return projects;
   }

}
