package uk.co.zoneofavoidance.my.tasks.controllers;

import static java.time.temporal.ChronoUnit.SECONDS;
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
   public void getTaskReturnsRequestedTask() throws Exception {
      get("/api/tasks/1")
         .andExpect(status().isOk())
         .andExpect(content().json("{"
            + "  'id': 1,"
            + "  'summary': 'First task',"
            + "  'priority': 'NORMAL',"
            + "  'state': 'TO_DO',"
            + "  'created': '2016-07-10T00:29:08.000+0000',"
            + "  'tags': [ 'First', 'Second' ],"
            + "  'notes': { 'html': '<p>My first task</p>', 'raw': 'My first task' },"
            + "  'project': { 'id': 1, 'name': 'First project' },"
            + "  'href': '/api/tasks/1'"
            + "}", STRICT));
      get("/api/tasks/2")
         .andExpect(status().isOk())
         .andExpect(content().json("{"
            + "  'id': 2,"
            + "  'summary': 'Second task',"
            + "  'priority': 'LOW',"
            + "  'state': 'TO_DO',"
            + "  'created': '2016-07-10T00:29:08.000+0000',"
            + "  'tags': [ ],"
            + "  'notes': { 'html': '<p>My second task</p>', 'raw': 'My second task' },"
            + "  'project': { 'id': 2, 'name': 'Second project' },"
            + "  'href': '/api/tasks/2'"
            + "}", STRICT));
   }

   @Test
   public void getUnknownTaskIsNotFound() throws Exception {
      get("/api/tasks/8")
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.code", equalTo("Not Found")))
         .andExpect(jsonPath("$.message", equalTo("The requested task could not be found.")));
   }

   @Test
   public void getInvalidTaskIsBadRequest() throws Exception {
      get("/api/tasks/abc")
         .andExpect(status().isBadRequest())
         .andExpect(jsonPath("$.code", equalTo("Bad Request")))
         .andExpect(jsonPath("$.message", equalTo("Invalid task ID: abc")));
   }

   @Test
   public void getTaskNotesReturnsNotesForRequestedTask() throws Exception {
      get("/api/tasks/1/notes")
         .andExpect(status().isOk())
         .andExpect(content().json("{"
            + "  'raw': 'My first task',"
            + "  'html': '<p>My first task</p>',"
            + "  'href': '/api/tasks/1/notes'"
            + "}", STRICT));
   }

   @Test
   public void getUnknownTaskNotesIsNotFound() throws Exception {
      get("/api/tasks/8/notes")
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.code", equalTo("Not Found")))
         .andExpect(jsonPath("$.message", equalTo("The requested task could not be found.")));
   }

   @Test
   public void getInvalidTaskNotesIsBadRequest() throws Exception {
      get("/api/tasks/abc/notes")
         .andExpect(status().isBadRequest())
         .andExpect(jsonPath("$.code", equalTo("Bad Request")))
         .andExpect(jsonPath("$.message", equalTo("Invalid task ID: abc")));
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
            + "}")) // Not STRICT because the value of 'updated' is unknown
         .andExpect(jsonPath("$.updated", dateWithin(1, SECONDS, new Date())));
   }

   @Test
   @DirtiesContext
   public void postStateUpdatePerformsStateTransition() throws Exception {
      final String task = "{'state': 'IN_PROGRESS'}";
      post("/api/tasks/1", task)
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.id", equalTo(1)))
         .andExpect(jsonPath("$.state", equalTo("IN_PROGRESS")))
         .andExpect(jsonPath("$.updated", dateWithin(1, SECONDS, new Date())));
   }

}
