package uk.co.zoneofavoidance.my.tasks.controllers.rest;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.net.URI;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import uk.co.zoneofavoidance.my.tasks.MyTasksApplication;

/**
 * Integration test for REST controllers using {@link MockMvc}.
 */
@WebIntegrationTest
@SpringApplicationConfiguration
@ActiveProfiles("dev")
@RunWith(SpringJUnit4ClassRunner.class)
public abstract class RestControllerIT {

   private static final MediaType APPLICATION_JSON = new MediaType("application", "json");

   @Autowired
   private WebApplicationContext webAppContext;

   private MockMvc mockMvc;

   @Before
   public void setUp() throws Exception {
      mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
   }

   /**
    * Perform an HTTP GET request to the given path.
    *
    * @param path the path within the servlet context
    * @return an instance of {@link ResultActions}; never {@code null}
    * @throws Exception if something went wrong
    */
   protected ResultActions get(final String path) throws Exception {
      return mockMvc.perform(MockMvcRequestBuilders.get(new URI(path)).with(csrf()));
   }

   /**
    * Perform an HTTP POST request to the given path.
    *
    * @param path the path within the servlet context
    * @param json string of JSON to send as the request body
    * @return an instance of {@link ResultActions}; never {@code null}
    * @throws Exception if something went wrong
    */
   protected ResultActions post(final String path, final String json) throws Exception {
      return mockMvc.perform(MockMvcRequestBuilders.post("/api/projects/").with(csrf())
         .contentType(APPLICATION_JSON).content(json));
   }

   /**
    * Hook from which to hang an import of the main configuration.
    */
   @Configuration
   @Import(MyTasksApplication.class)
   public static class Config {
   }

}
