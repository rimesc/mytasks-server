package uk.co.zoneofavoidance.my.tasks.controllers;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Test;
import org.springframework.test.context.jdbc.Sql;

import uk.co.zoneofavoidance.my.tasks.testing.ModelAndViewControllerIT;

/**
 * Integration tests for {@link MyTasksController}.
 */
@Sql(scripts = "/sql/setup_ProjectsRestControllerIT.sql", executionPhase = BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/cleanup.sql", executionPhase = AFTER_TEST_METHOD)
public class MyTasksControllerIT extends ModelAndViewControllerIT {

   @Test
   public void getRootReturnsHomeViewWithProjects() throws Exception {
      get("/").andExpect(status().isOk()).andExpect(view().name("home")).andExpect(model().attribute("projects", containsProjects("First project", "Second project", "Third project")));
   }

   @Test
   public void postRootIsNotAllowed() throws Exception {
      post("/").andExpect(status().isMethodNotAllowed());
   }

}
