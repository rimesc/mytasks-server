package uk.co.zoneofavoidance.my.tasks.controllers;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.ResultActions;

/**
 * Integration tests for {@link TasksController}.
 */
@Sql(scripts = "/sql/setup_TasksControllerIT.sql", executionPhase = BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/cleanup.sql", executionPhase = AFTER_TEST_METHOD)
@SuppressWarnings("checkstyle:magicnumber")
public class TasksControllerIT extends ControllerIT {

   @Test
   public void getTasksReturnsTasksForProject() throws Exception {
      get("/api/tasks/?project=1")
         .andExpect(status().isOk())
         .andExpect(content().json("["
            + "  {"
            + "    'id': 1,"
            + "    'summary': 'First task',"
            + "    'priority': 'NORMAL',"
            + "    'state': 'TO_DO',"
            + "    'created': '2016-07-10T00:29:08.000+0000',"
            + "    'updated': '2016-07-10T00:29:08.000+0000',"
            + "    'modified': false,"
            + "    'tags': [ 'First', 'Second' ],"
            + "    'project': {"
            + "      'id': 1,"
            + "      'name': 'First project'"
            + "    },"
            + "    'href': '/api/tasks/1'"
            + "  }"
            + "]", STRICT));
   }

   @Test
   public void getTasksReturnsTasksForProjectWithAnyState() throws Exception {
      final ResultActions result = get("/api/tasks/?project=2")
         .andExpect(status().isOk())
         .andExpect(jsonPath("$", hasSize(4)))
         .andExpect(jsonPath("$[0].id", equalTo(2)))
         .andExpect(jsonPath("$[0].summary", equalTo("Second task")))
         .andExpect(jsonPath("$[1].id", equalTo(3)))
         .andExpect(jsonPath("$[1].summary", equalTo("Third task")))
         .andExpect(jsonPath("$[2].id", equalTo(4)))
         .andExpect(jsonPath("$[2].summary", equalTo("Fourth task")))
         .andExpect(jsonPath("$[3].id", equalTo(5)))
         .andExpect(jsonPath("$[3].summary", equalTo("Fifth task")));
      for (int i = 0; i < 4; i++) {
         result.andExpect(jsonPath("$[" + i + "].project.id", equalTo(2)));
      }
   }

   @Test
   public void getTasksReturnsToDoTasksForProject() throws Exception {
      get("/api/tasks/?project=2&state=TO_DO")
         .andExpect(status().isOk())
         .andExpect(jsonPath("$", hasSize(1)))
         .andExpect(jsonPath("$[0].id", equalTo(2)))
         .andExpect(jsonPath("$[0].summary", equalTo("Second task")))
         .andExpect(jsonPath("$[0].project.id", equalTo(2)));
   }

   @Test
   public void getTasksReturnsInProgressTasksForProject() throws Exception {
      get("/api/tasks/?project=2&state=IN_PROGRESS")
         .andExpect(status().isOk())
         .andExpect(jsonPath("$", hasSize(1)))
         .andExpect(jsonPath("$[0].id", equalTo(3)))
         .andExpect(jsonPath("$[0].summary", equalTo("Third task")))
         .andExpect(jsonPath("$[0].project.id", equalTo(2)));
   }

   @Test
   public void getTasksReturnsOnHoldTasksForProject() throws Exception {
      get("/api/tasks/?project=2&state=ON_HOLD")
         .andExpect(status().isOk())
         .andExpect(jsonPath("$", hasSize(1)))
         .andExpect(jsonPath("$[0].id", equalTo(4)))
         .andExpect(jsonPath("$[0].summary", equalTo("Fourth task")))
         .andExpect(jsonPath("$[0].project.id", equalTo(2)));
   }

   @Test
   public void getTasksReturnsDoneTasksForProject() throws Exception {
      get("/api/tasks/?project=2&state=DONE")
         .andExpect(status().isOk())
         .andExpect(jsonPath("$", hasSize(1)))
         .andExpect(jsonPath("$[0].id", equalTo(5)))
         .andExpect(jsonPath("$[0].summary", equalTo("Fifth task")))
         .andExpect(jsonPath("$[0].project.id", equalTo(2)));
   }

   @Test
   public void getTasksReturnsOpenTasksForProject() throws Exception {
      get("/api/tasks/?project=2&state=TO_DO&state=IN_PROGRESS&state=ON_HOLD")
         .andExpect(status().isOk())
         .andExpect(jsonPath("$", hasSize(3)))
         .andExpect(jsonPath("$[0].id", equalTo(2)))
         .andExpect(jsonPath("$[0].summary", equalTo("Second task")))
         .andExpect(jsonPath("$[0].project.id", equalTo(2)))
         .andExpect(jsonPath("$[1].id", equalTo(3)))
         .andExpect(jsonPath("$[1].summary", equalTo("Third task")))
         .andExpect(jsonPath("$[1].project.id", equalTo(2)))
         .andExpect(jsonPath("$[2].id", equalTo(4)))
         .andExpect(jsonPath("$[2].summary", equalTo("Fourth task")))
         .andExpect(jsonPath("$[2].project.id", equalTo(2)));
   }

   @Test
   @DirtiesContext
   public void postUpdateTaskSavesTaskIfValid() throws Exception {
      final String task = "{'summary': 'My edited task', 'description': 'This is an *edited* task.', 'priority': 'LOW', 'tags': ['First', 'Second']}";
      post("/api/tasks/1", task)
         .andExpect(status().isOk())
         .andExpect(content().json("{"
            + "  'id': 1,"
            + "  'summary': 'My edited task',"
            + "  'priority': 'LOW',"
            + "  'state': 'TO_DO',"
            + "  'created': '2016-07-10T00:29:08.000+0000',"
            + "  'modified': true,"
            + "  'tags': [ 'First', 'Second' ],"
            + "  'notes': {"
            + "    'raw': 'This is an *edited* task.',"
            + "    'html': '<p>This is an <em>edited</em> task.</p>'"
            + "  },"
            + "  'project': {"
            + "    'id': 1,"
            + "    'name': 'First project'"
            + "  },"
            + "  'href': '/api/tasks/1'"
            + "}")); // Not STRICT because the value of 'updated' is unknown
   }

   @Test
   @DirtiesContext
   public void postStateUpdatePerformsStateTransition() throws Exception {
      final String task = "{'state': 'IN_PROGRESS'}";
      post("/api/tasks/1", task)
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.id", equalTo(1)))
         .andExpect(jsonPath("$.state", equalTo("IN_PROGRESS")));
   }

   // @Test
   // public void postNewProjectRaisesErrorForEmptyName() throws Exception {
   // final String project = "{'name': '', 'description': 'This is a new
   // project.'}";
   // post("/api/projects/", project)
   // .andExpect(status().isBadRequest())
   // .andExpect(jsonPath("errors", hasSize(2)))
   // .andExpect(jsonPath("errors[*].field", containsInAnyOrder("name",
   // "name")))
   // .andExpect(jsonPath("errors[*].code", containsInAnyOrder("Length",
   // "NotEmpty")))
   // .andExpect(jsonPath("errors[*].message", containsInAnyOrder("length must
   // be between 1 and 255", "may not be empty")));
   // }
   //
   // @Test
   // public void getProjectReturnsRequestedProject() throws Exception {
   // get("/api/projects/1")
   // .andExpect(status().isOk())
   // .andExpect(jsonPath("id", equalTo(1)))
   // .andExpect(jsonPath("name", equalTo("My first project")))
   // .andExpect(jsonPath("description", startsWith("This is my first sample
   // project.")))
   // .andExpect(jsonPath("numberOfOpenTasks", equalTo(2)))
   // .andExpect(jsonPath("href", equalTo("/api/projects/1")));
   // get("/api/projects/2")
   // .andExpect(status().isOk())
   // .andExpect(jsonPath("id", equalTo(2)))
   // .andExpect(jsonPath("name", equalTo("My second project")))
   // .andExpect(jsonPath("description", startsWith("This is my second sample
   // project.")))
   // .andExpect(jsonPath("numberOfOpenTasks", equalTo(2)))
   // .andExpect(jsonPath("href", equalTo("/api/projects/2")));
   // }
   //
   // @Test
   // public void getUnknownProjectIsNotFound() throws Exception {
   // get("/api/projects/6")
   // .andExpect(status().isNotFound())
   // .andExpect(jsonPath("code", equalTo("Not Found")))
   // .andExpect(jsonPath("message", equalTo("The requested project could not be
   // found.")));
   // }
   //
   // @Test
   // public void getInvalidProjectIsBadRequest() throws Exception {
   // get("/api/projects/abc")
   // .andExpect(status().isBadRequest())
   // .andExpect(jsonPath("code", equalTo("Bad Request")))
   // .andExpect(jsonPath("message", equalTo("Invalid project ID: abc")));
   // }
   //
   // @Test
   // public void getProjectReadMeReturnsReadMeIfAvailable() throws Exception {
   // get("/api/projects/1/readme")
   // .andExpect(status().isOk())
   // .andExpect(jsonPath("markdown", startsWith("# Lorem ipsum")))
   // .andExpect(jsonPath("html", startsWith("<h1>Lorem ipsum</h1>")));
   // }
   //
   // @Test
   // public void getUnknownProjectReadMeIsNotFound() throws Exception {
   // get("/api/projects/6/readme")
   // .andExpect(status().isNotFound())
   // .andExpect(jsonPath("code", equalTo("Not Found")))
   // .andExpect(jsonPath("message", equalTo("The requested project could not be
   // found.")));
   // }
   //
   // @Test
   // public void getProjectReadMeIsNotFoundIfUnavailable() throws Exception {
   // get("/api/projects/2/readme")
   // .andExpect(status().isNotFound())
   // .andExpect(jsonPath("code", equalTo("Not Found")))
   // .andExpect(jsonPath("message", equalTo("The requested note could not be
   // found.")));
   // }
   //
   // @Test
   // public void getInvalidProjectReadMeIsBadRequest() throws Exception {
   // get("/api/projects/abc/readme")
   // .andExpect(status().isBadRequest())
   // .andExpect(jsonPath("code", equalTo("Bad Request")))
   // .andExpect(jsonPath("message", equalTo("Invalid project ID: abc")));
   // }

}
