package uk.co.zoneofavoidance.my.tasks.rest.controllers;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.test.context.jdbc.Sql;

/**
 * Integration tests for {@link TagsRestController}.
 */
@Sql(scripts = "/sql/setup_TagsRestControllerIT.sql", executionPhase = BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/cleanup.sql", executionPhase = AFTER_TEST_METHOD)
@SuppressWarnings("checkstyle:magicnumber")
public class TagsRestControllerIT extends RestControllerIT {

   @Test
   public void getTagsReturnsAllUsedTags() throws Exception {
      get("/api/tags/")
         .andExpect(status().isOk())
         .andExpect(jsonPath("tags", hasSize(3)))
         .andExpect(jsonPath("tags[0]", equalTo("First")))
         .andExpect(jsonPath("tags[1]", equalTo("Second")))
         .andExpect(jsonPath("tags[2]", equalTo("Third")));
   }

   @Test
   public void postTagsIsNotAllowed() throws Exception {
      post("/api/tags")
         .andExpect(status().isMethodNotAllowed());
   }

}
