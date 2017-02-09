package uk.co.zoneofavoidance.my.tasks.security;

import java.util.Map;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.auth0.spring.security.api.Auth0JWTToken;

/**
 * Authenticated user from an Auth0 JWT token.
 */
public class Auth0AuthenticatedUser implements AuthenticatedUser {

   private static final String SUB_CLAIM = "sub";
   private static final String USER_ID_CLAIM = "user_id";

   private final String userId;

   /**
    * Construct a new instance.
    */
   @SuppressWarnings("unchecked")
   public Auth0AuthenticatedUser() {
      final Authentication token = SecurityContextHolder.getContext().getAuthentication();
      if (!(token instanceof Auth0JWTToken)) {
         throw new BadCredentialsException("Unable to parse authentication token");
      }
      final Map<String, String> details = (Map<String, String>) token.getDetails();
      userId = details.getOrDefault(USER_ID_CLAIM, details.get(SUB_CLAIM));
   }

   @Override
   public String getId() {
      return userId;
   }

}
