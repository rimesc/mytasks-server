package uk.co.zoneofavoidance.my.tasks.controllers;

import static java.util.Arrays.asList;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.ModelAndViewAssert.assertAndReturnModelAttributeOfType;
import static org.springframework.test.web.ModelAndViewAssert.assertModelAttributeAvailable;
import static org.springframework.test.web.ModelAndViewAssert.assertModelAttributeValue;
import static org.springframework.test.web.ModelAndViewAssert.assertViewName;
import static uk.co.zoneofavoidance.my.tasks.controllers.TaskSelection.ALL;
import static uk.co.zoneofavoidance.my.tasks.controllers.TaskSelection.CLOSED;
import static uk.co.zoneofavoidance.my.tasks.controllers.TaskSelection.OPEN;
import static uk.co.zoneofavoidance.my.tasks.domain.Priority.NORMAL;
import static uk.co.zoneofavoidance.my.tasks.domain.State.DONE;
import static uk.co.zoneofavoidance.my.tasks.domain.State.IN_PROGRESS;
import static uk.co.zoneofavoidance.my.tasks.domain.State.ON_HOLD;
import static uk.co.zoneofavoidance.my.tasks.domain.State.TO_DO;
import static uk.co.zoneofavoidance.my.tasks.testing.ModelAndViewAssertExtras.assertRedirect;

import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import uk.co.zoneofavoidance.my.tasks.domain.Note;
import uk.co.zoneofavoidance.my.tasks.domain.Project;
import uk.co.zoneofavoidance.my.tasks.domain.State;
import uk.co.zoneofavoidance.my.tasks.domain.Task;
import uk.co.zoneofavoidance.my.tasks.exceptions.NotFoundException;
import uk.co.zoneofavoidance.my.tasks.repositories.ProjectRepository;
import uk.co.zoneofavoidance.my.tasks.services.TaskService;

/**
 * Unit tests for {@link ProjectsController}.
 * <p>
 * Note that while these tests check the returned {@link ModelAndView} and the
 * thrown exceptions, they lack parameter validation and exception handling.
 */
public class ProjectsControllerTest {

   private static final long PROJECT_ID = 157L;

   private static final Project FOO_PROJECT = new Project("Foo", "The Foo project.");
   private static final Project BAR_PROJECT = new Project("Bar", "The Bar project.");

   @Rule
   public MockitoRule mockitoRule = MockitoJUnit.rule();

   @Mock
   private TaskService taskService;

   @Mock
   private ProjectRepository repository;

   @Mock
   private BindingResult bindingResult;

   private ProjectsController controller;

   @Before
   public void setUp() throws Exception {
      controller = new ProjectsController(repository, taskService);
   }

   @Test
   public void getProjectsReturnsProjectListViewWithProjects() throws Exception {
      final List<Project> projects = asList(FOO_PROJECT, BAR_PROJECT);
      when(repository.findAll()).thenReturn(projects);
      final ModelAndView modelAndView = controller.getProjects();
      assertViewName(modelAndView, "projects/list");
      assertModelAttributeValue(modelAndView, "projects", projects);
   }

   @Test
   public void getProjectReturnsProjectViewWithProject() throws Exception {
      when(repository.findOne(PROJECT_ID)).thenReturn(FOO_PROJECT);
      final ModelAndView modelAndView = controller.getProject(PROJECT_ID);
      assertViewName(modelAndView, "projects/view");
      assertModelAttributeValue(modelAndView, "project", FOO_PROJECT);
   }

   @Test(expected = NotFoundException.class)
   public void getProjectThrowsIfProjectNotFound() throws Exception {
      when(repository.findOne(PROJECT_ID)).thenReturn(null);
      controller.getProject(PROJECT_ID);
   }

   @Test
   public void getNewProjectReturnsNewProjectViewWithOnCancel() throws Exception {
      final ModelAndView modelAndView = controller.getNewProject(new ProjectForm());
      assertViewName(modelAndView, "projects/new");
      assertModelAttributeAvailable(modelAndView, "projectForm");
      assertModelAttributeValue(modelAndView, "onCancel", "/projects");
   }

   @Test
   public void postNewProjectCreatesProjectAndRedirectsToProjectView() throws Exception {
      when(bindingResult.hasErrors()).thenReturn(false);
      when(repository.save(isA(Project.class))).thenAnswer(invocation -> {
         final Project project = invocation.getArgumentAt(0, Project.class);
         project.setId(PROJECT_ID);
         return project;
      });
      final ProjectForm form = new ProjectForm("New project", "A new project.");
      final ModelAndView modelAndView = controller.postNewProject(form, bindingResult);
      assertRedirect(modelAndView, "/projects/" + PROJECT_ID);
      verify(repository).save(isA(Project.class));
   }

   @Test
   public void postNewProjectReturnsNewProjectViewIfBindingErrors() throws Exception {
      when(bindingResult.hasErrors()).thenReturn(true);
      final ProjectForm form = new ProjectForm("New project", "A new project.");
      final ModelAndView modelAndView = controller.postNewProject(form, bindingResult);
      assertViewName(modelAndView, "projects/new");
      assertModelAttributeValue(modelAndView, "projectForm", form);
      assertModelAttributeValue(modelAndView, "onCancel", "/projects");
   }

   @Test
   public void getEditProjectReturnsEditProjectViewWithFormAndOnCancel() throws Exception {
      when(repository.findOne(PROJECT_ID)).thenReturn(FOO_PROJECT);
      final ModelAndView modelAndView = controller.getEditProject(PROJECT_ID);
      assertViewName(modelAndView, "projects/edit");
      final ProjectForm form = assertAndReturnModelAttributeOfType(modelAndView, "projectForm", ProjectForm.class);
      assertEquals("Foo", form.getName());
      assertEquals("The Foo project.", form.getDescription());
   }

   @Test(expected = NotFoundException.class)
   public void getEditProjectThrowsIfProjectNotFound() throws Exception {
      when(repository.findOne(PROJECT_ID)).thenReturn(null);
      controller.getEditProject(PROJECT_ID);
   }

   @Test
   public void postEditProjectUpdatesProjectAndRedirectsToProjectView() throws Exception {
      when(repository.findOne(PROJECT_ID)).thenReturn(new Project("My Project", "Old description."));
      when(bindingResult.hasErrors()).thenReturn(false);
      final ProjectForm form = new ProjectForm("My Project", "New description.");
      final ModelAndView modelAndView = controller.postEditProject(PROJECT_ID, form, bindingResult);
      assertRedirect(modelAndView, "/projects/" + PROJECT_ID);
      final ArgumentCaptor<Project> argument = ArgumentCaptor.forClass(Project.class);
      verify(repository).save(argument.capture());
      assertEquals("New description.", argument.getValue().getDescription());
   }

   @Test
   public void postEditProjectReturnsEditProjectViewIfBindingErrors() throws Exception {
      when(repository.findOne(PROJECT_ID)).thenReturn(FOO_PROJECT);
      when(bindingResult.hasErrors()).thenReturn(true);
      final ProjectForm form = new ProjectForm("Illegal Name", "The Foo project.");
      final ModelAndView modelAndView = controller.postEditProject(PROJECT_ID, form, bindingResult);
      assertViewName(modelAndView, "projects/edit");
      assertModelAttributeValue(modelAndView, "projectForm", form);
      assertModelAttributeValue(modelAndView, "onCancel", "/projects/" + PROJECT_ID);
   }

   @Test(expected = NotFoundException.class)
   public void postEditProjectThrowsIfProjectNotFound() throws Exception {
      when(repository.findOne(PROJECT_ID)).thenReturn(null);
      controller.postEditProject(PROJECT_ID, new ProjectForm(), bindingResult);
   }

   @Test
   public void getDeleteProjectReturnsDeleteProjectViewWithProject() throws Exception {
      when(repository.findOne(PROJECT_ID)).thenReturn(FOO_PROJECT);
      final ModelAndView modelAndView = controller.getDeleteProject(PROJECT_ID);
      assertViewName(modelAndView, "projects/delete");
      assertModelAttributeValue(modelAndView, "project", FOO_PROJECT);
   }

   @Test(expected = NotFoundException.class)
   public void getDeleteProjectThrowsIfProjectNotFound() throws Exception {
      when(repository.findOne(PROJECT_ID)).thenReturn(null);
      controller.getDeleteProject(PROJECT_ID);
   }

   @Test
   public void postDeleteProjectDeletesProjectAndRedirectsToProjectsView() throws Exception {
      when(repository.findOne(PROJECT_ID)).thenReturn(FOO_PROJECT);
      final ModelAndView modelAndView = controller.postDeleteProject(PROJECT_ID);
      assertRedirect(modelAndView, "/projects");
      verify(repository).delete(FOO_PROJECT);
   }

   @Test(expected = NotFoundException.class)
   public void postDeleteProjectThrowsIfProjectNotFound() throws Exception {
      when(repository.findOne(PROJECT_ID)).thenReturn(null);
      controller.postDeleteProject(PROJECT_ID);
   }

   @Test
   public void getEditDocumentReturnsEditDocumentationViewWithProjectAndText() throws Exception {
      final Project project = new Project("My Project", "Description of my project.");
      final String markdown = "Some markdown text.";
      project.setReadMe(new Note(markdown));
      when(repository.findOne(PROJECT_ID)).thenReturn(project);
      final ModelAndView modelAndView = controller.getEditDocument(PROJECT_ID);
      assertViewName(modelAndView, "projects/edit-documentation");
      assertModelAttributeValue(modelAndView, "project", project);
      assertModelAttributeValue(modelAndView, "text", markdown);
   }

   @Test
   public void getEditDocumentReturnsModelWithEmptyTextIfNoDocumentation() throws Exception {
      final Project project = new Project("My Project", "Description of my project.");
      when(repository.findOne(PROJECT_ID)).thenReturn(project);
      final ModelAndView modelAndView = controller.getEditDocument(PROJECT_ID);
      assertViewName(modelAndView, "projects/edit-documentation");
      assertModelAttributeValue(modelAndView, "project", project);
      assertModelAttributeValue(modelAndView, "text", "");
   }

   @Test(expected = NotFoundException.class)
   public void getEditDocumentThrowsIfProjectNotFound() throws Exception {
      when(repository.findOne(PROJECT_ID)).thenReturn(null);
      controller.getEditDocument(PROJECT_ID);
   }

   @Test
   public void postEditDocumentUpdatesExistingDocumentationAndRedirectsToProjectView() throws Exception {
      final Project project = new Project("My Project", "Description of my project.");
      project.setReadMe(new Note("Old markdown text."));
      when(repository.findOne(PROJECT_ID)).thenReturn(project);
      final ModelAndView modelAndView = controller.postEditDocument(PROJECT_ID, "New markdown text.");
      assertRedirect(modelAndView, "/projects/" + PROJECT_ID);
      final ArgumentCaptor<Project> argument = ArgumentCaptor.forClass(Project.class);
      verify(repository).save(argument.capture());
      final Note documentation = argument.getValue().getReadMe();
      assertNotNull(documentation);
      assertEquals("New markdown text.", documentation.getText());
   }

   @Test
   public void postEditDocumentAddsNewDocumentationAndRedirectsToProjectView() throws Exception {
      final Project project = new Project("My Project", "Description of my project.");
      when(repository.findOne(PROJECT_ID)).thenReturn(project);
      final ModelAndView modelAndView = controller.postEditDocument(PROJECT_ID, "Some markdown text.");
      assertRedirect(modelAndView, "/projects/" + PROJECT_ID);
      final ArgumentCaptor<Project> argument = ArgumentCaptor.forClass(Project.class);
      verify(repository).save(argument.capture());
      final Note documentation = argument.getValue().getReadMe();
      assertNotNull(documentation);
      assertEquals("Some markdown text.", documentation.getText());
   }

   @Test(expected = NotFoundException.class)
   public void postEditDocumentThrowsIfProjectNotFound() throws Exception {
      when(repository.findOne(PROJECT_ID)).thenReturn(null);
      controller.postEditDocument(PROJECT_ID, "Some markdown text.");
   }

   @Test
   public void getTasksReturnsTasksViewFilteredToOpenTasks() throws Exception {
      final Task firstTask = new Task(FOO_PROJECT, "First task", "The first task.", NORMAL, TO_DO);
      final Task secondTask = new Task(FOO_PROJECT, "Second task", "The second task.", NORMAL, IN_PROGRESS);
      final List<Task> tasks = asList(firstTask, secondTask);
      when(repository.findOne(PROJECT_ID)).thenReturn(FOO_PROJECT);
      when(taskService.getForProject(PROJECT_ID, new State[] { TO_DO, IN_PROGRESS, ON_HOLD })).thenReturn(tasks);
      final ModelAndView modelAndView = controller.getTasks(PROJECT_ID, OPEN);
      assertViewName(modelAndView, "projects/tasks");
      assertModelAttributeValue(modelAndView, "project", FOO_PROJECT);
      assertModelAttributeValue(modelAndView, "selection", OPEN);
      assertModelAttributeValue(modelAndView, "tasks", tasks);
   }

   @Test
   public void getTasksReturnsTasksViewFilteredToClosedTasks() throws Exception {
      final Task firstTask = new Task(FOO_PROJECT, "First task", "The first task.", NORMAL, DONE);
      final Task secondTask = new Task(FOO_PROJECT, "Second task", "The second task.", NORMAL, DONE);
      final List<Task> tasks = asList(firstTask, secondTask);
      when(repository.findOne(PROJECT_ID)).thenReturn(FOO_PROJECT);
      when(taskService.getForProject(PROJECT_ID, new State[] { DONE })).thenReturn(tasks);
      final ModelAndView modelAndView = controller.getTasks(PROJECT_ID, CLOSED);
      assertViewName(modelAndView, "projects/tasks");
      assertModelAttributeValue(modelAndView, "project", FOO_PROJECT);
      assertModelAttributeValue(modelAndView, "selection", CLOSED);
      assertModelAttributeValue(modelAndView, "tasks", tasks);
   }

   @Test
   public void getTasksReturnsTasksViewFilteredToAllTasks() throws Exception {
      final Task firstTask = new Task(FOO_PROJECT, "First task", "The first task.", NORMAL, TO_DO);
      final Task secondTask = new Task(FOO_PROJECT, "Second task", "The second task.", NORMAL, DONE);
      final List<Task> tasks = asList(firstTask, secondTask);
      when(repository.findOne(PROJECT_ID)).thenReturn(FOO_PROJECT);
      when(taskService.getForProject(PROJECT_ID, State.values())).thenReturn(tasks);
      final ModelAndView modelAndView = controller.getTasks(PROJECT_ID, ALL);
      assertViewName(modelAndView, "projects/tasks");
      assertModelAttributeValue(modelAndView, "project", FOO_PROJECT);
      assertModelAttributeValue(modelAndView, "selection", ALL);
      assertModelAttributeValue(modelAndView, "tasks", tasks);
   }

   @Test(expected = NotFoundException.class)
   public void getTasksThrowsIfProjectNotFound() throws Exception {
      when(repository.findOne(PROJECT_ID)).thenReturn(null);
      controller.getTasks(PROJECT_ID, OPEN);
   }
}
