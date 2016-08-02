package uk.co.zoneofavoidance.my.tasks.rest.response;

import java.util.List;

/**
 * Response body containing details of a user.
 */
public class UserResponse {

   private final String username;

   private final List<String> authorities;

   /**
    * @param username name of the user
    * @param authorities list of authorities that the user possesses
    */
   public UserResponse(final String username, final List<String> authorities) {
      this.username = username;
      this.authorities = authorities;
   }

   /**
    * @return the name of the user
    */
   public String getUsername() {
      return username;
   }

   /**
    * @return a list authorities that the user possesses
    */
   public List<String> getAuthorities() {
      return authorities;
   }

}
