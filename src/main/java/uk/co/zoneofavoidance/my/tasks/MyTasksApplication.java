package uk.co.zoneofavoidance.my.tasks;

import static uk.co.zoneofavoidance.my.tasks.domain.Priority.CRITICAL;
import static uk.co.zoneofavoidance.my.tasks.domain.Priority.HIGH;
import static uk.co.zoneofavoidance.my.tasks.domain.Priority.LOW;
import static uk.co.zoneofavoidance.my.tasks.domain.Priority.NORMAL;
import static uk.co.zoneofavoidance.my.tasks.domain.State.CLOSED;
import static uk.co.zoneofavoidance.my.tasks.domain.State.IN_PROGRESS;
import static uk.co.zoneofavoidance.my.tasks.domain.State.ON_HOLD;
import static uk.co.zoneofavoidance.my.tasks.domain.State.TO_DO;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import uk.co.zoneofavoidance.my.tasks.domain.Task;
import uk.co.zoneofavoidance.my.tasks.repositories.TaskRepository;

@SpringBootApplication
public class MyTasksApplication {

   public static void main(final String[] args) {
      SpringApplication.run(MyTasksApplication.class, args);
   }

   @Bean
   @Profile("dev")
   public CommandLineRunner sampleData(final TaskRepository tasks) {
      return (args) -> {
         tasks.save(new Task("First sample task", "This is the first sample task.", HIGH, TO_DO));
         tasks.save(new Task("Second sample task", "This is the second sample task.", CRITICAL, CLOSED));
         tasks.save(new Task("Third sample task", "This is the third sample task.", LOW, TO_DO));
         tasks.save(new Task("Fourth sample task", "This is the fourth sample task.", NORMAL, IN_PROGRESS));
         tasks.save(new Task("Fifth sample task", "This is the fifth sample task.", HIGH, ON_HOLD));
      };
   }
}
