package uk.co.zoneofavoidance.my.tasks.rest.controllers;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import uk.co.zoneofavoidance.my.tasks.testing.BaseMockMvcTest;

/**
 * Integration test for REST controllers using {@link MockMvc}.
 */
public abstract class RestControllerIT extends BaseMockMvcTest {

   private static final MediaType APPLICATION_JSON = new MediaType("application", "json");

   /**
    * Perform an HTTP GET request to the given path.
    *
    * @param path the path within the servlet context
    * @return an instance of {@link ResultActions}; never {@code null}
    * @throws Exception if something went wrong
    */
   protected ResultActions get(final String path) throws Exception {
      return mockMvc().perform(MockMvcRequestBuilders.get(path));
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
      return mockMvc().perform(MockMvcRequestBuilders.post(path)
         .contentType(APPLICATION_JSON).content(json));
   }

   /**
    * Perform an HTTP POST request to the given path.
    *
    * @param path the path within the servlet context
    * @return an instance of {@link ResultActions}; never {@code null}
    * @throws Exception if something went wrong
    */
   protected ResultActions post(final String path) throws Exception {
      return mockMvc().perform(MockMvcRequestBuilders.post(path));
   }

}
