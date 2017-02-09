package uk.co.zoneofavoidance.my.tasks.services;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import uk.co.zoneofavoidance.my.tasks.domain.Project;
import uk.co.zoneofavoidance.my.tasks.exceptions.NotFoundException;
import uk.co.zoneofavoidance.my.tasks.exceptions.PermissionDeniedException;

/**
 * Unit tests for {@link ProjectService}.
 */
public class ProjectServiceTest extends BaseServiceTest {

   private ProjectService service;

   @Before
   public void setUp() throws Exception {
      service = new ProjectService(mockProjectRepository(), mockTaskRepository(), mockAuthenticatedUser());
   }

   @Test
   public void getReturnsOwnedProject() {
      when(mockProjectRepository().findOne(PROJECT_ID)).thenReturn(FOO_PROJECT);
      when(mockAuthenticatedUser().getId()).thenReturn(FOO_USER);
      assertEquals(FOO_PROJECT, service.get(PROJECT_ID));
   }

   @Test
   public void getReturnsPublicProject() {
      when(mockProjectRepository().findOne(PROJECT_ID)).thenReturn(PUBLIC_PROJECT);
      when(mockAuthenticatedUser().getId()).thenReturn(FOO_USER);
      assertEquals(PUBLIC_PROJECT, service.get(PROJECT_ID));
   }

   @Test(expected = NotFoundException.class)
   public void getThrowsIfProjectNotFound() {
      when(mockProjectRepository().findOne(PROJECT_ID)).thenReturn(null);
      service.get(PROJECT_ID);
   }

   @Test(expected = PermissionDeniedException.class)
   public void getThrowsIfProjectNotOwnedAndNotPublic() {
      when(mockProjectRepository().findOne(PROJECT_ID)).thenReturn(BAR_PROJECT);
      when(mockAuthenticatedUser().getId()).thenReturn(FOO_USER);
      service.get(PROJECT_ID);
   }

   @Test
   public void listReturnsAllOwnedAndPublicProjects() {
      when(mockProjectRepository().findAll()).thenReturn(asList(FOO_PROJECT, BAR_PROJECT, PUBLIC_PROJECT));
      when(mockAuthenticatedUser().getId()).thenReturn(FOO_USER);
      assertThat(service.list(), containsInAnyOrder(FOO_PROJECT, PUBLIC_PROJECT));
   }

   @Test
   public void savePassesOwnedProjectToRepository() {
      when(mockAuthenticatedUser().getId()).thenReturn(FOO_USER);
      service.save(FOO_PROJECT);
      verify(mockProjectRepository()).save(FOO_PROJECT);
   }

   @Test
   public void savePassesPublicProjectToRepository() {
      when(mockAuthenticatedUser().getId()).thenReturn(FOO_USER);
      service.save(PUBLIC_PROJECT);
      verify(mockProjectRepository()).save(PUBLIC_PROJECT);
   }

   @Test
   public void saveSetsOwnerToCurrentUserIfAbsent() {
      when(mockAuthenticatedUser().getId()).thenReturn(FOO_USER);
      final Project project = Project.create("New project", "The new project");
      service.save(project);
      verify(mockProjectRepository()).save(project);
      assertEquals(project.getOwner(), FOO_USER);
   }

   @Test(expected = PermissionDeniedException.class)
   public void saveThrowsIfProjectNotOwnedOrPublic() {
      when(mockAuthenticatedUser().getId()).thenReturn(FOO_USER);
      service.save(BAR_PROJECT);
   }

   @Test
   public void deleteDeletesOwnedProjectAndAllTasks() throws Exception {
      when(mockAuthenticatedUser().getId()).thenReturn(FOO_USER);
      when(mockProjectRepository().findOne(PROJECT_ID)).thenReturn(FOO_PROJECT);
      service.delete(PROJECT_ID);
      verify(mockProjectRepository()).delete(FOO_PROJECT);
      verify(mockTaskRepository()).deleteByProjectId(PROJECT_ID);
   }

   @Test
   public void deleteDeletesPublicProjectAndAllTasks() throws Exception {
      when(mockAuthenticatedUser().getId()).thenReturn(FOO_USER);
      when(mockProjectRepository().findOne(PROJECT_ID)).thenReturn(PUBLIC_PROJECT);
      service.delete(PROJECT_ID);
      verify(mockProjectRepository()).delete(PUBLIC_PROJECT);
      verify(mockTaskRepository()).deleteByProjectId(PROJECT_ID);
   }

   @Test(expected = NotFoundException.class)
   public void deleteThrowsIfProjectNotFound() throws Exception {
      when(mockProjectRepository().findOne(PROJECT_ID)).thenReturn(null);
      service.delete(PROJECT_ID);
   }

   @Test(expected = PermissionDeniedException.class)
   public void deleteThrowsIfProjectNotOwnedOrPublic() throws Exception {
      when(mockAuthenticatedUser().getId()).thenReturn(FOO_USER);
      when(mockProjectRepository().findOne(PROJECT_ID)).thenReturn(BAR_PROJECT);
      service.delete(PROJECT_ID);
   }

}
