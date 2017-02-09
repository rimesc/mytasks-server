package uk.co.zoneofavoidance.my.tasks.services;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.co.zoneofavoidance.my.tasks.domain.State.DONE;
import static uk.co.zoneofavoidance.my.tasks.domain.State.TO_DO;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import uk.co.zoneofavoidance.my.tasks.exceptions.NotFoundException;

/**
 * Unit tests for {@link TaskService}.
 */
public class TaskServiceTest extends BaseServiceTest {

   private TaskService service;

   @Before
   public void setUp() throws Exception {
      service = new TaskService(mockTaskRepository(), mockProjectRepository());
   }

   @Test
   public void getReturnsTask() {
      when(mockTaskRepository().findOne(TASK_ID)).thenReturn(FOO_TASK);
      assertEquals(FOO_TASK, service.get(TASK_ID));
   }

   @Test(expected = NotFoundException.class)
   public void getThrowsIfTaskNotFound() {
      when(mockTaskRepository().findOne(TASK_ID)).thenReturn(null);
      service.get(TASK_ID);
   }

   @Test
   public void getForProjectReturnsTasks() {
      when(mockTaskRepository().findByProjectId(PROJECT_ID)).thenReturn(asList(FOO_TASK, BAR_TASK));
      assertThat(service.getForProject(PROJECT_ID), containsInAnyOrder(FOO_TASK, BAR_TASK));
   }

   @Test(expected = NotFoundException.class)
   public void getForProjectThrowsIfProjectNotFound() {
      when(mockTaskRepository().findByProjectId(PROJECT_ID)).thenReturn(emptyList());
      when(mockProjectRepository().findOne(PROJECT_ID)).thenReturn(null);
      service.getForProject(PROJECT_ID);
   }

   @Test
   public void getForProjectByStateReturnsTasks() {
      when(mockTaskRepository().findByProjectIdAndStateIn(PROJECT_ID, asList(TO_DO, DONE))).thenReturn(asList(FOO_TASK, BAR_TASK));
      assertThat(service.getForProject(PROJECT_ID, TO_DO, DONE), containsInAnyOrder(FOO_TASK, BAR_TASK));
   }

   @Test(expected = NotFoundException.class)
   public void getForProjectByStateThrowsIfProjectNotFound() {
      when(mockTaskRepository().findByProjectIdAndStateIn(PROJECT_ID, asList(TO_DO, DONE))).thenReturn(emptyList());
      when(mockProjectRepository().findOne(PROJECT_ID)).thenReturn(null);
      service.getForProject(PROJECT_ID, TO_DO, DONE);
   }

   @Test
   public void savePassesTaskToRepository() {
      service.save(FOO_TASK);
      verify(mockTaskRepository()).save(FOO_TASK);
   }

   @Test
   public void deleteDeletesTask() throws Exception {
      when(mockTaskRepository().findOne(TASK_ID)).thenReturn(FOO_TASK);
      service.delete(TASK_ID);
      verify(mockTaskRepository()).delete(FOO_TASK);
   }

   @Test(expected = NotFoundException.class)
   public void deleteThrowsIfTaskNotFound() throws Exception {
      when(mockTaskRepository().findOne(TASK_ID)).thenReturn(null);
      service.delete(TASK_ID);
   }

}
