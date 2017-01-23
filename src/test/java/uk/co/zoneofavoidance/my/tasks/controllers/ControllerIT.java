package uk.co.zoneofavoidance.my.tasks.controllers;

import static org.apache.commons.lang3.StringUtils.strip;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * Integration test for REST controllers using {@link MockMvc}.
 */
public abstract class ControllerIT extends BaseMockMvcTest {

   /** Strict matching of JSON responses. */
   protected static final boolean STRICT = true;

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
      return post(path, convertQuotes(json), APPLICATION_JSON);
   }

   /**
    * Perform an HTTP POST request to the given path.
    *
    * @param path the path within the servlet context
    * @param content string to send as the request body
    * @param mediaType type of the request body
    * @return an instance of {@link ResultActions}; never {@code null}
    * @throws Exception if something went wrong
    */
   protected ResultActions post(final String path, final String content, final MediaType mediaType) throws Exception {
      return mockMvc().perform(MockMvcRequestBuilders.post(path)
         .contentType(mediaType).content(content));
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

   /**
    * Convert single-quoted pseudo-JSON to valid double-quoted JSON.
    *
    * @param json string containing JSON or pseudo-JSON
    * @return a string containing valid JSON
    */
   protected static String convertQuotes(final String json) {
      switch (json.trim().charAt(0)) {
         case '{':
            return new JSONObject(json).toString();
         case '[':
            return new JSONArray(json).toString();
         case '\'':
            return String.format("\"%s\"", strip(json, "\'"));
         default:
            return json;
      }
   }

}
