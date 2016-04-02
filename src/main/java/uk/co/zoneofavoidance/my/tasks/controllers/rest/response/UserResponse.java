package uk.co.zoneofavoidance.my.tasks.controllers.rest.response;

import java.util.List;

public class UserResponse {

   private final String username;

   private final List<String> authorities;

   public UserResponse(final String username, final List<String> roles) {
      this.username = username;
      this.authorities = roles;
   }

   public String getUsername() {
      return username;
   }

   public List<String> getAuthorities() {
      return authorities;
   }

}
