package uk.co.zoneofavoidance.my.tasks;

import org.pegdown.Extensions;
import org.pegdown.PegDownProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MyTasksApplication {

   public static void main(final String[] args) {
      SpringApplication.run(MyTasksApplication.class, args);
   }

   @Bean
   public PegDownProcessor markdown() {
      return new PegDownProcessor(Extensions.NONE);
   }

}
