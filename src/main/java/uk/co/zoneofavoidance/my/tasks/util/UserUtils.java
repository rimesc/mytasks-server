package uk.co.zoneofavoidance.my.tasks.util;

import static uk.co.zoneofavoidance.my.tasks.security.SecurityConfiguration.ADMIN_ROLE;

import org.springframework.security.core.userdetails.User;

public final class UserUtils {

   public boolean isAdmin(final User user) {
      return user == null ? false : user.getAuthorities().stream().anyMatch(x -> ADMIN_ROLE.equals(x.getAuthority()));
   }

}
