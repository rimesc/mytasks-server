package uk.co.zoneofavoidance.my.tasks.controllers;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.co.zoneofavoidance.my.tasks.controllers.JsonStringMatchers.dateWithin;

import java.util.Date;

import org.junit.Test;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

/**
 * Integration tests for {@link ProjectsController}.
 */
@Sql(scripts = "/sql/setup_ProjectsControllerIT.sql", executionPhase = BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/cleanup.sql", executionPhase = AFTER_TEST_METHOD)
@SuppressWarnings("checkstyle:magicnumber")
public class ProjectsControllerIT extends ControllerIT {

   @Test
   public void getProjectsReturnsAllProjects() throws Exception {
      get("/api/projects/")
         .andExpect(status().isOk())
         .andExpect(content().json("["
            + "  {"
            + "    'id': 1,"
            + "    'name': 'First project',"
            + "    'description': 'This is my first project.',"
            + "    'tasks': { 'total': 3, 'open': 2, 'closed': 1 },"
            + "    'href': '/api/projects/1'"
            + "  },"
            + "  {"
            + "    'id': 2,"
            + "    'name': 'Second project',"
            + "    'description': 'This is my second project.',"
            + "    'tasks': { 'total': 2, 'open': 2, 'closed': 0 },"
            + "    'href': '/api/projects/2'"
            + "  },"
            + "  {"
            + "    'id': 3,"
            + "    'name': 'Third project',"
            + "    'description': 'This is my third project.',"
            + "    'tasks': { 'total': 0, 'open': 0, 'closed': 0 },"
            + "    'href': '/api/projects/3'"
            + "  }"
            + "]", STRICT));
   }

   @Test
   public void postNewProjectSavesProjectIfValid() throws Exception {
      final String project = "{'name': 'My new project', 'description': 'This is a new project.'}";
      post("/api/projects/", project)
         .andExpect(status().isCreated())
         .andExpect(content().json("{"
            + "  'id': 4,"
            + "  'name': 'My new project',"
            + "  'description': 'This is a new project.',"
            + "  'notes': { 'html': '' , 'raw': '' },"
            + "  'tasks': { 'total': 0, 'open': 0, 'closed': 0 },"
            + "  'href': '/api/projects/4'"
            + "}", STRICT));
   }

   @Test
   public void postNewProjectRaisesErrorForEmptyName() throws Exception {
      final String project = "{'name': '', 'description': 'This is a new project.'}";
      post("/api/projects/", project)
         .andExpect(status().isBadRequest())
         .andExpect(jsonPath("$.errors", hasSize(2)))
         .andExpect(jsonPath("$.errors[*].field", containsInAnyOrder("name", "name")))
         .andExpect(jsonPath("$.errors[*].code", containsInAnyOrder("Length", "NotEmpty")))
         .andExpect(jsonPath("$.errors[*].message", containsInAnyOrder("length must be between 1 and 255", "may not be empty")));
   }

   @Test
   public void getProjectReturnsRequestedProject() throws Exception {
      get("/api/projects/1")
         .andExpect(status().isOk())
         .andExpect(content().json("{"
            + "  'id': 1,"
            + "  'name': 'First project',"
            + "  'description': 'This is my first project.',"
            + "  'notes': {"
            + "    'html': '<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit.</p>',"
            + "    'raw': 'Lorem ipsum dolor sit amet, consectetur adipiscing elit.'"
            + "  },"
            + "  'tasks': { 'total': 3, 'open': 2, 'closed': 1 },"
            + "  'href': '/api/projects/1'"
            + "}", STRICT));
      get("/api/projects/2")
         .andExpect(status().isOk())
         .andExpect(content().json("{"
            + "  'id': 2,"
            + "  'name': 'Second project',"
            + "  'description': 'This is my second project.',"
            + "  'notes': {"
            + "    'html': '<p>Ut enim ad minim veniam, quis nostrud exercitation.</p>',"
            + "    'raw': 'Ut enim ad minim veniam, quis nostrud exercitation.'"
            + "  },"
            + "  'tasks': { 'total': 2, 'open': 2, 'closed': 0 },"
            + "  'href': '/api/projects/2'"
            + "}", STRICT));
   }

   @Test
   public void getUnknownProjectIsNotFound() throws Exception {
      get("/api/projects/6")
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.code", equalTo("Not Found")))
         .andExpect(jsonPath("$.message", equalTo("The requested project could not be found.")));
   }

   @Test
   public void getInvalidProjectIsBadRequest() throws Exception {
      get("/api/projects/abc")
         .andExpect(status().isBadRequest())
         .andExpect(jsonPath("$.code", equalTo("Bad Request")))
         .andExpect(jsonPath("$.message", equalTo("Invalid project ID: abc")));
   }

   @Test
   public void getProjectNotesReturnsNotesIfAvailable() throws Exception {
      get("/api/projects/1/notes")
         .andExpect(status().isOk())
         .andExpect(content().json("{"
            + "  'raw': 'Lorem ipsum dolor sit amet, consectetur adipiscing elit.',"
            + "  'html': '<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit.</p>',"
            + "  'href': '/api/projects/1/notes'"
            + "}", STRICT));
   }

   @Test
   public void getUnknownProjectNotesIsNotFound() throws Exception {
      get("/api/projects/6/notes")
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.code", equalTo("Not Found")))
         .andExpect(jsonPath("$.message", equalTo("The requested project could not be found.")));
   }

   @Test
   public void getProjectNotesIsNotFoundIfUnavailable() throws Exception {
      get("/api/projects/3/notes")
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.code", equalTo("Not Found")))
         .andExpect(jsonPath("$.message", equalTo("The requested note could not be found.")));
   }

   @Test
   public void getInvalidProjectNotesIsBadRequest() throws Exception {
      get("/api/projects/abc/notes")
         .andExpect(status().isBadRequest())
         .andExpect(jsonPath("$.code", equalTo("Bad Request")))
         .andExpect(jsonPath("$.message", equalTo("Invalid project ID: abc")));
   }

   @Test
   @DirtiesContext
   public void postNewTaskSavesTaskIfValid() throws Exception {
      final String task = "{'summary': 'My new task', 'priority': 'NORMAL', 'tags': ['First', 'Second']}";
      post("/api/projects/1/tasks/", task)
         .andExpect(status().isCreated())
         .andExpect(content().json("{"
            + "  'id': 6,"
            + "  'summary': 'My new task',"
            + "  'priority': 'NORMAL',"
            + "  'state': 'TO_DO',"
            + "  'tags': [ 'First', 'Second' ],"
            + "  'notes': { 'raw': '', 'html': '' },"
            + "  'project': { 'id': 1, 'name': 'First project' },"
            + "  'href': '/api/tasks/6'"
            + "}")) // Not STRICT because the value of 'created' is unknown
         .andExpect(jsonPath("$.created", dateWithin(1, SECONDS, new Date())));
   }

}
