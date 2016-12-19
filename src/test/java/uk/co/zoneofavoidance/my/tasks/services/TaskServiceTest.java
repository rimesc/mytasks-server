package uk.co.zoneofavoidance.my.tasks.services;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.co.zoneofavoidance.my.tasks.domain.Priority.HIGH;
import static uk.co.zoneofavoidance.my.tasks.domain.Priority.NORMAL;
import static uk.co.zoneofavoidance.my.tasks.domain.State.DONE;
import static uk.co.zoneofavoidance.my.tasks.domain.State.IN_PROGRESS;
import static uk.co.zoneofavoidance.my.tasks.domain.State.ON_HOLD;
import static uk.co.zoneofavoidance.my.tasks.domain.State.TO_DO;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import uk.co.zoneofavoidance.my.tasks.domain.Project;
import uk.co.zoneofavoidance.my.tasks.domain.Task;
import uk.co.zoneofavoidance.my.tasks.exceptions.NotFoundException;
import uk.co.zoneofavoidance.my.tasks.repositories.ProjectRepository;
import uk.co.zoneofavoidance.my.tasks.repositories.TaskRepository;

/**
 * Unit tests for {@link TaskService}.
 */
public class TaskServiceTest {

   private static final long PROJECT_ID = 157L;
   private static final long TASK_ID = 324L;

   private static final Project FOO_PROJECT = Project.create("Foo", "The Foo project.");
   private static final Task FOO_TASK = Task.create(FOO_PROJECT, "Foo", "The Foo task.", NORMAL, emptySet(), TO_DO);
   private static final Task BAR_TASK = Task.create(FOO_PROJECT, "Bar", "The Bar task.", HIGH, emptySet(), DONE);

   @Mock
   private TaskRepository repository;

   @Mock
   private ProjectRepository projects;

   private TaskService service;

   @Before
   public void setUp() throws Exception {
      service = new TaskService(repository, projects);
   }

   @Test
   public void getReturnsTask() {
      when(repository.findOne(TASK_ID)).thenReturn(FOO_TASK);
      assertEquals(FOO_TASK, service.get(TASK_ID));
   }

   @Test(expected = NotFoundException.class)
   public void getThrowsIfTaskNotFound() {
      when(repository.findOne(TASK_ID)).thenReturn(null);
      service.get(TASK_ID);
   }

   @Test
   public void getForProjectReturnsTasks() {
      when(repository.findByProjectId(PROJECT_ID)).thenReturn(asList(FOO_TASK, BAR_TASK));
      assertThat(service.getForProject(PROJECT_ID), containsInAnyOrder(FOO_TASK, BAR_TASK));
   }

   @Test(expected = NotFoundException.class)
   public void getForProjectThrowsIfProjectNotFound() {
      when(repository.findByProjectId(PROJECT_ID)).thenReturn(emptyList());
      when(projects.findOne(PROJECT_ID)).thenReturn(null);
      service.getForProject(PROJECT_ID);
   }

   @Test
   public void getForProjectByStateReturnsTasks() {
      when(repository.findByProjectIdAndStateIn(PROJECT_ID, asList(TO_DO, DONE))).thenReturn(asList(FOO_TASK, BAR_TASK));
      assertThat(service.getForProject(PROJECT_ID, TO_DO, DONE), containsInAnyOrder(FOO_TASK, BAR_TASK));
   }

   @Test(expected = NotFoundException.class)
   public void getForProjectByStateThrowsIfProjectNotFound() {
      when(repository.findByProjectIdAndStateIn(PROJECT_ID, asList(TO_DO, DONE))).thenReturn(emptyList());
      when(projects.findOne(PROJECT_ID)).thenReturn(null);
      service.getForProject(PROJECT_ID, TO_DO, DONE);
   }

   @Test
   public void getOpenForProjectReturnsTasks() {
      when(repository.findByProjectIdAndStateIn(PROJECT_ID, asList(TO_DO, IN_PROGRESS, ON_HOLD))).thenReturn(asList(FOO_TASK, BAR_TASK));
      assertThat(service.getOpenForProject(PROJECT_ID), containsInAnyOrder(FOO_TASK, BAR_TASK));
   }

   @Test(expected = NotFoundException.class)
   public void getOpenForProjectThrowsIfProjectNotFound() {
      when(repository.findByProjectIdAndStateIn(PROJECT_ID, asList(TO_DO, IN_PROGRESS, ON_HOLD))).thenReturn(emptyList());
      when(projects.findOne(PROJECT_ID)).thenReturn(null);
      service.getOpenForProject(PROJECT_ID);
   }

   @Test
   public void getClosedForProjectReturnsTasks() {
      when(repository.findByProjectIdAndStateIn(PROJECT_ID, asList(DONE))).thenReturn(asList(FOO_TASK, BAR_TASK));
      assertThat(service.getClosedForProject(PROJECT_ID), containsInAnyOrder(FOO_TASK, BAR_TASK));
   }

   @Test(expected = NotFoundException.class)
   public void getClosedForProjectThrowsIfProjectNotFound() {
      when(repository.findByProjectIdAndStateIn(PROJECT_ID, asList(DONE))).thenReturn(emptyList());
      when(projects.findOne(PROJECT_ID)).thenReturn(null);
      service.getClosedForProject(PROJECT_ID);
   }

   @Test
   public void savePassesTaskToRepository() {
      service.save(FOO_TASK);
      verify(repository).save(FOO_TASK);
   }

   @Rule
   public MockitoRule mockitoRule() {
      return MockitoJUnit.rule();
   }

}
