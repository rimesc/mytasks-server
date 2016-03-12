package uk.co.zoneofavoidance.my.tasks.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Test;

import uk.co.zoneofavoidance.my.tasks.testing.MockMvcTest;

/**
 * Integration tests for {@link MyTasksController}.
 */
public class MyTasksControllerIT extends MockMvcTest {

   @Test
   public void getRootReturnsHomeViewWithProjects() throws Exception {
      get("/").andExpect(status().isOk()).andExpect(view().name("home")).andExpect(model().attribute("projects", containsProjects("My first project", "My second project", "My third project")));
   }

   @Test
   public void postRootIsNotAllowed() throws Exception {
      post("/").andExpect(status().isMethodNotAllowed());
   }

}
