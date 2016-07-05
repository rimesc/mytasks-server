package uk.co.zoneofavoidance.my.tasks;

import static com.google.common.base.Charsets.UTF_8;
import static java.util.Collections.singleton;
import static uk.co.zoneofavoidance.my.tasks.domain.Priority.CRITICAL;
import static uk.co.zoneofavoidance.my.tasks.domain.Priority.HIGH;
import static uk.co.zoneofavoidance.my.tasks.domain.Priority.LOW;
import static uk.co.zoneofavoidance.my.tasks.domain.Priority.NORMAL;
import static uk.co.zoneofavoidance.my.tasks.domain.State.DONE;
import static uk.co.zoneofavoidance.my.tasks.domain.State.IN_PROGRESS;
import static uk.co.zoneofavoidance.my.tasks.domain.State.ON_HOLD;
import static uk.co.zoneofavoidance.my.tasks.domain.State.TO_DO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableSet;
import com.google.common.io.Resources;

import uk.co.zoneofavoidance.my.tasks.domain.Note;
import uk.co.zoneofavoidance.my.tasks.domain.Project;
import uk.co.zoneofavoidance.my.tasks.domain.Tag;
import uk.co.zoneofavoidance.my.tasks.domain.Task;
import uk.co.zoneofavoidance.my.tasks.repositories.ProjectRepository;
import uk.co.zoneofavoidance.my.tasks.repositories.TaskRepository;
import uk.co.zoneofavoidance.my.tasks.services.UserService;

@Component
@Profile("dev")
public class SampleData implements CommandLineRunner {

   @Autowired
   private ProjectRepository projects;

   @Autowired
   private TaskRepository tasks;

   @Autowired
   private UserService users;

   @Override
   public void run(final String... args) throws Exception {
      users.createUser("dev", "secret", true);
      final Project firstProject = Project.create("My first project", "This is my first sample project.");
      final Project secondProject = Project.create("My second project", "This is my second sample project. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
      final Note readMe = new Note(Resources.toString(SampleData.class.getResource("/samples/readme.md"), UTF_8));
      firstProject.setReadMe(readMe);
      projects.save(firstProject);
      projects.save(secondProject);
      projects.save(Project.create("My third project", "This is my third sample project. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."));
      final Task firstTask = Task.create(firstProject, "First sample task", Resources.toString(SampleData.class.getResource("/samples/readme.md"), UTF_8), HIGH, TO_DO);
      firstTask.setTags(ImmutableSet.of(Tag.create("Bug"), Tag.create("Version 1")));
      tasks.save(firstTask);
      final Task secondTask = Task.create(firstProject, "Second sample task", "This is the second sample task.", CRITICAL, DONE);
      secondTask.setTags(singleton(Tag.create("Feature")));
      tasks.save(secondTask);
      tasks.save(Task.create(firstProject, "Third sample task", "This is the third sample task.", LOW, TO_DO));
      tasks.save(Task.create(secondProject, "Fourth sample task", "This is the fourth sample task.", NORMAL, IN_PROGRESS));
      tasks.save(Task.create(secondProject, "Fifth sample task", "This is the fifth sample task.", HIGH, ON_HOLD));
   }
}
