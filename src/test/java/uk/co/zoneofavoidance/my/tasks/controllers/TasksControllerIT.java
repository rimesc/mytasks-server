package uk.co.zoneofavoidance.my.tasks.controllers;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static uk.co.zoneofavoidance.my.tasks.domain.Priority.CRITICAL;
import static uk.co.zoneofavoidance.my.tasks.domain.Priority.HIGH;
import static uk.co.zoneofavoidance.my.tasks.domain.Priority.LOW;
import static uk.co.zoneofavoidance.my.tasks.domain.Priority.NORMAL;
import static uk.co.zoneofavoidance.my.tasks.domain.State.DONE;

import org.junit.Test;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.google.common.base.Strings;

import static org.junit.Assert.assertThat;

import uk.co.zoneofavoidance.my.tasks.domain.Priority;
import uk.co.zoneofavoidance.my.tasks.testing.MockMvcTest;

/**
 * Integration tests for {@link TasksController}.
 */
public class TasksControllerIT extends MockMvcTest {

   @Test
   public void getNewReturnsNewTaskViewWithEmptyFormAndOnCancel() throws Exception {
      get("/tasks/new?project=1")
         .andExpect(status().isOk())
         .andExpect(view().name("tasks/new"))
         .andExpect(model().attribute("taskForm", hasProperty("project", equalTo(1L))))
         .andExpect(model().attribute("taskForm", hasProperty("summary", nullValue())))
         .andExpect(model().attribute("taskForm", hasProperty("description", nullValue())))
         .andExpect(model().attribute("taskForm", hasProperty("priority", nullValue())));
   }

   @Test
   public void getNewWithMissingProjectIsBadRequest() throws Exception {
      get("/tasks/new").andExpect(status().isBadRequest());
   }

   @Test
   public void postNewWithEmptySummaryReturnsNewTaskViewWithErrors() throws Exception {
      post("/tasks/new", new TaskForm(1L, "", "", NORMAL))
         .andExpect(status().isOk())
         .andExpect(view().name("tasks/new"))
         .andExpect(model().attribute("taskForm", hasProperty("summary", equalTo(""))))
         .andExpect(model().attributeHasFieldErrorCode("taskForm", "summary", "Length"));
   }

   @Test
   public void postNewWithOverLongSummaryReturnsNewTaskViewWithErrors() throws Exception {
      final String longSummary = Strings.repeat("!", 256);
      post("/tasks/new", new TaskForm(1L, longSummary, "", NORMAL))
         .andExpect(status().isOk()).andExpect(view().name("tasks/new"))
         .andExpect(model().attribute("taskForm", hasProperty("summary", equalTo(longSummary))))
         .andExpect(model().attributeHasFieldErrorCode("taskForm", "summary", "Length"));
   }

   @Test
   @DirtiesContext
   public void postNewWithValidFormCreatesTaskAndRedirects() throws Exception {
      final String taskSummary = "A new task";
      final String taskDescription = "This is a new task.";
      post("/tasks/new", new TaskForm(2L, taskSummary, taskDescription, HIGH))
         .andExpect(status().isFound()).andDo(result -> {
            // follow the redirect
            final String view = result.getModelAndView().getViewName();
            get(toRedirectPath(view))
               .andExpect(status().isOk())
               .andExpect(view().name("tasks/view"))
               .andExpect(model().attribute("task", hasProperty("project", hasProperty("id", equalTo(2L)))))
               .andExpect(model().attribute("task", hasProperty("summary", equalTo(taskSummary))))
               .andExpect(model().attribute("task", hasProperty("description", equalTo(taskDescription))))
               .andExpect(model().attribute("task", hasProperty("priority", equalTo(HIGH))));
      });
   }

   @Test
   public void getTaskReturnsViewTaskViewWithTask() throws Exception {
      get("/tasks/1")
         .andExpect(status().isOk())
         .andExpect(view().name("tasks/view"))
         .andExpect(model().attribute("task", hasProperty("summary", equalTo("First sample task"))));
      get("/tasks/2")
         .andExpect(status().isOk())
         .andExpect(view().name("tasks/view"))
         .andExpect(model().attribute("task", hasProperty("summary", equalTo("Second sample task"))));
      get("/tasks/3")
         .andExpect(status().isOk())
         .andExpect(view().name("tasks/view"))
         .andExpect(model().attribute("task", hasProperty("summary", equalTo("Third sample task"))));
   }

   @Test
   public void getUnknownTaskIsNotFound() throws Exception {
      get("/projects/7").andExpect(status().isNotFound());
   }

   @Test
   public void getInvalidTaskIsBadRequest() throws Exception {
      get("/tasks/abc").andExpect(status().isBadRequest());
   }

   @Test
   public void postTaskWithMissingStateIsBadRequest() throws Exception {
      post("/tasks/1").andExpect(status().isBadRequest());
   }

   @Test
   @DirtiesContext
   public void postTaskUpdatesTaskStateAndReturnsViewTaskView() throws Exception {
      post("/tasks/1", "state", DONE.toString())
         .andExpect(status().isOk())
         .andExpect(view().name("tasks/view"))
         .andExpect(model().attribute("task", hasProperty("state", equalTo(DONE))));
   }

   @Test
   public void getEditReturnsEditTaskViewWithTaskFormAndPrioritiesAndOnCancel() throws Exception {
      get("/tasks/edit/2")
         .andExpect(status().isOk())
         .andExpect(view().name("tasks/edit"))
         .andExpect(model().attribute("taskForm", hasProperty("project", equalTo(1L))))
         .andExpect(model().attribute("taskForm", hasProperty("summary", equalTo("Second sample task"))))
         .andExpect(model().attribute("taskForm", hasProperty("description", equalTo("This is the second sample task."))))
         .andExpect(model().attribute("taskForm", hasProperty("priority", equalTo(CRITICAL))))
         .andExpect(model().attribute("priorities", Priority.values()))
         .andExpect(model().attribute("onCancel", "/tasks/2"));
   }

   @Test
   public void getEditUnknownTaskIsNotFound() throws Exception {
      get("/tasks/edit/11").andExpect(status().isNotFound());
   }

   @Test
   public void getEditInvalidTaskIsBadRequest() throws Exception {
      get("/tasks/edit/abc").andExpect(status().isBadRequest());
   }

   @Test
   public void postEditWithEmptySummaryReturnsEditTaskViewWithErrors() throws Exception {
      post("/tasks/edit/3", new TaskForm(1L, "", "This is the third sample task.", LOW))
         .andExpect(status().isOk())
         .andExpect(view().name("tasks/edit"))
         .andExpect(model().attribute("taskForm", hasProperty("summary", equalTo(""))))
         .andExpect(model().attributeHasFieldErrorCode("taskForm", "summary", "Length"));
   }

   @Test
   @DirtiesContext
   public void postEditWithValidTaskFormUpdatesTaskAndRedirects() throws Exception {
      final String updatedSummary = "My updated task";
      final String updatedDescription = "This is my updated task.";
      final Priority updatedPriority = HIGH;
      post("/tasks/edit/3", new TaskForm(1L, updatedSummary, updatedDescription, updatedPriority))
         .andExpect(status().isFound()).andDo(result -> {
            // follow the redirect
            final String view = result.getModelAndView().getViewName();
            final String redirect = toRedirectPath(view);
            assertThat(redirect, equalTo("/tasks/3"));
            get(redirect)
               .andExpect(status().isOk())
               .andExpect(model().attribute("task", hasProperty("summary", equalTo(updatedSummary))))
               .andExpect(model().attribute("task", hasProperty("description", equalTo(updatedDescription))))
               .andExpect(model().attribute("task", hasProperty("priority", equalTo(updatedPriority)))
            );
         });
   }

   @Test
   public void postEditUnknownTaskIsNotFound() throws Exception {
      post("/tasks/edit/7").andExpect(status().isNotFound());
   }

   @Test
   public void postEditInvalidTaskIsBadRequest() throws Exception {
      post("/tasks/edit/abc").andExpect(status().isBadRequest());
   }

   @Test
   public void testGetDeleteReturnsDeleteTaskViewWithTask() throws Exception {
      get("/tasks/delete/1")
         .andExpect(status().isOk())
         .andExpect(view().name("tasks/delete"))
         .andExpect(model().attribute("task", hasProperty("summary", equalTo("First sample task"))));
   }

   @Test
   public void getDeleteUnknownTaskIsNotFound() throws Exception {
      get("/tasks/delete/7").andExpect(status().isNotFound());
   }

   @Test
   public void getDeleteInvalidTaskIsBadRequest() throws Exception {
      get("/tasks/delete/abc").andExpect(status().isBadRequest());
   }

   @Test
   @DirtiesContext
   public void postDeleteRemovesTaskAndRedirectsToProject() throws Exception {
      post("/tasks/delete/3")
         .andExpect(status().isFound())
         .andExpect(view().name(isRedirect("/projects/1")));
      get("/tasks/3").andExpect(status().isNotFound());
   }

   @Test
   public void postDeleteUnknownTaskIsNotFound() throws Exception {
      post("/tasks/delete/7").andExpect(status().isNotFound());
   }

   @Test
   public void postDeleteInvalidTaskIsBadRequest() throws Exception {
      post("/tasks/delete/abc").andExpect(status().isBadRequest());
   }

   private ResultActions post(final String path, final TaskForm form) throws Exception {
      return mockMvc().perform(MockMvcRequestBuilders.post(path).with(csrf())
         .param("project", form.getProject().toString())
         .param("summary", form.getSummary())
         .param("description", form.getDescription())
         .param("priority", form.getPriority().toString()));
   }

   private ResultActions post(final String path, final String param, final String value) throws Exception {
      return mockMvc().perform(MockMvcRequestBuilders.post(path).with(csrf()).param(param, value));
   }

}
