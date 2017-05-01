package uk.co.zoneofavoidance.my.tasks;

import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

/**
 * Runs on startup in developer profile to add some sample data to the database.
 */
@Component
@Profile("dev")
public class SampleData implements CommandLineRunner {

   @Autowired
   private DataSource dataSource;

   private final ClassPathResource resource = new ClassPathResource("sample-data.sql");

   @Override
   @Transactional
   public void run(final String... args) throws Exception {
      new ResourceDatabasePopulator(resource).execute(dataSource);
   }

}
