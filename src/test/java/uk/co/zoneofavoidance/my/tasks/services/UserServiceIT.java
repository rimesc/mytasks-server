package uk.co.zoneofavoidance.my.tasks.services;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static uk.co.zoneofavoidance.my.tasks.security.SecurityConfiguration.ADMIN_ROLE;
import static uk.co.zoneofavoidance.my.tasks.security.SecurityConfiguration.USER_ROLE;

import java.util.Arrays;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertThat;

import uk.co.zoneofavoidance.my.tasks.MyTasksApplication;

/**
 * Integration test for {@link UserService}.
 */
@IntegrationTest
@SpringApplicationConfiguration
@ActiveProfiles("dev")
@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceIT {

   private static final String PASSWORD = "abcdefgh";

   @Autowired
   private UserService userService;

   @Autowired
   private AuthenticationManager authManager;

   @Test
   public void canCreateAdminUser() throws Exception {
      userService.createUser("superuser", PASSWORD, true);

      final UserDetails user = userService.getUser("superuser");
      assertThat(user.getUsername(), equalTo("superuser"));
      assertThat(user.getAuthorities(), hasRoles(ADMIN_ROLE, USER_ROLE));

      final Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken("superuser", PASSWORD));
      assertThat(authentication.getAuthorities(), hasRoles(ADMIN_ROLE, USER_ROLE));
   }

   @Test
   public void canCreateNonAdminUser() throws Exception {
      userService.createUser("user", PASSWORD, false);

      final UserDetails user = userService.getUser("user");
      assertThat(user.getUsername(), equalTo("user"));
      assertThat(user.getAuthorities(), hasRoles(USER_ROLE));

      final Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken("user", PASSWORD));
      assertThat(authentication.getAuthorities(), hasRoles(USER_ROLE));
   }

   private Matcher<Iterable<? extends GrantedAuthority>> hasRoles(final String... roles) {
      return containsInAnyOrder(Arrays.stream(roles).map(role -> hasProperty("authority", equalTo(role))).collect(toList()));
   }

   /**
    * Hook from which to hang an import of the main configuration.
    */
   @Configuration
   @Import(MyTasksApplication.class)
   public static class Config {
   }

}
