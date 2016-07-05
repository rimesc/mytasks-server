package uk.co.zoneofavoidance.my.tasks.rest.controllers;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;

public class TagsRestControllerIT extends RestControllerIT {

   @Test
   public void getTagsReturnsAllUsedTags() throws Exception {
      get("/api/tags/")
         .andExpect(status().isOk())
         .andExpect(jsonPath("tags", hasSize(3)))
         .andExpect(jsonPath("tags[0]", equalTo("Bug")))
         .andExpect(jsonPath("tags[1]", equalTo("Feature")))
         .andExpect(jsonPath("tags[1]", equalTo("Version 1")));
   }

   @Test
   public void postTagsIsNotAllowed() throws Exception {
      post("/api/tags")
         .andExpect(status().isMethodNotAllowed());
   }

}
