package uk.co.zoneofavoidance.my.tasks.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

/**
 * Configures the {@link UserDetailsManager}.
 */
@Configuration
public class SecurityConfiguration {

   /** Admin role. */
   public static final String ADMIN_ROLE = "ROLE_ADMIN";

   /** User role. */
   public static final String USER_ROLE = "ROLE_USER";

   @Autowired
   private DataSource dataSource;

   @Bean
   public UserDetailsManager userDetailsManager() {
      final JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager();
      userDetailsManager.setDataSource(dataSource);
      return userDetailsManager;
   }

   @Bean
   public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }

}
