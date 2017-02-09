package uk.co.zoneofavoidance.my.tasks.services;

import static uk.co.zoneofavoidance.my.tasks.security.AuthenticatedUser.GLOBAL_USER;

import uk.co.zoneofavoidance.my.tasks.security.AuthenticatedUser;

/**
 * Base class for services. Handles permissions, etc.
 */
abstract class BaseService {

   private final AuthenticatedUser user;

   BaseService(final AuthenticatedUser user) {
      this.user = user;
   }

   boolean isPermitted(final String owner) {
      return GLOBAL_USER.equals(owner) || user.getId().equals(owner);
   }

   AuthenticatedUser authenticatedUser() {
      return user;
   }

}
