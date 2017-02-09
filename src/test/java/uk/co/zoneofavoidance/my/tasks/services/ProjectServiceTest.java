package uk.co.zoneofavoidance.my.tasks.services;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import uk.co.zoneofavoidance.my.tasks.exceptions.NotFoundException;

/**
 * Unit tests for {@link ProjectService}.
 */
public class ProjectServiceTest extends BaseServiceTest {

   private ProjectService service;

   @Before
   public void setUp() throws Exception {
      service = new ProjectService(mockProjectRepository(), mockTaskRepository());
   }

   @Test
   public void getReturnsProject() {
      when(mockProjectRepository().findOne(PROJECT_ID)).thenReturn(FOO_PROJECT);
      assertEquals(FOO_PROJECT, service.get(PROJECT_ID));
   }

   @Test(expected = NotFoundException.class)
   public void getThrowsIfProjectNotFound() {
      when(mockProjectRepository().findOne(PROJECT_ID)).thenReturn(null);
      service.get(PROJECT_ID);
   }

   @Test
   public void listReturnsAllProjects() {
      when(mockProjectRepository().findAll()).thenReturn(asList(FOO_PROJECT, BAR_PROJECT));
      assertThat(service.list(), containsInAnyOrder(FOO_PROJECT, BAR_PROJECT));
   }

   @Test
   public void savePassesProjectToRepository() {
      service.save(FOO_PROJECT);
      verify(mockProjectRepository()).save(FOO_PROJECT);
   }

   @Test
   public void deleteDeletesProjectAndAllTasks() throws Exception {
      when(mockProjectRepository().findOne(PROJECT_ID)).thenReturn(FOO_PROJECT);
      service.delete(PROJECT_ID);
      verify(mockProjectRepository()).delete(FOO_PROJECT);
      verify(mockTaskRepository()).deleteByProjectId(PROJECT_ID);
   }

   @Test(expected = NotFoundException.class)
   public void deleteThrowsIfProjectNotFound() throws Exception {
      when(mockProjectRepository().findOne(PROJECT_ID)).thenReturn(null);
      service.delete(PROJECT_ID);
   }

}
