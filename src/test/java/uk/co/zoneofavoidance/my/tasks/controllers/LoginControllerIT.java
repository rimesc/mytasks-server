package uk.co.zoneofavoidance.my.tasks.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Test;

import uk.co.zoneofavoidance.my.tasks.testing.ModelAndViewControllerIT;

/**
 * Integration tests for {@link LoginController}.
 */
public class LoginControllerIT extends ModelAndViewControllerIT {

   @Test
   public void getLoginReturnsLoginView() throws Exception {
      get("/login")
         .andExpect(status().isOk())
         .andExpect(view().name("login"));
   }

   @Test
   public void getLoginOnLogoutReturnsLoginViewWithSuccess() throws Exception {
      get("/login?logout=")
         .andExpect(status().isOk())
         .andExpect(view().name("login"))
         .andExpect(model().attribute("success", "logout"));
   }

   @Test
   public void getLoginWithErrorReturnsLoginViewWithError() throws Exception {
      get("/login?error=")
         .andExpect(status().isOk())
         .andExpect(view().name("login"))
         .andExpect(model().attribute("error", "error.invalid.credentials"));
   }

}
