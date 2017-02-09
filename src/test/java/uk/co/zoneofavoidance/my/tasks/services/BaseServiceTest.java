package uk.co.zoneofavoidance.my.tasks.services;

import static java.util.Collections.emptySet;
import static uk.co.zoneofavoidance.my.tasks.domain.Priority.HIGH;
import static uk.co.zoneofavoidance.my.tasks.domain.Priority.NORMAL;
import static uk.co.zoneofavoidance.my.tasks.domain.State.DONE;
import static uk.co.zoneofavoidance.my.tasks.domain.State.TO_DO;
import static uk.co.zoneofavoidance.my.tasks.security.AuthenticatedUser.GLOBAL_USER;

import org.junit.Rule;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import uk.co.zoneofavoidance.my.tasks.domain.Project;
import uk.co.zoneofavoidance.my.tasks.domain.Task;
import uk.co.zoneofavoidance.my.tasks.repositories.ProjectRepository;
import uk.co.zoneofavoidance.my.tasks.repositories.TaskRepository;
import uk.co.zoneofavoidance.my.tasks.security.AuthenticatedUser;

/**
 * Base class for suites that unit test services that interact with the project
 * and task repositories. Provides mock repositories and sample domain objects.
 */
public abstract class BaseServiceTest {

   static final String FOO_USER = "foo";
   static final String BAR_USER = "bar";

   static final long PROJECT_ID = 157L;
   static final long TASK_ID = 324L;

   static final Project FOO_PROJECT = createProject("Foo", "The Foo project.", FOO_USER);
   static final Project BAR_PROJECT = createProject("Bar", "The Bar project.", BAR_USER);
   static final Project PUBLIC_PROJECT = createProject("Public", "A public project.", GLOBAL_USER);
   static final Task FOO_TASK = Task.create(FOO_PROJECT, "Foo", "The Foo task.", NORMAL, emptySet(), TO_DO);
   static final Task BAR_TASK = Task.create(FOO_PROJECT, "Bar", "The Bar task.", HIGH, emptySet(), DONE);

   @Mock
   private TaskRepository tasks;

   @Mock
   private ProjectRepository projects;

   @Mock
   private AuthenticatedUser user;

   @Rule
   public MockitoRule mockitoRule() {
      return MockitoJUnit.rule();
   }

   final TaskRepository mockTaskRepository() {
      return tasks;
   }

   final ProjectRepository mockProjectRepository() {
      return projects;
   }

   final AuthenticatedUser mockAuthenticatedUser() {
      return user;
   }

   static Project createProject(final String name, final String description, final String owner) {
      final Project project = Project.create(name, description);
      project.setOwner(owner);
      return project;
   }

}