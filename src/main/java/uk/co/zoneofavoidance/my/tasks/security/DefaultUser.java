package uk.co.zoneofavoidance.my.tasks.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import uk.co.zoneofavoidance.my.tasks.services.UserService;

/**
 * Sets up the database tables used by Spring Security.
 */
@Component
public class DefaultUser implements CommandLineRunner {

   // used to set up the inital account on first start
   private static final String DEFAULT_USER_NAME = "admin";
   private static final String DEFAULT_USER_PASSWORD = "secret";

   @Autowired
   private UserService users;

   @Override
   public void run(final String... args) throws Exception {
      if (users.size() == 0) {
         users.createUser(DEFAULT_USER_NAME, DEFAULT_USER_PASSWORD, true);
      }
   }

}
