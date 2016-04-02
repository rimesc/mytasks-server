package uk.co.zoneofavoidance.my.tasks.controllers.rest.response;

import java.util.List;

public class UsersResponse {

   private final List<UserResponse> users;

   public UsersResponse(final List<UserResponse> users) {
      this.users = users;
   }

   public List<UserResponse> getUsers() {
      return users;
   }
}
