package uk.co.zoneofavoidance.my.tasks;

import static com.google.common.base.Charsets.UTF_8;
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

import com.google.common.io.Resources;

import uk.co.zoneofavoidance.my.tasks.domain.Document;
import uk.co.zoneofavoidance.my.tasks.domain.Project;
import uk.co.zoneofavoidance.my.tasks.domain.Task;
import uk.co.zoneofavoidance.my.tasks.repositories.ProjectRepository;
import uk.co.zoneofavoidance.my.tasks.repositories.TaskRepository;

@Component
@Profile("dev")
public class SampleData implements CommandLineRunner {

   @Autowired
   private ProjectRepository projects;

   @Autowired
   private TaskRepository tasks;

   @Override
   public void run(final String... args) throws Exception {
      final Project firstProject = new Project("My first project", "This is my first sample project.");
      final Project secondProject = new Project("My second project", "This is my second sample project. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
      final Document document = new Document(Resources.toString(SampleData.class.getResource("/samples/readme.md"), UTF_8));
      firstProject.setDocumentation(document);
      projects.save(firstProject);
      projects.save(secondProject);
      projects.save(new Project("My third project", "This is my third sample project. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."));
      tasks.save(new Task(firstProject, "First sample task", "This is the first sample task.", HIGH, TO_DO));
      tasks.save(new Task(firstProject, "Second sample task", "This is the second sample task.", CRITICAL, DONE));
      tasks.save(new Task(firstProject, "Third sample task", "This is the third sample task.", LOW, TO_DO));
      tasks.save(new Task(secondProject, "Fourth sample task", "This is the fourth sample task.", NORMAL, IN_PROGRESS));
      tasks.save(new Task(secondProject, "Fifth sample task", "This is the fifth sample task.", HIGH, ON_HOLD));
   }
}
