package uk.co.zoneofavoidance.my.tasks.controllers;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static uk.co.zoneofavoidance.my.tasks.controllers.TaskSelection.ALL;
import static uk.co.zoneofavoidance.my.tasks.controllers.TaskSelection.CLOSED;
import static uk.co.zoneofavoidance.my.tasks.controllers.TaskSelection.OPEN;

import org.junit.Test;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.assertThat;

import uk.co.zoneofavoidance.my.tasks.testing.MockMvcTest;

/**
 * Integration tests for {@link ProjectsController}.
 */
public class ProjectsControllerIT extends MockMvcTest {

   @Test
   public void getProjectsReturnsProjectListViewWithProjects() throws Exception {
      get("/projects")
         .andExpect(status().isOk())
         .andExpect(view().name("projects/list"))
         .andExpect(model().attribute("projects", containsProjects("My first project", "My second project", "My third project")));
   }

   @Test
   public void postProjectsNotAllowed() throws Exception {
      post("/projects").andExpect(status().isMethodNotAllowed());
   }

   @Test
   public void getProjectReturnsProjectViewViewWithProject() throws Exception {
      get("/projects/1")
         .andExpect(status().isOk())
         .andExpect(view().name("projects/view"))
         .andExpect(model().attribute("project", named("My first project")));
      get("/projects/2")
         .andExpect(status().isOk())
         .andExpect(view().name("projects/view"))
         .andExpect(model().attribute("project", named("My second project")));
      get("/projects/3")
         .andExpect(status().isOk())
         .andExpect(view().name("projects/view"))
         .andExpect(model().attribute("project", named("My third project")));
   }

   @Test
   public void getUnknownProjectIsNotFound() throws Exception {
      get("/projects/4").andExpect(status().isNotFound());
   }

   @Test
   public void getInvalidProjectIsBadRequest() throws Exception {
      get("/projects/abc").andExpect(status().isBadRequest());
   }

   @Test
   public void postProjectIsNotAllowed() throws Exception {
      post("/projects/1").andExpect(status().isMethodNotAllowed());
   }

   @Test
   public void getNewReturnsNewProjectViewWithEmptyFormAndOnCancel() throws Exception {
      get("/projects/new")
         .andExpect(status().isOk())
         .andExpect(view().name("projects/new"))
         .andExpect(model().attribute("projectForm", named(null)))
         .andExpect(model().attribute("onCancel", "/projects"));
   }

   @Test
   public void postNewWithEmptyNameReturnsNewProjectViewWithErrors() throws Exception {
      post("/projects/new", new ProjectForm("", ""))
         .andExpect(status().isOk())
         .andExpect(view().name("projects/new"))
         .andExpect(model().attribute("projectForm", named("")))
         .andExpect(model().attributeHasFieldErrorCode("projectForm", "name", "Length"));
   }

   @Test
   @DirtiesContext
   public void postNewWithValidNameAndDescriptionCreatesProjectAndRedirects() throws Exception {
      final String projectName = "My new project";
      final String projectDescription = "This is my new project.";
      post("/projects/new", new ProjectForm(projectName, projectDescription))
         .andExpect(status().isFound())
         .andDo(result -> {
            // follow the redirect
            final String view = result.getModelAndView().getViewName();
            get(toRedirectPath(view))
               .andExpect(status().isOk())
               .andExpect(model().attribute("project", named(projectName)))
               .andExpect(model().attribute("project", hasProperty("description", equalTo(projectDescription))));
         });
   }

   @Test
   public void getEditReturnsEditProjectViewWithProjectFormAndOnCancel() throws Exception {
      get("/projects/edit/1")
         .andExpect(status().isOk())
         .andExpect(view().name("projects/edit"))
         .andExpect(model().attribute("projectForm", named("My first project")))
         .andExpect(model().attribute("projectForm", hasProperty("description", startsWith("This is my first sample project."))))
         .andExpect(model().attribute("onCancel", "/projects/1"));
   }

   @Test
   public void getEditUnknownProjectIsNotFound() throws Exception {
      get("/projects/edit/7").andExpect(status().isNotFound());
   }

   @Test
   public void getEditInvalidProjectIsBadRequest() throws Exception {
      get("/projects/edit/abc").andExpect(status().isBadRequest());
   }

   @Test
   public void postEditWithEmptyNameReturnsEditProjectViewWithErrors() throws Exception {
      post("/projects/edit/2", new ProjectForm("", ""))
         .andExpect(status().isOk())
         .andExpect(view().name("projects/edit"))
         .andExpect(model().attribute("projectForm", named("")))
         .andExpect(model().attributeHasFieldErrorCode("projectForm", "name", "Length"));
   }

   @Test
   @DirtiesContext
   public void postEditWithValidNameAndDescriptionUpdatesProjectAndRedirects() throws Exception {
      final String updatedName = "My updated project";
      final String updatedDescription = "This is my updated project.";
      post("/projects/edit/3", new ProjectForm(updatedName, updatedDescription))
         .andExpect(status().isFound())
         .andDo(result -> {
            // follow the redirect
            final String view = result.getModelAndView().getViewName();
            final String redirect = toRedirectPath(view);
            assertThat(redirect, equalTo("/projects/3"));
            get(redirect)
               .andExpect(status().isOk())
               .andExpect(model().attribute("project", named(updatedName)))
               .andExpect(model().attribute("project", hasProperty("description", equalTo(updatedDescription))));
         });
   }

   @Test
   public void postEditUnknownProjectIsNotFound() throws Exception {
      post("/projects/edit/7").andExpect(status().isNotFound());
   }

   @Test
   public void postEditInvalidProjectIsBadRequest() throws Exception {
      post("/projects/edit/abc").andExpect(status().isBadRequest());
   }

   @Test
   public void testGetDeleteReturnsDeleteProjectViewWithProject() throws Exception {
      get("/projects/delete/1")
         .andExpect(status().isOk())
         .andExpect(view().name("projects/delete"))
         .andExpect(model().attribute("project", named("My first project")));
   }

   @Test
   public void getDeleteUnknownProjectIsNotFound() throws Exception {
      get("/projects/delete/7").andExpect(status().isNotFound());
   }

   @Test
   public void getDeleteInvalidProjectIsBadRequest() throws Exception {
      get("/projects/delete/abc").andExpect(status().isBadRequest());
   }

   @Test
   @DirtiesContext
   public void postDeleteRemovesProjectAndRedirectsToProjectList() throws Exception {
      post("/projects/delete/3")
         .andExpect(status().isFound())
         .andExpect(view().name(isRedirect("/projects")));
      get("/projects/3").andExpect(status().isNotFound());
   }

   @Test
   public void postDeleteUnknownProjectIsNotFound() throws Exception {
      post("/projects/delete/7").andExpect(status().isNotFound());
   }

   @Test
   public void postDeleteInvalidProjectIsBadRequest() throws Exception {
      post("/projects/delete/abc").andExpect(status().isBadRequest());
   }

   @Test
   public void getTasksReturnsProjectTasksViewWithProjectAndOpenTasks() throws Exception {
      get("/projects/1/tasks")
         .andExpect(status().isOk())
         .andExpect(view().name("projects/tasks"))
         .andExpect(model().attribute("project", named("My first project")))
         .andExpect(model().attribute("selection", OPEN))
         .andExpect(model().attribute("tasks", containsTasks(1L, 3L)));
   }

   @Test
   public void getOpenTasksReturnsProjectTasksViewWithProjectAndOpenTasks() throws Exception {
      get("/projects/2/tasks?select=open")
         .andExpect(status().isOk())
         .andExpect(view().name("projects/tasks"))
         .andExpect(model().attribute("project", named("My second project")))
         .andExpect(model().attribute("selection", OPEN))
         .andExpect(model().attribute("tasks", containsTasks(4L, 5L)));
   }

   @Test
   public void getClosedTasksReturnsProjectTasksViewWithProjectAndClosedTasks() throws Exception {
      get("/projects/1/tasks?select=closed")
         .andExpect(status().isOk())
         .andExpect(view().name("projects/tasks"))
         .andExpect(model().attribute("project", named("My first project")))
         .andExpect(model().attribute("selection", CLOSED))
         .andExpect(model().attribute("tasks", containsTasks(2L)));
   }

   @Test
   public void getAllTasksReturnsProjectTasksViewWithProjectAndAllTasks() throws Exception {
      get("/projects/1/tasks?select=all")
         .andExpect(status().isOk())
         .andExpect(view().name("projects/tasks"))
         .andExpect(model().attribute("project", named("My first project")))
         .andExpect(model().attribute("selection", ALL))
         .andExpect(model().attribute("tasks", containsTasks(1L, 2L, 3L)));
   }

   @Test
   public void getTasksForUnknownProjectIsNotFound() throws Exception {
      get("/projects/7/tasks").andExpect(status().isNotFound());
   }

   @Test
   public void getTasksForInvalidProjectIsBadRequest() throws Exception {
      get("/projects/abc/tasks").andExpect(status().isBadRequest());
   }

   @Test
   public void postTasksNotAllowed() throws Exception {
      post("/projects/1/tasks").andExpect(status().isMethodNotAllowed());
   }

   @Test
   public void getEditDocumentReturnsEditDocumentationViewWithProjectAndText() throws Exception {
      get("/projects/edit/1/documentation")
         .andExpect(status().isOk())
         .andExpect(view().name("projects/edit-documentation"))
         .andExpect(model().attribute("project", named("My first project")))
         .andExpect(model().attribute("text", startsWith("# Lorem ipsum")));
   }

   @Test
   public void getEditDocumentForUnknownProjectIsNotFound() throws Exception {
      get("/projects/edit/7/documentation").andExpect(status().isNotFound());
   }

   @Test
   public void getEditDocumentForInvalidProjectIsBadRequest() throws Exception {
      get("/projects/edit/abc/documentation").andExpect(status().isBadRequest());
   }

   @Test
   @DirtiesContext
   public void postEditDocumentUpdatesDocumentAndRedirects() throws Exception {
      final String editedText = "Some new text.";
      post("/projects/edit/3/documentation", "text", editedText)
         .andExpect(status().isFound())
         .andDo(result -> {
            // follow the redirect
            final String view = result.getModelAndView().getViewName();
            final String redirect = toRedirectPath(view);
            assertThat(redirect, equalTo("/projects/3"));
            get(redirect)
               .andExpect(status().isOk())
               .andExpect(model().attribute("project", named("My third project")))
               .andExpect(model().attribute("project", hasProperty("readMe", hasProperty("text", equalTo(editedText)))));
         });
   }

   @Test
   public void postEditDocumentForUnknownProjectIsNotFound() throws Exception {
      post("/projects/edit/7/documentation", "text", "Some new text.").andExpect(status().isNotFound());
   }

   @Test
   public void postEditDocumentForInvalidProjectIsBadRequest() throws Exception {
      post("/projects/edit/abc/documentation", "text", "Some new text.").andExpect(status().isBadRequest());
   }

   private ResultActions post(final String path, final ProjectForm form) throws Exception {
      return mockMvc().perform(MockMvcRequestBuilders.post(path).with(csrf()).param("name", form.getName()).param("description", form.getDescription()));
   }

   private ResultActions post(final String path, final String param, final String value) throws Exception {
      return mockMvc().perform(MockMvcRequestBuilders.post(path).with(csrf()).param(param, value));
   }

}
