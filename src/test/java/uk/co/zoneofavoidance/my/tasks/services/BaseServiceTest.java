package uk.co.zoneofavoidance.my.tasks.services;

import static java.util.Collections.emptySet;
import static uk.co.zoneofavoidance.my.tasks.domain.Priority.HIGH;
import static uk.co.zoneofavoidance.my.tasks.domain.Priority.NORMAL;
import static uk.co.zoneofavoidance.my.tasks.domain.State.DONE;
import static uk.co.zoneofavoidance.my.tasks.domain.State.TO_DO;

import org.junit.Rule;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import uk.co.zoneofavoidance.my.tasks.domain.Project;
import uk.co.zoneofavoidance.my.tasks.domain.Task;
import uk.co.zoneofavoidance.my.tasks.repositories.ProjectRepository;
import uk.co.zoneofavoidance.my.tasks.repositories.TaskRepository;

/**
 * Base class for suites that unit test services that interact with the project
 * and task repositories. Provides mock repositories and sample domain objects.
 */
public abstract class BaseServiceTest {

   static final long PROJECT_ID = 157L;
   static final long TASK_ID = 324L;

   static final Project FOO_PROJECT = Project.create("Foo", "The Foo project.");
   static final Project BAR_PROJECT = Project.create("Bar", "The Bar project.");
   static final Task FOO_TASK = Task.create(FOO_PROJECT, "Foo", "The Foo task.", NORMAL, emptySet(), TO_DO);
   static final Task BAR_TASK = Task.create(FOO_PROJECT, "Bar", "The Bar task.", HIGH, emptySet(), DONE);

   @Mock
   private TaskRepository tasks;

   @Mock
   private ProjectRepository projects;

   @Rule
   public MockitoRule mockitoRule() {
      return MockitoJUnit.rule();
   }

   /**
    * @return mock {@link TaskRepository}
    */
   final TaskRepository mockTaskRepository() {
      return tasks;
   }

   /**
    * @return mock {@link ProjectRepository}
    */
   final ProjectRepository mockProjectRepository() {
      return projects;
   }

}