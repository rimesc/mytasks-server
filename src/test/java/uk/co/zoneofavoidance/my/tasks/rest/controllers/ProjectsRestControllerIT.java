package uk.co.zoneofavoidance.my.tasks.rest.controllers;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.test.context.jdbc.Sql;

/**
 * Integration tests for {@link ProjectsRestController}.
 */
@Sql(scripts = "/sql/setup_ProjectsRestControllerIT.sql", executionPhase = BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/cleanup.sql", executionPhase = AFTER_TEST_METHOD)
@SuppressWarnings("checkstyle:magicnumber")
public class ProjectsRestControllerIT extends RestControllerIT {

   @Test
   public void getProjectsReturnsAllProjects() throws Exception {
      get("/api/projects/")
         .andExpect(status().isOk())
         .andExpect(jsonPath("projects", hasSize(3)))
         .andExpect(jsonPath("projects[0].id", equalTo(1)))
         .andExpect(jsonPath("projects[0].name", equalTo("First project")))
         .andExpect(jsonPath("projects[0].description", startsWith("This is my first project.")))
         .andExpect(jsonPath("projects[0].numberOfOpenTasks", equalTo(2)))
         .andExpect(jsonPath("projects[0].href", equalTo("/api/projects/1")))
         .andExpect(jsonPath("projects[1].id", equalTo(2)))
         .andExpect(jsonPath("projects[1].name", equalTo("Second project")))
         .andExpect(jsonPath("projects[1].description", startsWith("This is my second project.")))
         .andExpect(jsonPath("projects[1].href", equalTo("/api/projects/2")))
         .andExpect(jsonPath("projects[1].numberOfOpenTasks", equalTo(2)))
         .andExpect(jsonPath("projects[2].id", equalTo(3)))
         .andExpect(jsonPath("projects[2].name", equalTo("Third project")))
         .andExpect(jsonPath("projects[2].description", startsWith("This is my third project.")))
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
         .andExpect(jsonPath("errors", hasSize(2)))
         .andExpect(jsonPath("errors[*].field", containsInAnyOrder("name", "name")))
         .andExpect(jsonPath("errors[*].code", containsInAnyOrder("Length", "NotEmpty")))
         .andExpect(jsonPath("errors[*].message", containsInAnyOrder("length must be between 1 and 255", "may not be empty")));
   }

   @Test
   public void getProjectReturnsRequestedProject() throws Exception {
      get("/api/projects/1")
         .andExpect(status().isOk())
         .andExpect(jsonPath("id", equalTo(1)))
         .andExpect(jsonPath("name", equalTo("First project")))
         .andExpect(jsonPath("description", startsWith("This is my first project.")))
         .andExpect(jsonPath("numberOfOpenTasks", equalTo(2)))
         .andExpect(jsonPath("href", equalTo("/api/projects/1")));
      get("/api/projects/2")
         .andExpect(status().isOk())
         .andExpect(jsonPath("id", equalTo(2)))
         .andExpect(jsonPath("name", equalTo("Second project")))
         .andExpect(jsonPath("description", startsWith("This is my second project.")))
         .andExpect(jsonPath("numberOfOpenTasks", equalTo(2)))
         .andExpect(jsonPath("href", equalTo("/api/projects/2")));
   }

   @Test
   public void getUnknownProjectIsNotFound() throws Exception {
      get("/api/projects/6")
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("code", equalTo("Not Found")))
         .andExpect(jsonPath("message", equalTo("The requested project could not be found.")));
   }

   @Test
   public void getInvalidProjectIsBadRequest() throws Exception {
      get("/api/projects/abc")
         .andExpect(status().isBadRequest())
         .andExpect(jsonPath("code", equalTo("Bad Request")))
         .andExpect(jsonPath("message", equalTo("Invalid project ID: abc")));
   }

   @Test
   public void getProjectReadMeReturnsReadMeIfAvailable() throws Exception {
      get("/api/projects/1/readme")
         .andExpect(status().isOk())
         .andExpect(jsonPath("markdown", startsWith("Lorem ipsum dolor sit amet,")))
         .andExpect(jsonPath("html", startsWith("<p>Lorem ipsum dolor sit amet,")))
         .andExpect(jsonPath("html", endsWith("ut labore et dolore magna aliqua.</p>")));
   }

   @Test
   public void getUnknownProjectReadMeIsNotFound() throws Exception {
      get("/api/projects/6/readme")
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("code", equalTo("Not Found")))
         .andExpect(jsonPath("message", equalTo("The requested project could not be found.")));
   }

   @Test
   public void getProjectReadMeIsNotFoundIfUnavailable() throws Exception {
      get("/api/projects/3/readme")
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("code", equalTo("Not Found")))
         .andExpect(jsonPath("message", equalTo("The requested note could not be found.")));
   }

   @Test
   public void getInvalidProjectReadMeIsBadRequest() throws Exception {
      get("/api/projects/abc/readme")
         .andExpect(status().isBadRequest())
         .andExpect(jsonPath("code", equalTo("Bad Request")))
         .andExpect(jsonPath("message", equalTo("Invalid project ID: abc")));
   }

}
