package uk.co.zoneofavoidance.my.tasks.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Sets up the database tables used by Spring Security.
 */
@Component
public class AuthenticationTables implements CommandLineRunner {

   // used to set up the inital account on first start
   private static final String DEFAULT_USER_NAME = "admin";
   private static final String DEFAULT_USER_PASSWORD = "secret";

   @Autowired
   private DataSource dataSource;

   @Autowired
   private PasswordEncoder passwordEncoder;

   @Override
   public void run(final String... args) throws Exception {
      createTables();
      if (numberOfUsers() == 0) {
         createDefaultUser();
      }
   }

   private void createDefaultUser() {
      final JdbcTemplate template = new JdbcTemplate(dataSource);
      template.execute(String.format("INSERT INTO users (username, password, enabled) VALUES ('%s', '%s', 'true');", DEFAULT_USER_NAME, passwordEncoder.encode(DEFAULT_USER_PASSWORD)));
      template.execute("INSERT INTO authorities (username, authority) VALUES ('admin', 'ROLE_ADMIN');");
   }

   private Integer numberOfUsers() {
      final JdbcTemplate template = new JdbcTemplate(dataSource);
      final Integer numberOfUsers = template.queryForObject("SELECT COUNT(*) FROM users;", Integer.class);
      return numberOfUsers;
   }

   private void createTables() {
      final JdbcTemplate template = new JdbcTemplate(dataSource);
      template.execute("CREATE TABLE IF NOT EXISTS users(username varchar_ignorecase(50) not null primary key, password varchar(60) not null, enabled boolean not null);");
      template.execute("CREATE TABLE IF NOT EXISTS authorities (username varchar_ignorecase(50) not null, authority varchar_ignorecase(50) not null, constraint fk_authorities_users foreign key(username) references users(username));");
      template.execute("CREATE UNIQUE INDEX IF NOT EXISTS ix_auth_username on authorities (username,authority);");
   }
}
