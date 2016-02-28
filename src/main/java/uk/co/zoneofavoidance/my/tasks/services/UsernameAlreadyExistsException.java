package uk.co.zoneofavoidance.my.tasks.services;

import org.springframework.security.core.AuthenticationException;

/**
 * Thrown if an attempt is made to create a user with a name that is already in
 * use.
 */
public class UsernameAlreadyExistsException extends AuthenticationException {

   public UsernameAlreadyExistsException(final String username) {
      super("The username '" + username + "' is already in use.");
   }

}
