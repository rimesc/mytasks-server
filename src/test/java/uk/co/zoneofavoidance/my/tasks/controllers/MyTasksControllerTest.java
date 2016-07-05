package uk.co.zoneofavoidance.my.tasks.controllers;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.ModelAndViewAssert.assertModelAttributeValue;
import static org.springframework.test.web.ModelAndViewAssert.assertViewName;

import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.web.servlet.ModelAndView;

import uk.co.zoneofavoidance.my.tasks.domain.Project;
import uk.co.zoneofavoidance.my.tasks.repositories.ProjectRepository;

/**
 * Unit tests of {@link MyTasksController}.
 */
public class MyTasksControllerTest {

   private static final Project FOO_PROJECT = Project.create("Foo", "The Foo project.");
   private static final Project BAR_PROJECT = Project.create("Bar", "The Bar project.");

   @Mock
   private ProjectRepository repository;

   private MyTasksController controller;

   @Before
   public void setUp() {
      controller = new MyTasksController(repository);
   }

   @Test
   public void getHomeReturnsHomeViewWithProjects() {
      final List<Project> projects = asList(FOO_PROJECT, BAR_PROJECT);
      when(repository.findAll()).thenReturn(projects);
      final ModelAndView modelAndView = controller.getHome();
      assertViewName(modelAndView, "home");
      assertModelAttributeValue(modelAndView, "projects", projects);
   }

   @Rule
   public MockitoRule mockitoRule() {
      return MockitoJUnit.rule();
   }

}
