package uk.co.zoneofavoidance.my.tasks.controllers.rest;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;

public class ProjectsRestControllerIT extends RestControllerIT {

   @Test
   public void getProjectsReturnsAllProjects() throws Exception {
      get("/api/projects/")
         .andExpect(status().isOk())
         .andExpect(jsonPath("projects", hasSize(3)))
         .andExpect(jsonPath("projects[0].id", equalTo(1)))
         .andExpect(jsonPath("projects[0].name", equalTo("My first project")))
         .andExpect(jsonPath("projects[0].description", startsWith("This is my first sample project.")))
         .andExpect(jsonPath("projects[0].numberOfOpenTasks", equalTo(2)))
         .andExpect(jsonPath("projects[0].href", equalTo("/api/projects/1")))
         .andExpect(jsonPath("projects[1].id", equalTo(2)))
         .andExpect(jsonPath("projects[1].name", equalTo("My second project")))
         .andExpect(jsonPath("projects[1].description", startsWith("This is my second sample project.")))
         .andExpect(jsonPath("projects[1].href", equalTo("/api/projects/2")))
         .andExpect(jsonPath("projects[1].numberOfOpenTasks", equalTo(2)))
         .andExpect(jsonPath("projects[2].id", equalTo(3)))
         .andExpect(jsonPath("projects[2].name", equalTo("My third project")))
         .andExpect(jsonPath("projects[2].description", startsWith("This is my third sample project.")))
         .andExpect(jsonPath("projects[2].href", equalTo("/api/projects/3")))
         .andExpect(jsonPath("projects[2].numberOfOpenTasks", equalTo(0)));
   }

   @Test
   public void postNewProjectSavesProjectIfValid() throws Exception {
      final String project = "{\"name\": \"My new project\", \"description\": \"This is a new project.\"}";
      post("/api/projects/", project)
         .andExpect(status().isAccepted())
         .andExpect(jsonPath("id", equalTo(4)))
         .andExpect(jsonPath("name", equalTo("My new project")))
         .andExpect(jsonPath("description", equalTo("This is a new project.")))
         .andExpect(jsonPath("numberOfOpenTasks", equalTo(0)))
         .andExpect(jsonPath("href", equalTo("/api/projects/4")));
   }

   @Test
   public void postNewProjectRaisesErrorForEmptyName() throws Exception {
      final String project = "{\"name\": \"\", \"description\": \"This is a new project.\"}";
      post("/api/projects/", project)
         .andExpect(status().isBadRequest())
         .andExpect(jsonPath("errors", hasSize(1)))
         .andExpect(jsonPath("errors[0].field", equalTo("name")))
         .andExpect(jsonPath("errors[0].code", equalTo("Length")))
         .andExpect(jsonPath("errors[0].message", equalTo("length must be between 1 and 255")));
   }

}
